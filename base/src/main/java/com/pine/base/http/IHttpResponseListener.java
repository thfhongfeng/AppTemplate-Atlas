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
        void onStart(int what, HttpRequestBean.HttpFileBean fileBean);

        void onCancel(int what, HttpRequestBean.HttpFileBean fileBean);

        void onProgress(int what, HttpRequestBean.HttpFileBean fileBean, int progress);

        void onFinish(int what, HttpRequestBean.HttpFileBean fileBean);

        void onError(int what, HttpRequestBean.HttpFileBean fileBean, Exception exception);
    }
}
