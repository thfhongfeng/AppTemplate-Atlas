package com.pine.base.component.image_loader.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pine.base.component.image_loader.IImageDownloadListener;
import com.pine.base.component.image_loader.IImageLoaderManager;
import com.pine.base.component.image_loader.ImageCacheStrategy;
import com.pine.base.component.image_loader.glide.loader.HttpRequestLoader;
import com.pine.tool.util.LogUtils;

import java.io.File;

/**
 * Created by tanghongfeng on 2018/10/11
 */

public class GlideImageLoaderManager implements IImageLoaderManager {
    private final static String TAG = LogUtils.makeLogTag(GlideImageLoaderManager.class);
    private static volatile IImageLoaderManager mInstance;
    private RequestOptions mDefaultOption = new RequestOptions();

    private GlideImageLoaderManager() {
    }

    public static IImageLoaderManager getInstance() {
        if (mInstance == null) {
            synchronized (GlideImageLoaderManager.class) {
                if (mInstance == null) {
                    LogUtils.releaseLog(TAG, "use image loader: glide");
                    mInstance = new GlideImageLoaderManager();
                }
            }
        }
        HttpRequestLoader.listener = null;
        return mInstance;
    }

    @Override
    public IImageLoaderManager initConfig(@NonNull int errorImageResId) {
        mDefaultOption.error(errorImageResId)    //错误加载
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        return mInstance;
    }

    @Override
    public IImageLoaderManager initConfig(@NonNull int errorImageResId, @NonNull int loadingImageResId) {
        mDefaultOption.error(errorImageResId)    //错误加载
                .placeholder(loadingImageResId)   //加载图
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        return mInstance;
    }

    @Override
    public IImageLoaderManager downloadListener(IImageDownloadListener listener) {
        HttpRequestLoader.listener = listener;
        return mInstance;
    }

    /**
     * 加载本地Res图片
     *
     * @param context   Context
     * @param res       DrawableRes
     * @param imageView
     */
    @Override
    public void loadImage(@NonNull Context context, @DrawableRes int res,
                          @NonNull ImageView imageView) {
        loadImage(context, res, -1, -1, imageView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void loadImage(@NonNull Context context, int res, int error, int placeholder,
                          @NonNull ImageView imageView) {
        RequestOptions options = mDefaultOption.clone()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        if (error > 0) {
            options.error(error);
        }
        if (placeholder > 0) {
            options.placeholder(placeholder);
        }
        Glide.with(context)
                .load(res)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadImage(@NonNull Context context, int res, Drawable error,
                          Drawable placeholder, @NonNull ImageView imageView) {
        RequestOptions options = mDefaultOption.clone()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        if (error != null) {
            options.error(error);
        }
        if (placeholder != null) {
            options.placeholder(placeholder);
        }
        Glide.with(context)
                .load(res)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载网络图片
     *
     * @param context   Context
     * @param url       图片地址
     * @param imageView
     */
    @Override
    public void loadImage(@NonNull Context context,
                          @NonNull String url, @NonNull ImageView imageView) {
        loadImage(context, url, -1, -1, imageView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, int error,
                          int placeholder, @NonNull ImageView imageView) {
        loadImage(context, url, error, placeholder, imageView, null);
    }

    @Override
    public void loadImage(@NonNull Context context, @NonNull String url,
                          Drawable error, Drawable placeholder, @NonNull ImageView imageView) {
        loadImage(context, url, error, placeholder, imageView, null);
    }

    /**
     * 加载网络图片
     *
     * @param context       Context
     * @param url           图片地址
     * @param imageView
     * @param cacheStrategy
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView, ImageCacheStrategy cacheStrategy) {
        loadImage(context, url, -1, -1, imageView, cacheStrategy);
    }

    @SuppressLint("ResourceType")
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, int error,
                          int placeholder, @NonNull ImageView imageView, ImageCacheStrategy cacheStrategy) {
        RequestOptions options = mDefaultOption.clone()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (error > 0) {
            options.error(error);
        }
        if (placeholder > 0) {
            options.placeholder(placeholder);
        }
        if (cacheStrategy != null) {
            switch (cacheStrategy) {
                case NONE:
                    options.diskCacheStrategy(DiskCacheStrategy.NONE);
                    break;
                case DATA:
                    options.diskCacheStrategy(DiskCacheStrategy.DATA);
                    break;
                case RESOURCE:
                    options.diskCacheStrategy(DiskCacheStrategy.DATA);
                    break;
                case ALL:
                    options.diskCacheStrategy(DiskCacheStrategy.ALL);
                    break;
                case AUTOMATIC:
                    options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                    break;
            }
        }
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, Drawable error,
                          Drawable placeholder, @NonNull ImageView imageView, ImageCacheStrategy cacheStrategy) {
        RequestOptions options = mDefaultOption.clone()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (error != null) {
            options.error(error);
        }
        if (placeholder != null) {
            options.placeholder(placeholder);
        }
        if (cacheStrategy != null) {
            switch (cacheStrategy) {
                case NONE:
                    options.diskCacheStrategy(DiskCacheStrategy.NONE);
                    break;
                case DATA:
                    options.diskCacheStrategy(DiskCacheStrategy.DATA);
                    break;
                case RESOURCE:
                    options.diskCacheStrategy(DiskCacheStrategy.DATA);
                    break;
                case ALL:
                    options.diskCacheStrategy(DiskCacheStrategy.ALL);
                    break;
                case AUTOMATIC:
                    options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                    break;
            }
        }
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载本地File图片
     *
     * @param context   Context
     * @param file      图片地址
     * @param imageView
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull File file,
                          @NonNull ImageView imageView) {
        loadImage(context, file, -1, -1, imageView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void loadImage(@NonNull Context context, @NonNull File file, int error,
                          int placeholder, @NonNull ImageView imageView) {
        RequestOptions options = mDefaultOption.clone()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        if (error > 0) {
            options.error(error);
        }
        if (placeholder > 0) {
            options.placeholder(placeholder);
        }
        Glide.with(context)
                .load(file)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadImage(@NonNull Context context, @NonNull File file,
                          Drawable error, Drawable placeholder, @NonNull ImageView imageView) {
        RequestOptions options = mDefaultOption.clone()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        if (error != null) {
            options.error(error);
        }
        if (placeholder != null) {
            options.placeholder(placeholder);
        }
        Glide.with(context)
                .load(file)
                .apply(options)
                .into(imageView);
    }

    /**
     * 清空图片缓存
     *
     * @param context Context
     */
    @Override
    public void clearDiskCache(@NonNull Context context) {
        Glide.get(context).clearDiskCache();
    }

    /**
     * 清空图片内存
     *
     * @param context Context
     */
    @Override
    public void clearMemory(@NonNull Context context) {
        Glide.get(context).clearMemory();
    }
}
