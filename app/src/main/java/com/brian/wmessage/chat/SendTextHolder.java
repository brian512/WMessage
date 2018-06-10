package com.brian.wmessage.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brian.common.utils.LogUtil;
import com.brian.wmessage.R;
import com.brian.wmessage.entity.UserInfo;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * 发送的文本类型
 * @author huamm
 */
public class SendTextHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.iv_avatar)
    protected ImageView mAvatarIv;
    @BindView(R.id.iv_fail_resend)
    protected ImageView mResendIv;
    @BindView(R.id.tv_time)
    protected TextView mTimeTv;
    @BindView(R.id.tv_message)
    protected TextView mMessageTv;
    @BindView(R.id.tv_send_status)
    protected TextView mSendStatusTv;
    @BindView(R.id.progress_load)
    protected ProgressBar mSendProgressBar;

    public SendTextHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
        super(context, root, R.layout.chat_sent_message_item, listener);
    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        UserInfo.showHead(mAvatarIv, info != null ? info.getAvatar() : "0");
        String time = dateFormat.format(message.getCreateTime());
        String content = message.getContent();
        mMessageTv.setText(content);
        mTimeTv.setText(time);

        int status = message.getSendStatus();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus()) {
            mResendIv.setVisibility(View.VISIBLE);
            mSendProgressBar.setVisibility(View.GONE);
        } else if (status == BmobIMSendStatus.SENDING.getStatus()) {
            mResendIv.setVisibility(View.GONE);
            mSendProgressBar.setVisibility(View.VISIBLE);
        } else {
            mResendIv.setVisibility(View.GONE);
            mSendProgressBar.setVisibility(View.GONE);
        }

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

        mAvatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("点击" + info.getName() + "的头像");
            }
        });

        //重发
        mResendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                c.resendMessage(message, new MessageSendListener() {
//                    @Override
//                    public void onStart(BmobIMMessage msg) {
//                        mSendProgressBar.setVisibility(View.VISIBLE);
//                        mResendIv.setVisibility(View.GONE);
//                        mSendStatusTv.setVisibility(View.INVISIBLE);
//                    }
//
//                    @Override
//                    public void done(BmobIMMessage msg, BmobException e) {
//                        if (e == null) {
//                            mSendStatusTv.setVisibility(View.VISIBLE);
//                            mSendStatusTv.setText("已发送");
//                            mResendIv.setVisibility(View.GONE);
//                            mSendProgressBar.setVisibility(View.GONE);
//                        } else {
//                            mResendIv.setVisibility(View.VISIBLE);
//                            mSendProgressBar.setVisibility(View.GONE);
//                            mSendStatusTv.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                });
            }
        });
    }

    public void showTime(boolean isShow) {
        mTimeTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
