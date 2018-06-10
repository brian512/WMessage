package com.brian.wmessage.entity;

import android.content.Context;
import android.text.TextUtils;

import com.brian.common.tools.GsonHelper;
import com.brian.common.utils.LogUtil;
import com.brian.wmessage.R;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.chat.ChatActivity;
import com.brian.wmessage.contact.UserListManager;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMConversationType;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 一对一会话
 * @author huamm on 2016/5/25.
 */
public class P2PConversation extends Conversation {

    private BmobIMMessage lastMsg;

    public P2PConversation(BmobIMConversation conversation) {
        super(conversation);
        cType = BmobIMConversationType.setValue(conversation.getConversationType());
        cId = conversation.getConversationId();
        if (cType == BmobIMConversationType.PRIVATE) {
            cName = conversation.getConversationTitle();
            if (TextUtils.isEmpty(cName)) cName = cId;
        } else {
            cName = "未知会话";
        }
        List<BmobIMMessage> msgs = conversation.getMessages();
        if (msgs != null && msgs.size() > 0) {
            lastMsg = msgs.get(0);
        }
    }

    public void setLastMsg(BmobIMMessage message) {
        if (message != null) {
            lastMsg = message;
        }
    }

    public BmobIMMessage getLastMsg() {
        return lastMsg;
    }

    public BmobIMConversation getConversation() {
        return mConversation;
    }

    @Override
    public void readAllMessages() {
        mConversation.updateLocalCache();
    }

    @Override
    public Object getAvatar() {
        if (cType == BmobIMConversationType.PRIVATE) {
            String avatar = mConversation.getConversationIcon();
            if (TextUtils.isEmpty(avatar)) {
                // 自带的聊天图标为空，则从聊天消息列表获取
                List<BmobIMMessage> messageList = mConversation.getMessages();
                for (BmobIMMessage message : messageList) {
                    LogUtil.d("message=" + GsonHelper.toJson(message));
                    // 消息发送者id 与 聊天id相同，则读取该用户的头像
                    if (TextUtils.equals(message.getFromId(), message.getConversationId())) {
                        BmobIMUserInfo userInfo = BmobHelper.getInstance().getUserInfo(message.getFromId());
//                        LogUtil.d("BmobIMUserInfo=" + GsonHelper.toJson(userInfo));
                        if (userInfo != null && !TextUtils.isEmpty(userInfo.getAvatar())) {
                            return userInfo.getAvatar();
                        } else {
                            UserInfo info = UserListManager.getInstance().getUserInfo(message.getFromId());
//                            LogUtil.d("UserInfo=" + GsonHelper.toJson(info));
                            if (info != null && !TextUtils.isEmpty(info.getAvatar())) {
                                return info.getAvatar();
                            }
                        }
                        if (message.getBmobIMUserInfo() != null && !TextUtils.isEmpty(message.getBmobIMUserInfo().getAvatar())) {
                            avatar = message.getBmobIMUserInfo().getAvatar();
                            break;
                        }
                    }
                }
                if (avatar == null) {
                    avatar = "";
                }
            }
            LogUtil.d("mAvatarIv=" + avatar);
            return avatar;
        } else {
            return R.mipmap.default_head_0;
        }
    }

    @Override
    public String getName() {
        if (cType == BmobIMConversationType.PRIVATE) {
            // 与 获取头像类似
            String name = mConversation.getConversationTitle();
            List<BmobIMMessage> messageList = mConversation.getMessages();
            for (BmobIMMessage message : messageList) {
//                LogUtil.d("message=" + GsonHelper.toJson(message));
                if (TextUtils.equals(message.getFromId(), message.getConversationId())) {
                    BmobIMUserInfo userInfo = BmobHelper.getInstance().getUserInfo(message.getFromId());
                    if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
                        return userInfo.getName();
                    } else {
                        UserInfo info = UserListManager.getInstance().getUserInfo(message.getFromId());
                        if (info != null && !TextUtils.isEmpty(info.getUsername())) {
                            return info.getUsername();
                        }
                    }
//                    LogUtil.d("getBmobIMUserInfo=" + GsonHelper.toJson(message.getBmobIMUserInfo()));
                    if (message.getBmobIMUserInfo() != null && !TextUtils.isEmpty(message.getBmobIMUserInfo().getAvatar())) {
                        name = message.getBmobIMUserInfo().getName();
                        break;
                    }
                    break;
                }
            }
            if (name == null) {
                name = "";
            }
            LogUtil.d("name=" + name);
            return name;
        } else {
            return cName;
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
        return (int) BmobIM.getInstance().getUnReadCount(mConversation.getConversationId());
    }

    @Override
    public void onClick(Context context) {
        ChatActivity.startActivity(context, this);
    }

    @Override
    public void onLongClick(Context context) {
        BmobIM.getInstance().deleteConversation(mConversation);
    }
}
