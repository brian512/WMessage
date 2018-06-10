package com.brian.wmessage.contact;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.common.views.recyclerview.BaseRecyclerAdapter;
import com.brian.wmessage.R;
import com.brian.wmessage.entity.UserInfo;

/**
 * @author huamm
 */
public class ContactAdapter extends BaseRecyclerAdapter<UserInfo, ContactAdapter.ItemViewHolder> {

    @Override
    public void onBindViewHolder(ItemViewHolder holder, UserInfo userInfo, int position) {
        userInfo.showHead(holder.headView);
        holder.nameView.setText(userInfo.getUsername());
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, null);
        return new ItemViewHolder(layout);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView headView;
        public TextView nameView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            headView = itemView.findViewById(R.id.iv_avatar);
            nameView = itemView.findViewById(R.id.tv_name);
        }

    }
}
