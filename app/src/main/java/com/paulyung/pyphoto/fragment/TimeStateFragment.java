package com.paulyung.pyphoto.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.activity.ImageWatchActivity;
import com.paulyung.pyphoto.adapter.BaseAdapter;
import com.paulyung.pyphoto.adapter.TimeStateAdapter;
import com.paulyung.pyphoto.callback.FileOperate;
import com.paulyung.pyphoto.callback.SelectStateCheck;
import com.paulyung.pyphoto.wrapper.RcyView;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class TimeStateFragment extends BaseFragment {
    private RecyclerView mRcyView;
    private TimeStateAdapter mAdapter;
    private FileOperate mFileOperate;
    private SelectStateCheck mCheckState;

    public TimeStateFragment() {
    }

    public static Fragment getInstance(Bundle bundle) {
        Fragment fragment = new TimeStateFragment();
        fragment.setArguments(bundle);
        return fragment;
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
    protected int getLayoutId() {
        return R.layout.fragment_time_state;
    }

    @Override
    protected void initView() {
        mRcyView = (RecyclerView) findViewById(R.id.recyclerview);
        RcyView.normalInit(mRcyView);
        mAdapter = new TimeStateAdapter(getActivity(), BaseApplication.getInstance().getTimeList());
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (mCheckState.getSelectState()) {//检查是否是选照片状态
                    if (mFileOperate.addFile(mAdapter.getPhotoItem(position).getAbsolutePath())) {
                        //如果添加成功，则显示CheckBox为打勾状态
                        mAdapter.getPhotoItem(position).setCheck(true);
                    } else {
                        mAdapter.getPhotoItem(position).setCheck(false);
                    }
                    mAdapter.update(position);
                } else {
                    Intent intent = new Intent(getActivity(), ImageWatchActivity.class);
                    intent.putExtra(BundleTag.WATCH_IMAGE_INDEX, position);
                    intent.putExtra(BundleTag.WITCH_TO_WATCH, ImageWatchActivity.TIME_WATCH);
                    startActivity(intent);
                }
            }
        });
        mAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                boolean res = false;
                if (!mCheckState.getSelectState()) {//在选择照片状态下，长按则无效
                    mCheckState.setSelectState(true);
                    for (int i = 0; i < mAdapter.getPhotoSize(); ++i) {
                        mAdapter.getPhotoItem(i).setShowCheckBox(true);
                    }
                    mAdapter.getPhotoItem(position).setCheck(true);
                    mFileOperate.addFile(mAdapter.getPhotoItem(position).getAbsolutePath());
                    mAdapter.notifyDataSetChanged();
                    res = true;
                }
                return res;
            }
        });
        mRcyView.setAdapter(mAdapter);
        RcyView.setGridLayoutManager(mRcyView, mAdapter, 3);
    }

    //选照片状态时点击返回键
    @Override
    public boolean onBackPressed() {
        mAdapter.clearCheckBoxStateAll();
        return true;
    }

    @Override
    public void onFileDelete() {
        //// TODO: 2016/12/2
        mAdapter.synchronizedData();
        mAdapter.clearCheckBoxStateAll();
    }
}
