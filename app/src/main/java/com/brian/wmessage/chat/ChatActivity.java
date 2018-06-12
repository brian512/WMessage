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
import com.brian.common.utils.TextDrawableUtil;
import com.brian.common.utils.ToastUtil;
import com.brian.common.views.TitleBar;
import com.brian.common.views.recyclerview.RecyclerViewUtil;
import com.brian.wmessage.R;
import com.brian.wmessage.imservice.bmob.BmobHelper;
import com.brian.wmessage.conversations.ConversationManager;
import com.brian.wmessage.entity.IMMessage;
import com.brian.wmessage.entity.P2PConversation;
import com.brian.wmessage.imservice.IIMServiceStateListener;
import com.brian.wmessage.imservice.IMServiceManager;
import com.brian.wmessage.imservice.MessageDispatcher;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 聊天页面
 * @author huamm
 */
public class ChatActivity extends BaseActivity implements IIMServiceStateListener {

    private static final String EXTRA_CONVERSATION = "conversation";

    @BindView(R.id.ll_chat)
    LinearLayout mRootLy;

    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.rc_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.chat_titlebar)
    TitleBar mTitleBar;

    @BindView(R.id.edit_msg)
    EditText mMessageEt;
    @BindView(R.id.btn_chat_send)
    TextView mSendBtn;

    private ChatAdapter mChatAdapter;

    private LinearLayoutManager mLayoutManager;

    private P2PConversation mConversation;

    private ConversationManager mConversationManager;

    public static void startActivity(Context context, P2PConversation conversation) {
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
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);

        mConversation = (P2PConversation) getIntent().getSerializableExtra(EXTRA_CONVERSATION);
        mConversationManager = new ConversationManager(mConversation);

        initUI();
        initListeners();


        queryMessages(true);
    }

    private void initUI() {
        mRefreshLayout.setEnabled(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mChatAdapter = new ChatAdapter();
        mRecyclerView.setAdapter(mChatAdapter);

        TextDrawableUtil.setLeftDrawable(mTitleBar.getLeftView(), R.drawable.ic_back);
        mTitleBar.setTitle(mConversation.getConversation().getConversationTitle());
        mTitleBar.getLeftView().setVisibility(View.VISIBLE);
    }

    private void initListeners() {
        mTitleBar.getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BmobHelper.getInstance().checkOnlineState()) {
                    ToastUtil.showMsg("尚未连接IM服务器");
                    return;
                }
                sendMessage();
            }
        });

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        MessageDispatcher.getInstance().registerListener(mMessageListener);
        IMServiceManager.getInstance().registerListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MessageDispatcher.getInstance().unregisterListener(mMessageListener);
        IMServiceManager.getInstance().unregisterListener(this);
    }

    private MessageDispatcher.IMessageListener mMessageListener = new MessageDispatcher.IMessageListener() {
        @Override
        public void onReceiveMessage(IMMessage message) {
            addMessage2Chat(message);
        }
    };

    /**
     * 添加消息到聊天界面中
     */
    private void addMessage2Chat(IMMessage message) {
        LogUtil.d("message=" + message.conversationId);
        if (mConversation != null && mConversation.getConversationId().equals(message.conversationId) //如果是当前会话的消息
                && !message.isTransient) {//并且不为暂态消息
            if (mChatAdapter.findPosition(message) < 0) {//如果未添加到界面中
                mChatAdapter.addMessage(message);
                scrollToBottom();

                mConversationManager.updateReceiveStatus(message);
            }
        } else {
            LogUtil.w("不是与当前聊天对象的消息");
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
        String text = mMessageEt.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            ToastUtil.showMsg("消息内容不能为空");
            return;
        }
        MessageSendHelper.OnMessageSendListener listener = new MessageSendHelper.OnMessageSendListener() {
            @Override
            public void onStart(IMMessage message) {
                // 统一在消息回调里将消息插入到列表
                MessageDispatcher.getInstance().dispatchMessage(message);
            }

            @Override
            public void onFinish(IMMessage message, int status) {
                // 更新消息状态
                mChatAdapter.updateMessage(message);
            }
        };
        if (IMServiceManager.getInstance().checkConnectState()) {
            MessageSendHelper.getInstance().sendTextMessage(mConversation, text, listener);
            mMessageEt.setText("");
        } else {
            ToastUtil.showMsg("连接已断开，请稍候再试");
        }
    }

    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     */
    public void queryMessages(final boolean isInit) {
        final IMMessage msg = isInit ? null : mChatAdapter.getFirstMessage();
        mConversationManager.queryMessages(msg, new ConversationManager.OnMessageQueryListener() {
            @Override
            public void onFinish(List<IMMessage> list) {
                mRefreshLayout.setRefreshing(false);
                if (null != list && list.size() > 0) {
                    if (isInit) {
                        mChatAdapter.bindMessages(list);
                    } else {
                        mChatAdapter.addMessages(list);
                    }
                    if (isInit) { // 仅首次加载需要滚动到底部
                        scrollToBottom();
                    }
                } else {
                    mRefreshLayout.setEnabled(false); // 已经加载完毕
                }
            }

            @Override
            public void onError(int code, String message) {
                ToastUtil.showMsg(message);
            }
        });
    }

    private void scrollToBottom() {
        RecyclerViewUtil.scrollToBottom(mRecyclerView);
    }

    @Override
    public void onIMStateChang(int state) {
        LogUtil.d("state=" + state);
        if (state == IMServiceManager.STATE_CONNECTED) {
            queryMessages(true);
            ToastUtil.showMsg("连接已恢复");
        }
    }
}
