package com.pine.mvp.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.mvp.MvpConstants;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.bean.MvpShopAndProductEntity;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.tool.util.DecimalUtils;
import com.pine.tool.util.GPSUtils;
import com.pine.tool.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopModel {
    private static final String TAG = LogUtils.makeLogTag(MvpShopModel.class);
    private static final int HTTP_ADD_SHOP = 1;
    private static final int HTTP_QUERY_SHOP_DETAIL = 2;
    private static final int HTTP_QUERY_SHOP_LIST = 3;
    private static final int HTTP_QUERY_SHOP_AND_PRODUCT_LIST = 4;

    public boolean requestAddShop(final HashMap<String, String> params,
                                  @NonNull final IModelAsyncResponse<MvpShopDetailEntity> callback) {
        String url = MvpUrlConstants.Add_Shop;
        return HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_ADD_SHOP,
                handleHttpResponse(callback));
    }

    public boolean requestShopDetailData(final HashMap<String, String> params,
                                         @NonNull final IModelAsyncResponse<MvpShopDetailEntity> callback) {
        String url = MvpUrlConstants.Query_ShopDetail;
        return HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_QUERY_SHOP_DETAIL,
                handleHttpResponse(callback));
    }

    public boolean requestShopListData(final HashMap<String, String> params,
                                       @NonNull final IModelAsyncResponse<ArrayList<MvpShopItemEntity>> callback) {
        String url = MvpUrlConstants.Query_ShopList;
        return HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_QUERY_SHOP_LIST,
                handleHttpResponse(callback));
    }

    public boolean requestShopAndProductListData(HashMap<String, String> params,
                                                 @NonNull final IModelAsyncResponse<ArrayList<MvpShopAndProductEntity>> callback) {
        String url = MvpUrlConstants.Query_ShopAndProductList;
        return HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_QUERY_SHOP_AND_PRODUCT_LIST,
                handleHttpResponse(callback));
    }

    private <T> HttpJsonCallback handleHttpResponse(final IModelAsyncResponse<T> callback) {
        return new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                if (what == HTTP_ADD_SHOP) {
                    // Test code begin
                    jsonObject = getShopDetailData();
                    // Test code end
                    if (jsonObject.optBoolean(MvpConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<MvpShopDetailEntity>() {
                        }.getType());
                        callback.onResponse(retData);
                    } else {
                        callback.onFail(new Exception(jsonObject.optString("message")));
                    }
                } else if (what == HTTP_QUERY_SHOP_DETAIL) {
                    // Test code begin
                    jsonObject = getShopDetailData();
                    // Test code end
                    if (jsonObject.optBoolean(MvpConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<MvpShopDetailEntity>() {
                        }.getType());
                        callback.onResponse(retData);
                    } else {
                        callback.onFail(new Exception(jsonObject.optString("message")));
                    }
                } else if (what == HTTP_QUERY_SHOP_LIST) {
                    // Test code begin
                    jsonObject = getShopListData();
                    // Test code end
                    if (jsonObject.optBoolean(MvpConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpShopItemEntity>>() {
                        }.getType());
                        callback.onResponse(retData);
                    } else {
                        callback.onFail(new Exception(jsonObject.optString("message")));
                    }
                } else if (what == HTTP_QUERY_SHOP_AND_PRODUCT_LIST) {
                    // Test code begin
                    jsonObject = getShopAndProductListData();
                    // Test code end
                    if (jsonObject.optBoolean(MvpConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpShopAndProductEntity>>() {
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
    private JSONObject getShopDetailData() {
        double endLatBd = 31.221367;
        double endLonBd = 121.635707;
        double startLatBd = DecimalUtils.add(endLatBd, new Random().nextDouble(), 6);
        double startLonBd = DecimalUtils.add(endLonBd, new Random().nextDouble(), 6);
        double[] locations = GPSUtils.bd09_To_gps84(endLatBd, endLonBd);
        double distance = GPSUtils.getDistance(locations[0], locations[1],
                startLatBd, startLonBd);
        String distanceStr = String.valueOf(distance);
        int startIndex = new Random().nextInt(10000);
        String res = "{success:true,code:200,message:'',data:" +
                "{id:'" + startIndex + "',name:'Shop Item " + startIndex +
                "', distance:'" + distanceStr + "',imgUrl:''," +
                "description:'Shop Detail description Shop Detail description Shop Detail description Shop Detail description Shop Detail description'}}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private JSONObject getShopListData() {
        double endLatBd = 31.221367;
        double endLonBd = 121.635707;
        double startLatBd = DecimalUtils.add(endLatBd, new Random().nextDouble(), 6);
        double startLonBd = DecimalUtils.add(endLonBd, new Random().nextDouble(), 6);
        double[] locations = GPSUtils.bd09_To_gps84(endLatBd, endLonBd);
        double distance = GPSUtils.getDistance(locations[0], locations[1],
                startLatBd, startLonBd);
        String distanceStr = String.valueOf(distance);
        int startIndex = new Random().nextInt(10000);
        String res = "{success:true,code:200,message:'',data:" +
                "[{id:'" + startIndex + "',name:'Shop Item " + startIndex +
                "', distance:'" + distanceStr + "',imgUrl:''}";
        for (int i = 1; i < 10; i++) {
            distance += 1333;
            distanceStr = String.valueOf(distance);
            res += ",{id:'" + (startIndex + i) + "',name:'Shop Item " + (startIndex + i) +
                    "', distance:'" + distanceStr + "',imgUrl:''}";
        }
        res += "]}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private JSONObject getShopAndProductListData() {
        double endLatBd = 31.221367;
        double endLonBd = 121.635707;
        double startLatBd = DecimalUtils.add(endLatBd, new Random().nextDouble(), 6);
        double startLonBd = DecimalUtils.add(endLonBd, new Random().nextDouble(), 6);
        double[] locations = GPSUtils.bd09_To_gps84(endLatBd, endLonBd);
        double distance = GPSUtils.getDistance(locations[0], locations[1],
                startLatBd, startLonBd);
        String distanceStr = String.valueOf(distance);
        int startIndex = new Random().nextInt(10000);
        String res = "{success:true,code:200,message:'',data:" +
                "[{id:'" + startIndex + "',name:'Shop Item " + startIndex + "', distance:'" + distanceStr +
                "',imgUrl:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp'," +
                "products:[{name:'Product Item 1'}, " +
                "{name:'Product Item 2'},{name:'Product Item 3'}]}";
        for (int i = 1; i < 10; i++) {
            distance += 1333;
            distanceStr = String.valueOf(distance);
            res += ",{id:'" + (startIndex + i) + "',name:'Shop Item " + (startIndex + i) +
                    "', distance:'" + distanceStr + "',imgUrl:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp', " +
                    "products:[{name:'Product Item 1'}, {name:'Product Item 2'}]}";
        }
        res += "]}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
    // Test code end
}
