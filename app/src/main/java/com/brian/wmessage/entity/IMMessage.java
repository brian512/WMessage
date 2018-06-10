package com.brian.wmessage.entity;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * @author Brian on 2018/6/9.
 */
public class IMMessage extends BmobIMMessage {


    public BmobIMMessage mMessage;

    public BmobIMUserInfo mFromUserInfo;

    public IMMessage(BmobIMMessage message) {
        mMessage = message;
    }

    public BmobIMMessage getBmobIMMessage() {
        return mMessage;
    }

    public void setFromUserInfo(BmobIMUserInfo fromUserInfo) {
        mFromUserInfo = fromUserInfo;
    }

    public BmobIMUserInfo getFromUserInfo() {
        return mFromUserInfo;
    }
}
