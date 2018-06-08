package com.brian.common.imageloader.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.brian.common.imageloader.IImageLoaderInterface;
import com.brian.common.imageloader.ImageLoaderListener;
import com.brian.common.imageloader.ImageLoaderProgressListener;
import com.brian.common.tools.Env;
import com.brian.common.tools.ThreadManager;
import com.brian.common.utils.FileTypeUtil;
import com.brian.common.utils.LogUtil;
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Glide图片加载库封装
 * Glide特点
 * 使用简单
 * 可配置度高，自适应程度高
 * 支持常见图片格式 Jpg png gif webp
 * 支持多种数据源  网络、本地、资源、Assets 等
 * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
 * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
 * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
 * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
 * Created by huamm on 2017/4/6 0006.
 */

public class GlideHelper implements IImageLoaderInterface {

    // com.bumptech.glide.load.model.AssetUriParser.ASSET_PREFIX
    private static final String PRE_ASSET = "file:///android_asset/";

    private RequestManager mRequestManager;

    private Context mContext;

    private Handler mHandler;

    public GlideHelper(Context context) {
        mContext = context.getApplicationContext();
        mRequestManager = Glide.with(context);

        mHandler = new Handler(Looper.getMainLooper());
    }



    public void showImage(ImageView imageView, String path) {
        mRequestManager.load(path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void showBlurImage(ImageView imageView, String imagePath) {
        mRequestManager.load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .bitmapTransform(new BlurTransformation(imageView.getContext(), 7, 3))
                .into(imageView);
    }

    @Override
    public void showImage(ImageView imageView, String uri, int defaultResId) {
        if (TextUtils.isEmpty(uri)) {
            imageView.setImageResource(defaultResId);
            return;
        }
        mRequestManager.load(uri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(defaultResId)
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public void showImageAsset(ImageView imageView, String uri, int defaultResId) {
        if (!uri.startsWith(PRE_ASSET)) {
            uri = PRE_ASSET + uri;
        }
        showImage(imageView, uri, defaultResId);
    }

    //设置加载中以及加载失败图片并且指定大小
    @Override
    public void showImage(ImageView imageView, String uri, int defaultResId, int errorResId) {
        if (TextUtils.isEmpty(uri)) {
            imageView.setImageResource(defaultResId);
            return;
        }
        mRequestManager.load(uri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(defaultResId)
                .error(errorResId)
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public void showImage(final ImageView imageView, final String uri,
                          int width, int height,
                          int defaultResId, int errorResId) {
        if (TextUtils.isEmpty(uri)) {
            imageView.setImageResource(defaultResId);
            return;
        }
        DrawableRequestBuilder builder = mRequestManager.load(uri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(defaultResId)
                .error(errorResId)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade(500)
//                .dontAnimate()
                ;

        if (width > 0 && height > 0) {
            builder.override(width, height);
        }

        builder.into(imageView);
    }

    //设置跳过内存缓存
    public void showImageCache(ImageView imageView, String path) {
        mRequestManager.load(path).skipMemoryCache(true).into(imageView);
    }

    //设置下载优先级
    public void showImagePriority(ImageView imageView, String path) {
        mRequestManager.load(path).priority(Priority.NORMAL).into(imageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public void loadImageViewDiskCache(ImageView imageView, String path) {
        mRequestManager.load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * api也提供了几个常用的动画：比如crossFade()
     */

    //设置加载动画
    public void loadImageViewAnim(ImageView imageView, String path, int anim) {
        mRequestManager.load(path).animate(anim).into(imageView);
    }

    /**
     * 设置缩略图支持
     */
    public void showImageViewThumbnail(String path, ImageView imageView) {
        mRequestManager.load(path).thumbnail(0.1f).into(imageView);
    }

    /**
     * 设置动态转换
     * api提供了比如：centerCrop()、fitCenter()等
     */
    public void showImageViewCrop(String path, ImageView imageView) {
        mRequestManager.load(path).centerCrop().into(imageView);
    }

    //设置动态GIF加载方式
    public void showImageViewDynamicGif(String path, ImageView imageView) {
        mRequestManager.load(path).asGif().into(imageView);
    }

    //设置静态GIF加载方式
    public void showImageViewStaticGif(String path, ImageView imageView) {
        mRequestManager.load(path).asBitmap().into(imageView);
    }

    //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘

    //设置监听请求接口
    public void showImageViewListener(ImageView imageView, String path, int defaultResId, RequestListener<String, GlideDrawable> requstlistener) {
        mRequestManager.load(path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(defaultResId)
                .listener(requstlistener)
                .dontAnimate()
                .into(imageView);
    }

    //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排

    //设置要加载的内容
    public void loadImageViewContent(String path, SimpleTarget<GlideDrawable> simpleTarget) {
        mRequestManager.load(path).centerCrop().into(simpleTarget);
    }

    @Override
    public void loadImage(final String uri, final ImageLoaderListener listener) {
        loadImage(uri, 0, 0, listener, null);
    }

    @Override
    public void loadImage(final String uri, final ImageLoaderProgressListener listener) {
        ThreadManager.getPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final File imageFile = mRequestManager.load(uri)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFileLoaded(imageFile.getAbsolutePath());
                        }
                    });
                } catch (Exception e) {
                    LogUtil.printError(e);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFileLoaded(null);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void loadImage(String uri, int width, int height, ImageLoaderListener listener) {
        loadImage(uri, width, height, listener, null);
    }

    @Override
    public void loadImage(final String uri, final int width, final int height, final ImageLoaderListener listener,
                          final ImageLoaderProgressListener progressListener) {
        ThreadManager.getPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final File imageFile = mRequestManager.load(uri)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    LogUtil.log("imageFile=" + imageFile);
//                    LogUtil.log("" + new MimetypesFileTypeMap().getContentType(imageFile));
                    if (listener != null && imageFile != null) {
                        final BitmapTypeRequest request = mRequestManager.load(imageFile).asBitmap();
                        if (width > 0 && height > 0) {
                            request.override(width, height);
                        }
                        final String newPath = imageFile.getPath().replace(".0", "." + FileTypeUtil.getFileType(imageFile));
                        copyFile(imageFile.getPath(), newPath);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                request.into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        listener.onLoadingComplete(uri, newPath, null, resource);
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    LogUtil.log("uri=" + uri);
                    LogUtil.printError(e);
                }
            }
        });
    }

    @Override
    public boolean isDiskCacheExist(String uri) {
        return false;
    }

    @Override
    public void removeFromMemoryCache(String uri) {

    }

    public void onTrimMemory(int level) {
        Glide.get(Env.getContext()).trimMemory(level);
    }

    @Override
    public void clearMemoryCache() {
        Glide.get(mContext).clearMemory();
    }

    @Override
    public void clearDiskCache() {
        Glide.get(mContext).clearDiskCache();
    }

    @Override
    public void destroy() {
        Glide.get(mContext).clearMemory();
    }


    /**
     * oldPath: 图片缓存的路径
     * newPath: 图片缓存copy的路径
     */
    private static void copyFile(String oldPath, String newPath) {
        try {
            int byteRead;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ( (byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
