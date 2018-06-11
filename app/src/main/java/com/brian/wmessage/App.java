package com.brian.wmessage;

import com.brian.common.base.BaseApplication;
import com.brian.wmessage.imservice.IMServiceManager;

/**
 * @author huamm
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess()) {
            IMServiceManager.getInstance().init(this);
        }
    }
}
