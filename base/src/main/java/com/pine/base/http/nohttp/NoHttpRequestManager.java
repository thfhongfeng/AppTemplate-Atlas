package com.pine.base.http.nohttp;

import android.content.Context;

import com.pine.base.http.HttpRequestBean;
import com.pine.base.http.HttpRequestMethod;
import com.pine.base.http.HttpResponse;
import com.pine.base.http.IHttpRequestManager;
import com.pine.base.http.IHttpResponseListener;
import com.pine.tool.util.LogUtils;
import com.yanzhenjie.nohttp.BasicBinary;
import com.yanzhenjie.nohttp.Binary;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.IBasicRequest;
import com.yanzhenjie.nohttp.InputStreamBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.OnUploadListener;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.tools.HeaderUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class NoHttpRequestManager implements IHttpRequestManager {
    private final static String TAG = LogUtils.makeLogTag(NoHttpRequestManager.class);
    private static volatile NoHttpRequestManager mInstance;
    private static String mMobileModel = "android";
    private static HashMap<String, String> mHeadParams = new HashMap<>();
    private RequestQueue mRequestQueue;
    private DownloadQueue mDownloadQueue;
    private String mSessionId;

    private NoHttpRequestManager() {

    }

    public static NoHttpRequestManager getInstance() {
        if (mInstance == null) {
            synchronized (NoHttpRequestManager.class) {
                if (mInstance == null) {
                    mInstance = new NoHttpRequestManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 标准回调请求
     */
    private OnResponseListener getStringListener(final IHttpResponseListener.OnResponseListener listener) {
        return new OnResponseListener() {
            @Override
            public void onStart(int what) {
                listener.onStart(what);
            }

            @Override
            public void onSucceed(int what, Response response) {
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setSucceed(response.isSucceed());
                httpResponse.setResponseCode(response.responseCode());
                httpResponse.setTag(response.getTag());
                httpResponse.setData(response.get());
                httpResponse.setException(response.getException());
                httpResponse.setCookies(response.getHeaders().getCookies());
                listener.onSucceed(what, httpResponse);
            }

            @Override
            public void onFailed(int what, Response response) {
                HttpResponse httpResponse = new HttpResponse();
                httpResponse.setSucceed(response.isSucceed());
                httpResponse.setResponseCode(response.responseCode());
                httpResponse.setTag(response.getTag());
                httpResponse.setData(response.get());
                httpResponse.setException(response.getException());
                httpResponse.setCookies(response.getHeaders().getCookies());
                listener.onFailed(what, httpResponse);
            }

            @Override
            public void onFinish(int what) {
                listener.onFinish(what);
            }
        };
    }

    // 下载callback
    private DownloadListener getDownloadListener(final IHttpResponseListener.OnDownloadListener listener) {
        return new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                listener.onDownloadError(what, exception);
            }

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                listener.onStart(what, isResume, rangeSize, allCount);
            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                listener.onProgress(what, progress, fileCount, speed);
            }

            @Override
            public void onFinish(int what, String filePath) {
                listener.onFinish(what, filePath);
            }

            @Override
            public void onCancel(int what) {
                listener.onCancel(what);
            }
        };
    }

    private OnUploadListener getUploadListener(final IHttpResponseListener.OnUploadListener listener) {
        return new OnUploadListener() {
            @Override
            public void onStart(int what) {
                listener.onStart(what);
            }

            @Override
            public void onCancel(int what) {
                listener.onCancel(what);
            }

            @Override
            public void onProgress(int what, int progress) {
                listener.onProgress(what, progress);
            }

            @Override
            public void onFinish(int what) {
                listener.onFinish(what);
            }

            @Override
            public void onError(int what, Exception exception) {
                listener.onError(what, exception);
            }
        };
    }

    @Override
    public void init(Context context, HashMap<String, String> head) {
        if (head != null) {
            mHeadParams = head;
        }
        DBCookieStore dbCookieStore = (DBCookieStore) new DBCookieStore(context).setEnable(false);
        dbCookieStore.setCookieStoreListener(new DBCookieStore.CookieStoreListener() {
            @Override
            public void onSaveCookie(URI uri, HttpCookie cookie) {
                if ("JSessionId".equals(cookie.getName()))
                    cookie.setMaxAge(HeaderUtil.getMaxExpiryMillis());
            }

            @Override
            public void onRemoveCookie(URI uri, HttpCookie cookie) {

            }
        });
        // NoHttp初始化
        NoHttp.initialize(context, new NoHttp.Config()
                .setCookieStore(dbCookieStore) // 设置cookie
                .setNetworkExecutor(new OkHttpNetworkExecutor())); // OkHttp请求
        mRequestQueue = NoHttp.newRequestQueue();
        mDownloadQueue = NoHttp.newDownloadQueue();
    }

    @Override
    public void setJsonRequest(HttpRequestBean requestBean, IHttpResponseListener.OnResponseListener listener) {
        IBasicRequest request = NoHttp.createStringRequest(requestBean.getUrl(), transferToNoHttpHttpMethod(requestBean.getRequestMethod()));
        if (requestBean.getSign() != null) {
            request.setCancelSign(requestBean.getSign());
        }
        addSessionCookie(request);
        mRequestQueue.add(requestBean.getWhat(), (Request) addParams(request, requestBean.getParams()), getStringListener(listener));
    }

    @Override
    public void setDownloadRequest(HttpRequestBean requestBean, IHttpResponseListener.OnDownloadListener listener) {
        IBasicRequest request = NoHttp.createDownloadRequest(requestBean.getUrl(),
                transferToNoHttpHttpMethod(requestBean.getRequestMethod()),
                requestBean.getFileFolder(), requestBean.getFileName(),
                requestBean.isContinue(), requestBean.isDeleteOld());
        if (requestBean.getSign() != null) {
            request.setCancelSign(requestBean.getSign());
        }
        addSessionCookie(request);
        mDownloadQueue.add(requestBean.getWhat(), (DownloadRequest) addParams(request, requestBean.getParams()), getDownloadListener(listener));
    }

    @Override
    public void setUploadRequest(HttpRequestBean requestBean, IHttpResponseListener.OnUploadListener processListener,
                                 IHttpResponseListener.OnResponseListener responseListener) {
        Request<String> request = NoHttp.createStringRequest(requestBean.getUrl(), RequestMethod.POST);
        List<Binary> binaries = new ArrayList<>();
        for (File file : requestBean.getFileList()) {
            BasicBinary binary = null;
            try {
                binary = new InputStreamBinary(new FileInputStream(file), "png");
                binary.setUploadListener(requestBean.getWhat(), getUploadListener(processListener));
                binaries.add(binary);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        request.add(requestBean.getFileKey(), binaries);
        if (requestBean.getParams() != null) {
            Iterator<Map.Entry<String, String>> iterator = requestBean.getParams().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String entryKey = entry.getKey();
                String entryValue = entry.getValue();
                request = (Request<String>) request.add(entryKey, entryValue);
            }
        }
        request.setCancelSign(requestBean.getSign());

        addSessionCookie(request);
        mRequestQueue.add(requestBean.getWhat(), request, getStringListener(responseListener));
    }

    @Override
    public void cancelBySign(Object sign) {
        mRequestQueue.cancelBySign(sign);
    }

    @Override
    public void cancelAll() {
        mRequestQueue.cancelAll();
    }

    @Override
    public String getSessionId() {
        return mSessionId;
    }

    @Override
    public void setSessionId(String sessionId) {
        mSessionId = sessionId;
    }

    // 添加SessionCookie
    @Override
    public void addSessionCookie(Map<String, String> headers) {
        StringBuilder builder = new StringBuilder();
        if (mSessionId != null && mSessionId.length() > 0) {
            builder.append(SESSION_ID);
            builder.append("=");
            builder.append(mSessionId);
        }
        if (headers.containsKey(COOKIE_KEY)) {
            builder.append("; ");
            builder.append(headers.get(COOKIE_KEY));
        }
        headers.put(COOKIE_KEY, builder.toString());
        headers.put(MOBILE_MODEL_KEY, mMobileModel);
    }

    private void addSessionCookie(IBasicRequest request) {
        StringBuilder builder = new StringBuilder();
        if (mSessionId != null && mSessionId.length() > 0) {
            builder.append(SESSION_ID);
            builder.append("=");
            builder.append(mSessionId);
            builder.append("; ");
        }
        request.addHeader(COOKIE_KEY, builder.toString());
        if (mHeadParams.size() != 0) {
            Collection keys = mHeadParams.keySet();
            for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                Object key = iterator.next();
                request.add(key.toString(), mHeadParams.get(key));
            }
        }
        request.addHeader(MOBILE_MODEL_KEY, mMobileModel);
    }

    // 添加参数
    private IBasicRequest addParams(IBasicRequest request, Map<String, String> params) {
        if (params == null) {
            return request;
        }
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String entryKey = entry.getKey();
            String entryValue = entry.getValue();
            request = request.add(entryKey, entryValue);
        }
        return request;
    }

    private RequestMethod transferToNoHttpHttpMethod(HttpRequestMethod method) {
        switch (method.toString().toUpperCase()) {
            case "GET":
                return RequestMethod.GET;
            case "POST":
                return RequestMethod.POST;
            case "PUT":
                return RequestMethod.PUT;
            case "DELETE":
                return RequestMethod.DELETE;
            case "PATCH":
                return RequestMethod.PATCH;
            case "OPTIONS":
                return RequestMethod.OPTIONS;
            case "TRACE":
                return RequestMethod.TRACE;
        }
        return null;
    }
}
