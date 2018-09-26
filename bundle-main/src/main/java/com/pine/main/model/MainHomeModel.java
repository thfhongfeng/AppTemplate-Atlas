package com.pine.main.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.base.http.callback.HttpStringCallback;
import com.pine.base.mvp.model.IModelAsyncResponse;
import com.pine.main.MainUrlConstants;
import com.pine.main.bean.MainHomeGridViewEntity;
import com.pine.tool.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MainHomeModel {
    private static final String TAG = LogUtils.makeLogTag(MainHomeModel.class);
    private static final int HTTP_REQUEST_QUERY_BUSINESS_LIST = 1;

    public void requestBusinessListData(@NonNull final IModelAsyncResponse<ArrayList<MainHomeGridViewEntity>> callback) {
        String url = MainUrlConstants.Query_BusinessList_Data;
        HttpStringCallback httpStringCallback = handleHttpResponse(callback);
        HttpRequestManagerProxy.setStringRequest(url, new HashMap<String, String>(), TAG, HTTP_REQUEST_QUERY_BUSINESS_LIST, httpStringCallback);
    }

    private HttpStringCallback handleHttpResponse(@NonNull final IModelAsyncResponse<ArrayList<MainHomeGridViewEntity>> callback) {
        return new HttpStringCallback() {
            @Override
            public void onResponse(int what, String res) {
                if (HTTP_REQUEST_QUERY_BUSINESS_LIST == what) {
                    ArrayList<MainHomeGridViewEntity> retList = new ArrayList<MainHomeGridViewEntity>();
                    // Test code begin
                    res = "[{name:BusinessA,bundle:business_a_bundle,command:goBusinessAHomeActivity}," +
                            "{name:BusinessB,bundle:business_b_bundle,command:goBusinessBHomeActivity}]";
                    // Text code end
                    retList = new Gson().fromJson(res, new TypeToken<ArrayList<MainHomeGridViewEntity>>() {
                    }.getType());
                    callback.onResponse(retList);
                }
            }

            @Override
            public boolean onError(int what, Exception exception) {
                return false;
            }
        };
    }
}
