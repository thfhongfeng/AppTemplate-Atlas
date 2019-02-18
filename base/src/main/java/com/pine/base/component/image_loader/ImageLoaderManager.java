package com.pine.base.component.image_loader;

import com.pine.base.R;
import com.pine.base.component.image_loader.glide.GlideImageLoaderManager;
import com.pine.config.BuildConfig;

/**
 * Created by tanghongfeng on 2018/10/12
 */

public class ImageLoaderManager {
    static {
        getInstance().initConfig(R.mipmap.base_ic_default_image);
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
