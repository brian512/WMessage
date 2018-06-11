package com.brian.wmessage.chat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.wmessage.R;
import com.brian.wmessage.contact.UserListManager;
import com.brian.wmessage.entity.IMMessage;
import com.brian.wmessage.entity.UserInfo;

import java.text.SimpleDateFormat;

import butterknife.BindView;

/**
 * 接收到的文本类型
 * @author huamm
 */
public class ReceiveTextHolder extends BaseViewHolder<IMMessage> {

    @BindView(R.id.iv_avatar)
    ImageView mAvatarIv;

    @BindView(R.id.tv_time)
    TextView mTimeTv;

    @BindView(R.id.tv_message)
    TextView mMessageTv;

    public ReceiveTextHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.chat_received_message_item);
    }

    @Override
    public void bindData(final IMMessage message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(message.createTime);
        mTimeTv.setText(time);
        if (message.mUserInfo != null && !TextUtils.isEmpty(message.mUserInfo.avatar)) {
            UserInfo.showHead(mAvatarIv, message.mUserInfo.avatar);
        } else {
            UserInfo userInfo = UserListManager.getInstance().getUserInfo(message.fromId);
            if (userInfo != null && !TextUtils.isEmpty(userInfo.avatar)) {
                UserInfo.showHead(mAvatarIv, userInfo.avatar);
            } else {
                UserInfo.showHead(mAvatarIv, "0");
            }
        }
        mMessageTv.setText(message.content);
    }

    public void showTime(boolean isShow) {
        mTimeTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}