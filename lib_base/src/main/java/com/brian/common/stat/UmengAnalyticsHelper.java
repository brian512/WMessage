package com.brian.common.stat;

import android.content.Context;

import com.brian.common.utils.AppInfoUtil;
import com.brian.common.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * 友盟统计
 * Created by huamm on 2017/3/29 0029.
 */

public class UmengAnalyticsHelper {

    private Context mContext;

    public UmengAnalyticsHelper(Context context) {
        mContext = context.getApplicationContext();
        init();
    }

    private void init() {
        String umengKey = "5b0fb1d98f4a9d2fa20000b0";
        String channel = AppInfoUtil.sChannelName;
        LogUtil.log("umengKey=" + umengKey + "; channel=" + channel);
        MobclickAgent.setCheckDevice(false); // 不采集mac地址，默认为true
        MobclickAgent. startWithConfigure(new MobclickAgent.UMAnalyticsConfig(mContext, umengKey, channel, MobclickAgent.EScenarioType.E_UM_NORMAL));

        MobclickAgent.enableEncrypt(true);
        MobclickAgent.setCatchUncaughtExceptions(false);
    }

    public void onResume(Context context, String pageName) {
        MobclickAgent.onResume(context);
        MobclickAgent.onPageStart(pageName);
    }
    public void onPause(Context context, String pageName) {
        MobclickAgent.onPageEnd(pageName);
        MobclickAgent.onPause(context);
    }

    public void onEvent(String eventID) {
        MobclickAgent.onEvent(mContext, eventID);
    }

    public void onEvent(String eventID, String key) {
        MobclickAgent.onEvent(mContext, eventID, key);
    }

    public void onEvent(String eventID, HashMap<String,String> map) {
        MobclickAgent.onEvent(mContext, eventID, map);
    }

    public void reportError(Throwable e) {
        MobclickAgent.reportError(mContext, e);
    }

    public void reportError(String error) {
        MobclickAgent.reportError(mContext, error);
    }

}
