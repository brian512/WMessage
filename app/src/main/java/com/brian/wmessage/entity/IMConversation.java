package com.brian.wmessage.entity;

import java.io.Serializable;

import cn.bmob.newim.bean.BmobIMConversation;

public class IMConversation implements Serializable {

    private BmobIMConversation mConversation;

    public IMConversation(BmobIMConversation conversation) {
        mConversation = conversation;
    }

    public void setConversation(BmobIMConversation conversation) {
        mConversation = conversation;
    }

    public BmobIMConversation getConversation() {
        return mConversation;
    }
}
