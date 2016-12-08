package com.paulyung.pyphoto.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.paul.actionanimset.ActionAnimatorSet;
import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.adapter.PhotoViewPagerAdapter;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.utils.UIHelper;
import com.paulyung.pyphoto.widget.TouchViewPager;

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
    private ActionAnimatorSet animatorSet;
    private ObjectAnimator toolbarShow;
    private ObjectAnimator toolbarHide;

    private boolean isShow = true;
    private boolean intoDetail;

    @BindView(R.id.view_pager)
    TouchViewPager viewPager;
    Toolbar toolbar;
    private List<PhotoMsg> mDatas = new ArrayList<>();
    private int firstP;
    private PagerAdapter mAdapter;
    private PhotoMsg mCurrentMsg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_watch;
    }

    @Override
    protected Toolbar getInitToolbar() {
        return toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.photo_detail).setVisible(!intoDetail);
        return super.onPrepareOptionsMenu(menu);
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
                break;
            case R.id.photo_detail:
                resetMenu();
                Intent intent = new Intent(this, PhotoDetailActivity.class);
                intent.putExtra(BundleTag.IMAGE_DETAIL, mCurrentMsg);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetMenu() {
        intoDetail = false;
        supportInvalidateOptionsMenu();
    }

    @Override
    protected void onRestart() {
        resetMenu();
        super.onRestart();
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
        String coverName = "";
        switch (intent.getStringExtra(BundleTag.WITCH_TO_WATCH)) {
            case PHOTO_LIST_WATCH://照片列表进入
                coverName = intent.getStringExtra(BundleTag.COVER_NAME_2);
                mDatas.addAll(BaseApplication.getInstance().getPhotoMsg().get(coverName));
                Collections.reverse(mDatas);
                break;
            case TIME_WATCH://时间照片列表进入
                selectShow(intent);
                break;
            case POSITION_WATCH://地址照片列表进入
                coverName = intent.getStringExtra(BundleTag.COVER_NAME_2);
                mDatas.addAll(BaseApplication.getInstance().getPositionMap().get(coverName));
                Collections.reverse(mDatas);
                break;
        }
        mCurrentMsg = mDatas.get(firstP);
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
        viewPager.setOnViewPagerClickListener(new TouchViewPager.OnViewPagerClickListener() {
            @Override
            public void onClick() {
                if (isShow) {
                    hidetoolbar();
                } else {
                    showToolbar();
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentMsg = mDatas.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initAnim();
    }

    private void initAnim() {
        toolbarShow = ObjectAnimator.ofFloat(toolbar, "translationY", -UIHelper.dpToPx(56), 0);
        toolbarShow.setDuration(500);
        toolbarHide = ObjectAnimator.ofFloat(toolbar, "translationY", -UIHelper.dpToPx(56));
        toolbarHide.setDuration(500);

        toolbarShow.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShow = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        toolbarHide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShow = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void showToolbar() {
        if (!toolbarHide.isRunning())
            toolbarShow.start();
    }

    private void hidetoolbar() {
        if (!toolbarShow.isRunning())
            toolbarHide.start();
    }
}
