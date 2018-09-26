package com.pine.base.http.Interceptor;

import com.pine.base.http.HttpRequestBean;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public interface IHttpRequestInterceptor {
    boolean onIntercept(int what, HttpRequestBean requestBean);
}
