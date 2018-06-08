package com.brian.wmessage.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.common.imageloader.ImageLoader;
import com.brian.wmessage.R;
import com.brian.wmessage.account.UserInfoActivity;
import com.brian.wmessage.chat.BaseViewHolder;
import com.brian.wmessage.chat.OnRecyclerViewListener;
import com.brian.wmessage.entity.UserInfo;

import butterknife.BindView;

/**
 * @author huamm
 */
public class SearchUserHolder extends BaseViewHolder {

    @BindView(R.id.avatar)
    public ImageView avatar;
    @BindView(R.id.name)
    public TextView name;
    @BindView(R.id.btn_add)
    public Button btn_add;

    public SearchUserHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_search_user, onRecyclerViewListener);
    }

    @Override
    public void bindData(Object o) {
        final UserInfo user = (UserInfo) o;
        ImageLoader.get().showImage(avatar, user.getAvatar(), R.mipmap.default_head_2);
        name.setText(user.getUsername());
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//查看个人详情
                UserInfoActivity.startActivity(v.getContext(), user);
            }
        });
    }
}