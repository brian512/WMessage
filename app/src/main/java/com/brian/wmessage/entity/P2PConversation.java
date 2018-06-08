package com.brian.wmessage.entity;

import android.content.Context;
import android.text.TextUtils;

import com.brian.wmessage.R;
import com.brian.wmessage.chat.ChatActivity;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversationType;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;

/**
 * 一对一会话
 * @author huamm on 2016/5/25.
 */
public class P2PConversation extends Conversation {

    private IMConversation mConversation;
    private BmobIMMessage lastMsg;

    public P2PConversation(IMConversation conversation) {
        this.mConversation = conversation;
        cType = BmobIMConversationType.setValue(conversation.getConversation().getConversationType());
        cId = conversation.getConversation().getConversationId();
        if (cType == BmobIMConversationType.PRIVATE) {
            cName = conversation.getConversation().getConversationTitle();
            if (TextUtils.isEmpty(cName)) cName = cId;
        } else {
            cName = "未知会话";
        }
        List<BmobIMMessage> msgs = conversation.getConversation().getMessages();
        if (msgs != null && msgs.size() > 0) {
            lastMsg = msgs.get(0);
        }
    }

    public IMConversation getConversation() {
        return mConversation;
    }

    @Override
    public void readAllMessages() {
        mConversation.getConversation().updateLocalCache();
    }

    @Override
    public Object getAvatar() {
        if (cType == BmobIMConversationType.PRIVATE) {
            String avatar = mConversation.getConversation().getConversationIcon();
            if (TextUtils.isEmpty(avatar)) {//头像为空，使用默认头像
                return R.mipmap.default_head_1;
            } else {
                return avatar;
            }
        } else {
            return R.mipmap.default_head_0;
        }
    }

    @Override
    public String getLastMessageContent() {
        if (lastMsg != null) {
            String content = lastMsg.getContent();
            if (lastMsg.getMsgType().equals(BmobIMMessageType.TEXT.getType()) || lastMsg.getMsgType().equals("agree")) {
                return content;
            } else if (lastMsg.getMsgType().equals(BmobIMMessageType.IMAGE.getType())) {
                return "[图片]";
            } else if (lastMsg.getMsgType().equals(BmobIMMessageType.VOICE.getType())) {
                return "[语音]";
            } else if (lastMsg.getMsgType().equals(BmobIMMessageType.LOCATION.getType())) {
                return "[位置]";
            } else if (lastMsg.getMsgType().equals(BmobIMMessageType.VIDEO.getType())) {
                return "[视频]";
            } else {//开发者自定义的消息类型，需要自行处理
                return "[未知]";
            }
        } else {//防止消息错乱
            return "";
        }
    }

    @Override
    public long getLastMessageTime() {
        if (lastMsg != null) {
            return lastMsg.getCreateTime();
        } else {
            return 0;
        }
    }

    @Override
    public int getUnReadCount() {
        return (int) BmobIM.getInstance().getUnReadCount(mConversation.getConversation().getConversationId());
    }

    @Override
    public void onClick(Context context) {
        ChatActivity.startActivity(context, mConversation);
    }

    @Override
    public void onLongClick(Context context) {
        BmobIM.getInstance().deleteConversation(mConversation.getConversation());
    }
}
