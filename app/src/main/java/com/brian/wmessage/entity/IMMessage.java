package com.brian.wmessage.entity;

import com.brian.common.tools.GsonHelper;
import com.brian.common.utils.LogUtil;

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
        LogUtil.d("mFromUserInfo=" + GsonHelper.toJson(mFromUserInfo));
        mFromUserInfo = fromUserInfo;
    }

    public BmobIMUserInfo getFromUserInfo() {
        return mFromUserInfo;
    }
}
