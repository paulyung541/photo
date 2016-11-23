package com.paulyung.pyphoto.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.PermissionManager;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.activity.PhotoListActivity;
import com.paulyung.pyphoto.adapter.BaseAdapter;
import com.paulyung.pyphoto.adapter.PhotoStateAdapter;
import com.paulyung.pyphoto.callback.CoverOperate;
import com.paulyung.pyphoto.callback.OnPhotoMsgBackListener;
import com.paulyung.pyphoto.callback.SelectStateCheck;
import com.paulyung.pyphoto.utils.DBHelper;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class PhotoStateFragment extends BaseFragment implements OnPhotoMsgBackListener {

    private RecyclerView mRyView;
    private PhotoStateAdapter adapter;
    private CoverOperate mCoverOperate;
    private SelectStateCheck mCheckState;

    public PhotoStateFragment() {
    }

    public static Fragment getInstance(Bundle bundle) {
        Fragment fragment = new PhotoStateFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_state;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CoverOperate && context instanceof SelectStateCheck) {
            mCoverOperate = (CoverOperate) context;
            mCheckState = (SelectStateCheck) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CoverOperate or SelectStateCheck interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCoverOperate = null;
        mCheckState = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionManager.getInstants().addInitCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                new PermissionManager.InitOptions() {
                    @Override
                    public void doInit() {
                        DBHelper.scanPhoto(PhotoStateFragment.this);//扫描SD卡，获取图片信息
                    }
                })
                .enableDialog(false)
                .cheackAndRequest(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
                }, false);
    }

    @Override
    protected void initView() {
        mRyView = (RecyclerView) findViewById(R.id.ry_view);
        mRyView.setHasFixedSize(true);
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setChangeDuration(0);//解决刷新使用默认item动画闪屏问题
        mRyView.setItemAnimator(animator);
        adapter = new PhotoStateAdapter(getActivity(), this);
        //普通按
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (mCheckState.getSelectState()) {//检查是否是选照片状态
                    if (mCoverOperate.addCover(adapter.getItem(position).getCover())) {
                        //如果添加成功，则显示CheckBox为打勾状态
                        adapter.getItem(position).setCheck(true);
                    } else {
                        adapter.getItem(position).setCheck(false);
                    }
                    adapter.update(position);
                } else {
                    Intent intent = new Intent(getActivity(), PhotoListActivity.class);
                    intent.putExtra(BundleTag.PHOTO_STATE_FRAGMENT_WIDTH, v.getWidth());//item宽
                    intent.putExtra(BundleTag.PHOTO_STATE_FRAGMENT_HEIGHT, v.getHeight());//item高
                    intent.putExtra(BundleTag.COVER_NAME, adapter.getItem(position).getCoverName());//相册名
                    startActivity(intent);
                }
            }
        });
        //长按
        adapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                boolean res = false;
                if (!mCheckState.getSelectState()) {//在选择照片状态下，长按则无效
                    mCheckState.setSelectState(true);
                    for (int i = 0; i < adapter.getDataSize(); ++i) {
                        adapter.getItem(i).setShowCheckBox(true);
                    }
                    adapter.getItem(position).setCheck(true);
                    mCoverOperate.addCover(adapter.getItem(position).getCover());
                    adapter.notifyDataSetChanged();
                    res = true;
                }
                return res;
            }
        });
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(adapter.getGridLookUp(2));
        mRyView.setLayoutManager(manager);
        mRyView.setAdapter(adapter);
    }

    //选照片状态时点击返回键
    @Override
    public boolean onBackPressed() {
        for (int i = 0; i < adapter.getDataSize(); ++i) {
            adapter.getItem(i).setShowCheckBox(false);
            adapter.getItem(i).setCheck(false);
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onFileDelete() {
        onAllPhotoGet();
    }

    //获取到照片后回调
    @Override
    public void onAllPhotoGet() {
        adapter.clear();
        adapter.addAll(BaseApplication.getInstance().getCovers());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.getInstants().handleRequest(getActivity(), requestCode, permissions, grantResults);
    }
}