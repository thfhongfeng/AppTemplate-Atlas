package com.pine.main.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.main.MainConstants;
import com.pine.main.MainUrlConstants;
import com.pine.main.bean.MainHomeGridViewEntity;
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

    public void requestBusinessListData(@NonNull final IModelAsyncResponse<ArrayList<MainHomeGridViewEntity>> callback) {
        String url = MainUrlConstants.Query_BusinessList_Data;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback);
        HttpRequestManagerProxy.setJsonRequest(url, new HashMap<String, String>(), TAG, HTTP_QUERY_BUSINESS_LIST, httpStringCallback);
    }

    private HttpJsonCallback handleHttpResponse(@NonNull final IModelAsyncResponse<ArrayList<MainHomeGridViewEntity>> callback) {
        return new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                if (HTTP_QUERY_BUSINESS_LIST == what) {
                    ArrayList<MainHomeGridViewEntity> retList = new ArrayList<MainHomeGridViewEntity>();
                    // Test code begin
                    String res = "{success:true,code:200,message:'',data:" +
                            "[{name:'Business Mvc',bundle:business_mvc_bundle,command:goBusinessMvcHomeActivity}," +
                            "{name:'Business Mvp',bundle:business_mvp_bundle,command:goBusinessMvpHomeActivity}]}";
                    try {
                        jsonObject = new JSONObject(res);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Test code end
                    retList = new Gson().fromJson(jsonObject.optString(MainConstants.DATA), new TypeToken<ArrayList<MainHomeGridViewEntity>>() {
                    }.getType());
                    callback.onResponse(retList);
                }
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
    }
}
