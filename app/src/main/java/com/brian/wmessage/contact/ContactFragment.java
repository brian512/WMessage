package com.brian.wmessage.contact;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.brian.common.base.BaseFragment;
import com.brian.common.utils.LogUtil;
import com.brian.common.views.recyclerview.BaseRecyclerAdapter;
import com.brian.wmessage.R;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.chat.ChatActivity;
import com.brian.wmessage.entity.IMConversation;
import com.brian.wmessage.entity.UserInfo;

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

    ContactAdapter mListAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootLy = inflater.inflate(R.layout.list_fragment_layout, null);
        mRecyclerView = rootLy.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListAdapter = new ContactAdapter();
        mRecyclerView.setAdapter(mListAdapter);

        mListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                UserInfo user = mListAdapter.getItem(position);
                BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
                ChatActivity.startActivity(getContext(), info, new IMConversation(conversationEntrance));
            }
        });

        rootLy.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootLy.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                query();
            }
        });
        return rootLy;
    }

    /**
     * 查询本地会话
     */
    public void query() {
        BmobHelper.getInstance().queryUsers(" ", 20,
                new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
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
