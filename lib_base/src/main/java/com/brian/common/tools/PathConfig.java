package com.brian.common.tools;

import android.text.TextUtils;

import com.brian.common.utils.FileUtil;
import com.brian.common.utils.LogUtil;
import com.brian.common.utils.Md5Util;
import com.brian.common.utils.PathUtil;

public class PathConfig {

    private static String sMediaCachePath;
    private static String sMediaSavePath;
    private static String sHttpCache;

    // PS：注意程序中所有出现的路径，结尾都不带 "/"

    /**
     * 发帖时，图片、视频的输出目录
     *
     * @return
     */
    public static String getCameraOutputDir() {
        String dir = PathUtil.getCacheDir(Env.getContext()) + "/camera";
        FileUtil.ensureDir(dir);
        if (FileUtil.isDirExist(dir)) {
            return dir;
        }

        // 如果实在是创建失败则直接返回缓存根目录
        return PathUtil.getCacheDir(Env.getContext());
    }

    public static String getObjDir(){
        String dir = PathUtil.getCacheDir(Env.getContext()) + "/obj";
        FileUtil.ensureDir(dir);
        if (FileUtil.isDirExist(dir)) {
            return dir;
        }

        // 如果实在是创建失败则直接返回缓存根目录
        return PathUtil.getCacheDir(Env.getContext());
    }

    public static String getObjFilePath(String fileName){
        return getObjDir()+"/"+fileName;
    }

    /**
     * 视频封面保存到本地
     *
     * @return
     */
    public static final String getVideoPosterOutputDir() {
        String dir = PathUtil.getCacheDir(Env.getContext()) + "/poster";

        FileUtil.deleteDir(dir);
        FileUtil.ensureDir(dir);

        if (FileUtil.isDirExist(dir)) {
            return dir;
        }

        // 如果实在是创建失败则直接返回缓存根目录
        return PathUtil.getCacheDir(Env.getContext());
    }


    /**
     * @return
     */
    public static final String getVideoPosterOutputPicPath(String filename) {
        if (TextUtils.isEmpty(filename)){
            return getVideoPosterOutputDir() + "/pic.jpg";
        }else {
            return getVideoPosterOutputDir() + "/"+filename;
        }
    }

    /**
     * 发帖时，拍照输出路径（相册选择照片发送，也存这个路径）
     *
     * @return
     */
    public static String getCameraOutputPicPath() {
        return getCameraOutputDir() + "/pic.jpg";
    }

    /**
     * 发帖时，视频输出路径
     *
     * @return
     */
    public static String getCameraOutputVideoPath() {
        return getCameraOutputDir() + "/video.mp4";
    }



    /**
     * 发帖时，图片、视频的进行上传前，作为临时上传文件的保存目录
     *
     * @return
     */
    public static String getMediaUploadDir() {
        String dir = PathUtil.getCacheDir(Env.getContext()) + "/upload";
        FileUtil.ensureDir(dir);
        if (FileUtil.isDirExist(dir)) {
            return dir;
        }

        // 如果实在是创建失败则直接返回缓存根目录
        return PathUtil.getCacheDir(Env.getContext());
    }

    /**
     * 发帖时，图片、视频的进行上传前，如果需要保存图片、视频的话，则单独保存的目录
     * PS：此时文件会存在两份，一份在缓存中（可清理），一份在保存目录下
     *
     * @return
     */
    public static String getMediaSaveDir() {
        if (!TextUtils.isEmpty(sMediaSavePath)) {
            return sMediaSavePath;
        }

        // 首先尝试在系统相册目录下创建子目录
        String dir = PathUtil.getSDCardDir() + "/DCIM" + "/Rokk";
        FileUtil.ensureDir(dir);
        if (FileUtil.isDirExist(dir)) {
            sMediaSavePath = dir;
            return dir;
        }

        // 如果不行，再到data目录下创建缓存目录 PS：问题在于程序被卸载后，照片也会被删除
        dir = PathUtil.getCacheDir(Env.getContext()) + "/save";
        FileUtil.ensureDir(dir);
        if (FileUtil.isDirExist(dir)) {
            sMediaSavePath = dir;
            return dir;
        }

        // 如果实在是创建失败则直接返回缓存根目录
        sMediaSavePath = PathUtil.getCacheDir(Env.getContext());
        return sMediaSavePath;
    }

    /**
     * 浏览帖子时，图片、视频的保存目录
     * 注意：2.1版本后，JDImageLoader和JDFileLoader使用的同样的缓存目录和文件命名规则，所以JDFileLoader下载的文件不需要拷贝到JDImageloader缓存目录下
     *
     * @return
     */
    public static String getMediaCacheDir() {
        if (!TextUtils.isEmpty(sMediaCachePath)) {
            LogUtil.log(sMediaCachePath);
            return sMediaCachePath;
        }
        String dir = PathUtil.getCacheDir(Env.getContext());
        FileUtil.ensureDir(dir);

        if (FileUtil.isDirExist(dir)) {
            sMediaCachePath = dir;
            return sMediaCachePath;
        }

        // 如果实在是创建失败则直接返回缓存根目录
        sMediaCachePath = PathUtil.getCacheDir(Env.getContext())  + "/Rokk";
        FileUtil.ensureDir(dir);
        return sMediaCachePath;
    }

    /**
     *  获取Http缓存目录
     *
     * @return
     */
    public static String getHttpCacheDir(){
        if (!TextUtils.isEmpty(sHttpCache)) {
            return sHttpCache;
        }

        String dir      = PathUtil.getCacheDir(Env.getContext()) + "/http_cache";
        FileUtil.ensureDir(dir);
        if(FileUtil.isDirExist(dir)){
            sHttpCache  = dir;
            return sHttpCache;
        }
        sHttpCache  = PathUtil.getCacheDir(Env.getContext());
        return sHttpCache;
    }


    /**
     * 本地上传头像时，编辑图片的保存路径
     */
    public static String getHeadImageEditDir() {
        return PathUtil.getCacheDir(Env.getContext()) + "/head_image_edit";
    }

    /**
     * 聊天录音的路径
     *
     * @return
     */
    public static String getAudioDir() {
        String dir = PathUtil.getCacheDir(Env.getContext()) + "/audio";
        FileUtil.ensureDir(dir);

        if (FileUtil.isDirExist(dir)) {
            return dir;
        }

        return PathUtil.getCacheDir(Env.getContext());
    }

    /**
     * 视频录制完成后音乐保存的目录
     * @return
     */
    public static String getMusicOutputDir(){
        String dir = PathUtil.getCacheDir(Env.getContext()) + "/music";
        FileUtil.ensureDir(dir);

        if (FileUtil.isDirExist(dir)) {
            return dir;
        }

        return PathUtil.getCacheDir(Env.getContext());
    }

    /**
     * 视频录制完成后音乐保存的绝对路径
     */
    public static String getMusicOutputPath() {
        return getMediaSaveDir() + "/out.mp3";
    }

    public static String getMaskAndAnimPath() {
        String dir = PathUtil.getDataDir(Env.getContext()) + "/mask";
        FileUtil.ensureDir(dir);

        if (FileUtil.isDirExist(dir)) {
            return dir;
        }

        return PathUtil.getDataDir(Env.getContext());
    }

    public static String getMediaSavePath(String fileUrl) {
        return PathConfig.getMediaSaveDir() + "/Rokk_img/" + Md5Util.getMD5(fileUrl) + ".jpg";
    }

    public static String getGifSavePath(String fileUrl) {
        return PathConfig.getMediaSaveDir() + "/Rokk_gif/" + Md5Util.getMD5(fileUrl) + ".gif";
    }
}
