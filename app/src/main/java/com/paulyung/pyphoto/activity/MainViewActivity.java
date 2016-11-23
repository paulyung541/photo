package com.paulyung.pyphoto.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.callback.SelectStateCheck;
import com.paulyung.pyphoto.fragment.BaseFragment;
import com.paulyung.pyphoto.fragment.PhotoStateFragment;
import com.paulyung.pyphoto.fragment.PositionStateFragment;
import com.paulyung.pyphoto.fragment.TimeStateFragment;

/**
 * Created by yang on 2016/11/16.
 * paulyung@outlook.com
 * 视图层
 */

public class MainViewActivity extends BaseActivity implements SelectStateCheck {
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;

    //是否处于选择状态
    private boolean isCheckState;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void beforeSetView() {
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void initView() {
        cheackFragmentById(R.id.action_photo_state);
    }

    @Override
    protected Toolbar getInitToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //MenuBuilder间接继承自Menu，v7包有自己的MenuBuilder，所以用不着反射
        MenuBuilder builder = (MenuBuilder) menu;
        builder.setOptionalIconsVisible(true);//设置Icon可见
        if (isCheckState) {
            menu.setGroupVisible(R.id.group, false);
            //不知为什么在xml中设置android:showAsAction="always"无效，但是代码里设置却有效
            menu.findItem(R.id.delete).setVisible(true).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            menu.setGroupVisible(R.id.group, true);
            menu.findItem(R.id.delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isCheckState) {
            BaseApplication.getInstance().showToast("删除");
            onDeletePressed();//执行删除
        } else {
            item.setChecked(true);
            cheackFragmentById(item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    //删除
    protected void onDeletePressed() {

    }

    public BaseFragment getCurrentFragment() {
        return (BaseFragment) mCurrentFragment;
    }

    /**
     * 切换Fragment
     */
    private void cheackFragmentById(int id) {
        Fragment fragment = mFragmentManager.findFragmentByTag(String.valueOf(id));
        if (fragment == null) {
            switch (id) {
                case R.id.action_photo_state:
                    fragment = PhotoStateFragment.getInstance(null);
                    break;
                case R.id.action_time_state:
                    fragment = TimeStateFragment.getInstance(null);
                    break;
                case R.id.action_position_state:
                    fragment = PositionStateFragment.getInstance(null);
                    break;
            }
            mFragmentManager.beginTransaction().add(R.id.lay_container, fragment, String.valueOf(id)).commit();
            if (mCurrentFragment != null)
                mFragmentManager.beginTransaction().hide(mCurrentFragment).commit();
            mCurrentFragment = fragment;
            return;
        }
        if (mCurrentFragment != null)
            mFragmentManager.beginTransaction().hide(mCurrentFragment).commit();
        mCurrentFragment = fragment;
        mFragmentManager.beginTransaction().show(fragment).commit();
    }

    ///////////////////SelectStateCheck///////////////////
    @Override
    public void setSelectState(boolean selectState) {
        isCheckState = selectState;
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean getSelectState() {
        return isCheckState;
    }
    ///////////////////SelectStateCheck///////////////////

    @Override
    public void onBackPressed() {
        if (isCheckState) {//如果为选照片状态则调用Fragment方法，
            if (!getCurrentFragment().onBackPressed())
                super.onBackPressed();//如果当前Fragment没有重写 onBackPressed() 方法，则任然使用默认返回
            setSelectState(false);
        } else {
            super.onBackPressed();
        }
    }
}
