package com.paulyung.pyphoto.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.paulyung.pyphoto.BundleTag;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.callback.FileOperate;
import com.paulyung.pyphoto.callback.SelectStateCheck;
import com.paulyung.pyphoto.fragment.BaseFragment;
import com.paulyung.pyphoto.fragment.PhotoListFragment;
import com.paulyung.pyphoto.task.PhotoListDeleteTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/11/18.
 * paulyung@outlook.com
 * 点击相册后进入的照片列表
 */

public class PhotoListActivity extends BaseActivity implements SelectStateCheck, FileOperate {
    private FragmentManager mFragmentManager;
    private boolean isCheckState;
    private BaseFragment mFragment;
    //被选中的照片
    private List<String> mSelectPhotos = new ArrayList<>();
    private String title;//标题
    private String witch;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_list;
    }

    @Override
    protected void beforeSetView() {
        witch = getIntent().getStringExtra(BundleTag.WITCH_TO_LIST);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void initView() {
        Bundle bundle = new Bundle();
        bundle.putString(BundleTag.COVER_NAME, title);
        bundle.putString(BundleTag.WITCH_TO_LIST, witch);
        mFragment = PhotoListFragment.getInstance(bundle);
        mFragmentManager.beginTransaction()
                .add(R.id.lay_container, mFragment, getClass().getSimpleName()).commit();
    }

    @Override
    protected Toolbar getInitToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = getIntent().getStringExtra(BundleTag.COVER_NAME);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.gray1));
        return toolbar;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuBuilder builder = (MenuBuilder) menu;
        builder.setOptionalIconsVisible(true);
        if (isCheckState) {
            menu.findItem(R.id.delete).setVisible(true).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            menu.findItem(R.id.delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isCheckState) {
            //执行删除
            onDeletePressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectState(boolean selectState) {
        isCheckState = selectState;
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean getSelectState() {
        return isCheckState;
    }

    @Override
    public void onBackPressed() {
        if (isCheckState) {//如果为选照片状态则调用Fragment方法，
            if (!mFragment.onBackPressed())
                super.onBackPressed();//如果当前Fragment没有重写 onBackPressed() 方法，则任然使用默认返回
            setSelectState(false);
        } else {
            super.onBackPressed();
        }
    }

    //点击删除
    private void onDeletePressed() {

        switch (witch) {
            case "photo_state_list":
                new PhotoListDeleteTask(this, mFragment, mSelectPhotos, title).execute();
                break;
            case "position_cover_list":
                break;
        }
    }

    @Override
    public boolean addFile(String imgFile) {
        boolean res = true;
        if (mSelectPhotos.contains(imgFile)) {
            mSelectPhotos.remove(imgFile);
            res = false;
        } else {
            mSelectPhotos.add(imgFile);
        }
        return res;
    }
}
