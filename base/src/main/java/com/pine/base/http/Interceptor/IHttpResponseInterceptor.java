package com.pine.base.http.Interceptor;

import com.pine.base.http.HttpRequestBean;
import com.pine.base.http.HttpResponse;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public interface IHttpResponseInterceptor {
    boolean onIntercept(int what, HttpRequestBean requestBean, HttpResponse response);
}
