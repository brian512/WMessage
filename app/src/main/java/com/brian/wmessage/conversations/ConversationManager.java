package com.brian.wmessage.conversations;

import com.brian.common.utils.LogUtil;
import com.brian.wmessage.entity.Conversation;
import com.brian.wmessage.entity.IMMessage;
import com.brian.wmessage.entity.P2PConversation;
import com.brian.wmessage.entity.UserInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;

public class ConversationManager {

    private Conversation mConversation;

    public ConversationManager(Conversation conversation) {
        mConversation = conversation;
    }


    public static List<Conversation> loadAllConversation() {
        List<Conversation> conversationList = new ArrayList<>();
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if (list != null && list.size() > 0) {
            for (BmobIMConversation item : list) {
                switch (item.getConversationType()) {
                    case 1://私聊
                        if (item.getMessages() != null && item.getMessages().size() > 0) {
//                            LogUtil.d("conversation=" + GsonHelper.toJson(item));
                            conversationList.add(new P2PConversation(item));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }

    public void queryMessages(IMMessage lastMessage, final OnMessageQueryListener listener) {
        BmobIMMessage last = lastMessage == null ? null : lastMessage.getBmobIMMessage();
        mConversation.getConversation().queryMessages(last, 20, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    List<IMMessage> messageList = new ArrayList<>();
                    for (BmobIMMessage message : list) {
                        messageList.add(IMMessage.convert(message));
                    }
                    listener.onFinish(messageList);
                } else {
                    listener.onError(e.getErrorCode(), e.getMessage());
                    LogUtil.d(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    public void updateReceiveStatus(IMMessage message) {
        try {
            // 更新该会话下面的已读状态
            LogUtil.d("mConversation.getConversation().client=" + mConversation.getConversation().client);
            if (mConversation.getConversation().client == null) {
                mConversation.getConversation().client = BmobIMClient.getInstance();
            }
            mConversation.getConversation().updateReceiveStatus(message.mMessage);
        } catch (Exception e) {
            LogUtil.printError(e);
        }
    }

    public static P2PConversation getConversation(UserInfo userInfo) {
        BmobIMUserInfo info = new BmobIMUserInfo(userInfo.getObjectId(), userInfo.getUsername(), userInfo.avatar);
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
        return new P2PConversation(conversationEntrance);
    }

    public static P2PConversation getConversation(IMMessage message) {
        BmobIMConversation conversation = message.getBmobIMMessage().getBmobIMConversation();
        return new P2PConversation(conversation);
    }

    public interface OnMessageQueryListener {
        void onFinish(List<IMMessage> list);
        void onError(int code, String message);
    }
}
