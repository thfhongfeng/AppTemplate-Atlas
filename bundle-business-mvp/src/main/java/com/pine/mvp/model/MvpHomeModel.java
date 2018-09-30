package com.pine.mvp.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.base.mvp.model.IModelAsyncResponse;
import com.pine.mvp.MvpConstants;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.bean.MvpHomePartAEntity;
import com.pine.mvp.bean.MvpHomePartBEntity;
import com.pine.mvp.bean.MvpHomePartCEntity;
import com.pine.tool.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomeModel {
    private static final String TAG = LogUtils.makeLogTag(MvpHomeModel.class);
    private static final int HTTP_REQUEST_PART_QUERY_A_LIST = 1;
    private static final int HTTP_REQUEST_PART_QUERY_B_LIST = 2;
    private static final int HTTP_REQUEST_PART_QUERY_C_LIST = 3;

    public void requestHomePartAListData(final HashMap<String, String> params, @NonNull final IModelAsyncResponse<ArrayList<MvpHomePartAEntity>> callback) {
        String url = MvpUrlConstants.Query_HomePartAList_Data;
        HttpJsonCallback httpStringCallback = new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                ArrayList<MvpHomePartAEntity> retList = new ArrayList<MvpHomePartAEntity>();
                // Test code begin
                int pageNo = Integer.parseInt(params.get("pageNo"));
                int pageSize = Integer.parseInt(params.get("pageSize"));
                String res = "{success:true,code:200,message:'',data:" +
                        "[{title:'Part A Item " + ((pageNo - 1) * pageSize) + "'}";
                if (pageNo < 5) {
                    for (int i = 1; i < pageSize; i++) {
                        res += ",{title:'Part A Item " + ((pageNo - 1) * pageSize + i) + "'}";
                    }
                }
                res += "]}";
                try {
                    jsonObject = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Text code end

                retList = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpHomePartAEntity>>() {
                }.getType());
                callback.onResponse(retList);
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
        HttpRequestManagerProxy.setJsonRequest(url, params, TAG, HTTP_REQUEST_PART_QUERY_A_LIST, httpStringCallback);
    }

    public void requestHomePartBListData(HashMap<String, String> params, @NonNull final IModelAsyncResponse<ArrayList<MvpHomePartBEntity>> callback) {
        String url = MvpUrlConstants.Query_HomePartBList_Data;
        HttpJsonCallback httpStringCallback = new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                ArrayList<MvpHomePartBEntity> retList = new ArrayList<MvpHomePartBEntity>();
                // Test code begin
                String res = "{success:true,code:200,message:'',data:[]}";
                try {
                    jsonObject = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Text code end
                retList = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpHomePartBEntity>>() {
                }.getType());
                callback.onResponse(retList);
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
        HttpRequestManagerProxy.setJsonRequest(url, params, TAG, HTTP_REQUEST_PART_QUERY_B_LIST, httpStringCallback);
    }

    public void requestHomePartCListData(HashMap<String, String> params, @NonNull final IModelAsyncResponse<ArrayList<MvpHomePartCEntity>> callback) {
        String url = MvpUrlConstants.Query_HomePartCList_Data;
        HttpJsonCallback httpStringCallback = new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                ArrayList<MvpHomePartCEntity> retList = new ArrayList<MvpHomePartCEntity>();
                // Test code begin
                String res = "{success:true,code:200,message:'',data:" +
                        "[{title:'Part B Item 0'},{title:'Part B Item 1'}," +
                        "{title:'Part B Item 2'},{title:'Part B Item 3'}," +
                        "{title:'Part B Item 4'},{title:'Part B Item 5'}," +
                        "{title:'Part B Item 6'},{title:'Part B Item 7'}," +
                        "{title:'Part B Item 8'},{title:'Part B Item 9'}," +
                        "{title:'Part B Item 10'},{title:'Part B Item 11'}," +
                        "{title:'Part B Item 12'},{title:'Part B Item 13'}," +
                        "{title:'Part B Item 14'},{title:'Part B Item 15'}]}";
                try {
                    jsonObject = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Text code end
                retList = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpHomePartCEntity>>() {
                }.getType());
                callback.onResponse(retList);
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
        HttpRequestManagerProxy.setJsonRequest(url, params, TAG, HTTP_REQUEST_PART_QUERY_C_LIST, httpStringCallback);
    }
}
