package com.brian.wmessage.chat;

/**
 * 为RecycleView添加点击事件
 * @author huamm
 */
public interface OnRecyclerViewListener {
    void onItemClick(int position);
    boolean onItemLongClick(int position);
}
