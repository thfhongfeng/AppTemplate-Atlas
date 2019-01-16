package com.pine.main.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.main.MainConstants;
import com.pine.main.MainUrlConstants;
import com.pine.main.bean.MainBusinessItemEntity;
import com.pine.router.RouterBundleKey;
import com.pine.router.RouterCommand;
import com.pine.tool.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MainHomeModel {
    private static final String TAG = LogUtils.makeLogTag(MainHomeModel.class);
    private static final int HTTP_QUERY_BUSINESS_LIST = 1;

    public boolean requestBusinessListData(@NonNull final IModelAsyncResponse<ArrayList<MainBusinessItemEntity>> callback) {
        String url = MainUrlConstants.Query_BusinessList_Data;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback);
        return HttpRequestManager.setJsonRequest(url, new HashMap<String, String>(), TAG,
                HTTP_QUERY_BUSINESS_LIST, httpStringCallback);
    }

    private <T> HttpJsonCallback handleHttpResponse(final IModelAsyncResponse<T> callback) {
        return new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                if (HTTP_QUERY_BUSINESS_LIST == what) {
                    // Test code begin
                    jsonObject = getBusinessListData();
                    // Test code end
                    if (jsonObject.optBoolean(MainConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(MainConstants.DATA), new TypeToken<ArrayList<MainBusinessItemEntity>>() {
                        }.getType());
                        callback.onResponse(retData);
                    } else {
                        callback.onFail(new Exception(jsonObject.optString("message")));
                    }
                }
            }

            @Override
            public boolean onFail(int what, Exception e) {
                return callback.onFail(e);
            }
        };
    }

    // Test code begin
    private JSONObject getBusinessListData() {
        String res = "{success:true,code:200,message:'',data:" +
                "[{name:'Business Mvc',bundle:" + RouterBundleKey.BUSINESS_MVC_BUNDLE_KEY
                + ",command:" + RouterCommand.BUSINESS_goBusinessMvcHomeActivity + "},"
                + "{name:'Business Mvp',bundle:" + RouterBundleKey.BUSINESS_MVP_BUNDLE_KEY
                + ",command:" + RouterCommand.BUSINESS_goBusinessMvpHomeActivity + "},"
                + "{name:'Business Mvvm',bundle:" + RouterBundleKey.BUSINESS_MVVM_BUNDLE_KEY
                + ",command:" + RouterCommand.BUSINESS_goBusinessMvvmHomeActivity + "},"
                + "{name:'Business Demo',bundle:" + RouterBundleKey.BUSINESS_DEMO_BUNDLE_KEY
                + ",command:" + RouterCommand.BUSINESS_goBusinessDemoHomeActivity + "}]}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
    // Test code end
}
