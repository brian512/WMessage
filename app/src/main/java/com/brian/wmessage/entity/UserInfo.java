package com.brian.wmessage.entity;

import android.text.TextUtils;
import android.widget.ImageView;

import com.brian.wmessage.R;

import cn.bmob.v3.BmobUser;

/**
 * @author huamm
 */
public class UserInfo extends BmobUser {

    public static int[] DEFAULT_HEADS = {
            R.mipmap.default_head_0,
            R.mipmap.default_head_1,
            R.mipmap.default_head_2,
            R.mipmap.default_head_3,
            R.mipmap.default_head_4,
            R.mipmap.default_head_5,
    };

    private String avatar;

    public String getUserId() {
        return getObjectId();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void showHead(ImageView headView) {
        showHead(headView, avatar);
    }

    public static void showHead(ImageView headView, String avatar) {
        int index = 0;
        if (!TextUtils.isEmpty(avatar) && TextUtils.isDigitsOnly(avatar)) {
            index = Integer.valueOf(avatar);
        }
        for (int i=0; i < DEFAULT_HEADS.length; i++) {
            if (index == DEFAULT_HEADS[i]) {
                headView.setImageResource(index);
                return;
            }
        }
        headView.setImageResource(DEFAULT_HEADS[index%DEFAULT_HEADS.length]);
    }
}
