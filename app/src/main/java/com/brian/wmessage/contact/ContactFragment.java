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
import com.brian.common.views.recyclerview.BaseRecyclerAdapter;
import com.brian.wmessage.R;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.chat.ChatActivity;
import com.brian.wmessage.entity.Friend;
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
        final View rootLy = inflater.inflate(R.layout.list_fragment, null);
        mRecyclerView = rootLy.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListAdapter = new ContactAdapter();
        mRecyclerView.setAdapter(mListAdapter);

        mListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Friend friend = mListAdapter.getItem(position);
                UserInfo user = friend.getFriendUser();
                BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
                ChatActivity.startActivity(getContext(), new IMConversation(conversationEntrance));
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
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }

    /**
     * 查询本地会话
     */
    public void query() {
        BmobHelper.getInstance().queryFriends(new FindListener<Friend>() {
                    @Override
                    public void done(List<Friend> list, BmobException e) {
                        if (e == null) {
                            mListAdapter.bindData(list);
                        }
                    }
                }
        );
    }

}
