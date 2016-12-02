package com.paulyung.pyphoto.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
    public static final String PHOTO_LIST_WATCH = "photo_list_watch";
    public static final String TIME_WATCH = "time_watch";
    public static final String POSITION_WATCH = "position_watch";

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
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
        switch (intent.getStringExtra(BundleTag.WITCH_TO_WATCH)) {
            case PHOTO_LIST_WATCH://照片列表进入
                String coverName = intent.getStringExtra(BundleTag.COVER_NAME_2);
                mDatas.addAll(BaseApplication.getInstance().getPhotoMsg().get(coverName));
                Collections.reverse(mDatas);
                break;
            case TIME_WATCH://时间照片列表进入
                selectShow(intent);
                break;
            case POSITION_WATCH://地址照片列表进入
                break;
        }
    }

    private void selectShow(Intent intent) {
        List<PhotoMsg> list = BaseApplication.getInstance().getTimeList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAbsolutePath() != null)
                mDatas.add(list.get(i));
        }
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
