package com.brian.wmessage.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brian.common.base.BaseActivity;
import com.brian.common.utils.DipPixelUtil;
import com.brian.common.utils.LogUtil;
import com.brian.common.utils.ToastUtil;
import com.brian.common.views.TitleBar;
import com.brian.common.views.recyclerview.RecyclerViewUtil;
import com.brian.wmessage.R;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.entity.IMConversation;
import com.brian.wmessage.message.MessageDispatcher;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 聊天页面
 * @author huamm
 */
public class ChatActivity extends BaseActivity {

    private static final String EXTRA_CONVERSATION = "conversation";
    private static final String EXTRA_USERINFO = "user_info";

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

    private BmobIMUserInfo mUserInfo;

    private LinearLayoutManager mLayoutManager;

    private IMConversation mConversation;

    public static void startActivity(Context context, IMConversation conversation) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CONVERSATION, conversation);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startActivity(Context context, BmobIMUserInfo userInfo, IMConversation conversation) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CONVERSATION, conversation);
        intent.putExtra(EXTRA_USERINFO, userInfo);
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
        mUserInfo = (BmobIMUserInfo) getIntent().getSerializableExtra(EXTRA_USERINFO);

        mRefreshLayout.setEnabled(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mChatAdapter = new ChatAdapter();
        mRecyclerView.setAdapter(mChatAdapter);

        mRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isKeyboardShown(mRefreshLayout.getRootView())) {
                    scrollToBottom();
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firsItemIndex = mLayoutManager.findFirstVisibleItemPosition();
                    LogUtil.d("firsItemIndex=" + firsItemIndex);
                    if (firsItemIndex <= 1) {
                        queryMessages(false);
                    }
                }
            }
        });

        //下拉加载
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryMessages(false);
            }
        });

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

        queryMessages(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MessageDispatcher.getInstance().registerListener(mMessageListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MessageDispatcher.getInstance().unregisterListener(mMessageListener);
    }

    private MessageDispatcher.IMessageListener mMessageListener = new MessageDispatcher.IMessageListener() {
        @Override
        public void onReceiveMessage(BmobIMMessage message) {
            addMessage2Chat(message);
        }
    };

    /**
     * 添加消息到聊天界面中
     */
    private void addMessage2Chat(BmobIMMessage message) {
        if (mConversation != null && mConversation.getConversation().getConversationId().equals(message.getConversationId()) //如果是当前会话的消息
                && !message.isTransient()) {//并且不为暂态消息
            if (mChatAdapter.findPosition(message) < 0) {//如果未添加到界面中
                mChatAdapter.addMessage(message);
                scrollToBottom();
                try {
                    // 更新该会话下面的已读状态
                    mConversation.getConversation().updateReceiveStatus(message);
                } catch (Exception e) {
                    LogUtil.printError(e);
                }
            }
        } else {
            LogUtil.d("不是与当前聊天对象的消息");
        }
    }

    private boolean isKeyboardShown(View rootView) {
        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128;

        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int heightDiff = rootView.getBottom() - r.bottom;

        return heightDiff > DipPixelUtil.dip2px(SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD);
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
        MessageSendListener listener = new MessageSendListener() {
            @Override
            public void onStart(BmobIMMessage message) {
                super.onStart(message);
                mChatAdapter.addMessage(message);
                scrollToBottom();
            }

            @Override
            public void done(BmobIMMessage message, BmobException e) {
                mChatAdapter.updateMessage(message);
                if (e != null) {
                    LogUtil.d(e.getMessage());
                }
            }
        };
        MessageSendHelper.getInstance().sendTextMessage(mConversation, text, listener);
        edit_msg.setText("");
    }

    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     */
    public void queryMessages(boolean isInit) {
        final BmobIMMessage msg = isInit ? null : mChatAdapter.getFirstMessage();
        mConversation.getConversation().queryMessages(msg, 20, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                mRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        mChatAdapter.addMessages(list);
                        if (msg == null) {
                            scrollToBottom();
                        }
                    } else {
                        mRefreshLayout.setEnabled(false); // 已经加载完毕
                    }
                } else {
                    LogUtil.d(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    private void scrollToBottom() {
        RecyclerViewUtil.scrollToBottom(mRecyclerView);
    }

}
