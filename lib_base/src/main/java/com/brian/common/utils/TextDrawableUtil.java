package com.brian.common.utils;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * 设置TextView的drawable
 * Created by brian on 2017/9/22.
 */

public class TextDrawableUtil {

    public static void setTopDrawable(TextView textView, int resId) {
        Drawable drawable = ResourceUtil.getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    public static void setLeftDrawable(TextView textView, int resId) {
        Drawable drawable = ResourceUtil.getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    public static void setRightDrawable(TextView textView, int resId) {
        Drawable drawable = ResourceUtil.getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    public static void setBottomDrawable(TextView textView, int resId) {
        Drawable drawable = ResourceUtil.getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, null, drawable);
    }

    public static void setDrawable(TextView textView, int leftId, int topId, int rightId, int bottomId) {
        Drawable drawableLeft = null;
        Drawable drawableTop = null;
        Drawable drawableRight = null;
        Drawable drawableBottom = null;
        if (leftId > 0) {
            drawableLeft = ResourceUtil.getDrawable(leftId);
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
        }
        if (topId > 0) {
            drawableTop = ResourceUtil.getDrawable(topId);
            drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
        }
        if (rightId > 0) {
            drawableRight = ResourceUtil.getDrawable(rightId);
            drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        }
        if (bottomId > 0) {
            drawableBottom = ResourceUtil.getDrawable(bottomId);
            drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());
        }
        textView.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public static void clearDrawable(TextView textView) {
        textView.setCompoundDrawables(null, null, null, null);
    }

}
