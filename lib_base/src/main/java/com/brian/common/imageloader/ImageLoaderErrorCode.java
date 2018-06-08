package com.brian.common.imageloader;

/**
 * 图片加载失败的错误码
 *
 * @author ls
 */
public interface ImageLoaderErrorCode {
    /**
     * Input/output error. Can be caused by network communication fail or error while caching image on file system.
     */
    int IO_ERROR = 1;

    /**
     * 文件不存在导致的错误
     */
    int FILE_NOT_FOUNT_ERROR = IO_ERROR + 1;

    /**
     * Error while
     * {@linkplain android.graphics.BitmapFactory#decodeStream(java.io.InputStream, android.graphics.Rect, android.graphics.BitmapFactory.Options)
     * decode image to Bitmap}
     */
    int DECODING_ERROR = FILE_NOT_FOUNT_ERROR + 1;

    /**
     * {@linkplain com.nostra13.universalimageloader.core.ImageLoader#denyNetworkDownloads(boolean) Network
     * downloads are denied} and requested image wasn't cached in disk cache before.
     */
    int NETWORK_DENIED = DECODING_ERROR + 1;

    /**
     * Not enough memory to create needed Bitmap for image
     */
    int OUT_OF_MEMORY = NETWORK_DENIED + 1;

    /**
     * not enough storage to store the image cache
     */
    int STORAGE_NOT_ENOUGH = OUT_OF_MEMORY + 1;

    /**
     * Unknown error was occurred while loading image
     */
    int UNKNOWN = STORAGE_NOT_ENOUGH + 1;
}
