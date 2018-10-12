package com.pine.base.image;

import com.pine.base.R;
import com.pine.base.image.glide.GlideImageLoaderManager;
import com.pine.tool.util.LogUtils;

/**
 * Created by tanghongfeng on 2018/10/12
 */

public class ImageLoaderManager {
    private final static String TAG = LogUtils.makeLogTag(ImageLoaderManager.class);
    private static volatile IImageLoaderManager mInstance;

    static {
        getInstance().initConfig(R.mipmap.base_iv_empty, R.mipmap.base_iv_loading);
    }

    private ImageLoaderManager() {
    }

    public static IImageLoaderManager getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderManager.class) {
                if (mInstance == null) {
                    mInstance = GlideImageLoaderManager.getInstance();
                }
            }
        }
        return mInstance;
    }
}
