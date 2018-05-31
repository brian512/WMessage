package com.brian.wmessage.recentlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brian.common.base.BaseFragment;
import com.brian.wmessage.R;
import com.brian.wmessage.entity.RecentListItemData;

import java.util.ArrayList;
import java.util.List;

public class RecentListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private RecentListAdapter mListAdapter;


    public static RecentListFragment newInstance() {
        RecentListFragment fragment = new RecentListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLy = inflater.inflate(R.layout.recentlist_fragment, null);
        mRecyclerView = rootLy.findViewById(R.id.recentlist_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListAdapter = new RecentListAdapter();
        mRecyclerView.setAdapter(mListAdapter);
        mListAdapter.bindData(getDatas());
        return rootLy;
    }


    private List<RecentListItemData> getDatas() {
        ArrayList<RecentListItemData> list = new ArrayList<>();
        for (int i=0; i < 20; i++) {
            RecentListItemData data = new RecentListItemData();
            data.nickName = "nickName" + i;
            data.lastMsg = "lastMsg" + i;
            data.time = "12:" + i;
            list.add(data);
        }
        return list;
    }

}
