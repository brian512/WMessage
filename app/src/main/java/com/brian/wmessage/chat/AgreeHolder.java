package com.brian.wmessage.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brian.wmessage.R;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * 同意添加好友的agree类型
 * @author huamm
 */
public class AgreeHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.tv_time)
    protected TextView mTimeTv;

    @BindView(R.id.tv_message)
    protected TextView mMessageTv;

    public AgreeHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
        super(context, root, R.layout.chat_agree_item, listener);
    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = dateFormat.format(message.getCreateTime());
        String content = message.getContent();
        mMessageTv.setText(content);
        mTimeTv.setText(time);
    }

    public void showTime(boolean isShow) {
        mTimeTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
