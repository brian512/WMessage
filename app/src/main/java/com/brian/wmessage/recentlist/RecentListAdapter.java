package com.brian.wmessage.recentlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.common.views.recyclerview.BaseRecyclerAdapter;
import com.brian.wmessage.R;
import com.brian.wmessage.entity.RecentListItemData;

public class RecentListAdapter extends BaseRecyclerAdapter<RecentListItemData, RecentListAdapter.ItemViewHolder> {


    @Override
    public void onBindViewHolder(ItemViewHolder holder, RecentListItemData item, int position) {
        holder.nameView.setText(item.nickName);
        holder.msgView.setText(item.lastMsg);
        holder.timeView.setText(item.time);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recentlist_item, null);
        return new ItemViewHolder(layout);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView headView;
        public TextView nameView;
        public TextView msgView;
        public TextView timeView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            headView = itemView.findViewById(R.id.recent_item_userhead);
            nameView = itemView.findViewById(R.id.recent_item_nikename);
            msgView = itemView.findViewById(R.id.recent_item_message);
            timeView = itemView.findViewById(R.id.recent_item_time);
        }

    }

}
