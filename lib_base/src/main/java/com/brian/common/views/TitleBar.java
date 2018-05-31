
package com.brian.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brian.common.R;
import com.brian.common.utils.TextDrawableUtil;


/**
 * 首页的标题栏
 */
public class TitleBar extends RelativeLayout {

    /**
     * UI元素
     */
    private TextView mLeftView;
    private TextView mRightView;
    private TextView mTitleText;

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context) {
        this(context, null, 0);
    }

    private void initUI(Context ctx) {
        View rootLy = LayoutInflater.from(ctx).inflate(R.layout.common_title_bar, this);
        mLeftView = findViewById(R.id.titlebar_left);
        mRightView = findViewById(R.id.titlebar_right);
        mTitleText = findViewById(R.id.titlebar_title);
    }

    /**
     * 左边按钮点击
     */
    public void setLeftListener(OnClickListener l) {
        mLeftView.setOnClickListener(l);
    }

    /**
     * 设置左边按钮的显示 默认是显示
     */
    public void setLeftImageVisible(int visible) {
        mLeftView.setVisibility(visible);
    }

    /**
     * 设置左边按钮图标 默认是返回键
     */
    public void setLeftImageResource(int resourceID) {
        TextDrawableUtil.setLeftDrawable(mLeftView, resourceID);
    }

    /**
     * 右边按钮点击
     */
    public void setRightListener(OnClickListener l) {
        mRightView.setOnClickListener(l);
    }

    /**
     * 设置左边按钮的显示 默认不显示
     */
    public void setRightImageVisible(int visible) {
        mRightView.setVisibility(visible);
    }

    /**
     * 设置右边按钮图标 没有默认图标，需要自己设置
     */
    public void setRightImageResource(int resourceID) {
        TextDrawableUtil.setRightDrawable(mRightView, resourceID);
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        mTitleText.setText(title);
    }

    /**
     * 设置标题
     */
    public void setTitleResource(int resourceID) {
        mTitleText.setText(resourceID);
    }

    /**
     * 设置title是否显示 默认显示title，没有文字
     */
    public void setTitleVisible(int visible) {
        mTitleText.setVisibility(visible);
    }

    /**
     * 设置title 颜色
     */
    public void setTitleTextColor(int color) {
        mTitleText.setTextColor(color);
    }

}
