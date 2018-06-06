package com.brian.wmessage.message;

import java.util.ArrayList;

/**
 * 消息分发
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
        mMessageListeners.add(listener);
    }

    public void unregisterListener(IMessageListener listener) {
        mMessageListeners.remove(listener);
    }


    public interface IMessageListener {

    }



}
