package com.brian.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.brian.common.stat.Stat;
import com.brian.common.tools.NetworkMonitor;
import com.brian.common.utils.InputMethodUtil;
import com.brian.common.utils.LogUtil;
import com.brian.common.utils.SDKUtil;

public abstract class BaseActivity extends AppCompatActivity implements NetworkMonitor.OnNetworkChangedListener {

    /**
     * 记录当前最顶部的Activity
     */
    private static BaseActivity sTopActivity = null;

    /**
     * 记录界面切这个界面的时候是否是在后台
     */
    private static boolean sIsInBackstage = false;

    /**
     * 标志activity是否正处於活跃状态
     */
    private static boolean mIsAppActive = false;

    /**
     * 获取顶部的Activity
     */
    public static BaseActivity getTopActivity() {
        return sTopActivity;
    }

    private boolean isStatusBarTranslate = false;

    private boolean isFullScreen = false;

    /**
     * onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.log(getClass().getSimpleName());

        sTopActivity = this; // 确保最新一个Activity创建后，TopActivity立马指向它，此时一些操作可以在onCreate函数执行
    }

    @Override
    protected void onStart() {
        LogUtil.log(getClass().getSimpleName());
        super.onStart();
    }

    @Override
    protected void onRestart() {
        LogUtil.log(getClass().getSimpleName());
        super.onRestart();
    }

    /**
     * onResume
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.log(getClass().getSimpleName());

        sTopActivity = this; // 确保上一个Activity销毁后，TopActivity指到最上一个Activity
        mIsAppActive = true;
        Stat.onResume(this, getClass().getSimpleName());

        NetworkMonitor.getInstance().subscribe(this, this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LogUtil.log("onPostCreate：" + getClass().getSimpleName());
    }

    /**
     * onPause
     */
    @Override
    protected void onPause() {
        Stat.onPause(this, getClass().getSimpleName());
        mIsAppActive = false;

        LogUtil.log(getClass().getSimpleName());

        NetworkMonitor.getInstance().unsubscribe(this, this);
        super.onPause();
    }

    /**
     * onDestroy
     */
    @Override
    protected void onDestroy() {
        LogUtil.log(getClass().getSimpleName());

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        LogUtil.log(getClass().getSimpleName());
        InputMethodUtil.hiddenInput(this, getWindow().getDecorView());

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtil.log(getClass().getSimpleName());
        // 在 onSaveInstanceState 和onCreate 都不能用  PersistableBundle persistentState 这个参数，否则EventBus 可能会出现崩溃
        // 具体见：http://blog.csdn.net/lovejjfg/article/details/49796665
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        LogUtil.log(getClass().getSimpleName());
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.log(getClass().getSimpleName());
        super.onNewIntent(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtil.log("onRequestPermissionsResult：" + getClass().getSimpleName() + "; requestCode=" + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * onLowMemory
     */
    @Override
    public void onLowMemory() {
        LogUtil.log(getClass().getSimpleName());
        super.onLowMemory();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LogUtil.log("onWindowFocusChanged：" + getClass().getSimpleName() + "; hasFocus=" + hasFocus);
    }

    private long mLastTouchDownTime;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) { // 防止连击事件
            if (System.currentTimeMillis() - mLastTouchDownTime < 300) {
                return true;
            }
            mLastTouchDownTime = System.currentTimeMillis();
        }

        return super.dispatchTouchEvent(ev);
    }

    public static boolean isAppActive() {
        return mIsAppActive;
    }

    /**
     * 若状态栏的状态为FLAG_TRANSLUCENT_STATUS，则需要在titlebar添加一个填充view
     */
    public boolean isStatusBarTranslate() {
        return isStatusBarTranslate;
    }

    /**
     * 初始化状态栏相关，
     * PS: FLAG_TRANSLUCENT_STATUS会让布局占用状态栏
     * SDK23：可以设置状态栏为浅色（SYSTEM_UI_FLAG_LIGHT_STATUS_BAR），字体就回反转为黑色。
     */
    protected void initStatusBar() {
        isStatusBarTranslate = SDKUtil.initTranslucentStatusBar(this);
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    /**
     * 动态切换是否全屏显示
     */
    public void setFullScreenEnable(boolean enable) {
        LogUtil.log("isFullScreen=" + enable);
        if (SDKUtil.setFullScreenEnable(this, enable)) {
            isFullScreen = enable;
            initStatusBar();
        } else {
            isFullScreen = false;
        }
    }

    /**
     * 是否保持常亮
     */
    public void setKeepScreenOn(boolean enable) {
        SDKUtil.setKeepScreenOn(this, enable);
    }

    /**
     * 在UI执行任务
     */
    public void post(Runnable action) {
        getWindow().getDecorView().post(action);
    }

    public void postDelay(Runnable action, long delay) {
        getUIHandler().postDelayed(action, delay);
    }

    @Override
    public void onNetworkChanged(boolean isConnected, int type) {
    }

    private static Handler sH;
    public static Handler getUIHandler() {
        synchronized (BaseActivity.class) {
            if (sH == null && sTopActivity != null) {
                sH = sTopActivity.getWindow().getDecorView().getHandler();
            }
            if (sH == null) {
                sH = new Handler(Looper.getMainLooper());
            }
        }
        return sH;
    }
}
