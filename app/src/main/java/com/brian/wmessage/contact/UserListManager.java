package com.brian.wmessage.contact;

import com.brian.wmessage.entity.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian on 2018/6/10.
 */
public class UserListManager {

    private static UserListManager sInstance = new UserListManager();

    private Map<String, UserInfo> mUserInfoMap;


    public static UserListManager getInstance() {
        return sInstance;
    }

    private UserListManager() {
        mUserInfoMap = new HashMap<>();
    }

    public void addUser(UserInfo info) {
        mUserInfoMap.put(info.getUserId(), info);
    }

    public UserInfo getUserInfo(String userId) {
        return mUserInfoMap.get(userId);
    }

}
