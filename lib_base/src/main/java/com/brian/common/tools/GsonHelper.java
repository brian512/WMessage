package com.brian.common.tools;

import com.brian.common.utils.LogUtil;
import com.google.gson.Gson;

/**
 * Gson辅助类
 * Created by huamm on 2016/11/1 0001.
 */

public class GsonHelper {

    private static Gson sGson = new Gson();

//    public static String convert2String(List<T> list) {
//        try {
//            return sGson.toJson(list);
//        } catch (Exception e) {
//            LogUtil.printError(e);
//            return "";
//        }
//    }
//
//    public static List<T> fromJson(String json) {
//        try {
//            return sGson.fromJson(json, new TypeToken<ArrayList<T>>(){}.getType());
//        } catch (Exception e) {
//            LogUtil.printError(e);
//            return null;
//        }
//    }

    public static String toJson(Object object) {
        try {
            return sGson.toJson(object);
        } catch (Exception e) {
            LogUtil.printError(e);
            return "";
        }
    }
}
