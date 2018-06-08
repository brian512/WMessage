package com.brian.common.imageloader;


public class ImageLoaderConfig {
    /**
     * 内存缓存区大小
     */
    public static final int MAX_IMAGE_MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory()) / 8;
    /**
     * 磁盘缓存区大小 :256M
     */
    public static final int MAX_IMAGE_DISK_CACHE_SIZE = 256 * 1024 * 1024;

    /**
     * 图片缓存目录
     */
    public static final String CACHE_DIR = "/image-caches";

}
