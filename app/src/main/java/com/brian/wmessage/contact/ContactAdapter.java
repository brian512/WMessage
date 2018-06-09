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

import java.text.SimpleDateFormat;
import java.util.Date;

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
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, null);
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
                result = "今天 " + getHourAndMin(clearTime);
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
