package com.brian.wmessage.search;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.brian.wmessage.chat.BaseViewHolder;
import com.brian.wmessage.chat.OnRecyclerViewListener;
import com.brian.wmessage.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huamm
 */
public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserInfo> mUserInfoList = new ArrayList<>();

    public SearchUserAdapter() {
    }

    public void setDatas(List<UserInfo> list) {
        mUserInfoList.clear();
        if (null != list) {
            mUserInfoList.addAll(list);
        }
    }

    /**
     * 获取用户
     */
    public UserInfo getItem(int position){
        return mUserInfoList.get(position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchUserHolder(parent.getContext(), parent, onRecyclerViewListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(mUserInfoList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return mUserInfoList.size();
    }
}
