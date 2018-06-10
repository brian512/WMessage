package com.brian.wmessage.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    public ImageView mAvatarIv;
    @BindView(R.id.name)
    public TextView mNameTv;
    @BindView(R.id.btn_add)
    public Button mAddBtn;

    public SearchUserHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.search_user_item, onRecyclerViewListener);
    }

    @Override
    public void bindData(Object o) {
        final UserInfo user = (UserInfo) o;
        user.showHead(mAvatarIv);
        mNameTv.setText(user.getUsername());
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//查看个人详情
                UserInfoActivity.startActivity(v.getContext(), user);
            }
        });
    }
}