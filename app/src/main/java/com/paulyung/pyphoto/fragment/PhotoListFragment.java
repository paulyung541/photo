package com.paulyung.pyphoto.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.activity.ImageWatchActivity;
import com.paulyung.pyphoto.adapter.BaseAdapter;
import com.paulyung.pyphoto.adapter.PhotoListAdapter;
import com.paulyung.pyphoto.adapter.viewholder.PhotoListViewHolder;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.callback.FileOperate;
import com.paulyung.pyphoto.callback.SelectStateCheck;
import com.paulyung.pyphoto.wrapper.RcyView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yang on 2016/11/23.
 * paulyung@outlook.com
 */

public class PhotoListFragment extends BaseFragment {
    private RecyclerView mRyView;
    private PhotoListAdapter mAdapter;
    private List<PhotoMsg> mPhotoLists = new ArrayList<>();
    private SelectStateCheck mCheckState;
    private FileOperate mPhotoOperate;
    private String coverName;
    private String witch;
    public PhotoListFragment() {
    }

    public static BaseFragment getInstance(Bundle bundle) {
        BaseFragment fragment = new PhotoListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectStateCheck && context instanceof FileOperate) {
            mCheckState = (SelectStateCheck) context;
            mPhotoOperate = (FileOperate) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SelectStateCheck and mPhotoOperate interface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coverName = getArguments().getString(BundleTag.COVER_NAME);
        witch = getArguments().getString(BundleTag.WITCH_TO_LIST);
        switch (witch) {
            case "photo_state_list":
                if (coverName.equals("相机"))
                    coverName = "Camera";
                mPhotoLists.addAll(BaseApplication.getInstance().getPhotoMsg().get(coverName));
                break;
            case "position_cover_list":
                mPhotoLists.addAll(BaseApplication.getInstance().getPositionMap().get(coverName));
                break;
        }
        Collections.reverse(mPhotoLists);//反转顺序，让时间最近的排在前面
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCheckState = null;
    }

    @Override
    protected void initView() {
        //// TODO: 2016/11/28 图片初始拉伸问题，就是出在初始化的时候 
        mRyView = (RecyclerView) findViewById(R.id.ry_view);
        RcyView.normalInit(mRyView);
        mAdapter = new PhotoListAdapter(getActivity(), this);
        RcyView.setGridLayoutManager(mRyView, mAdapter, PhotoListViewHolder.SPAN_COUNT);
        mRyView.setAdapter(mAdapter);
        mAdapter.addAll(mPhotoLists);
        //普通按
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (mCheckState.getSelectState()) {//检查是否是选照片状态
                    if (mPhotoOperate.addFile(mAdapter.getItem(position).getAbsolutePath())) {
                        //如果添加成功，则显示CheckBox为打勾状态
                        mAdapter.getItem(position).setCheck(true);
                    } else {
                        mAdapter.getItem(position).setCheck(false);
                    }
                    mAdapter.update(position);
                } else {
                    //// TODO: 2016/11/24 点击查看大图
                    Intent intent = new Intent(getActivity(), ImageWatchActivity.class);
                    if (witch.equals("photo_state_list")) {
                        intent.putExtra(BundleTag.WITCH_TO_WATCH, ImageWatchActivity.PHOTO_LIST_WATCH);
                    } else if (witch.equals("position_cover_list")) {
                        intent.putExtra(BundleTag.WITCH_TO_WATCH, ImageWatchActivity.POSITION_WATCH);
                    }
                    intent.putExtra(BundleTag.WATCH_IMAGE_INDEX, position);
                    intent.putExtra(BundleTag.COVER_NAME_2, coverName);
                    startActivity(intent);
                }
            }
        });

        //长按
        mAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                boolean res = false;
                if (!mCheckState.getSelectState()) {//在选择照片状态下，长按则无效
                    mCheckState.setSelectState(true);
                    for (int i = 0; i < mAdapter.getDataSize(); ++i) {
                        mAdapter.getItem(i).setShowCheckBox(true);
                    }
                    mAdapter.getItem(position).setCheck(true);
                    mPhotoOperate.addFile(mAdapter.getItem(position).getAbsolutePath());
                    mAdapter.notifyDataSetChanged();
                    res = true;
                }
                return res;
            }
        });
    }

    @Override
    public void onFileDelete() {
        mAdapter.clear();
        mAdapter.addAll(BaseApplication.getInstance().getPhotoMsg().get(coverName));
        mAdapter.clearCheckBoxStateAll();//清除CheckBox
        Collections.reverse(mAdapter.getAllData());//反转顺序，让时间最近的排在前面
    }

    //选照片状态时点击返回键
    @Override
    public boolean onBackPressed() {
        for (int i = 0; i < mAdapter.getDataSize(); ++i) {
            mAdapter.getItem(i).setShowCheckBox(false);
            mAdapter.getItem(i).setCheck(false);
        }
        if (mAdapter.getDataSize() != 0)
            mAdapter.notifyDataSetChanged();
        return true;
    }
}
