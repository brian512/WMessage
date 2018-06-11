package com.brian.wmessage.conversations;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.common.views.recyclerview.BaseRecyclerAdapter;
import com.brian.wmessage.R;
import com.brian.wmessage.chat.ChatActivity;
import com.brian.wmessage.entity.Conversation;
import com.brian.wmessage.entity.P2PConversation;
import com.brian.wmessage.entity.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 会话列表适配
 * @author huamm
 */
public class ConversationListAdapter extends BaseRecyclerAdapter<Conversation, ConversationListAdapter.ItemViewHolder> {


    @Override
    public void addOrUpdate(int index, Conversation c) {
        for (Conversation conversation : mDataList) {
            if (TextUtils.equals(conversation.getcId(), c.getConversation().getConversationId())) {
                if (conversation instanceof P2PConversation && c instanceof P2PConversation) {
                    ((P2PConversation)conversation).setLastMsg(((P2PConversation) c).getLastMsg());
                } else {
                    conversation.getConversation().getMessages().clear();
                    conversation.getConversation().getMessages().addAll(c.getConversation().getMessages());
                }
                super.addOrUpdate(0, conversation);
                return;
            }
        }
        add(index, c);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final Conversation item, int position) {
        String avatar = (String) item.getAvatar(); //TODO 增加聊天类型后需要区分
        UserInfo.showHead(holder.headView, TextUtils.isEmpty(avatar) ? "0" : avatar);
        item.getConversation().setConversationIcon(avatar);

        String cName = item.getName();
        holder.nameView.setText(cName);
        item.getConversation().setConversationTitle(cName);

        holder.msgView.setText(item.getLastMessageContent().replace("\n", " "));
        holder.timeView.setText(getChatTime(false, item.getLastMessageTime()));

        int unreadCnt = item.getUnReadCount();
        if (unreadCnt > 0) {
            holder.unreadView.setText("" + unreadCnt);
            holder.unreadView.setVisibility(View.VISIBLE);
        } else {
            holder.unreadView.setText("0");
            holder.unreadView.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item instanceof P2PConversation) {
                    holder.unreadView.setText("0");
                    holder.unreadView.setVisibility(View.INVISIBLE);
                    ChatActivity.startActivity(v.getContext(), (P2PConversation)item);
                }
            }
        });
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversatio_list_item, null);
        return new ItemViewHolder(layout);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView headView;
        public TextView nameView;
        public TextView msgView;
        public TextView timeView;
        public TextView unreadView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            headView = itemView.findViewById(R.id.recent_item_userhead);
            nameView = itemView.findViewById(R.id.recent_item_nikename);
            msgView = itemView.findViewById(R.id.recent_item_message);
            timeView = itemView.findViewById(R.id.recent_item_time);
            unreadView = itemView.findViewById(R.id.recent_item_unread);
        }

    }

    private String getChatTime(boolean hasYear, long timesamp) {
        long clearTime = timesamp;
        String result;
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(clearTime);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));
        switch (temp) {
            case 0:
                result = getHourAndMin(clearTime);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(clearTime);
                break;
            case 2:
                result = "前天 " + getHourAndMin(clearTime);
                break;
            default:
                result = getTime(hasYear, clearTime);
                break;
        }
        return result;
    }

    public static String getTime(boolean hasYear, long time) {
        String pattern = "yyyy-MM-dd HH:mm";
        if (!hasYear) {
            pattern = "MM-dd HH:mm";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }
}
