package com.paulyung.pyphoto.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.paulyung.pyphoto.AppManager;
import com.paulyung.pyphoto.BuildConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //如果只设置下面这个，5.0以上是半透明效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//4.4以上5.0以下设置状态栏透明
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | localLayoutParams.flags);
        }
        //如果要设置全透明status_bar就要打开下面注释，同时注意status_bar会覆盖Content-View
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0以上
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//设置了这个，status_bar就会覆盖Content-View
//            window.setStatusBarColor(Color.TRANSPARENT);//全透明
//        }
        AppManager.getAppManager().addActivity(this);
        beforeSetView();
        if (getLayoutId() != 0)
            setContentView(getLayoutId());
        mToolbar = getInitToolbar();
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            setActionBar(getSupportActionBar());
        }
        if (savedInstanceState == null)
            initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onResume(this);
            MobclickAgent.onPageStart(this.getClass().getSimpleName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onPause(this);
            MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        }
    }

    protected void beforeSetView() {

    }

    protected void initView() {

    }

    protected int getLayoutId() {
        return 0;
    }

    protected Toolbar getInitToolbar() {
        return null;
    }

    protected void setActionBar(ActionBar actionBar) {

    }
}
