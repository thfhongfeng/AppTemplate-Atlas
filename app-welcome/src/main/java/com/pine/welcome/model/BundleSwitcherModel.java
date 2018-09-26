package com.pine.welcome.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.base.http.callback.HttpStringCallback;
import com.pine.base.mvp.model.IModelAsyncResponse;
import com.pine.tool.util.LogUtils;
import com.pine.welcome.WelcomeUrlConstants;
import com.pine.welcome.bean.BundleSwitcherEntity;

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
        HttpStringCallback httpStringCallback = handleHttpResponse(callback);
        HttpRequestManagerProxy.setStringRequest(url, new HashMap<String, String>(), TAG, HTTP_REQUEST_QUERY_BUNDLE_SWITCHER, httpStringCallback);
    }

    private HttpStringCallback handleHttpResponse(final IModelAsyncResponse<ArrayList<BundleSwitcherEntity>> callback) {
        return new HttpStringCallback() {
            @Override
            public void onResponse(int what, String res) {
                if (HTTP_REQUEST_QUERY_BUNDLE_SWITCHER == what) {
                    // Test code begin
                    res = "[{bundleKey:'login_bundle', open:true},{bundleKey:'main_bundle', open:true}," +
                            "{bundleKey:'user_bundle', open:true},{bundleKey:'business_a_bundle', open:true}," +
                            "{bundleKey:'business_b_bundle', open:true}]";
                    // Test code end
                    ArrayList<BundleSwitcherEntity> list = new Gson().fromJson(res, new TypeToken<ArrayList<BundleSwitcherEntity>>() {
                    }.getType());
                    callback.onResponse(list);
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
