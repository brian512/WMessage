package com.brian.common.utils;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brian.common.R;

import java.lang.reflect.Method;
import java.util.Locale;


/**
 * 用来封装所有和版本有关的接口
 *
 * @author lipeilong
 */
public class SDKUtil {

    /**
     * PS:因为API 16以上不推荐使用setBackgroundDrawable ，所以强烈建议使用我们自己封装的方法来设置背景
     *
     * @param view
     * @param background
     */
    @SuppressLint("NewApi")
    public static void setBackground(View view, Drawable background) {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    /**
     * PS:为了方便使用，添加一个直接设置图片的接口
     *
     * @param view
     * @param bitmap
     */
    public static void setBackgroundBitmap(View view, Bitmap bitmap) {
        if (view == null) {
            return;
        }

        setBackground(view, new BitmapDrawable(bitmap));
    }

    public static void setAlpha(View view, float alpha) {
        if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.setAlpha(alpha);
        }
    }

    /**
     * 获得textView的最大限制行数
     * 如果API不支持的话，则返回传入的默认值
     *
     * @param textView
     * @param defaultNum
     * @return
     */
    @SuppressLint("NewApi")
    public static int getMaxLine(TextView textView, int defaultNum) {
        // PS: 官方文档是api16以上的都支持这个方法
        //     实际上厂商定制的ROM中不一定包含这个方法（根据友盟反馈的崩溃日志）

        try {
            Method method = TextView.class.getMethod("getMaxLines");
            if (method == null) {
                return defaultNum;
            } else {
                return textView.getMaxLines();
            }
        } catch (NoSuchMethodException e) {
            LogUtil.printError(e);
            return defaultNum;
        }
    }

    @SuppressLint("NewApi")
    public static void removeRule(RelativeLayout.LayoutParams layoutParams, int rule) {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.removeRule(rule);
        } else {
            layoutParams.addRule(rule, 0);
        }
    }

    @SuppressLint("NewApi")
    public static void postInvalidateOnAnimation(View view) {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            view.postInvalidateOnAnimation();
        } else {
            view.postInvalidate();
        }
    }

    /**
     * PS：低API版本情况下会导致显示UI显示有问题
     *
     * @param textView
     * @param start
     * @param top
     * @param end
     * @param bottom
     */
    @SuppressLint("NewApi")
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, int start, int top, int end, int bottom) {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);
        }
    }

    @SuppressLint("NewApi")
    public static void setScrollBarSize(AbsListView absListView, int size) {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            absListView.setScrollBarSize(size);
        } else {
        }
    }

    public static boolean initTranslucentStatusBar(Activity activity) {
        if (VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN) {
            return false;
        }
        Window window = activity.getWindow();
        int visibility = window.getDecorView().getSystemUiVisibility();

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
                // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
                visibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                window.getDecorView().setSystemUiVisibility(visibility);
            } else if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ResourceUtil.getColor(R.color.thirty_percent_transparency_black));
            }
        }

        visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        window.getDecorView().setSystemUiVisibility(visibility);
        return true;
    }

    public static boolean setFullScreenEnable(Activity activity, boolean enable) {
        if (VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN) {
            return false;
        }
        Window window = activity.getWindow();
        int visibility = window.getDecorView().getSystemUiVisibility();
        if (enable) {
            visibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        } else {
            visibility &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (!DeviceUtil.hasNavigationBar()) {
            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        window.getDecorView().setSystemUiVisibility(visibility);
        return true;
    }

    public static boolean setKeepScreenOn(Activity activity, boolean enable) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (enable) {
            params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        } else {
            params.flags &= (~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        window.setAttributes(params);

        return true;
    }

    public static String getSystemLanguage(Context context) {
        return getLocale(context).getLanguage();
    }

    public static String getSystemRegion(Context context) {
        return getLocale(context).getCountry();
    }

    public static Locale getLocale(Context context) {
        Locale locale;
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
//        LogUtil.log("getLanguage=" + locale.getLanguage());
//        LogUtil.log("getCountry=" + locale.getCountry());
//        LogUtil.log("getDisplayLanguage=" + locale.getDisplayLanguage());
//        LogUtil.log("getDisplayCountry=" + locale.getDisplayCountry());
        return locale;
    }

    public static void pauseAnimator(Animator animator) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            animator.pause();
        } else {
//            animator.cancel();
            animator.end();
        }
    }

    public static void resumeAnimator(Animator animator) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            if (animator.isPaused()) {
                animator.resume();
            } else {
                animator.start();
            }
        } else {
            animator.start();
        }
    }
}
