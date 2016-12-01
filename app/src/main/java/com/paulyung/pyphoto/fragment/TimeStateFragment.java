package com.paulyung.pyphoto.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.adapter.TimeStateAdapter;
import com.paulyung.pyphoto.wrapper.RcyView;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class TimeStateFragment extends BaseFragment {
    private RecyclerView mRcyView;
    private TimeStateAdapter mAdapter;

    public TimeStateFragment() {
    }

    public static Fragment getInstance(Bundle bundle) {
        Fragment fragment = new TimeStateFragment();
        fragment.setArguments(bundle);
        return fragment;
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
        mRcyView.setAdapter(mAdapter);
        RcyView.setGridLayoutManager(mRcyView, mAdapter, 3);
    }
}
