package com.brian.wmessage.contact;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brian.common.base.BaseFragment;
import com.brian.common.utils.LogUtil;
import com.brian.common.views.recyclerview.BaseRecyclerAdapter;
import com.brian.wmessage.R;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.chat.ChatActivity;
import com.brian.wmessage.entity.P2PConversation;
import com.brian.wmessage.entity.UserInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 联系人界面
 * @author huamm
 */
public class ContactFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private SmartRefreshLayout mRefreshLayout;

    private ContactAdapter mListAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootLy = inflater.inflate(R.layout.list_fragment_layout, null);
        mRefreshLayout = rootLy.findViewById(R.id.refreshLayout);
        mRecyclerView = rootLy.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListAdapter = new ContactAdapter();
        mRecyclerView.setAdapter(mListAdapter);

        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(false);

        mRefreshLayout.setRefreshHeader(new ClassicsHeader(mRefreshLayout.getContext()));
        initListeners();

        query();

        return rootLy;
    }

    private void initListeners() {

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                query();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

        mListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                UserInfo user = mListAdapter.getItem(position);
                BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
                ChatActivity.startActivity(getContext(), info, new P2PConversation(conversationEntrance));
            }
        });
    }

    /**
     * 查询本地会话
     */
    public void query() {
        BmobHelper.getInstance().queryUsers(" ", 20,
                new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
                        mRefreshLayout.finishRefresh();
                        if (e == null) {
                            mListAdapter.bindData(list);
                        } else {
                            mListAdapter.bindData(null);
                            LogUtil.printError(e);
                        }
                    }
                }
        );
    }

}
