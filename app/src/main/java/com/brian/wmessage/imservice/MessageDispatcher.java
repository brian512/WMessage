package com.brian.wmessage.imservice;

import com.brian.wmessage.chat.MessageSendHelper;
import com.brian.wmessage.entity.IMMessage;

import java.util.ArrayList;

/**
 * 消息分发
 * @author huamm
 */
public class MessageDispatcher {

    private static MessageDispatcher sInstance;

    private ArrayList<IMessageListener> mMessageListeners;


    private MessageDispatcher() {
        mMessageListeners = new ArrayList<>();
    }

    public static MessageDispatcher getInstance() {
        if (sInstance == null) {
            synchronized (MessageSendHelper.class) {
                if (sInstance == null) {
                    sInstance = new MessageDispatcher();
                }
            }
        }
        return sInstance;
    }

    public void registerListener(IMessageListener listener) {
        synchronized (MessageDispatcher.class) {
            mMessageListeners.add(listener);
        }
    }

    public void unregisterListener(IMessageListener listener) {
        synchronized (MessageDispatcher.class) {
            mMessageListeners.remove(listener);
        }
    }

    public void dispatchMessage(IMMessage message) {
        synchronized (MessageDispatcher.class) {
            for (IMessageListener listener : mMessageListeners) {
                listener.onReceiveMessage(message);
            }
        }
    }


    public interface IMessageListener {
        void onReceiveMessage(IMMessage message);
    }



}
