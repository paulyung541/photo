package com.paulyung.pyphoto.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.paulyung.pyphoto.R;

/**
 * Created by yang on 2016/12/7.
 * paulyung@outlook.com
 */

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    protected void setActionBar(ActionBar actionBar) {
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    protected Toolbar getInitToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }
}
