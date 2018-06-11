package com.brian.wmessage.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.brian.wmessage.entity.IMMessage;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * @author huamm
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //文本
    private final int TYPE_RECEIVER_TXT = 0;
    private final int TYPE_SEND_TXT = 1;
    //图片
    private final int TYPE_SEND_IMAGE = 2;
    private final int TYPE_RECEIVER_IMAGE = 3;
    //位置
    private final int TYPE_SEND_LOCATION = 4;
    private final int TYPE_RECEIVER_LOCATION = 5;
    //语音
    private final int TYPE_SEND_VOICE = 6;
    private final int TYPE_RECEIVER_VOICE = 7;
    //视频
    private final int TYPE_SEND_VIDEO = 8;
    private final int TYPE_RECEIVER_VIDEO = 9;

    //同意添加好友成功后的样式
    private final int TYPE_AGREE = 10;

    /**
     * 显示时间间隔:10分钟
     */
    private static final long TIME_INTERVAL = 10 * 60 * 1000;

    private List<IMMessage> mMessageList = new ArrayList<>();

    private String currentUid = "";

    public ChatAdapter() {
        try {
            currentUid = BmobUser.getCurrentUser().getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int findPosition(IMMessage message) {
        int index = this.getCount();
        int position = -1;
        while (index-- > 0) {
            if (message.equals(this.getItem(index))) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int findPosition(long id) {
        int index = this.getCount();
        int position = -1;
        while (index-- > 0) {
            if (this.getItemId(index) == id) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int getCount() {
        return this.mMessageList == null ? 0 : this.mMessageList.size();
    }

    public void addMessages(List<IMMessage> messages) {
        mMessageList.addAll(0, messages);
        notifyItemRangeInserted(0, messages.size());
    }

    public void addMessage(IMMessage message) {
        mMessageList.add(message);
        notifyItemInserted(mMessageList.size()-1);
    }

    public void updateMessage(IMMessage message) {
        int index = mMessageList.indexOf(message);
        notifyItemChanged(index);
    }

    /**
     * 获取消息
     */
    public IMMessage getItem(int position) {
        return this.mMessageList == null ? null : (position >= this.mMessageList.size() ? null : this.mMessageList.get(position));
    }

    /**
     * 移除消息
     */
    public void remove(int position) {
        mMessageList.remove(position);
        notifyDataSetChanged();
    }

    public IMMessage getFirstMessage() {
        if (null != mMessageList && mMessageList.size() > 0) {
            return mMessageList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEND_TXT) {
            return new SendTextHolder(parent.getContext(), parent);
        } else if (viewType == TYPE_RECEIVER_TXT) {
            return new ReceiveTextHolder(parent.getContext(), parent);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).bindData(mMessageList.get(position));
        if (holder instanceof ReceiveTextHolder) {
            ((ReceiveTextHolder) holder).showTime(shouldShowTime(position));
        } else if (holder instanceof SendTextHolder) {
            ((SendTextHolder) holder).showTime(shouldShowTime(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        IMMessage message = mMessageList.get(position);
        if (message.msgType == IMMessage.MSG_TYPE_IMAGE) {
            return TextUtils.equals(message.fromId, currentUid) ? TYPE_SEND_IMAGE : TYPE_RECEIVER_IMAGE;
        } else if (message.msgType == IMMessage.MSG_TYPE_LOCATION) {
            return TextUtils.equals(message.fromId, currentUid) ? TYPE_SEND_LOCATION : TYPE_RECEIVER_LOCATION;
        } else if (message.msgType == IMMessage.MSG_TYPE_AUDIO) {
            return TextUtils.equals(message.fromId, currentUid) ? TYPE_SEND_VOICE : TYPE_RECEIVER_VOICE;
        } else if (message.msgType == IMMessage.MSG_TYPE_TEXT) {
            return TextUtils.equals(message.fromId, currentUid) ? TYPE_SEND_TXT : TYPE_RECEIVER_TXT;
        } else if (message.msgType == IMMessage.MSG_TYPE_VIDEO) {
            return TextUtils.equals(message.fromId, currentUid) ? TYPE_SEND_VIDEO : TYPE_RECEIVER_VIDEO;
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = mMessageList.get(position - 1).createTime;
        long curTime = mMessageList.get(position).createTime;
        return curTime - lastTime > TIME_INTERVAL;
    }
}
