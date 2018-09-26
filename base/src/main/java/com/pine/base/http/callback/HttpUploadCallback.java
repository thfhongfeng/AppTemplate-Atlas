package com.pine.base.http.callback;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public abstract class HttpUploadCallback  extends HttpAbstractBaseCallback {

    public abstract void onStart(int what);

    public abstract void onCancel(int what);

    public abstract void onProgress(int what, int progress);

    public abstract boolean onError(int what, Exception exception);

    public abstract void onFinish(int what);
}
