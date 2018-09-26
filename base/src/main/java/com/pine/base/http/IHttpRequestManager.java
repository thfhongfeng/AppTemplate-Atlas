package com.pine.base.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public interface IHttpRequestManager {
    String SESSION_ID = "JSESSIONID";
    String COOKIE_KEY = "Cookie";
    String MOBILE_MODEL_KEY = "mobileModel";

    void init(Context context, HashMap<String, String> head);

    void setStringRequest(HttpRequestBean requestBean, IHttpResponseListener.OnResponseListener listener);

    void setDownloadRequest(HttpRequestBean requestBean, IHttpResponseListener.OnDownloadListener listener);

    void setUploadRequest(HttpRequestBean requestBean, IHttpResponseListener.OnUploadListener processListener,
                          IHttpResponseListener.OnResponseListener responseListener);

    void addSessionCookie(Map<String, String> headers);

    void cancelBySign(Object sign);

    void cancelAll();

    void setSessionId(String sessionId);

    String getSessionId();

    enum RequestType {
        STRING, // stringRequest
        UPLOAD, //  uploadRequest
        DOWNLOAD, // downloadRequest
        BITMAP   // bitmapRequest
    }

    enum ActionType {
        COMMON, // common
        RETRY_AFTER_RE_LOGIN, //  retry after re-login
        RETRY_WHEN_ERROR     // retry when error
    }
}
