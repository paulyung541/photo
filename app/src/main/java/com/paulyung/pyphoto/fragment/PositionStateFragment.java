package com.paulyung.pyphoto.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.activity.PhotoListActivity;
import com.paulyung.pyphoto.adapter.BaseAdapter;
import com.paulyung.pyphoto.adapter.PositionCoverAdapter;
import com.paulyung.pyphoto.task.PositionScanTask;
import com.paulyung.pyphoto.wrapper.RcyView;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class PositionStateFragment extends BaseFragment {
    private RecyclerView mRyView;
    private ProgressBar mProgress;
    private PositionCoverAdapter mAdapter;

    public PositionStateFragment() {
    }

    public static Fragment getInstance(Bundle bundle) {
        Fragment fragment = new PositionStateFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_position_state;
    }

    @Override
    protected void initView() {
        RecyclerView mRyView = (RecyclerView) findViewById(R.id.ry_view);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        RcyView.normalInit(mRyView);
        mRyView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PositionCoverAdapter(getActivity());
        mRyView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), PhotoListActivity.class);
                intent.putExtra(BundleTag.COVER_NAME, mAdapter.getItem(position).getTitle());//地名
                intent.putExtra(BundleTag.WITCH_TO_LIST, "position_cover_list");
                startActivity(intent);
            }
        });
        new PositionScanTask(mAdapter, mRyView, mProgress).execute();
    }
}
