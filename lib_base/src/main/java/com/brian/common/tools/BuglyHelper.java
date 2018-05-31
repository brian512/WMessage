package com.brian.common.tools;

import android.content.Context;

import com.brian.common.utils.AppInfoUtil;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 腾讯崩溃收集SDK的封装类
 * 
 * @author huamm
 *
 */
public class BuglyHelper {
    
    /**
     * URL:http://bugly.qq.com/
     */
    private static final String APP_ID = "cbeb451a74";
    
    
    /**
     * 初始化
     */
    public static void init(Context context) {
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(context);
        userStrategy.setAppChannel(AppInfoUtil.sChannelName);
        userStrategy.setAppVersion(AppInfoUtil.getVersion(context));


        CrashReport.initCrashReport(context, APP_ID, DebugConfig.DEBUG_ENABLE, userStrategy);
    }
    
    /**
     * 设置用户ID，使用的随遇userID
     * @param userID
     */
    public static void setUserID(String userID) {
        CrashReport.setUserId(userID);
    }

}
