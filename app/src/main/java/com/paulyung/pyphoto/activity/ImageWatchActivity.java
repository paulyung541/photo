package com.paulyung.pyphoto.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.adapter.PhotoViewPagerAdapter;
import com.paulyung.pyphoto.bean.PhotoMsg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yang on 2016/11/24.
 * paulyung@outlook.com
 */

public class ImageWatchActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private List<PhotoMsg> mDatas = new ArrayList<>();
    private int firstP;
    private PagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_watch;
    }

    @Override
    protected Toolbar getInitToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_watch, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void setActionBar(ActionBar actionBar) {
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void beforeSetView() {
        Intent intent = getIntent();
        firstP = intent.getIntExtra(BundleTag.WATCH_IMAGE_INDEX, -1);
        String coverName = intent.getStringExtra(BundleTag.COVER_NAME_2);
        mDatas.addAll(BaseApplication.getInstance().getPhotoMsg().get(coverName));
        Collections.reverse(mDatas);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        viewPager.setOffscreenPageLimit(PhotoViewPagerAdapter.OFFSCREEN_PAGES);
        mAdapter = new PhotoViewPagerAdapter(viewPager, mDatas);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(firstP);
    }
}
