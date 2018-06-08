package com.brian.common.imageloader;

import android.widget.ImageView;

/**
 * 图片加载接口，统一第三方库的使用
 * Created by huamm on 2017/4/6 0006.
 */

public interface IImageLoaderInterface {


    void loadImage(String uri, ImageLoaderListener listener);

    void loadImage(String uri, ImageLoaderProgressListener listener);

    void loadImage(String uri, int width, int height, ImageLoaderListener listener);

    void loadImage(String uri, int width, int height, ImageLoaderListener listener, ImageLoaderProgressListener progressListener);

    void loadImageViewDiskCache(ImageView imageView, String path);

    void showImage(ImageView imageView, String imagePath);

    void showBlurImage(ImageView imageView, String imagePath);

    void showImage(ImageView imageView, String uri, int defaultResId);

    void showImageAsset(ImageView imageView, String uri, int defaultResId);

    void showImage(ImageView imageView, String path, int defaultResId, int errorResId);

    void showImage(ImageView imageView, String path, int width, int height, int defaultResId, int errorResId);

    boolean isDiskCacheExist(String uri);

    void removeFromMemoryCache(String uri);

    void onTrimMemory(int level);

    void clearMemoryCache();

    void clearDiskCache();

    void destroy();
}
