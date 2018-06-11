package com.brian.wmessage.chat;

import com.brian.common.utils.LogUtil;
import com.brian.wmessage.entity.IMMessage;
import com.brian.wmessage.entity.P2PConversation;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMLocationMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 消息发送封装
 * @author huamm
 */
public class MessageSendHelper {

    private static MessageSendHelper sMessageSendHelper;

    private MessageSendHelper() {}

    public static MessageSendHelper getInstance() {
        if (sMessageSendHelper == null) {
            synchronized (MessageSendHelper.class) {
                if (sMessageSendHelper == null) {
                    sMessageSendHelper = new MessageSendHelper();
                }
            }
        }
        return sMessageSendHelper;
    }

    public interface OnMessageSendListener {
        int STATUS_DONE = 0;
        int STATUS_ERROR = -1;

        void onStart(IMMessage message);
        void onFinish(IMMessage message, int status);
    }

    /**
     * 消息发送监听器
     */
    private MessageSendListener mMessageSendListener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            LogUtil.i("onProgress：" + value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            LogUtil.i("msg：" + msg);
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            LogUtil.i("msg：" + msg);
        }
    };


    public void sendTextMessage(P2PConversation conversation, String text) {
        BmobIMTextMessage textMessage = new BmobIMTextMessage();
        textMessage.setContent(text);
        //可随意设置额外信息
//        Map<String, Object> map = new HashMap<>();
//        map.put("level", "1");
//        msg.setExtraMap(map);
//        msg.setExtra("OK");
        sendMessage(conversation, textMessage);
    }

    public void sendTextMessage(P2PConversation conversation, String text, OnMessageSendListener listener) {
        BmobIMTextMessage textMessage = new BmobIMTextMessage();
        textMessage.setContent(text);
        sendMessage(conversation, textMessage, listener);
    }

    /**
     * 发送语音消息
     */
    public void sendVoiceMessage(P2PConversation conversation, String local) {
        BmobIMAudioMessage audioMsg = new BmobIMAudioMessage(local);
        sendMessage(conversation, audioMsg);
    }

    /**
     * 发送图片消息
     */
    public void sendImageMessage(P2PConversation conversation, String local) {
        BmobIMImageMessage imageMessage = new BmobIMImageMessage(local);
        sendMessage(conversation, imageMessage);
    }

    /**
     * 发送视频消息
     */
    public void sendVideoMessage(P2PConversation conversation, String local) {
        BmobIMVideoMessage videoMessage = new BmobIMVideoMessage(local);
        sendMessage(conversation, videoMessage);
    }

    /**
     * 发送地址消息
     */
    public void sendVideoMessage(P2PConversation conversation, String address, double latitude, double longitude) {
        BmobIMLocationMessage audioMsg = new BmobIMLocationMessage(address, latitude, longitude);
        sendMessage(conversation, audioMsg);
    }

    private void sendMessage(P2PConversation conversation, BmobIMMessage message) {
        sendMessage(conversation, message, null);
    }

    public void sendMessage(P2PConversation conversation, BmobIMMessage bmobIMMessage, final OnMessageSendListener listener) {
        final IMMessage message = IMMessage.convert(bmobIMMessage);
        BmobIMConversation conversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversation.getConversation());
        conversationManager.sendMessage(bmobIMMessage, new MessageSendListener() {

            @Override
            public void onProgress(int value) {
                super.onProgress(value);
                //文件类型的消息才有进度值
                LogUtil.i("onProgress：" + value);
            }

            @Override
            public void onStart(BmobIMMessage msg) {
                super.onStart(msg);
                LogUtil.i("msg：" + msg);
                message.updateData(msg);
                if (listener != null) {
                    listener.onStart(message);
                }
            }

            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                LogUtil.i("msg：" + msg);
                message.updateData(msg);
                if (listener != null) {
                    if (e == null) {
                        listener.onFinish(message, OnMessageSendListener.STATUS_DONE);
                    } else {
                        listener.onFinish(message, OnMessageSendListener.STATUS_ERROR);
                    }
                }
            }
        });
    }

    public void resendMessage(BmobIMConversation conversation, final IMMessage message, final OnMessageSendListener listener) {
        BmobIMConversation conversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversation);
        conversationManager.sendMessage(message.mMessage, new MessageSendListener() {

            @Override
            public void onProgress(int value) {
                super.onProgress(value);
                //文件类型的消息才有进度值
                LogUtil.i("onProgress：" + value);
            }

            @Override
            public void onStart(BmobIMMessage msg) {
                super.onStart(msg);
                LogUtil.i("msg：" + msg);
                message.updateData(msg);
                if (listener != null) {
                    listener.onStart(message);
                }
            }

            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                LogUtil.i("msg：" + msg);
                message.updateData(msg);
                if (listener != null) {
                    if (e == null) {
                        listener.onFinish(message, OnMessageSendListener.STATUS_DONE);
                    } else {
                        listener.onFinish(message, OnMessageSendListener.STATUS_ERROR);
                    }
                }
            }
        });
    }
}
