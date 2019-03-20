package com.pine.mvp.model.net;

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
import com.pine.mvp.model.IMvpShopModel;
import com.pine.tool.util.DecimalUtils;
import com.pine.tool.util.GPSUtils;
import com.pine.tool.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopModel implements IMvpShopModel {
    private final String TAG = LogUtils.makeLogTag(this.getClass());
    private static final int HTTP_ADD_SHOP = 1;
    private static final int HTTP_QUERY_SHOP_DETAIL = 2;
    private static final int HTTP_QUERY_SHOP_LIST = 3;
    private static final int HTTP_QUERY_SHOP_AND_PRODUCT_LIST = 4;

    protected MvpShopModel() {

    }

    @Override
    public void requestAddShop(final Map<String, String> params,
                               @NonNull final IModelAsyncResponse<MvpShopDetailEntity> callback) {
        String url = MvpUrlConstants.Add_Shop;
        HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_ADD_SHOP,
                handleHttpResponse(callback, null));
    }

    @Override
    public void requestShopDetailData(final Map<String, String> params,
                                      @NonNull final IModelAsyncResponse<MvpShopDetailEntity> callback) {
        String url = MvpUrlConstants.Query_ShopDetail;
        HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_QUERY_SHOP_DETAIL,
                handleHttpResponse(callback, null));
    }

    @Override
    public void requestShopListData(final Map<String, String> params,
                                    @NonNull final IModelAsyncResponse<ArrayList<MvpShopItemEntity>> callback) {
        String url = MvpUrlConstants.Query_ShopList;
        HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_QUERY_SHOP_LIST,
                handleHttpResponse(callback, params.get(MvpConstants.PAGE_NO)));
    }

    @Override
    public void requestShopAndProductListData(Map<String, String> params,
                                              @NonNull final IModelAsyncResponse<ArrayList<MvpShopAndProductEntity>> callback) {
        String url = MvpUrlConstants.Query_ShopAndProductList;
        HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_QUERY_SHOP_AND_PRODUCT_LIST,
                handleHttpResponse(callback, params.get(MvpConstants.PAGE_NO)));
    }

    private <T> HttpJsonCallback handleHttpResponse(final IModelAsyncResponse<T> callback,
                                                    final Object carryData) {
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
                    jsonObject = getShopListData(carryData != null ? Integer.parseInt(carryData.toString()) : 1);
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
                    jsonObject = getShopAndProductListData(carryData != null ? Integer.parseInt(carryData.toString()) : 1);
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
            public boolean onFail(int what, Exception e) {
                return callback.onFail(e);
            }

            @Override
            public void onCancel(int what) {
                callback.onCancel();
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
                "',type:'2',typeName:'食品',mainImgUrl:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp'" +
                ",distance:'" + distanceStr + "',latitude:'" + endLatBd + "',longitude:'" + endLonBd +
                "',imgUrls:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp,https://hellorfimg.zcool.cn/preview/70789213.jpg'" +
                ",onlineDate:'2019-03-01',mobile:'18672943566'" +
                ",addressDistrict:'上海市浦东新区浦东新区',addressZipCode:'310115'" +
                ",addressStreet:'盛夏路888号'" +
                ",description:'Shop Detail description'}}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private JSONObject getShopListData(int pageNo) {
        if (new Random().nextInt(10) == 9) {
            try {
                return new JSONObject("{success:true,code:200,message:'',data:[]}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        double endLatBd = 31.221367;
        double endLonBd = 121.635707;
        double startLatBd = DecimalUtils.add(endLatBd, new Random().nextDouble(), 6);
        double startLonBd = DecimalUtils.add(endLonBd, new Random().nextDouble(), 6);
        double[] locations = GPSUtils.bd09_To_gps84(endLatBd, endLonBd);
        double distance = GPSUtils.getDistance(locations[0], locations[1],
                startLatBd, startLonBd);
        String distanceStr = String.valueOf(distance);
        int startIndex = (pageNo - 1) * 10 + 1;
        String res = "{success:true,code:200,message:'',data:" +
                "[{id:'" + startIndex + "',name:'Shop Item " + startIndex +
                "', distance:'" + distanceStr + "',mainImgUrl:''}";
        for (int i = 1; i < 10; i++) {
            distance += 1333;
            distanceStr = String.valueOf(distance);
            res += ",{id:'" + (startIndex + i) + "',name:'Shop Item " + (startIndex + i) +
                    "', distance:'" + distanceStr + "',mainImgUrl:''}";
        }
        res += "]}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private JSONObject getShopAndProductListData(int pageNo) {
        if (new Random().nextInt(5) == 4) {
            try {
                return new JSONObject("{success:true,code:200,message:'',data:[]}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        double endLatBd = 31.221367;
        double endLonBd = 121.635707;
        double startLatBd = DecimalUtils.add(endLatBd, new Random().nextDouble(), 6);
        double startLonBd = DecimalUtils.add(endLonBd, new Random().nextDouble(), 6);
        double[] locations = GPSUtils.bd09_To_gps84(endLatBd, endLonBd);
        double distance = GPSUtils.getDistance(locations[0], locations[1],
                startLatBd, startLonBd);
        String distanceStr = String.valueOf(distance);
        int startIndex = (pageNo - 1) * 10 + 1;
        String res = "{success:true,code:200,message:'',data:" +
                "[{id:'" + startIndex + "',name:'Shop Item " + startIndex + "', distance:'" + distanceStr +
                "',mainImgUrl:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp'," +
                "products:[{name:'Product Item 1'}, " +
                "{name:'Product Item 2'},{name:'Product Item 3'}]}";
        for (int i = 1; i < 10; i++) {
            distance += 1333;
            distanceStr = String.valueOf(distance);
            res += ",{id:'" + (startIndex + i) + "',name:'Shop Item " + (startIndex + i) +
                    "', distance:'" + distanceStr + "',mainImgUrl:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp', " +
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
