package com.brian.wmessage.message;

import java.util.ArrayList;

import cn.bmob.newim.bean.BmobIMMessage;

/**
 * 消息分发
 * @author huamm
 */
public class MessageDispatcher {

    private static final MessageDispatcher sInstance = new MessageDispatcher();

    private ArrayList<IMessageListener> mMessageListeners;


    private MessageDispatcher() {
        mMessageListeners = new ArrayList<>();
    }

    public static MessageDispatcher getInstance() {
        return sInstance;
    }

    public void registerListener(IMessageListener listener) {
        synchronized (sInstance) {
            mMessageListeners.add(listener);
        }
    }

    public void unregisterListener(IMessageListener listener) {
        synchronized (sInstance) {
            mMessageListeners.remove(listener);
        }
    }

    public void dispatchMessage(BmobIMMessage message) {
        synchronized (sInstance) {
            for (IMessageListener listener : mMessageListeners) {
                listener.onReceiveMessage(message);
            }
        }
    }


    public interface IMessageListener {
        void onReceiveMessage(BmobIMMessage message);
    }



}
