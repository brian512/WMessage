package com.brian.wmessage.conversations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brian.common.base.BaseFragment;
import com.brian.common.tools.GsonHelper;
import com.brian.common.utils.HandlerUtil;
import com.brian.common.utils.LogUtil;
import com.brian.wmessage.R;
import com.brian.wmessage.entity.Conversation;
import com.brian.wmessage.entity.IMMessage;
import com.brian.wmessage.entity.P2PConversation;
import com.brian.wmessage.message.MessageDispatcher;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;

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
    }

    @Override
    public void onDestroy() {
        MessageDispatcher.getInstance().unregisterListener(mMessageListener);
        super.onDestroy();
    }

    private MessageDispatcher.IMessageListener mMessageListener = new MessageDispatcher.IMessageListener() {
        @Override
        public void onReceiveMessage(IMMessage message) {
            BmobIMMessage bmobIMMessage = message.getBmobIMMessage();
            LogUtil.d("mFromUserInfo=" + GsonHelper.toJson(message.mFromUserInfo));
            BmobIMConversation conversation = bmobIMMessage.getBmobIMConversation();
            P2PConversation p2PConversation = new P2PConversation(conversation);
            p2PConversation.setLastMsg(bmobIMMessage);
            LogUtil.d("message=" + bmobIMMessage.toString());
            mListAdapter.addOrUpdate(0, p2PConversation);
        }
    };

    /**
     * 获取会话列表的数据：增加新朋友会话
     */
    private List<Conversation> getConversations() {
        //添加会话
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        //查询全部会话
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if (list != null && list.size() > 0) {
            for (BmobIMConversation item : list) {
                switch (item.getConversationType()) {
                    case 1://私聊
                        if (item.getMessages() != null && item.getMessages().size() > 0) {
                            LogUtil.d("conversation=" + GsonHelper.toJson(item));
                            conversationList.add(new P2PConversation(item));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }

}
