package com.paulyung.pyphoto;

import android.app.Application;
import android.widget.Toast;

import com.paulyung.pyphoto.utils.PYLog;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class BaseApplication extends Application {
    private static BaseApplication _INSTANCE;

    public synchronized static BaseApplication getInstance() {
        return _INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _INSTANCE = this;
        PYLog.setDebug(BuildConfig.DEBUG);
    }

    public static void showToast(String msg) {
        Toast.makeText(_INSTANCE, msg, Toast.LENGTH_SHORT).show();
    }
}
