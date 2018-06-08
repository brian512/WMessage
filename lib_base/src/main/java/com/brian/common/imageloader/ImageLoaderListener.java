package com.brian.common.imageloader;

import android.graphics.Bitmap;
import android.view.View;

/**
 * 图片加载回调接口
 * 
 * @author ls
 *
 */
public interface ImageLoaderListener {

    /**
     * Is called when image loading task was started
     *
     * @param imageUri Loading image URI
     * @param view     View for image. Can be <b>null</b>.
     */
    void onLoadingStarted(String imageUri, View view);

    /**
     * Is called when an error was occurred during image loading
     *
     * @param imageUri   Loading image URI
     * @param view       View for image. Can be <b>null</b>.
     * @param failReason why image loading was failed
     */
    void onLoadingFailed(String imageUri, View view, int failReason);

    /**
     * Is called when image is loaded successfully (and displayed in View if one was specified)
     *
     * @param imageUri    Loaded image URI
     * @param view        View for image. Can be <b>null</b>.
     * @param loadedImage Bitmap of loaded and decoded image
     */
    void onLoadingComplete(String imageUri, String filePath, View view, Bitmap loadedImage);

    /**
     * Is called when image loading task was cancelled because View for image was reused in newer task
     *
     * @param imageUri Loading image URI
     * @param view     View for image. Can be <b>null</b>.
     */
    void onLoadingCancelled(String imageUri, View view);
}
