package com.brian.wmessage;

import com.brian.common.base.BaseApplication;
import com.brian.wmessage.bmob.BmobHelper;

/**
 * @author huamm
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess()) {
            BmobHelper.getInstance().init(this);
        }
    }
}
