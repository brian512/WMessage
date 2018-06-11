package com.brian.wmessage.bmob;

import android.content.Context;
import android.text.TextUtils;

import com.brian.common.base.IBaseRequestDataListener;
import com.brian.common.utils.LogUtil;
import com.brian.common.utils.RandomUtil;
import com.brian.wmessage.contact.UserListManager;
import com.brian.wmessage.entity.FriendInfo;
import com.brian.wmessage.entity.UserInfo;
import com.brian.wmessage.message.WMessageHandler;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Bmob接口调用封装
 * @author huamm
 */
public class BmobHelper {

    private static BmobHelper sInstance = new BmobHelper();

    private BmobHelper() {}

    public static BmobHelper getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        BmobIM.init(context);
        BmobIM.registerDefaultMessageHandler(new WMessageHandler());
    }

    public boolean isMySelef(String userId) {
        return TextUtils.equals(userId, getCurrentUser().getUserId());
    }

    public UserInfo getCurrentUser() {
        return BmobUser.getCurrentUser(UserInfo.class);
    }

    public boolean checkOnlineState() {
        final UserInfo user = BmobUser.getCurrentUser(UserInfo.class);
        if (user == null) {
            return false;
        }
        // 登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        // 判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getUserId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getUserId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        // 服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        // 更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.avatar));
//                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                        LogUtil.d(e.getMessage());
                    }
                }
            });
            // 监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    LogUtil.d(status.getMsg());
                }
            });
        }
        return true;
    }


    /**
     * 查询好友
     */
    public void queryFriends(final FindListener<FriendInfo> listener) {
        BmobQuery<FriendInfo> query = new BmobQuery<>();
        UserInfo user = BmobUser.getCurrentUser(UserInfo.class);
        query.addWhereEqualTo("user", user);
        query.include("friendUser");
        query.order("-updatedAt");
        query.findObjects(new FindListener<FriendInfo>() {
            @Override
            public void done(List<FriendInfo> list, BmobException e) {
                if (e != null) {
                    listener.done(list, e);
                } else {
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            FriendInfo friend = list.get(i);
                            String username = friend.getFriendUser().getUsername();
                            if (username != null) {
                                String pinyin = Pinyin.toPinyin(username.charAt(0));
                                friend.setPinyin(pinyin.substring(0, 1).toUpperCase());
                            }
                        }
                        listener.done(list, null);
                    } else {
                        listener.done(list, new BmobException(0, "暂无联系人"));
                    }
                }
            }
        });
    }


    /**
     * 登录
     */
    public void login(String username, String password, final OnLoginListener listener) {
        final UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo user, BmobException e) {
                if (e == null) {
                    listener.onFinish(BmobUser.getCurrentUser(UserInfo.class));
                } else {
                    listener.onError(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }

    /**
     * 注册
     */
    public void register(String username, String password, final OnLoginListener listener) {
        final UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(password);
        user.avatar = "" + RandomUtil.getRandInt(UserInfo.DEFAULT_HEADS.length);
        user.signUp(new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo user, BmobException e) {
                if (e == null) {
                    listener.onFinish(user);
                } else {
                    listener.onError(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }


    /**
     * 查询用户
     */
    public void queryUsers(String username, final int limit, final OnUserQueryListener listener) {
        BmobQuery<UserInfo> query = new BmobQuery<>();
        //去掉当前用户
        try {
            BmobUser user = BmobUser.getCurrentUser();
            query.addWhereNotEqualTo("username", user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    if (list != null) {
                        for (UserInfo info : list) {
                            UserListManager.getInstance().addUser(info);
                        }
                        listener.onFinish(list);
                    } else {
                        listener.onFinish(new ArrayList<UserInfo>());
                    }
                } else {
                    listener.onError(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }

    public void logout() {
        BmobUser.logOut();
        BmobIM.getInstance().disConnect();
    }

    public BmobIMUserInfo getUserInfo(String userId) {
        return BmobIM.getInstance().getUserInfo(userId);
    }

    public interface OnUserQueryListener extends IBaseRequestDataListener {
        void onFinish(List<UserInfo> list);
    }

    public interface OnLoginListener extends IBaseRequestDataListener {
        void onFinish(UserInfo userInfo);
    }
}
