package com.brian.wmessage;

import com.brian.common.base.BaseApplication;
import com.brian.common.utils.LogUtil;
import com.brian.wmessage.imservice.IMServiceManager;

/**
 * @author huamm
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.mIsDebugMode = BuildConfig.DEBUG_ENABLE;
        if (isMainProcess()) {
            IMServiceManager.getInstance().init(this);
        }
    }
}
