package com.brian.wmessage.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brian.common.base.BaseActivity;
import com.brian.common.utils.LogUtil;
import com.brian.common.utils.ToastUtil;
import com.brian.common.views.TitleBar;
import com.brian.wmessage.R;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.entity.IMConversation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 聊天页面
 * @author huamm
 */
public class ChatActivity extends BaseActivity {

    private static final String EXTRA_CONVERSATION = "conversation";

    @BindView(R.id.ll_chat)
    LinearLayout ll_chat;

    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.rc_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.chat_titlebar)
    TitleBar mTitleBar;

    @BindView(R.id.edit_msg)
    EditText edit_msg;
    @BindView(R.id.btn_chat_send)
    TextView btn_chat_send;

    private ChatAdapter mChatAdapter;

    private IMConversation mConversation;

    public static void startActivity(Context context, IMConversation conversation) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CONVERSATION, conversation);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mConversation = (IMConversation) getIntent().getSerializableExtra(EXTRA_CONVERSATION);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatAdapter = new ChatAdapter();
        mRecyclerView.setAdapter(mChatAdapter);

        mTitleBar.getLeftView().setVisibility(View.GONE);
        mTitleBar.setTitle(mConversation.getConversation().getConversationTitle());


        btn_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BmobHelper.getInstance().checkOnlineState()) {
                    ToastUtil.showMsg("尚未连接IM服务器");
                    return;
                }
                sendMessage();
            }
        });

        queryMessages(null);
    }

    /**
     * 发送文本消息
     */
    private void sendMessage() {
        String text = edit_msg.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            ToastUtil.showMsg("请输入内容");
            return;
        }
        BmobIMTextMessage msg = MessageSendHelper.getInstance().sendTextMessage(mConversation, text);
        mChatAdapter.addMessage(msg);
        edit_msg.setText("");
    }

    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     *
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg) {
        //TODO 消息：5.2、查询指定会话的消息记录
        mConversation.getConversation().queryMessages(msg, 20, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        mChatAdapter.addMessages(list);
                    }
                } else {
                    LogUtil.d(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }
}
