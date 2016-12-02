package com.paulyung.pyphoto.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by yang on 2016/12/1.
 * paulyung@outlook.com
 */

public class StringUtils {
    /***
     * 截取字符串
     *
     * @param start 从那里开始，0算起
     * @param num   截取多少个
     * @param str   截取的字符串
     * @return
     */
    public static String getSubString(int start, int num, String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > length) {
            start = length;
        }
        if (num < 0) {
            num = 1;
        }
        int end = start + num;
        if (end > length) {
            end = length;
        }
        return str.substring(start, end);
    }

    /**
     * 毫秒转成 2009-8-17 10:32:38 这种格式时间
     *
     * @param mill 毫秒
     * @return 返回上述格式时间字符串
     */
    public static String getTimeByMilliseconds(long mill) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mill);
        return format.format(cal.getTime());
    }
}
