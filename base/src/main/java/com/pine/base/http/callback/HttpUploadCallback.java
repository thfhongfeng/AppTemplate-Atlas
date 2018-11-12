package com.pine.base.http.callback;

import com.pine.base.http.HttpRequestBean;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public abstract class HttpUploadCallback extends HttpAbstractBaseCallback {

    public abstract void onStart(int what, HttpRequestBean.HttpFileBean fileBean);

    public abstract void onCancel(int what, HttpRequestBean.HttpFileBean fileBean);

    public abstract void onProgress(int what, HttpRequestBean.HttpFileBean fileBean, int progress);

    public abstract boolean onError(int what, HttpRequestBean.HttpFileBean fileBean, Exception e);

    public abstract void onFinish(int what, HttpRequestBean.HttpFileBean fileBean);
}
