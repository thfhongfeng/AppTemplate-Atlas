package com.pine.base.http;

import com.pine.base.BuildConfig;
import com.pine.base.http.nohttp.NoHttpRequestManager;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class HttpRequestManagerFactory {
    private HttpRequestManagerFactory() {

    }

    protected static IHttpRequestManager getRequestManager() {
        switch (BuildConfig.APP_THIRD_HTTP_REQUEST_PROVIDER) {
            case "nohttp":
                return NoHttpRequestManager.getInstance();
            default:
                return NoHttpRequestManager.getInstance();
        }
    }
}
