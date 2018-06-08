package com.brian.wmessage.entity;

import cn.bmob.v3.BmobUser;

/**
 * @author huamm
 */
public class UserInfo extends BmobUser {

    private String avatar;

    public String getUserId() {
        return getObjectId();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
