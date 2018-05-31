package com.brian.common.utils;


import android.os.Handler;
import android.os.Looper;

public class HandlerUtil {

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static Handler getUIHandler() {
        return sHandler;
    }
}
