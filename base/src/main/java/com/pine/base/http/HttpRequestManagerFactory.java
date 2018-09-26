package com.pine.base.http;

import com.pine.base.http.nohttp.NoHttpRequestManager;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class HttpRequestManagerFactory {

    public static IHttpRequestManager getRequestManager() {
        return NoHttpRequestManager.getInstance();
    }
}
