package com.pine.base.http.callback;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public abstract class HttpDownloadCallback extends HttpAbstractBaseCallback {

    public abstract void onStart(int what, boolean isResume, long rangeSize, long allCount);

    public abstract void onProgress(int what, int progress, long fileCount, long speed);

    public abstract void onFinish(int what, String filePath);

    public abstract boolean onError(int what, Exception e);

    public abstract void onCancel(int what);
}
