package com.pine.mvp.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.mvp.MvpConstants;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.bean.MvpTravelNoteCommentEntity;
import com.pine.mvp.bean.MvpTravelNoteDetailEntity;
import com.pine.mvp.bean.MvpTravelNoteItemEntity;
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

public class MvpTravelNoteModel {
    private static final String TAG = LogUtils.makeLogTag(MvpTravelNoteModel.class);
    private static final int HTTP_ADD_TRAVEL_NOTE = 1;
    private static final int HTTP_QUERY_TRAVEL_NOTE_DETAIL = 2;
    private static final int HTTP_QUERY_TRAVEL_NOTE_LIST = 3;
    private static final int HTTP_QUERY_TRAVEL_NOTE_COMMENT_LIST = 4;

    public boolean requestAddTravelNote(final Map<String, String> params,
                                        @NonNull final IModelAsyncResponse<MvpTravelNoteDetailEntity> callback) {
        String url = MvpUrlConstants.Add_TravelNote;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback);
        return HttpRequestManager.setJsonRequest(url, params, TAG,
                HTTP_ADD_TRAVEL_NOTE, httpStringCallback);
    }

    public boolean requestTravelNoteDetailData(final Map<String, String> params,
                                               @NonNull final IModelAsyncResponse<MvpTravelNoteDetailEntity> callback) {
        String url = MvpUrlConstants.Query_TravelNoteDetail;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback);
        return HttpRequestManager.setJsonRequest(url, params, TAG,
                HTTP_QUERY_TRAVEL_NOTE_DETAIL, httpStringCallback);
    }

    public boolean requestTravelNoteListData(final Map<String, String> params,
                                             @NonNull final IModelAsyncResponse<ArrayList<MvpTravelNoteItemEntity>> callback) {
        String url = MvpUrlConstants.Query_TravelNoteList;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback);
        return HttpRequestManager.setJsonRequest(url, params, TAG,
                HTTP_QUERY_TRAVEL_NOTE_LIST, httpStringCallback);
    }

    public boolean requestTravelNoteCommentData(final Map<String, String> params,
                                                @NonNull final IModelAsyncResponse<ArrayList<MvpTravelNoteCommentEntity>> callback) {
        String url = MvpUrlConstants.Query_TravelNoteCommentList;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback);
        return HttpRequestManager.setJsonRequest(url, params, TAG,
                HTTP_QUERY_TRAVEL_NOTE_COMMENT_LIST, httpStringCallback);
    }

    private <T> HttpJsonCallback handleHttpResponse(final IModelAsyncResponse<T> callback) {
        return new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                if (what == HTTP_ADD_TRAVEL_NOTE) {
                    // Test code begin
                    jsonObject = getTravelNoteDetailData();
                    // Test code end
                    if (jsonObject.optBoolean(MvpConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<MvpTravelNoteDetailEntity>() {
                        }.getType());
                        callback.onResponse(retData);
                    } else {
                        callback.onFail(new Exception(jsonObject.optString("message")));
                    }
                } else if (what == HTTP_QUERY_TRAVEL_NOTE_DETAIL) {
                    // Test code begin
                    jsonObject = getTravelNoteDetailData();
                    // Test code end
                    if (jsonObject.optBoolean(MvpConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<MvpTravelNoteDetailEntity>() {
                        }.getType());
                        callback.onResponse(retData);
                    } else {
                        callback.onFail(new Exception(jsonObject.optString("message")));
                    }
                } else if (what == HTTP_QUERY_TRAVEL_NOTE_LIST) {
                    // Test code begin
                    jsonObject = getTravelNoteListData();
                    // Test code end
                    if (jsonObject.optBoolean(MvpConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpTravelNoteItemEntity>>() {
                        }.getType());
                        callback.onResponse(retData);
                    } else {
                        callback.onFail(new Exception(jsonObject.optString("message")));
                    }
                } else if (what == HTTP_QUERY_TRAVEL_NOTE_COMMENT_LIST) {
                    // Test code begin
                    jsonObject = getTravelNoteCommentData();
                    // Test code end
                    if (jsonObject.optBoolean(MvpConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpTravelNoteCommentEntity>>() {
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
    private JSONObject getTravelNoteDetailData() {
        String res = "{success:true,code:200,message:'',data:" +
                "{id:'1',title:'Travel Note Title', subTitle:'sub title',imgUrl:''," +
                "name:'作者',createTime:'2018-10-10 10:10',likeCount:100," +
                "isLike:true,readCount:10000," +
                "preface:'这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言',";
        res += "days:[{id:'1',day:'第1天',content:'第1天的内容第1天的内容第1天的内容第1天的内容第1天的内容第1天的内容第1天的内容第1天的内容第1天的内容'}";
        for (int i = 1; i < 10; i++) {
            String str = "第" + (i + 1) + "天的内容";
            str += str;
            str += str;
            str += str;
            str += str;
            str += str;
            res += ",{id:'" + (i + 1) + "',day:'第" + (i + 1) + "天',content:'" + str + "'}";
        }
        res += "]}}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private JSONObject getTravelNoteListData() {
        if (new Random().nextInt(10) == 9) {
            try {
                return new JSONObject("{success:true,code:200,message:'',data:[]}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        int startIndex = new Random().nextInt(10000);
        String res = "{success:true,code:200,message:'',data:" +
                "[{id:'" + startIndex + "',title:'Travel Note Item " + startIndex + "'," +
                "createTime:'2018-10-10 10:10'}";
        for (int i = 1; i < 10; i++) {
            res += ",{id:'" + (startIndex + i) + "'," +
                    "title:'Travel Note Item " + (startIndex + i) + "'," +
                    "createTime:'2018-10-10 10:10'}";
        }
        res += "]}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private JSONObject getTravelNoteCommentData() {
        if (new Random().nextInt(10) == 9) {
            try {
                return new JSONObject("{success:true,code:200,message:'',data:[]}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        int startIndex = new Random().nextInt(10000);
        String res = "{success:true,code:200,message:'',data:" +
                "[{id:'" + startIndex + "',content:'Comment Item " + startIndex + "'," +
                "name:'评论人员1',imgUrl:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp'," +
                "createTime:'2018-10-10 10:10'}";
        for (int i = 1; i < 10; i++) {
            res += ",{id:'" + (startIndex + i) + "'," +
                    "content:'Comment Item " + (startIndex + i) + "'," +
                    "name:'评论人员" + (startIndex + i) + "'," +
                    "imgUrl:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp'," +
                    "createTime:'2018-10-10 10:10'}";
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
