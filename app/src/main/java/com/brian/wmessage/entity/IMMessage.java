package com.brian.wmessage.entity;

import java.util.Map;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**
 * @author Brian on 2018/6/9.
 */
public class IMMessage extends BmobIMExtraMessage {


    public BmobIMExtraMessage mMessage;

    public IMMessage(BmobIMExtraMessage message) {
        mMessage = message;
    }

    public Map<String, Object> getExtraMap() {
        return super.getExtraMap();
    }
}
