package com.pine.base.image.config;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.pine.base.BaseConstants;

import java.io.File;

/**
 * Created by tanghongfeng on 2018/10/11
 */

public class GlideImageLoaderConfig extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int memoryCacheSize = maxMemory / 8;
        File cacheDir = new File(BaseConstants.IMAGE_LOADER_CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        int diskCacheSize = 1024 * 1024 * 512;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "Images", diskCacheSize));
    }
}
