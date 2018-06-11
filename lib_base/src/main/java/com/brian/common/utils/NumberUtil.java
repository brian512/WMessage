package com.brian.common.utils;

import android.text.TextUtils;

/**
 * 字符串数字转换
 */
public class NumberUtil {

    public static int toInt(String number) {
        return toInt(number, 0);
    }

    public static int toInt(String number, int defaultNum) {
        int result = defaultNum;
        if (!TextUtils.isEmpty(number) && TextUtils.isDigitsOnly(number)) {
            try {
                result = Integer.valueOf(number);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static long toLong(String number) {
        return toLong(number, 0L);
    }

    public static long toLong(String number, long defaultNum) {
        long result = defaultNum;
        if (!TextUtils.isEmpty(number) && TextUtils.isDigitsOnly(number)) {
            try {
                result = Integer.valueOf(number);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
