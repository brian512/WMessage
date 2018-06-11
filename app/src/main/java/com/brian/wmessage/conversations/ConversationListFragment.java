package com.brian.wmessage.conversations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brian.common.base.BaseFragment;
import com.brian.common.utils.HandlerUtil;
import com.brian.common.utils.LogUtil;
import com.brian.wmessage.R;
import com.brian.wmessage.entity.Conversation;
import com.brian.wmessage.entity.IMMessage;
import com.brian.wmessage.entity.P2PConversation;
import com.brian.wmessage.imservice.IIMServiceStateListener;
import com.brian.wmessage.imservice.IMServiceManager;
import com.brian.wmessage.imservice.MessageDispatcher;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * 回话列表
 * @author huamm
 */
public class ConversationListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private SmartRefreshLayout mRefreshLayout;

    private ConversationListAdapter mListAdapter;


    public static ConversationListFragment newInstance() {
        ConversationListFragment fragment = new ConversationListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLy = inflater.inflate(R.layout.list_fragment_layout, null);
        mRefreshLayout = rootLy.findViewById(R.id.refreshLayout);
        mRecyclerView = rootLy.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListAdapter = new ConversationListAdapter();
        mRecyclerView.setAdapter(mListAdapter);

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);

        return rootLy;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListAdapter.bindData(getConversations());
        // 更新数据
        HandlerUtil.getUIHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListAdapter.bindData(getConversations());
            }
        }, 500);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MessageDispatcher.getInstance().registerListener(mMessageListener);

        IMServiceManager.getInstance().registerListener(new IIMServiceStateListener() {
            @Override
            public void onIMStateChang(int state) {
                LogUtil.d("state=" + state);
                if (state == IMServiceManager.STATE_CONNECTED) {
                    mListAdapter.bindData(getConversations());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        MessageDispatcher.getInstance().unregisterListener(mMessageListener);
        super.onDestroy();
    }

    private MessageDispatcher.IMessageListener mMessageListener = new MessageDispatcher.IMessageListener() {
        @Override
        public void onReceiveMessage(IMMessage message) {
            P2PConversation p2PConversation = ConversationManager.getConversation(message);
            p2PConversation.setLastMsg(message);
            mListAdapter.addOrUpdate(0, p2PConversation);
        }
    };

    /**
     * 获取会话列表的数据
     */
    private List<Conversation> getConversations() {
        return ConversationManager.loadAllConversation();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mListAdapter != null && IMServiceManager.getInstance().checkConnectState()) {
            mListAdapter.bindData(getConversations());
        }
    }
}
