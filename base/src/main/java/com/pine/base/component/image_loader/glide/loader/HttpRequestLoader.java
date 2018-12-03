package com.pine.base.component.image_loader.glide.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.pine.base.component.image_loader.IImageDownloadListener;

import java.io.InputStream;

/**
 * Created by tanghongfeng on 2018/11/19
 */

public class HttpRequestLoader implements ModelLoader<GlideUrl, InputStream> {
    /**
     * An integer option that is used to determine the maximum connect and read timeout durations (in
     * milliseconds) for network connections.
     * <p>
     * <p>Defaults to 2500ms.
     */
    public static final Option<Integer> TIMEOUT = Option.memory(
            "com.pine.base.component.image_loader.glide.model_loader.HttpRequestLoader.Timeout", 2500);
    public static IImageDownloadListener listener;
    @Nullable
    private final ModelCache<GlideUrl, GlideUrl> modelCache;

    public HttpRequestLoader() {
        this(null);
    }

    public HttpRequestLoader(@Nullable ModelCache<GlideUrl, GlideUrl> modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public LoadData<InputStream> buildLoadData(@NonNull GlideUrl model, int width, int height,
                                               @NonNull Options options) {
        // GlideUrls memoize parsed URLs so caching them saves a few object instantiations and time
        // spent parsing urls.
        GlideUrl url = model;
        if (modelCache != null) {
            url = modelCache.get(model, 0, 0);
            if (url == null) {
                modelCache.put(model, 0, 0, model);
                url = model;
            }
        }
        int timeout = options.get(TIMEOUT);
        return new LoadData<>(url, new HttpDataFetcher(url, timeout, listener));
    }

    @Override
    public boolean handles(@NonNull GlideUrl model) {
        return true;
    }

    /**
     * The default factory for {@link HttpRequestLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final ModelCache<GlideUrl, GlideUrl> modelCache = new ModelCache<>(500);

        @NonNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new HttpRequestLoader(modelCache);
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }
}