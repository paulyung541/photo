package com.paulyung.pyphoto.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.paulyung.pyphoto.AppManager;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppManager.getAppManager().addActivity(this);
        beforeSetView();
        if (getLayoutId() != 0)
            setContentView(getLayoutId());
        mToolbar = getInitToolbar();
        if (mToolbar != null)
            setSupportActionBar(mToolbar);
        if (savedInstanceState == null)
            initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
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
}
