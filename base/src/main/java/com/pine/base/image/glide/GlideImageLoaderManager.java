package com.pine.base.image.glide;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pine.base.image.IImageLoaderManager;
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
                    mInstance = new GlideImageLoaderManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public IImageLoaderManager initConfig(@NonNull int emptyImageResId) {
        mDefaultOption.error(emptyImageResId)    //错误加载
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        return mInstance;
    }

    @Override
    public IImageLoaderManager initConfig(@NonNull int emptyImageResId, @NonNull int loadingImageResId) {
        mDefaultOption.error(emptyImageResId)    //错误加载
                .placeholder(loadingImageResId)   //加载图
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
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
        Glide.with(context)
                .load(res)
                .apply(mDefaultOption.clone().diskCacheStrategy(DiskCacheStrategy.NONE))
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
        Glide.with(context)
                .load(url)
                .apply(mDefaultOption.clone().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
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
        Glide.with(context)
                .load(file)
                .apply(mDefaultOption.clone().diskCacheStrategy(DiskCacheStrategy.NONE))
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
