package com.brian.common.imageloader;

import com.brian.common.imageloader.glide.GlideHelper;
import com.brian.common.tools.Env;

/**
 * 图片的加载类
 * @author huamm
 */
public class ImageLoader {

    private static IImageLoaderInterface sImageLoader;

    /**
     * 构造函数，初始化操作
     */
    private ImageLoader() {
    }

    public static IImageLoaderInterface get() {
        if (sImageLoader == null) {
            synchronized (ImageLoader.class) {
                if (sImageLoader == null) {
//                    sImageLoader = new UniversalImageLoaderHelper(Env.getContext());
                    sImageLoader = new GlideHelper(Env.getContext());
                }
            }
        }

        return sImageLoader;
    }

    /**
     * 获取文件缓存目录
     */
    public static String getDiskCacheDir() {
        // 这里是取的StorageUtils类中的预设的缓存目录，如果ImageLoader控件更新，需要更新这个目录
        return ImageLoaderConfig.CACHE_DIR;
    }
}
