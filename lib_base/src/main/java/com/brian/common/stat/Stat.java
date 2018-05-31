package com.brian.common.stat;

import android.content.Context;

import com.brian.common.tools.DebugConfig;
import com.brian.common.utils.LogUtil;

/**
 * 统计类
 * 实现用各种统计方案上报统计数据,目前实现的方式有：UmengAnalyticsHelper
 *
 * @author huamm
 */
public class Stat {
    public static final String TAG = Stat.class.getSimpleName();

    public static final boolean ENABLE_UMENG = true;

    public static final boolean DEBUG = DebugConfig.DEBUG_ENABLE;

    /**
     * 开关总入口
     */
    public static final boolean ENABLE = true;// 始终上报

    private static UmengAnalyticsHelper mUmengHelper;


    public static void init(Context context) {
        mUmengHelper = new UmengAnalyticsHelper(context);
    }

    /**
     * 不带参数的统计
     */
    public static void reportData(String eventID) {
        if (ENABLE) {
            try {
                if (ENABLE_UMENG) {
                    mUmengHelper.onEvent(eventID);
                }
            } catch (Throwable error) {}
        }
        if (DEBUG) {
            LogUtil.log("eventID = " + eventID);
        }
    }

    public static void reportData(String eventID, String key) {
        if (ENABLE) {
            try {
                if (ENABLE_UMENG) {
                    mUmengHelper.onEvent(eventID, key);
                }
            } catch (Throwable error) {}
        }
        if (DEBUG) {
            LogUtil.log("eventID=" + eventID + "; key=" + key);
        }
    }

    /**
     * 带参数的统计
     */
    public static void reportData(String eventID, String key, long value) {
        if (DEBUG) {
            LogUtil.log("eventID = " + eventID + "; key/value:" + key + ":" + value);
        }
    }

    public static void reportError(String error) {
        if (ENABLE) {
            try {
                if (ENABLE_UMENG) {
                    mUmengHelper.reportError(error);
                }
            } catch (Throwable e) {}
        }
        LogUtil.logError(error);
    }

    public static void reportError(Throwable e) {
        if (ENABLE) {
            try {
                if (ENABLE_UMENG) {
                    mUmengHelper.reportError(e);
                }
            } catch (Throwable error) {}
        }
        LogUtil.printError(e);
    }

    public static void onResume(Context context, String pageName) {
        if (ENABLE) {
            try {
                if (ENABLE_UMENG) {
                    mUmengHelper.onResume(context, pageName);
                }
            } catch (Throwable error) {}
        }
        if (DEBUG) {
            LogUtil.log(pageName);
        }
    }

    public static void onPause(Context context, String pageName) {
        if (ENABLE) {
            try {
                if (ENABLE_UMENG) {
                    mUmengHelper.onPause(context, pageName);
                }
            } catch (Throwable error) {}
        }
        if (DEBUG) {
            LogUtil.log(pageName);
        }
    }

    public static void setUserID(String userID) {
        if (ENABLE) {
        }
        if (DEBUG) {
            LogUtil.log(userID);
        }
    }

}
