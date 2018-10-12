package com.pine.mvp.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.mvp.MvpConstants;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.bean.MvpShopAndProductEntity;
import com.pine.mvp.bean.MvpShopEntity;
import com.pine.tool.util.DecimalUtils;
import com.pine.tool.util.GPSUtils;
import com.pine.tool.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomeModel {
    private static final String TAG = LogUtils.makeLogTag(MvpHomeModel.class);
    private static final int HTTP_QUERY_SHOP_LIST = 1;
    private static final int HTTP_QUERY_SHOP_AND_PRODUCT_LIST = 2;

    public void requestShopListData(final HashMap<String, String> params,
                                    @NonNull final IModelAsyncResponse<ArrayList<MvpShopEntity>> callback) {
        String url = MvpUrlConstants.Query_HomeShopList;
        HttpJsonCallback httpStringCallback = new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                ArrayList<MvpShopEntity> retList = new ArrayList<MvpShopEntity>();
                // Test code begin
                int pageNo = 1;
                int pageSize = 15;
                if (!TextUtils.isEmpty(params.get("pageNo"))) {
                    pageNo = Integer.parseInt(params.get("pageNo"));
                }
                if (!TextUtils.isEmpty(params.get("pageSize"))) {
                    pageSize = Integer.parseInt(params.get("pageSize"));
                }
                String distanceStr = "";
                double distance = -1d;
                if (params.get("latitude") != null && params.get("longitude") != null) {
                    BigDecimal startLatBd = new BigDecimal(params.get("latitude"));
                    BigDecimal startLonBd = new BigDecimal(params.get("longitude"));
                    double endLatBd = 31.221367;
                    double endLonBd = 121.635707;
                    double[] locations = GPSUtils.bd09_To_gps84(endLatBd, endLonBd);
                    distance = GPSUtils.getDistance(locations[0], locations[1],
                            startLatBd.doubleValue(), startLonBd.doubleValue());
                    distance += ((pageNo - 1) * 1000 + 50) * pageSize * (pageNo - 1);
                    distanceStr = String.valueOf(distance);
                }
                String res = "{success:true,code:200,message:'',data:" +
                        "[{name:'Shop Item " + ((pageNo - 1) * pageSize) +
                        "', distance:'" + distanceStr + "',imgUrl:''}";
                if (pageNo < 5) {
                    for (int i = 1; i < pageSize; i++) {
                        if (!DecimalUtils.isEqual(distance, -1f)) {
                            distance += (pageNo - 1) * 1000 + 50;
                            distanceStr = String.valueOf(distance);
                        }
                        res += ",{name:'Shop Item " + ((pageNo - 1) * pageSize + i) +
                                "', distance:'" + distanceStr + "',imgUrl:''}";
                    }
                }
                res += "]}";
                try {
                    jsonObject = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Text code end

                retList = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpShopEntity>>() {
                }.getType());
                callback.onResponse(retList);
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
        HttpRequestManagerProxy.setJsonRequest(url, params, TAG, HTTP_QUERY_SHOP_LIST, httpStringCallback);
    }

    public void requestShopAndProductListData(final HashMap<String, String> params,
                                              @NonNull final IModelAsyncResponse<ArrayList<MvpShopAndProductEntity>> callback) {
        String url = MvpUrlConstants.Query_HomeShopAndProductList;
        HttpJsonCallback httpStringCallback = new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                ArrayList<MvpShopAndProductEntity> retList = new ArrayList<>();
                // Test code begin
                int pageNo = 1;
                int pageSize = 15;
                if (!TextUtils.isEmpty(params.get("pageNo"))) {
                    pageNo = Integer.parseInt(params.get("pageNo"));
                }
                if (!TextUtils.isEmpty(params.get("pageSize"))) {
                    pageSize = Integer.parseInt(params.get("pageSize"));
                }
                String distanceStr = "";
                double distance = -1d;
                if (params.get("latitude") != null && params.get("longitude") != null) {
                    BigDecimal startLatBd = new BigDecimal(params.get("latitude"));
                    BigDecimal startLonBd = new BigDecimal(params.get("longitude"));
                    double endLatBd = 31.221367;
                    double endLonBd = 121.635707;
                    double[] locations = GPSUtils.bd09_To_gps84(endLatBd, endLonBd);
                    distance = GPSUtils.getDistance(locations[0], locations[1],
                            startLatBd.doubleValue(), startLonBd.doubleValue());
                    distance += ((pageNo - 1) * 1000 + 50) * pageSize * (pageNo - 1);
                    distanceStr = String.valueOf(distance);
                }
                String res = "{success:true,code:200,message:'',data:" +
                        "[{name:'Shop Item " + ((pageNo - 1) * pageSize) +
                        "', distance:'" + distanceStr + "',imgUrl:''," +
                        "products:[{name:'Product Item 1'}, " +
                        "{name:'Product Item 2'},{name:'Product Item 3'}]}";
                if (pageNo < 5) {
                    for (int i = 1; i < pageSize; i++) {
                        if (!DecimalUtils.isEqual(distance, -1f)) {
                            distance += (pageNo - 1) * 1000 + 50;
                            distanceStr = String.valueOf(distance);
                        }
                        res += ",{name:'Shop Item " + ((pageNo - 1) * pageSize + i) +
                                "', distance:'" + distanceStr + "',imgUrl:'', " +
                                "products:[{name:'Product Item 1'}, {name:'Product Item 2'}]}";
                    }
                }
                res += "]}";
                try {
                    jsonObject = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Text code end

                retList = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpShopAndProductEntity>>() {
                }.getType());
                callback.onResponse(retList);
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
        HttpRequestManagerProxy.setJsonRequest(url, params, TAG, HTTP_QUERY_SHOP_AND_PRODUCT_LIST, httpStringCallback);
    }
}
