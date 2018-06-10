package com.brian.wmessage.message;

import com.brian.wmessage.entity.IMMessage;

import java.util.ArrayList;

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

    public void dispatchMessage(IMMessage message) {
        synchronized (sInstance) {
            for (IMessageListener listener : mMessageListeners) {
                listener.onReceiveMessage(message);
            }
        }
    }


    public interface IMessageListener {
        void onReceiveMessage(IMMessage message);
    }



}
