package com.pine.welcome.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.tool.util.LogUtils;
import com.pine.welcome.WelcomeConstants;
import com.pine.welcome.WelcomeUrlConstants;
import com.pine.welcome.bean.VersionEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class VersionModel {
    private static final String TAG = LogUtils.makeLogTag(VersionModel.class);
    private static final int HTTP_QUERY_VERSION_INFO = 1;

    public boolean requestUpdateVersionData(@NonNull IModelAsyncResponse<VersionEntity> callback) {
        String url = WelcomeUrlConstants.Query_Version_Data;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback);
        return HttpRequestManager.setJsonRequest(url, new HashMap<String, String>(),
                TAG, HTTP_QUERY_VERSION_INFO, httpStringCallback);
    }

    private <T> HttpJsonCallback handleHttpResponse(final IModelAsyncResponse<T> callback) {
        return new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                if (HTTP_QUERY_VERSION_INFO == what) {
                    // Test code begin
                    jsonObject = getUpdateVersionData();
                    // Test code end
                    if (jsonObject.optBoolean(WelcomeConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(WelcomeConstants.DATA), new TypeToken<VersionEntity>() {
                        }.getType());
                        callback.onResponse(retData);
                    } else {
                        callback.onFail(new Exception(jsonObject.optString("message")));
                    }
                }
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
    }

    // Test code begin
    private JSONObject getUpdateVersionData() {
        String res = "{success:true,code:200,message:'',data:" +
                "{package:'com.pine.template', 'versionCode':2," +
                "versionName:'1.0.2',minSupportedVersion:1," +
                "force:false, fileName:'pine_app_template-V1.0.2-release.apk', " +
                "path:'https://yanyangtian.purang.com/download/bsd_purang.apk'}}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
    // Test code end
}
