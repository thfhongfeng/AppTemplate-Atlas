package com.pine.mvp.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

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
import java.util.HashMap;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpTravelNoteModel {
    private static final String TAG = LogUtils.makeLogTag(MvpTravelNoteModel.class);
    private static final int HTTP_QUERY_TRAVEL_NOTE_LIST = 1;
    private static final int HTTP_QUERY_TRAVEL_NOTE_DETAIL = 2;
    private static final int HTTP_QUERY_TRAVEL_NOTE_COMMENT_LIST = 3;

    public void requestTravelNoteListData(final HashMap<String, String> params,
                                          @NonNull final IModelAsyncResponse<ArrayList<MvpTravelNoteItemEntity>> callback) {
        String url = MvpUrlConstants.Query_TravelNoteList;
        HttpJsonCallback httpStringCallback = new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                ArrayList<MvpTravelNoteItemEntity> retList = new ArrayList<>();
                // Test code begin
                int pageNo = 1;
                int pageSize = 15;
                if (!TextUtils.isEmpty(params.get("pageNo"))) {
                    pageNo = Integer.parseInt(params.get("pageNo"));
                }
                if (!TextUtils.isEmpty(params.get("pageSize"))) {
                    pageSize = Integer.parseInt(params.get("pageSize"));
                }
                String res = "{success:true,code:200,message:'',data:" +
                        "[{id:'" + ((pageNo - 1) * pageSize) + "',title:'Travel Note Item " + ((pageNo - 1) * pageSize) + "'," +
                        "createTime:'2018-10-10 10:10'}";
                if (pageNo < 500) {
                    for (int i = 1; i < pageSize; i++) {
                        res += ",{id:'" + ((pageNo - 1) * pageSize + i) + "'," +
                                "title:'Travel Note Item " + ((pageNo - 1) * pageSize + i) + "'," +
                                "createTime:'2018-10-10 10:10'}";
                    }
                }
                res += "]}";
                try {
                    jsonObject = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Test code end

                retList = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpTravelNoteItemEntity>>() {
                }.getType());
                callback.onResponse(retList);
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
        HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_QUERY_TRAVEL_NOTE_LIST, httpStringCallback);
    }

    public void requestTravelNoteDetailData(final HashMap<String, String> params,
                                            @NonNull final IModelAsyncResponse<MvpTravelNoteDetailEntity> callback) {
        String url = MvpUrlConstants.Query_TravelNoteDetail;
        HttpJsonCallback httpStringCallback = new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                MvpTravelNoteDetailEntity entity;
                // Test code begin
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
                    jsonObject = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Test code end

                entity = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<MvpTravelNoteDetailEntity>() {
                }.getType());
                callback.onResponse(entity);
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
        HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_QUERY_TRAVEL_NOTE_DETAIL, httpStringCallback);
    }

    public void requestTravelNoteCommentData(final HashMap<String, String> params,
                                             @NonNull final IModelAsyncResponse<ArrayList<MvpTravelNoteCommentEntity>> callback) {
        String url = MvpUrlConstants.Query_TravelNoteCommentList;
        HttpJsonCallback httpStringCallback = new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                ArrayList<MvpTravelNoteCommentEntity> retList = new ArrayList<>();
                // Test code begin
                int pageNo = 1;
                int pageSize = 15;
                if (!TextUtils.isEmpty(params.get("pageNo"))) {
                    pageNo = Integer.parseInt(params.get("pageNo"));
                }
                if (!TextUtils.isEmpty(params.get("pageSize"))) {
                    pageSize = Integer.parseInt(params.get("pageSize"));
                }
                String res = "{success:true,code:200,message:'',data:" +
                        "[{id:'" + ((pageNo - 1) * pageSize) + "',content:'Comment Item " + ((pageNo - 1) * pageSize) + "'," +
                        "name:'评论人员1',imgUrl:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp'," +
                        "createTime:'2018-10-10 10:10'}";
                if (pageNo < 500) {
                    for (int i = 1; i < pageSize; i++) {
                        res += ",{id:'" + ((pageNo - 1) * pageSize + i) + "'," +
                                "content:'Comment Item " + ((pageNo - 1) * pageSize + i) + "'," +
                                "name:'评论人员" + ((pageNo - 1) * pageSize + i) + "'," +
                                "imgUrl:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp'," +
                                "createTime:'2018-10-10 10:10'}";
                    }
                }
                res += "]}";
                try {
                    jsonObject = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Test code end

                retList = new Gson().fromJson(jsonObject.optString(MvpConstants.DATA), new TypeToken<List<MvpTravelNoteCommentEntity>>() {
                }.getType());
                callback.onResponse(retList);
            }

            @Override
            public boolean onError(int what, Exception e) {
                return callback.onFail(e);
            }
        };
        HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_QUERY_TRAVEL_NOTE_COMMENT_LIST, httpStringCallback);
    }
}
