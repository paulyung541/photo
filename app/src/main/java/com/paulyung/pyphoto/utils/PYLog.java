package com.paulyung.pyphoto.utils;

import android.util.Log;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class PYLog {
    private static boolean isDebug;

    public static void setDebug(boolean enableDebug) {
        isDebug = enableDebug;
    }

    public static void debug(String TAG, String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }
}
