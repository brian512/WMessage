package com.brian.common.imageloader.glide;

import android.content.Context;

import com.brian.common.imageloader.ImageLoaderConfig;
import com.brian.common.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * Glide配置
 * Created by brian on 17-7-31.
 */

public class GlideModelConfig implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 定义缓存大小和位置
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, ImageLoaderConfig.MAX_IMAGE_DISK_CACHE_SIZE));  //内存中
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, ImageLoaderConfig.CACHE_DIR, ImageLoaderConfig.MAX_IMAGE_DISK_CACHE_SIZE)); //sd卡中

        // 默认内存和图片池大小
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); // 默认内存大小
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize(); // 默认图片池大小
        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize)); // 该两句无需设置，是默认的
        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));

        // 自定义内存和图片池大小
        builder.setMemoryCache(new LruResourceCache(ImageLoaderConfig.MAX_IMAGE_MEMORY_CACHE_SIZE));
        builder.setBitmapPool(new LruBitmapPool(ImageLoaderConfig.MAX_IMAGE_MEMORY_CACHE_SIZE));

        // 定义图片格式
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
//        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565); // 默认
        LogUtil.log("builder=" + builder);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
