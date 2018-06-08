package com.brian.common.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.brian.common.tools.BuglyHelper;
import com.brian.common.tools.Env;
import com.brian.common.utils.AppInfoUtil;
import com.brian.common.utils.LogUtil;

public class BaseApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LogUtil.d(getClass().getSimpleName());
        Env.setContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(getClass().getSimpleName());
        final Context appContext = getApplicationContext();

        BuglyHelper.init(appContext); // 初始化Bugly崩溃收集器

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.d(getClass().getSimpleName());
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtil.d("level=" + level);
    }

    public boolean isMainProcess() {
        return getApplicationInfo().packageName.equals(AppInfoUtil.getMyProcessName());
    }

}
