package com.pine.base.component.image_loader.glide.loader;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Synthetic;
import com.pine.base.component.image_loader.IImageDownloadListener;
import com.pine.tool.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/11/21
 */

public class HttpDataFetcher implements DataFetcher<InputStream> {
    @VisibleForTesting
    static final HttpDataFetcher.HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY =
            new HttpDataFetcher.DefaultHttpUrlConnectionFactory();
    private static final int MAXIMUM_REDIRECTS = 5;
    /**
     * Returned when a connection error prevented us from receiving an http error.
     */
    private static final int INVALID_STATUS_CODE = -1;
    private final String TAG = LogUtils.makeLogTag(this.getClass());
    private final GlideUrl glideUrl;
    private final int timeout;
    private final HttpDataFetcher.HttpUrlConnectionFactory connectionFactory;

    private HttpURLConnection urlConnection;
    private InputStream stream;
    private volatile boolean isCancelled;
    private IImageDownloadListener listener;

    public HttpDataFetcher(GlideUrl glideUrl, int timeout,
                           IImageDownloadListener listener) {
        this(glideUrl, timeout, DEFAULT_CONNECTION_FACTORY, listener);
    }

    @VisibleForTesting
    HttpDataFetcher(GlideUrl glideUrl, int timeout,
                    HttpDataFetcher.HttpUrlConnectionFactory connectionFactory,
                    IImageDownloadListener listener) {
        this.glideUrl = glideUrl;
        this.timeout = timeout;
        this.connectionFactory = connectionFactory;
        this.listener = listener;
    }

    // Referencing constants is less clear than a simple static method.
    private static boolean isHttpOk(int statusCode) {
        return statusCode / 100 == 2;
    }

    // Referencing constants is less clear than a simple static method.
    private static boolean isHttpRedirect(int statusCode) {
        return statusCode / 100 == 3;
    }

    @Override
    public void loadData(@NonNull Priority priority,
                         @NonNull DataCallback<? super InputStream> callback) {
        long startTime = LogTime.getLogTime();
        try {
            if (listener != null) {
                listener.onRequest(glideUrl.toURL(), glideUrl.getHeaders());
            }
            InputStream result = loadDataWithRedirects(glideUrl.toURL(), 0, null, glideUrl.getHeaders());
            callback.onDataReady(result);
        } catch (IOException e) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Failed to load data for url", e);
            }
            callback.onLoadFailed(e);
        } finally {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Finished http url fetcher fetch in " + LogTime.getElapsedMillis(startTime));
            }
        }
    }

    private InputStream loadDataWithRedirects(URL url, int redirects, URL lastUrl,
                                              Map<String, String> headers) throws IOException {
        if (redirects >= MAXIMUM_REDIRECTS) {
            throw new HttpException("Too many (> " + MAXIMUM_REDIRECTS + ") redirects!");
        } else {
            // Comparing the URLs using .equals performs additional network I/O and is generally broken.
            // See http://michaelscharf.blogspot.com/2006/11/javaneturlequals-and-hashcode-make.html.
            try {
                if (lastUrl != null && url.toURI().equals(lastUrl.toURI())) {
                    throw new HttpException("In re-direct loop");

                }
            } catch (URISyntaxException e) {
                // Do nothing, this is best effort.
            }
        }

        urlConnection = connectionFactory.build(url);
        for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
            urlConnection.addRequestProperty(headerEntry.getKey(), headerEntry.getValue());
        }
        urlConnection.setConnectTimeout(timeout);
        urlConnection.setReadTimeout(timeout);
        urlConnection.setUseCaches(false);
        urlConnection.setDoInput(true);

        // Stop the urlConnection instance of HttpUrlConnection from following redirects so that
        // redirects will be handled by recursive calls to this method, loadDataWithRedirects.
        urlConnection.setInstanceFollowRedirects(false);

        // Connect explicitly to avoid errors in decoders if connection fails.
        urlConnection.connect();
        // Set the stream so that it's closed in cleanup to avoid resource leaks. See #2352.
        stream = urlConnection.getInputStream();
        if (isCancelled) {
            return null;
        }
        final int statusCode = urlConnection.getResponseCode();
        if (isHttpOk(statusCode)) {
            InputStream inputStream = getStreamForSuccessfulRequest(urlConnection);
            if (listener != null) {
                listener.onResponse(statusCode, urlConnection.getHeaderFields());
            }
            return inputStream;
        } else if (isHttpRedirect(statusCode)) {
            String redirectUrlString = urlConnection.getHeaderField("Location");
            if (TextUtils.isEmpty(redirectUrlString)) {
                throw new HttpException("Received empty or null redirect url");
            }
            URL redirectUrl = new URL(url, redirectUrlString);
            // Closing the stream specifically is required to avoid leaking ResponseBodys in addition
            // to disconnecting the url connection below. See #2352.
            cleanup();
            return loadDataWithRedirects(redirectUrl, redirects + 1, url, headers);
        } else if (statusCode == INVALID_STATUS_CODE) {
            if (listener != null) {
                listener.onFail(statusCode, "");
            }
            throw new HttpException(statusCode);
        } else {
            listener.onFail(statusCode, urlConnection.getResponseMessage());
            throw new HttpException(urlConnection.getResponseMessage(), statusCode);
        }
    }

    private InputStream getStreamForSuccessfulRequest(HttpURLConnection urlConnection)
            throws IOException {
        if (TextUtils.isEmpty(urlConnection.getContentEncoding())) {
            int contentLength = urlConnection.getContentLength();
            stream = ContentLengthInputStream.obtain(urlConnection.getInputStream(), contentLength);
        } else {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Got non empty content encoding: " + urlConnection.getContentEncoding());
            }
            stream = urlConnection.getInputStream();
        }
        return stream;
    }

    @Override
    public void cleanup() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        urlConnection = null;
    }

    @Override
    public void cancel() {
        // TODO: we should consider disconnecting the url connection here, but we can't do so
        // directly because cancel is often called on the main thread.
        isCancelled = true;
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }

    interface HttpUrlConnectionFactory {
        HttpURLConnection build(URL url) throws IOException;
    }

    private static class DefaultHttpUrlConnectionFactory implements HttpDataFetcher.HttpUrlConnectionFactory {

        @Synthetic
        DefaultHttpUrlConnectionFactory() {
        }

        @Override
        public HttpURLConnection build(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
        }
    }
}
