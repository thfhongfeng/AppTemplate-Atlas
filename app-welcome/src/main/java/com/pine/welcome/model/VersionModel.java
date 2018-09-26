package com.pine.welcome.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.base.http.callback.HttpStringCallback;
import com.pine.base.mvp.model.IModelAsyncResponse;
import com.pine.tool.util.LogUtils;
import com.pine.welcome.WelcomeUrlConstants;
import com.pine.welcome.bean.VersionEntity;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class VersionModel {
    private static final String TAG = LogUtils.makeLogTag(VersionModel.class);
    private static final int HTTP_REQUEST_QUERY_VERSION_INFO = 1;

    public void requestUpdateVersionData(@NonNull IModelAsyncResponse<VersionEntity> callback) {
        String url = WelcomeUrlConstants.Query_Version_Data;
        HttpStringCallback httpStringCallback = handleHttpResponse(callback);
        HttpRequestManagerProxy.setStringRequest(url, new HashMap<String, String>(), TAG, HTTP_REQUEST_QUERY_VERSION_INFO, httpStringCallback);
    }

    private HttpStringCallback handleHttpResponse(final IModelAsyncResponse<VersionEntity> callback) {
        return new HttpStringCallback() {
            @Override
            public void onResponse(int what, String res) {
                if (HTTP_REQUEST_QUERY_VERSION_INFO == what) {
                    // Test code begin
                    res = "{'package':'com.pine.template', 'versionCode':2, " +
                            "'versionName':'1.0.2','minSupportedVersion':1,'force':true, " +
                            "'fileName':'template.apk', 'path':'https://www.baidu.com'}";

                    res = "{\"package\":\"com.purang.bsd_purang\", \"versionCode\":2," +
                            " \"versionName\":\"1.0.2\",\"minSupportedVersion\":1," +
                            "\"force\":false, \"fileName\":\"bsd_purang.apk\", " +
                            "\"path\":\"https://yanyangtian.purang.com/download/bsd_purang.apk\"}";
                    // Test code end
                    VersionEntity versionEntity = new Gson().fromJson(res, VersionEntity.class);
                    callback.onResponse(versionEntity);
                }
            }

            @Override
            public boolean onError(int what, Exception exception) {
                callback.onFail();
                return false;
            }
        };
    }
}
