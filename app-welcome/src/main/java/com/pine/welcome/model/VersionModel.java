package com.pine.welcome.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.base.mvp.model.IModelAsyncResponse;
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
    private static final int HTTP_REQUEST_QUERY_VERSION_INFO = 1;

    public void requestUpdateVersionData(@NonNull IModelAsyncResponse<VersionEntity> callback) {
        String url = WelcomeUrlConstants.Query_Version_Data;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback);
        HttpRequestManagerProxy.setJsonRequest(url, new HashMap<String, String>(), TAG, HTTP_REQUEST_QUERY_VERSION_INFO, httpStringCallback);
    }

    private HttpJsonCallback handleHttpResponse(final IModelAsyncResponse<VersionEntity> callback) {
        return new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                if (HTTP_REQUEST_QUERY_VERSION_INFO == what) {
                    // Test code begin
                    String res = "{success:true,code:200,message:'',data:" +
                            "{package:'com.pine.template', 'versionCode':2," +
                            "versionName:'1.0.2',minSupportedVersion:1," +
                            "force:false, fileName:'pine_app_template-V1.0.2-release.apk', " +
                            "path:'https://yanyangtian.purang.com/download/bsd_purang.apk'}}";
                    try {
                        jsonObject = new JSONObject(res);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Test code end
                    VersionEntity versionEntity = new Gson().fromJson(jsonObject.optString(WelcomeConstants.DATA), VersionEntity.class);
                    callback.onResponse(versionEntity);
                }
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
    }
}
