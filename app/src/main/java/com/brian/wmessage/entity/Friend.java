package com.brian.wmessage.entity;

import cn.bmob.v3.BmobObject;

/**
 * 好友表
 *
 * @author huamm
 */
public class Friend extends BmobObject {

    private UserInfo user;
    private UserInfo friendUser;

    private transient String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public UserInfo getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(UserInfo friendUser) {
        this.friendUser = friendUser;
    }
}
