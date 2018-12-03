package com.pine.base.component.image_loader.glide;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.pine.base.component.image_loader.glide.loader.HttpRequestLoader;
import com.pine.tool.util.PathUtils;

import java.io.File;
import java.io.InputStream;

/**
 * Created by tanghongfeng on 2018/10/11
 */

@GlideModule
public class GlideImageLoaderConfig extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new HttpRequestLoader.Factory());
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int memoryCacheSize = maxMemory / 8;
        File cacheDir = PathUtils.getAppFileDirectory(Environment.DIRECTORY_PICTURES);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        int diskCacheSize = 1024 * 1024 * 512;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "Images", diskCacheSize));
    }
}
