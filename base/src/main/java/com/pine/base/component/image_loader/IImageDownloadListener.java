package com.pine.base.component.image_loader;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/11/19
 */

public interface IImageDownloadListener {
    void onRequest(URL url, Map<String, String> requestHeaders);

    void onResponse(int statusCode, Map<String, List<String>> responseHeaders);

    void onFail(int statusCode, String s);
}
