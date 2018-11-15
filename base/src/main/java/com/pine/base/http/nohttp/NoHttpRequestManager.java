package com.pine.base.http.nohttp;

import android.content.Context;
import android.text.TextUtils;

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
                    LogUtils.releaseLog(TAG, "use http request: nohttp");
                    mInstance = new NoHttpRequestManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 标准回调请求
     */
    private OnResponseListener getResponseListener(final IHttpResponseListener.OnResponseListener listener) {
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

    private OnUploadListener getUploadListener(final IHttpResponseListener.OnUploadListener listener,
                                               final HttpRequestBean.HttpFileBean fileBean) {
        return new OnUploadListener() {
            @Override
            public void onStart(int what) {
                listener.onStart(what, fileBean);
            }

            @Override
            public void onCancel(int what) {
                listener.onCancel(what, fileBean);
            }

            @Override
            public void onProgress(int what, int progress) {
                listener.onProgress(what, fileBean, progress);
            }

            @Override
            public void onFinish(int what) {
                listener.onFinish(what, fileBean);
            }

            @Override
            public void onError(int what, Exception exception) {
                listener.onError(what, fileBean, exception);
            }
        };
    }

    @Override
    public void init(Context context, HashMap<String, String> head) {
        if (head != null) {
            mHeadParams = head;
        }
        DBCookieStore dbCookieStore = (DBCookieStore) new DBCookieStore(context).setEnable(true);
        dbCookieStore.setCookieStoreListener(new DBCookieStore.CookieStoreListener() {
            // 当NoHttp的Cookie被保存的时候被调用
            @Override
            public void onSaveCookie(URI uri, HttpCookie cookie) {
                if (SESSION_ID.equals(cookie.getName().toUpperCase())) {
                    cookie.setMaxAge(HeaderUtil.getMaxExpiryMillis());
                    setSessionId(cookie.getValue());
                }
                LogUtils.d(TAG, "setCookie url:" + uri.toString() +
                        "\r\ncookie:" + cookie.toString());
            }

            // 当NoHttp的Cookie过期时被删除时此方法被调用
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
        IBasicRequest request = NoHttp.createStringRequest(requestBean.getUrl(),
                transferToNoHttpHttpMethod(requestBean.getRequestMethod()));
        if (requestBean.getSign() != null) {
            request.setCancelSign(requestBean.getSign());
        }
        insertGlobalSessionCookie(request);
        mRequestQueue.add(requestBean.getWhat(), (Request) addParams(request,
                requestBean.getParams()), getResponseListener(listener));
    }

    @Override
    public void setDownloadRequest(HttpRequestBean requestBean, IHttpResponseListener.OnDownloadListener listener) {
        IBasicRequest request = NoHttp.createDownloadRequest(requestBean.getUrl(),
                transferToNoHttpHttpMethod(requestBean.getRequestMethod()),
                requestBean.getSaveFolder(), requestBean.getSaveFileName(),
                requestBean.isContinue(), requestBean.isDeleteOld());
        if (requestBean.getSign() != null) {
            request.setCancelSign(requestBean.getSign());
        }
        insertGlobalSessionCookie(request);
        mDownloadQueue.add(requestBean.getWhat(), (DownloadRequest) addParams(request,
                requestBean.getParams()), getDownloadListener(listener));
    }

    @Override
    public void setUploadRequest(HttpRequestBean requestBean, IHttpResponseListener.OnUploadListener processListener,
                                 IHttpResponseListener.OnResponseListener responseListener) {
        if (requestBean.getUploadFileList() == null) {
            return;
        }
        Request<String> request = NoHttp.createStringRequest(requestBean.getUrl(), RequestMethod.POST);
        List<Binary> binaries = new ArrayList<>();
        boolean isMulFileKey = TextUtils.isEmpty(requestBean.getUpLoadFileKey());
        for (int i = 0; i < requestBean.getUploadFileList().size(); i++) {
            HttpRequestBean.HttpFileBean fileBean = requestBean.getUploadFileList().get(i);
            BasicBinary binary = null;
            try {
                binary = new InputStreamBinary(new FileInputStream(fileBean.getFile()), fileBean.getFileName());
                binary.setUploadListener(fileBean.getWhat(), getUploadListener(processListener, fileBean));
                binaries.add(binary);
                if (isMulFileKey) {
                    request.add(TextUtils.isEmpty(fileBean.getFileKey()) ? "file" + i : fileBean.getFileKey(), binaries);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (!isMulFileKey) {
            request.add(requestBean.getUpLoadFileKey(), binaries);
        }
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

        insertGlobalSessionCookie(request);
        mRequestQueue.add(requestBean.getWhat(), request, getResponseListener(responseListener));
    }

    private void insertGlobalSessionCookie(IBasicRequest request) {
        if (mHeadParams.size() != 0) {
            Collection keys = mHeadParams.keySet();
            for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                Object key = iterator.next();
                request.add(key.toString(), mHeadParams.get(key));
            }
        }
        request.addHeader(MOBILE_MODEL_KEY, mMobileModel);
    }

    @Override
    public void cancelBySign(Object sign) {
        mRequestQueue.cancelBySign(sign);
        mDownloadQueue.cancelBySign(sign);
    }

    @Override
    public void cancelAll() {
        mRequestQueue.cancelAll();
        mDownloadQueue.cancelAll();
    }

    @Override
    public void addGlobalSessionCookie(HashMap<String, String> headerMap) {
        if (headerMap == null) {
            return;
        }
        mHeadParams.putAll(headerMap);
    }

    @Override
    public void removeGlobalSessionCookie(List<String> keyList) {
        if (keyList == null || keyList.size() < 1) {
            return;
        }
        for (String key : keyList) {
            mHeadParams.remove(key);
        }
    }

    @Override
    public String getSessionId() {
        return mSessionId;
    }

    @Override
    public void setSessionId(String sessionId) {
        mSessionId = sessionId;
    }

    @Override
    public void clearCookie() {
        NoHttp.getCookieManager().getCookieStore().removeAll();
    }

    @Override
    public List<HttpCookie> getSessionCookie() {
        return NoHttp.getCookieManager().getCookieStore().getCookies();
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
