package com.paulyung.pyphoto.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.bean.PhotoMsg;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yang on 2016/12/7.
 * paulyung@outlook.com
 */

public class PhotoDetailActivity extends BaseActivity {
    @BindView(R.id.date)
    TextView mTvDate;
    @BindView(R.id.location)
    TextView mTvLocation;
    @BindView(R.id.name)
    TextView mTvName;
    @BindView(R.id.size)
    TextView mTvSize;
    @BindView(R.id.path)
    TextView mTvPath;
    private PhotoMsg msg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setActionBar(ActionBar actionBar) {
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected Toolbar getInitToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        msg = (PhotoMsg) getIntent().getSerializableExtra(BundleTag.IMAGE_DETAIL);
        mTvDate.setText(msg.getTime());
        if (!TextUtils.isEmpty(msg.getLocation()))
            mTvLocation.setText(msg.getLocation());
        else
            mTvLocation.setVisibility(View.GONE);
        mTvName.setText(msg.getSimpleName());
        mTvPath.setText(msg.getAbsolutePath());
        File file = new File(msg.getAbsolutePath());
        float size = file.length() / (1024 * 1024);
        mTvSize.setText(size + "M");
    }
}
