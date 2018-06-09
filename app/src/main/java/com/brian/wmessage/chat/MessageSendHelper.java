package com.brian.wmessage.chat;

import com.brian.common.utils.LogUtil;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.entity.IMConversation;
import com.brian.wmessage.entity.IMMessage;
import com.brian.wmessage.message.MessageDispatcher;

import java.util.HashMap;
import java.util.Map;

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

    private static MessageSendHelper sMessageSendHelper = new MessageSendHelper();

    private MessageSendHelper() {}

    public static MessageSendHelper getInstance() {
        return sMessageSendHelper;
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


    public BmobIMTextMessage sendTextMessage(IMConversation conversation, String text) {
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        //可随意设置额外信息
//        Map<String, Object> map = new HashMap<>();
//        map.put("level", "1");
//        msg.setExtraMap(map);
//        msg.setExtra("OK");
        sendMessage(conversation, new IMMessage(msg));
        return msg;
    }

    public BmobIMTextMessage sendTextMessage(IMConversation conversation, String text, MessageSendListener listener) {
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        sendMessage(conversation, new IMMessage(msg), listener);
        return msg;
    }

    /**
     * 发送语音消息
     */
    public void sendVoiceMessage(IMConversation conversation, String local) {
        BmobIMAudioMessage audioMsg = new BmobIMAudioMessage(local);
        sendMessage(conversation, new IMMessage(audioMsg));
    }

    /**
     * 发送图片消息
     */
    public void sendImageMessage(IMConversation conversation, String local) {
        BmobIMImageMessage audioMsg = new BmobIMImageMessage(local);
        sendMessage(conversation, new IMMessage(audioMsg));
    }

    /**
     * 发送视频消息
     */
    public void sendVideoMessage(IMConversation conversation, String local) {
        BmobIMVideoMessage audioMsg = new BmobIMVideoMessage(local);
        sendMessage(conversation, new IMMessage(audioMsg));
    }

    /**
     * 发送地址消息
     */
    public void sendVideoMessage(IMConversation conversation, String address, double latitude, double longitude) {
        BmobIMLocationMessage audioMsg = new BmobIMLocationMessage(address, latitude, longitude);
        sendMessage(conversation, new IMMessage(audioMsg));
    }

    private void sendMessage(IMConversation conversation, IMMessage message) {
        sendMessage(conversation, message, mMessageSendListener);
    }

    public void sendMessage(IMConversation conversation, IMMessage message, MessageSendListener listener) {
        BmobIMConversation conversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversation.getConversation());
        //可随意设置额外信息
        Map<String, Object> map = message.getExtraMap();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put("my_avatar", BmobHelper.getInstance().getCurrentUser().getAvatar());
        map.put("my_name", BmobHelper.getInstance().getCurrentUser().getUsername());
        message.mMessage.setExtraMap(map);
        conversationManager.sendMessage(message.mMessage, listener);
        MessageDispatcher.getInstance().dispatchMessage(message.mMessage);
    }

}
