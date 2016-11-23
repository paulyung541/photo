package com.paulyung.pyphoto.activity;

import android.support.v7.widget.Toolbar;

import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.R;

/**
 * Created by yang on 2016/11/18.
 * paulyung@outlook.com
 * 点击相册后进入的照片列表
 */

public class PhotoListActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_list;
    }

    @Override
    protected Toolbar getInitToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = getIntent().getStringExtra(BundleTag.COVER_NAME);
        toolbar.setTitle(title);
        return toolbar;
    }
}
