package com.brian.wmessage.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.common.base.BaseActivity;
import com.brian.common.utils.ToastUtil;
import com.brian.wmessage.R;
import com.brian.wmessage.chat.ChatActivity;
import com.brian.wmessage.entity.IMConversation;
import com.brian.wmessage.entity.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.v3.BmobUser;

/**
 * 用户资料
 * @author huamm
 */
public class UserInfoActivity extends BaseActivity {

    private static final String EXTRA_USERINFO = "userinfo";

    @BindView(R.id.iv_avator)
    ImageView iv_avator;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.btn_chat)
    Button btn_chat;


    //用户
    private UserInfo mUserInfo;
    //用户信息
    private BmobIMUserInfo info;

    public static void startActivity(Context context, UserInfo userInfo) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(EXTRA_USERINFO, userInfo);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        //用户
        mUserInfo = (UserInfo) getIntent().getSerializableExtra(EXTRA_USERINFO);
        if (mUserInfo.getObjectId().equals(BmobUser.getCurrentUser(UserInfo.class).getObjectId())) {//用户为登录用户
            btn_chat.setVisibility(View.GONE);
        } else {//用户为非登录用户
            btn_chat.setVisibility(View.VISIBLE);
        }
        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(mUserInfo.getObjectId(), mUserInfo.getUsername(), mUserInfo.getAvatar());
        //加载头像
        mUserInfo.showHead(iv_avator);
        //显示名称
        tv_name.setText(mUserInfo.getUsername());

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
            }
        });
    }


    /**
     * 与陌生人聊天
     */
    private void chat() {
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            ToastUtil.showMsg("尚未连接IM服务器");
            return;
        }
        //TODO 会话：4.1、创建一个常态会话入口，陌生人聊天
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
        ChatActivity.startActivity(this, new IMConversation(conversationEntrance));
    }
}