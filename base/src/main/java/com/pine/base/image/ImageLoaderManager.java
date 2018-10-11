package com.pine.base.image;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pine.base.R;
import com.pine.tool.util.LogUtils;

import java.io.File;

/**
 * Created by tanghongfeng on 2018/10/11
 */

public class ImageLoaderManager {
    private final static String TAG = LogUtils.makeLogTag(ImageLoaderManager.class);
    private static volatile ImageLoaderManager mInstance;
    private RequestOptions mOption = new RequestOptions()
            .error(R.mipmap.base_iv_empty)    //错误加载
            .placeholder(R.mipmap.base_iv_loading)   //加载图
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE);


    private ImageLoaderManager() {
    }

    public static ImageLoaderManager getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderManager.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 加载本地Res图片
     *
     * @param context   Context
     * @param res       DrawableRes
     * @param imageView
     */
    public void loadResImage(@NonNull Context context, @DrawableRes int res,
                             @NonNull ImageView imageView) {
        Glide.with(context)
                .load(res)
                .apply(mOption.diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(imageView);
    }

    /**
     * 加载网络图片
     *
     * @param context   Context
     * @param url       图片地址
     * @param imageView
     */
    public void loadUrlImage(@NonNull Context context,
                             @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(mOption.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(imageView);
    }

    /**
     * 加载本地File图片
     *
     * @param context   Context
     * @param file      图片地址
     * @param imageView
     */
    public void loadFileImage(@NonNull Context context, @NonNull File file,
                              @NonNull ImageView imageView) {
        Glide.with(context)
                .load(file)
                .apply(mOption.diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(imageView);
    }

    /**
     * 清空缓存
     *
     * @param context Context
     */
    public void clearDiskCache(@NonNull Context context) {
        Glide.get(context).clearDiskCache();
    }

    /**
     * 清空内存
     *
     * @param context Context
     */
    public void clearMemory(@NonNull Context context) {
        Glide.get(context).clearMemory();
    }
}
