package com.paulyung.pyphoto.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.PermissionManager;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.activity.PhotoListActivity;
import com.paulyung.pyphoto.adapter.BaseAdapter;
import com.paulyung.pyphoto.adapter.PhotoStateAdapter;
import com.paulyung.pyphoto.adapter.viewholder.PhotoViewHolder;
import com.paulyung.pyphoto.callback.FileOperate;
import com.paulyung.pyphoto.callback.OnPhotoMsgBackListener;
import com.paulyung.pyphoto.callback.SelectStateCheck;
import com.paulyung.pyphoto.utils.DBHelper;
import com.paulyung.pyphoto.wrapper.RcyView;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class PhotoStateFragment extends BaseFragment implements OnPhotoMsgBackListener {

    private RecyclerView mRyView;
    private PhotoStateAdapter adapter;
    private FileOperate mFileOperate;
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
        if (context instanceof FileOperate && context instanceof SelectStateCheck) {
            mFileOperate = (FileOperate) context;
            mCheckState = (SelectStateCheck) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FileOperate or SelectStateCheck interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFileOperate = null;
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
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
                }, false);
    }

    @Override
    protected void initView() {
        mRyView = (RecyclerView) findViewById(R.id.ry_view);
        RcyView.normalInit(mRyView);
        adapter = new PhotoStateAdapter(getActivity(), this);
        //普通按
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (mCheckState.getSelectState()) {//检查是否是选照片状态
                    if (mFileOperate.addFile(adapter.getItem(position).getCover())) {
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
                    intent.putExtra(BundleTag.WITCH_TO_LIST, "photo_state_list");
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
                    mFileOperate.addFile(adapter.getItem(position).getCover());
                    adapter.notifyDataSetChanged();
                    res = true;
                }
                return res;
            }
        });
        RcyView.setGridLayoutManager(mRyView, adapter, PhotoViewHolder.SPAN_COUNT);
        mRyView.setAdapter(adapter);
    }

    //选照片状态时点击返回键
    @Override
    public boolean onBackPressed() {
        adapter.clearCheckBoxStateAll();
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
