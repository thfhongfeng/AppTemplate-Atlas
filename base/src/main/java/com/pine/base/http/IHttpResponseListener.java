package com.pine.base.http;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public interface IHttpResponseListener {
    interface OnResponseListener {
        void onStart(int what);

        void onSucceed(int what, HttpResponse response);

        void onFailed(int what, HttpResponse response);

        void onFinish(int what);
    }

    interface OnDownloadListener {
        void onDownloadError(int what, Exception exception);

        void onStart(int what, boolean isResume, long rangeSize, long allCount);

        void onProgress(int what, int progress, long fileCount, long speed);

        void onFinish(int what, String filePath);

        void onCancel(int what);
    }

    interface OnUploadListener {
        void onStart(int what);

        void onCancel(int what);

        void onProgress(int what, int progress);

        void onFinish(int what);

        void onError(int what, Exception exception);
    }
}
