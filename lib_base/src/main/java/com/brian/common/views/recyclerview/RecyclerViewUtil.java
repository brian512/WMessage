package com.brian.common.views.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author Brian on 2018/6/9.
 */
public class RecyclerViewUtil {


    public static boolean isVisBottom(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = recyclerView.getScrollState();
        return visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == RecyclerView.SCROLL_STATE_IDLE;
    }

    public static void scrollToBottom(RecyclerView recyclerView) {
        if (isVisBottom(recyclerView)) {
            return;
        }
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }
}
