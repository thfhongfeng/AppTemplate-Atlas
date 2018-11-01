package com.pine.base.image;

import com.pine.base.BuildConfig;
import com.pine.base.R;
import com.pine.base.image.glide.GlideImageLoaderManager;

/**
 * Created by tanghongfeng on 2018/10/12
 */

public class ImageLoaderManager {
    static {
        getInstance().initConfig(R.mipmap.base_iv_no_item);
    }

    private ImageLoaderManager() {

    }

    public static IImageLoaderManager getInstance() {
        switch (BuildConfig.APP_THIRD_IMAGE_LOADER_PROVIDER) {
            case "glide":
                return GlideImageLoaderManager.getInstance();
            default:
                return GlideImageLoaderManager.getInstance();
        }
    }
}
