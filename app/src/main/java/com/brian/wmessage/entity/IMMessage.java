package com.brian.wmessage.entity;

import com.brian.common.tools.GsonHelper;
import com.brian.common.utils.LogUtil;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;

/**
 * @author Brian on 2018/6/9.
 */
public class IMMessage extends BaseType {

    public static final int MSG_TYPE_UNKNOW = -1;
    public static final int MSG_TYPE_TEXT = 0;
    public static final int MSG_TYPE_IMAGE = 1;
    public static final int MSG_TYPE_AUDIO = 2;
    public static final int MSG_TYPE_VIDEO = 3;
    public static final int MSG_TYPE_LOCATION = 4;


    public static final int STATUS_NULL = 0;
    public static final int STATUS_SENDING = 1;
    public static final int STATUS_SENT = 2;
    public static final int STATUS_SEND_FAILED = 3;
    public static final int STATUS_RECEIPT = 4;
    public static final int STATUS_UPLOAD_FAILED = 5;

    public String msgId;
    public String conversationId;
    public String fromId;
    public String toId;
    public String content;
    public int msgType;
    public int sendStatus;
    public int receiveStatus;
    public long createTime;
    public long updateTime;
    public transient boolean isTransient;
    public boolean isDraft;

    public UserInfo mUserInfo;

    public BmobIMMessage mMessage;

    public IMMessage() {
    }

    public IMMessage(BmobIMMessage message) {
        mMessage = message;
    }

    public BmobIMMessage getBmobIMMessage() {
        return mMessage;
    }

    public String formatMessage() {
        if (msgType == MSG_TYPE_TEXT) {
            if (isDraft) {
                return "[草稿]" + content;
            }
            return content;
        } else if (msgType == MSG_TYPE_IMAGE) {
            return "[图片]";
        } else if (msgType == MSG_TYPE_AUDIO) {
            return "[语音]";
        } else if (msgType == MSG_TYPE_LOCATION) {
            return "[位置]";
        } else if (msgType == MSG_TYPE_VIDEO) {
            return "[视频]";
        } else {//开发者自定义的消息类型，需要自行处理
            return "[未知]";
        }
    }

    public static IMMessage convert(BmobIMMessage bmobMessage) {
        IMMessage message = new IMMessage();
        message.updateData(bmobMessage);
        return message;
    }

    public void updateData(BmobIMMessage bmobMessage) {
        IMMessage message = this;
        message.content = bmobMessage.getContent();
        message.msgId = String.valueOf(bmobMessage.getId());
        message.toId = bmobMessage.getToId();
        message.fromId = bmobMessage.getFromId();
        message.conversationId = bmobMessage.getConversationId();
        message.sendStatus = bmobMessage.getSendStatus();
        message.receiveStatus = bmobMessage.getReceiveStatus();
        message.createTime = bmobMessage.getCreateTime();
        message.updateTime = bmobMessage.getUpdateTime();
        message.isTransient = bmobMessage.isTransient();
        message.isDraft = false;
        try {
            message.mUserInfo = UserInfo.convert(bmobMessage.getBmobIMUserInfo());
        } catch (Exception e) {
            LogUtil.printError(e);
        }

        message.mMessage = bmobMessage;

        if (bmobMessage.getMsgType().equals(BmobIMMessageType.TEXT.getType())) {
            message.msgType = MSG_TYPE_TEXT;
        } else if (bmobMessage.getMsgType().equals(BmobIMMessageType.IMAGE.getType())) {
            message.msgType = MSG_TYPE_IMAGE;
        } else if (bmobMessage.getMsgType().equals(BmobIMMessageType.VOICE.getType())) {
            message.msgType = MSG_TYPE_AUDIO;
        } else if (bmobMessage.getMsgType().equals(BmobIMMessageType.LOCATION.getType())) {
            message.msgType = MSG_TYPE_LOCATION;
        } else if (bmobMessage.getMsgType().equals(BmobIMMessageType.VIDEO.getType())) {
            message.msgType = MSG_TYPE_VIDEO;
        } else {
            message.msgType = MSG_TYPE_UNKNOW;
        }
    }

    @Override
    public String toString() {
        return super.toString() + GsonHelper.toJson(mMessage);
    }
}
