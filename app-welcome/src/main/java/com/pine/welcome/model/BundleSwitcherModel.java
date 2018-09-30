package com.pine.welcome.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.base.mvp.model.IModelAsyncResponse;
import com.pine.tool.util.LogUtils;
import com.pine.welcome.WelcomeConstants;
import com.pine.welcome.WelcomeUrlConstants;
import com.pine.welcome.bean.BundleSwitcherEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public class BundleSwitcherModel {
    private static final String TAG = LogUtils.makeLogTag(BundleSwitcherModel.class);
    private static final int HTTP_REQUEST_QUERY_BUNDLE_SWITCHER = 1;

    public void requestBundleSwitcherData(@NonNull IModelAsyncResponse<ArrayList<BundleSwitcherEntity>> callback) {
        String url = WelcomeUrlConstants.Query_BundleSwitcher_Data;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback);
        HttpRequestManagerProxy.setJsonRequest(url, new HashMap<String, String>(), TAG, HTTP_REQUEST_QUERY_BUNDLE_SWITCHER, httpStringCallback);
    }

    private HttpJsonCallback handleHttpResponse(final IModelAsyncResponse<ArrayList<BundleSwitcherEntity>> callback) {
        return new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                if (HTTP_REQUEST_QUERY_BUNDLE_SWITCHER == what) {
                    // Test code begin
                    String res = "{success:true,code:200,message:'',data:" +
                            "[{bundleKey:'login_bundle', open:true},{bundleKey:'main_bundle', open:true}," +
                            "{bundleKey:'user_bundle', open:true},{bundleKey:'business_mvc_bundle', open:true}," +
                            "{bundleKey:'business_mvp_bundle', open:true}]}";
                    try {
                        jsonObject = new JSONObject(res);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Test code end
                    ArrayList<BundleSwitcherEntity> list = new Gson().fromJson(jsonObject.optString(WelcomeConstants.DATA),
                            new TypeToken<ArrayList<BundleSwitcherEntity>>() {
                            }.getType());
                    callback.onResponse(list);
                }
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
    }
}
