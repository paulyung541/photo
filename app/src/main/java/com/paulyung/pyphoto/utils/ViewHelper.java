package com.paulyung.pyphoto.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yang on 2016/11/30.
 * paulyung@outlook.com
 */

public class ViewHelper {
    public static boolean hasChildView(ViewGroup parent, View child) {
        boolean res = false;
        for (int i = 0; i < parent.getChildCount(); ++i) {
            if (parent.getChildAt(i).equals(child)) {
                res = true;
                break;
            }
        }
        return res;
    }
}
