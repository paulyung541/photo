package com.paulyung.pyphoto.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yang on 2016/11/17.
 * paulyung@outlook.com
 */

public class TimeUtil {
    public static String getFormatTime(long mS) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        return form.format(new Date(mS));
    }

    public static String getFormatTime(String mS) {
        if (mS == null)
            return null;
        SimpleDateFormat form = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        return form.format(new Date(mS));
    }
}
