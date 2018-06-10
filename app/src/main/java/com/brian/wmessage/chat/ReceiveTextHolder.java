package com.brian.wmessage.chat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.common.utils.LogUtil;
import com.brian.wmessage.R;
import com.brian.wmessage.contact.UserListManager;
import com.brian.wmessage.entity.UserInfo;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 接收到的文本类型
 * @author huamm
 */
public class ReceiveTextHolder extends BaseViewHolder {

    @BindView(R.id.iv_avatar)
    ImageView mAvatarIv;

    @BindView(R.id.tv_time)
    TextView mTimeTv;

    @BindView(R.id.tv_message)
    TextView mMessageTv;

    public ReceiveTextHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.chat_received_message_item, onRecyclerViewListener);
    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(message.getCreateTime());
        mTimeTv.setText(time);
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        if (info != null && !TextUtils.isEmpty(info.getAvatar())) {
            UserInfo.showHead(mAvatarIv, info.getAvatar());
        } else {
            UserInfo userInfo = UserListManager.getInstance().getUserInfo(message.getFromId());
            if (userInfo != null && !TextUtils.isEmpty(userInfo.getAvatar())) {
                UserInfo.showHead(mAvatarIv, userInfo.getAvatar());
            } else {
                UserInfo.showHead(mAvatarIv, "0");
            }
        }
        String content = message.getContent();
        mMessageTv.setText(content);
        mAvatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info == null) {
                    LogUtil.d("由message获得的用户信息为空");
                    return;
                }
                LogUtil.d("点击" + info.getName() + "的头像");
            }
        });
        mMessageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("点击" + message.getContent());
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        mMessageTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });
    }

    public void showTime(boolean isShow) {
        mTimeTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}