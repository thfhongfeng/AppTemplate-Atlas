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
    private final String TAG = LogUtils.makeLogTag(this.getClass());
    private static final int HTTP_ADD_TRAVEL_NOTE = 1;
    private static final int HTTP_QUERY_TRAVEL_NOTE_DETAIL = 2;
    private static final int HTTP_QUERY_TRAVEL_NOTE_LIST = 3;
    private static final int HTTP_QUERY_TRAVEL_NOTE_COMMENT_LIST = 4;

    public void requestAddTravelNote(final Map<String, String> params,
                                     @NonNull final IModelAsyncResponse<MvpTravelNoteDetailEntity> callback) {
        String url = MvpUrlConstants.Add_TravelNote;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback, null);
        HttpRequestManager.setJsonRequest(url, params, TAG,
                HTTP_ADD_TRAVEL_NOTE, httpStringCallback);
    }

    public void requestTravelNoteDetailData(final Map<String, String> params,
                                            @NonNull final IModelAsyncResponse<MvpTravelNoteDetailEntity> callback) {
        String url = MvpUrlConstants.Query_TravelNoteDetail;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback, null);
        HttpRequestManager.setJsonRequest(url, params, TAG,
                HTTP_QUERY_TRAVEL_NOTE_DETAIL, httpStringCallback);
    }

    public void requestTravelNoteListData(final Map<String, String> params,
                                          @NonNull final IModelAsyncResponse<ArrayList<MvpTravelNoteItemEntity>> callback) {
        String url = MvpUrlConstants.Query_TravelNoteList;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback, params.get(MvpConstants.PAGE_NO));
        HttpRequestManager.setJsonRequest(url, params, TAG,
                HTTP_QUERY_TRAVEL_NOTE_LIST, httpStringCallback);
    }

    public void requestTravelNoteCommentData(final Map<String, String> params,
                                             @NonNull final IModelAsyncResponse<ArrayList<MvpTravelNoteCommentEntity>> callback) {
        String url = MvpUrlConstants.Query_TravelNoteCommentList;
        HttpJsonCallback httpStringCallback = handleHttpResponse(callback, params.get(MvpConstants.PAGE_NO));
        HttpRequestManager.setJsonRequest(url, params, TAG,
                HTTP_QUERY_TRAVEL_NOTE_COMMENT_LIST, httpStringCallback);
    }

    private <T> HttpJsonCallback handleHttpResponse(final IModelAsyncResponse<T> callback,
                                                    final Object carryData) {
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
                    jsonObject = getTravelNoteListData(carryData != null ? Integer.parseInt(carryData.toString()) : 1);
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
                    jsonObject = getTravelNoteCommentData(carryData != null ? Integer.parseInt(carryData.toString()) : 1);
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

            @Override
            public void onCancel(int what) {
                callback.onCancel();
            }
        };
    }

    // Test code begin
    private final String[] IMAGE_ARR = {"http://pic9.nipic.com/20100824/2531170_082435310724_2.jpg",
            "http://img.juimg.com/tuku/yulantu/140218/330598-14021R23A410.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1568060428,2727116091&fm=26&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2189972113,381634258&fm=26&gp=0.jpg",
            "http://pic31.nipic.com/20130720/5793914_122325176000_2.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3528623204,755864954&fm=26&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1922419374,2716826347&fm=26&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3130635505,2228339018&fm=26&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=1372993673,3445969129&fm=26&gp=0.jpg"};

    private JSONObject getTravelNoteDetailData() {
        String res = "{success:true,code:200,message:'',data:" +
                "{id:'1',title:'Travel Note Title', setOutDate:'2018-10-11 10:10',headImg:''," +
                "author:'作者',belongShops:[{id:'1', name:'Shop Item 1'},{id:'2', name:'Shop Item 2'}],createTime:'2018-10-10 10:10',likeCount:100," +
                "isLike:" + (new Random().nextInt(10) > 5) + ",readCount:10000," +
                "preface:'这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言这是一段前言',";
        res += "days:[{id:'1',day:'第1天',contentList:[{type:'text',index:'1',text:'第1天第1段'}," +
                "{type:'text',index:'2',text:'第1天第2段'}]}";
        for (int i = 1; i < 10; i++) {
            String str = "[{type:'text',index:'1',text:'第" + (i + 1) + "天第1段'}," +
                    "{type:'image',index:'2',remoteFilePath:'" + IMAGE_ARR[i - 1] + "',text:'第" + (i + 1) + "天第2段'}," +
                    "{type:'text',index:'3',text:'第" + (i + 1) + "天第3段'}]";
            res += ",{id:'" + (i + 1) + "',day:'第" + (i + 1) + "天',contentList:" + str + "}";
        }
        res += "]}}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private JSONObject getTravelNoteListData(int pageNo) {
        if (new Random().nextInt(10) == 9) {
            try {
                return new JSONObject("{success:true,code:200,message:'',data:[]}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        int startIndex = (pageNo - 1) * 10 + 1;
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

    private JSONObject getTravelNoteCommentData(int pageNo) {
        if (new Random().nextInt(10) == 9) {
            try {
                return new JSONObject("{success:true,code:200,message:'',data:[]}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        int startIndex = (pageNo - 1) * 10 + 1;
        String res = "{success:true,code:200,message:'',data:" +
                "[{id:'" + startIndex + "',content:'Comment Item " + startIndex + "'," +
                "author:'评论人员1',imgUrl:'https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp'," +
                "createTime:'2018-10-10 10:10'}";
        for (int i = 1; i < 10; i++) {
            res += ",{id:'" + (startIndex + i) + "'," +
                    "content:'Comment Item " + (startIndex + i) + "'," +
                    "author:'评论人员" + (startIndex + i) + "'," +
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
