package com.paulyung.pyphoto.task;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.adapter.BaseAdapter;
import com.paulyung.pyphoto.utils.DBHelper;

/**
 * Created by yang on 2016/12/7.
 * paulyung@outlook.com
 */

public class PositionScanTask extends AsyncTask<Void, Void, Void> {
    private BaseAdapter mAdapter;
    private RecyclerView mRcView;
    private ProgressBar mProgress;

    public PositionScanTask() {
    }

    public PositionScanTask(BaseAdapter adapter, RecyclerView rcView, ProgressBar progressBar) {
        mAdapter = adapter;
        mRcView = rcView;
        mProgress = progressBar;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DBHelper.savePositionPhoto();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mProgress != null) {
            mProgress.setVisibility(View.GONE);
            mRcView.setVisibility(View.VISIBLE);
            mAdapter.addAll(BaseApplication.getInstance().getPositionCoverList());
        }
    }
}
