package com.brian.wmessage.imservice;

import android.content.Context;

import com.brian.common.tools.NetworkMonitor;
import com.brian.common.utils.LogUtil;
import com.brian.wmessage.imservice.bmob.BmobHelper;

import java.util.ArrayList;

import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectStatusChangeListener;

/**
 * IM服务管理
 * @author huamm
 */
public class IMServiceManager implements NetworkMonitor.OnNetworkChangedListener {

    public static final int STATE_DISCONNECT = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_NETWORK_UNAVAILABLE = -1;
    public static final int STATE_KICK_ASS = -2;

    private static IMServiceManager sInstance;

    private int mIMState = 0;

    private ArrayList<IIMServiceStateListener> mStateListeners;


    public static IMServiceManager getInstance() {
        if (sInstance == null) {
            synchronized (IMServiceManager.class) {
                if (sInstance == null) {
                    sInstance = new IMServiceManager();
                }
            }
        }
        return sInstance;
    }

    private IMServiceManager() {
        mStateListeners = new ArrayList<>();
    }

    public void init(Context context) {
        BmobHelper.getInstance().init(context);
        BmobHelper.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus connectionStatus) {
                LogUtil.d("connectionStatus=" + connectionStatus);
                int newState = STATE_DISCONNECT;
                switch (connectionStatus) {
                    case CONNECTED:
                        newState = STATE_CONNECTED;
                        break;
                    case CONNECTING:
                        newState = STATE_CONNECTING;
                        break;
                    case DISCONNECT:
                        newState = STATE_DISCONNECT;
                        break;
                    case KICK_ASS:
                        newState = STATE_KICK_ASS;
                        break;
                    case NETWORK_UNAVAILABLE:
                        newState = STATE_NETWORK_UNAVAILABLE;
                        break;
                }
                LogUtil.d("newState=" + newState + "; mIMState=" + mIMState);
                if (newState != mIMState) {
                    mIMState = newState;
                    notifyIMState(newState);
                }
            }
        });
    }

    public boolean checkConnectState() {
        return mIMState == STATE_CONNECTED;
    }

    public synchronized void registerListener(IIMServiceStateListener listener) {
        mStateListeners.add(listener);
    }

    public synchronized void unregisterListener(IIMServiceStateListener listener) {
        mStateListeners.remove(listener);
    }

    public synchronized void notifyIMState(int state) {
        for (IIMServiceStateListener listener : mStateListeners) {
            listener.onIMStateChang(state);
        }
    }

    @Override
    public void onNetworkChanged(boolean isConnected, int type) {
        if (isConnected) {
            checkConnectState();
        }
    }
}
