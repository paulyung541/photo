package com.paulyung.pyphoto.activity;

import com.paulyung.pyphoto.AppManager;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 * 业务层
 */
public class MainActivity extends MainViewActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().AppExit();//exit
    }
}
