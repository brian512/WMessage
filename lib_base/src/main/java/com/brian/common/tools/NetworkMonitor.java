package com.brian.common.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.brian.common.utils.LogUtil;
import com.brian.common.utils.NetworkUtil;

import java.util.ArrayList;

/**
 * 网络状态监听
 */
public class NetworkMonitor extends BroadcastReceiver {

    public interface OnNetworkChangedListener {
        void onNetworkChanged(boolean isConnected, int type);
    }

    private static NetworkMonitor sInst;
    private ArrayList<OnNetworkChangedListener> mListeners;

    public static NetworkMonitor getInstance() {
        if (sInst == null) {
            synchronized (NetworkMonitor.class) {
                if (sInst == null) {
                    sInst = new NetworkMonitor();
                }
            }
        }
        return sInst;
    }

    private NetworkMonitor() {
        mListeners = new ArrayList<>();
    }

    public void subscribe(Context ctx, OnNetworkChangedListener l) {
        subscribe(ctx, l, false);
    }

    public void subscribe(Context ctx, OnNetworkChangedListener listener, boolean fireSoon) {
        if (mListeners.size() <= 0) {
            start(ctx);
        }
        mListeners.add(listener);

        if (fireSoon) {
            try {
                boolean conn = NetworkUtil.isNetworkAvailable(ctx);
                int type = NetworkUtil.getNetWorkType(ctx);
                listener.onNetworkChanged(conn, type);
            } catch (Throwable e) {
                LogUtil.printError(e);
            }
        }
    }

    public void unsubscribe(Context ctx, OnNetworkChangedListener listener) {
        mListeners.remove(listener);

        if (mListeners.size() <= 0) {
            stop(ctx);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            final boolean isConnected = NetworkUtil.isNetworkAvailable(context);
            final int type = NetworkUtil.getNetWorkType(context);

            LogUtil.d("isConnected=" + isConnected + "; type=" + type);
            for (OnNetworkChangedListener listener : mListeners) {
                listener.onNetworkChanged(isConnected, type);
            }
        } catch (Throwable e) {
            LogUtil.printError(e);
        }
    }

    private void start(Context ctx) {
        try {
            // 每次一次注册都会触发网络状态回调
            ctx.getApplicationContext().registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Throwable e) {
            LogUtil.printError(e);
        }
    }

    private void stop(Context ctx) {
        try {
            ctx.getApplicationContext().unregisterReceiver(this);
        } catch (Throwable e) {
            LogUtil.printError(e);
        }
    }


}
