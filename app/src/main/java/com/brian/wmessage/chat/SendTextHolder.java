package com.brian.wmessage.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brian.common.utils.TimeUtil;
import com.brian.wmessage.R;
import com.brian.wmessage.entity.IMMessage;
import com.brian.wmessage.entity.UserInfo;

import java.text.SimpleDateFormat;

import butterknife.BindView;

/**
 * 发送的文本类型
 * @author huamm
 */
public class SendTextHolder extends BaseViewHolder<IMMessage> {

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

    private OnResendClickListener mResendClickListener;

    public SendTextHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.chat_sent_message_item);
    }

    public void setResendClickListener(OnResendClickListener listener) {
        mResendClickListener = listener;
    }

    @Override
    public void bindData(final IMMessage message) {
        UserInfo.showHead(mAvatarIv, message.mUserInfo != null ? message.mUserInfo.avatar : "0");
        mMessageTv.setText(message.content);
        mTimeTv.setText(TimeUtil.convTimeForChat(message.createTime));

        int status = message.sendStatus;
        if (status == IMMessage.STATUS_SEND_FAILED) {
            mResendIv.setVisibility(View.VISIBLE);
            mSendProgressBar.setVisibility(View.GONE);
        } else if (status == IMMessage.STATUS_SENDING) {
            mResendIv.setVisibility(View.GONE);
            mSendProgressBar.setVisibility(View.VISIBLE);
        } else {
            mResendIv.setVisibility(View.GONE);
            mSendProgressBar.setVisibility(View.GONE);
        }

        //重发
        mResendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResendClickListener != null) {
                    mResendClickListener.onClick();
                }
                // 后续可将重试逻辑放在业务层，这里只是viewholder
                MessageSendHelper.getInstance().resendMessage(message.getBmobIMMessage().getBmobIMConversation(), message, new MessageSendHelper.OnMessageSendListener() {
                    @Override
                    public void onStart(IMMessage message) {
                        mSendProgressBar.setVisibility(View.VISIBLE);
                        mResendIv.setVisibility(View.GONE);
                        mSendStatusTv.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFinish(IMMessage message, int status) {
                        if (status == MessageSendHelper.OnMessageSendListener.STATUS_DONE) {
                            mSendStatusTv.setVisibility(View.VISIBLE);
                            mSendStatusTv.setText("已发送");
                            mResendIv.setVisibility(View.GONE);
                            mSendProgressBar.setVisibility(View.GONE);
                        } else {
                            mResendIv.setVisibility(View.VISIBLE);
                            mSendProgressBar.setVisibility(View.GONE);
                            mSendStatusTv.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void showTime(boolean isShow) {
        mTimeTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public interface OnResendClickListener {
        void onClick();
    }
}
