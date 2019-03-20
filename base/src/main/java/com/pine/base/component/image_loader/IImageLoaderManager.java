package com.pine.base.component.image_loader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by tanghongfeng on 2018/10/12
 */

public interface IImageLoaderManager {

    /**
     * 初始化基本配置项
     */
    IImageLoaderManager initConfig(@NonNull int errorImageResId);

    /**
     * 初始化基本配置项
     */
    IImageLoaderManager initConfig(@NonNull int errorImageResId, @NonNull int loadingImageResId);

    /**
     * 设置下载请求监听
     *
     * @param listener
     * @return
     */
    IImageLoaderManager downloadListener(IImageDownloadListener listener);

    /**
     * 加载本地Res图片
     *
     * @param context   Context
     * @param res       DrawableRes
     * @param imageView
     */
    void loadImage(@NonNull Context context, @DrawableRes int res,
                   @NonNull ImageView imageView);

    /**
     * 加载本地Res图片
     *
     * @param context     Context
     * @param res         DrawableRes
     * @param error       DrawableRes
     * @param placeholder DrawableRes
     * @param imageView
     */
    void loadImage(@NonNull Context context, @DrawableRes int res, @DrawableRes int error,
                   @DrawableRes int placeholder, @NonNull ImageView imageView);

    /**
     * 加载本地Res图片
     *
     * @param context     Context
     * @param res         DrawableRes
     * @param error       Drawable
     * @param placeholder Drawable
     * @param imageView
     */
    void loadImage(@NonNull Context context, @DrawableRes int res, Drawable error,
                   Drawable placeholder, @NonNull ImageView imageView);

    /**
     * 加载网络图片
     *
     * @param context   Context
     * @param url       图片地址
     * @param imageView
     */
    void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView);

    /**
     * 加载网络图片
     *
     * @param context     Context
     * @param url         图片地址
     * @param error       DrawableRes
     * @param placeholder DrawableRes
     * @param imageView
     */
    void loadImage(@NonNull Context context, @NonNull String url, @DrawableRes int error,
                   @DrawableRes int placeholder, @NonNull ImageView imageView);

    /**
     * 加载网络图片
     *
     * @param context     Context
     * @param url         图片地址
     * @param error       Drawable
     * @param placeholder Drawable
     * @param imageView
     */
    void loadImage(@NonNull Context context, @NonNull String url, Drawable error,
                   Drawable placeholder, @NonNull ImageView imageView);

    /**
     * 加载网络图片
     *
     * @param context   Context
     * @param url       图片地址
     * @param imageView
     */
    void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView,
                   ImageCacheStrategy cacheStrategy);

    /**
     * 加载网络图片
     *
     * @param context       Context
     * @param url           图片地址
     * @param error         DrawableRes
     * @param placeholder   DrawableRes
     * @param imageView
     * @param cacheStrategy ImageCacheStrategy
     */
    void loadImage(@NonNull Context context, @NonNull String url, @DrawableRes int error,
                   @DrawableRes int placeholder, @NonNull ImageView imageView,
                   ImageCacheStrategy cacheStrategy);

    /**
     * 加载网络图片
     *
     * @param context       Context
     * @param url           图片地址
     * @param error         Drawable
     * @param placeholder   Drawable
     * @param imageView
     * @param cacheStrategy ImageCacheStrategy
     */
    void loadImage(@NonNull Context context, @NonNull String url, Drawable error,
                   Drawable placeholder, @NonNull ImageView imageView,
                   ImageCacheStrategy cacheStrategy);

    /**
     * 加载本地File图片
     *
     * @param context   Context
     * @param file      图片地址
     * @param imageView
     */
    void loadImage(@NonNull Context context, @NonNull File file,
                   @NonNull ImageView imageView);

    /**
     * 加载本地File图片
     *
     * @param context     Context
     * @param file        图片地址
     * @param error       DrawableRes
     * @param placeholder DrawableRes
     * @param imageView
     */
    void loadImage(@NonNull Context context, @NonNull File file, @DrawableRes int error,
                   @DrawableRes int placeholder, @NonNull ImageView imageView);

    /**
     * 加载本地File图片
     *
     * @param context     Context
     * @param file        图片地址
     * @param error       Drawable
     * @param placeholder Drawable
     * @param imageView
     */
    void loadImage(@NonNull Context context, @NonNull File file, Drawable error,
                   Drawable placeholder, @NonNull ImageView imageView);

    /**
     * 清空缓存
     *
     * @param context Context
     */
    void clearDiskCache(@NonNull Context context);

    /**
     * 清空内存
     *
     * @param context Context
     */
    void clearMemory(@NonNull Context context);
}
