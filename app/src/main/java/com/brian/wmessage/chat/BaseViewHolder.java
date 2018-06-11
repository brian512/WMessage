package com.brian.wmessage.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * @author huamm
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private OnItemClickListener mOnItemClickListener;
    protected Context context;

    protected T mData;

    public BaseViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
        this.context = context;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public Context getContext() {
        return itemView.getContext();
    }

    public void bindData(T t) {
        mData = t;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}