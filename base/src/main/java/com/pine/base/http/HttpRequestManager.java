package com.pine.base.http;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.pine.base.R;
import com.pine.base.http.IHttpRequestManager.ActionType;
import com.pine.base.http.IHttpRequestManager.RequestType;
import com.pine.base.http.Interceptor.IHttpRequestInterceptor;
import com.pine.base.http.Interceptor.IHttpResponseInterceptor;
import com.pine.base.http.callback.HttpDownloadCallback;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.base.http.callback.HttpUploadCallback;
import com.pine.tool.util.AppUtils;
import com.pine.tool.util.LogUtils;
import com.yanzhenjie.nohttp.error.NetworkError;

import java.io.File;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/7.
 */

public class HttpRequestManager {
    private final static String TAG = LogUtils.makeLogTag(HttpRequestManager.class);
    private static Context mApplicationContext;
    private static IHttpRequestManager mRequestManager;

    // 正在进行的请求
    private static Map<String, HttpRequestBean> mLoadingRequestMap = null;
    // 错误返回的请求
    private static Map<String, HttpRequestBean> mErrorRequestMap = null;

    private static List<IHttpRequestInterceptor> mRequestInterceptorList = new ArrayList<IHttpRequestInterceptor>();

    private static List<IHttpResponseInterceptor> mResponseInterceptorList = new ArrayList<IHttpResponseInterceptor>();

    public static void init(Context context) {
        init(context, new HashMap<String, String>());
    }

    /**
     * 初始化
     *
     * @param context
     * @param head
     */
    public static void init(Context context, HashMap<String, String> head) {
        if (context != null) {
            mApplicationContext = context;
        } else {
            mApplicationContext = AppUtils.getApplication();
        }
        mRequestManager = HttpRequestManagerFactory.getRequestManager();
        mRequestManager.init(context, head);
        mLoadingRequestMap = new HashMap<>();
        mErrorRequestMap = new HashMap<>();
    }

    public static void setGlobalResponseInterceptor(IHttpResponseInterceptor interceptor) {
        if (!mResponseInterceptorList.contains(interceptor)) {
            mResponseInterceptorList.add(interceptor);
            LogUtils.releaseLog(TAG, "Global response interceptor: " + interceptor.getClass() + " was added");
        }
    }

    public static void setGlobalRequestInterceptor(IHttpRequestInterceptor interceptor) {
        if (!mRequestInterceptorList.contains(interceptor)) {
            mRequestInterceptorList.add(interceptor);
            LogUtils.releaseLog(TAG, "Global request interceptor: " + interceptor.getClass() + " was added");
        }
    }

    // json请求
    public static boolean setJsonRequest(String url, Map<String, String> params, String moduleTag, HttpJsonCallback callBack) {
        return setJsonRequest(url, params, moduleTag, -1, RequestType.STRING, callBack);
    }

    // json请求
    public static boolean setJsonRequest(String url, Map<String, String> params, String moduleTag, int what, HttpJsonCallback callBack) {
        return setJsonRequest(url, params, moduleTag, what, IHttpRequestManager.RequestType.STRING, callBack);
    }

    // json请求
    public static boolean setJsonRequest(String url, Map<String, String> params, String moduleTag, int what, boolean needLogin, HttpJsonCallback callBack) {
        return setJsonRequest(url, params, moduleTag, what, RequestType.STRING, needLogin, callBack);
    }

    // json请求
    public static boolean setJsonRequest(String url, Map<String, String> params, String moduleTag, int what, Object sign, HttpJsonCallback callBack) {
        return setJsonRequest(url, HttpRequestMethod.POST, params, moduleTag, what, sign, false, RequestType.STRING, callBack);
    }

    // json请求
    public static boolean setJsonRequest(String url, Map<String, String> params, String moduleTag, int what, Object sign, boolean needLogin, HttpJsonCallback callBack) {
        return setJsonRequest(url, HttpRequestMethod.POST, params, moduleTag, what, sign, needLogin, RequestType.STRING, callBack);
    }

    // json请求
    public static boolean setJsonRequest(String url, HttpRequestMethod method, Map<String, String> params, String moduleTag,
                                         int what, HttpJsonCallback callBack) {
        return setJsonRequest(url, method, params, moduleTag, what, null, false, RequestType.STRING, callBack);
    }

    // json请求
    public static boolean setJsonRequest(HttpRequestBean requestBean, ActionType actionType) {
        requestBean.setActionType(actionType);
        return setJsonRequest(requestBean.getUrl(), requestBean.getRequestMethod(), requestBean.getParams(),
                requestBean.getModuleTag(), requestBean.getWhat(), requestBean.getSign(), requestBean.isNeedLogin(),
                requestBean.getRequestType(), (HttpJsonCallback) requestBean.getCallBack());
    }

    // json请求
    public static boolean setJsonRequest(HttpRequestBean requestBean) {
        return setJsonRequest(requestBean.getUrl(), requestBean.getRequestMethod(), requestBean.getParams(),
                requestBean.getModuleTag(), requestBean.getWhat(), requestBean.getSign(), requestBean.isNeedLogin(),
                requestBean.getRequestType(), (HttpJsonCallback) requestBean.getCallBack());
    }

    /**
     * json请求
     *
     * @param url         地址
     * @param method      请求方式：GET、POST等
     * @param params      参数
     * @param moduleTag   模块标识
     * @param what        请求标识code
     * @param sign        cancel标识
     * @param needLogin   是否需要登陆
     * @param requestType 请求分类，目前只区分通用和登录
     * @param callBack    回调
     * @return false表示请求没有被发送出去；true表示请求正常发出
     */
    public static boolean setJsonRequest(String url, HttpRequestMethod method, Map<String, String> params, String moduleTag,
                                         int what, Object sign, boolean needLogin, RequestType requestType, HttpJsonCallback callBack) {
        //设置模块名
        if (!TextUtils.isEmpty(moduleTag)) {
            callBack.setModuleTag(moduleTag);
        }
        callBack.setUrl(url);
        callBack.setWhat(what);

        HttpRequestBean requestBean = new HttpRequestBean(what, callBack);
        requestBean.setUrl(url);
        requestBean.setRequestMethod(method);
        requestBean.setParams(params);
        requestBean.setModuleTag(moduleTag);
        requestBean.setWhat(what);
        requestBean.setSign(sign);
        if (!TextUtils.isEmpty(moduleTag)) {
            requestBean.setModuleTag(moduleTag);
        }
        requestBean.setNeedLogin(needLogin);
        requestBean.setRequestType(requestType);
        requestBean.setCallBack(callBack);
        if (mRequestInterceptorList != null) {
            for (int i = 0; i < mRequestInterceptorList.size(); i++) {
                if (mRequestInterceptorList.get(i).onIntercept(what, requestBean)) {
                    callBack.onCancel(what);
                    return false;
                }
            }
        }
        mLoadingRequestMap.put(requestBean.getKey(), requestBean);

        LogUtils.d(TAG, "Request in json queue - " + requestBean.getModuleTag() +
                "(what:" + requestBean.getWhat() + ")" + "\r\n- url: " +
                requestBean.getUrl() + "\r\n- params:" + requestBean.getParams() +
                "\r\n- Cookies: " + getCookiesLog());
        mRequestManager.setJsonRequest(requestBean, getResponseListener(requestBean.getKey(), callBack));
        return true;
    }

    // 下载文件
    public static boolean setDownloadRequest(String url, String fileFolder, String fileName,
                                             int what, Object sign, HttpDownloadCallback callBack) {
        return setDownloadRequest(url, fileFolder, fileName, HttpRequestMethod.GET, new HashMap<String, String>(),
                null, false, true, what, sign, false, callBack);
    }

    // 下载文件
    public static boolean setDownloadRequest(String url, String fileFolder, String fileName, String moduleTag,
                                             int what, HttpDownloadCallback callBack) {
        return setDownloadRequest(url, fileFolder, fileName, HttpRequestMethod.GET, new HashMap<String, String>(),
                moduleTag, false, true, what, null, false, callBack);
    }

    // 下载文件
    public static boolean setDownloadRequest(String url, String fileFolder, String fileName, String moduleTag,
                                             int what, boolean needLogin, HttpDownloadCallback callBack) {
        return setDownloadRequest(url, fileFolder, fileName, HttpRequestMethod.GET, new HashMap<String, String>(),
                moduleTag, false, true, what, null, needLogin, callBack);
    }

    // 下载文件
    public static boolean setDownloadRequest(String url, String fileFolder, String fileName,
                                             String moduleTag, boolean isContinue, boolean isDeleteOld,
                                             int what, boolean needLogin, HttpDownloadCallback callBack) {
        return setDownloadRequest(url, fileFolder, fileName, HttpRequestMethod.GET, new HashMap<String, String>(),
                moduleTag, isContinue, isDeleteOld, what, null, needLogin, callBack);
    }

    // 下载文件
    public static boolean setDownloadRequest(String url, String fileFolder, String fileName, HttpRequestMethod method,
                                             HashMap<String, String> params, String moduleTag, boolean isContinue, boolean isDeleteOld,
                                             int what, boolean needLogin, HttpDownloadCallback callBack) {
        return setDownloadRequest(url, fileFolder, fileName, method, params, moduleTag, isContinue,
                isDeleteOld, what, null, needLogin, callBack);
    }

    /**
     * 下载文件
     *
     * @param url         地址
     * @param fileFolder  下载文件保存目录
     * @param fileName    下载文件保存文件名
     * @param method      请求方式：GET、POST等
     * @param params      参数
     * @param moduleTag   模块标识
     * @param isContinue  是否继续之前的下载
     * @param isDeleteOld 是否删除之前的下载
     * @param what        请求标识code
     * @param sign        cancel标识
     * @param needLogin   是否需要登陆
     * @param callBack    回调
     * @return false表示请求没有被发送出去；true表示请求正常发出
     */
    public static boolean setDownloadRequest(String url, String fileFolder, String fileName,
                                             HttpRequestMethod method, HashMap<String, String> params,
                                             String moduleTag, boolean isContinue, boolean isDeleteOld,
                                             int what, Object sign, boolean needLogin, HttpDownloadCallback callBack) {
        //设置模块名
        if (!TextUtils.isEmpty(moduleTag)) {
            callBack.setModuleTag(moduleTag);
        }
        callBack.setUrl(url);
        callBack.setWhat(what);

        HttpRequestBean requestBean = new HttpRequestBean(what, callBack);
        requestBean.setUrl(url);
        requestBean.setSaveFolder(fileFolder);
        requestBean.setSaveFileName(fileName);
        requestBean.setRequestMethod(method);
        requestBean.setParams(params);
        requestBean.setModuleTag(moduleTag);
        requestBean.setContinue(isContinue);
        requestBean.setDeleteOld(isDeleteOld);
        requestBean.setWhat(what);
        requestBean.setSign(sign);
        if (!TextUtils.isEmpty(moduleTag)) {
            requestBean.setModuleTag(moduleTag);
        }
        requestBean.setRequestType(RequestType.DOWNLOAD);
        requestBean.setNeedLogin(needLogin);
        requestBean.setCallBack(callBack);

        if (mRequestInterceptorList != null) {
            for (int i = 0; i < mRequestInterceptorList.size(); i++) {
                if (mRequestInterceptorList.get(i).onIntercept(what, requestBean)) {
                    callBack.onCancel(what);
                    return false;
                }
            }
        }
        mLoadingRequestMap.put(requestBean.getKey(), requestBean);

        LogUtils.d(TAG, "Request in download queue - " + requestBean.getModuleTag() +
                "(what:" + requestBean.getWhat() + ")" + "\r\n- url: " +
                requestBean.getUrl() + "\r\n -params: " + requestBean.getParams() +
                "\r\n- Cookies: " + getCookiesLog());
        mRequestManager.setDownloadRequest(requestBean, getDownloadListener(requestBean.getKey(), callBack));
        return true;
    }

    /**
     * 上传单个文件
     */
    public static boolean setUploadRequest(String url, Map<String, String> params,
                                           String fileKey, String fileName, File file,
                                           int what, Object sign,
                                           HttpUploadCallback processCallback, HttpJsonCallback requestCallback) {
        return setUploadRequest(url, params, null, fileKey, fileName, file, what, sign,
                false, processCallback, requestCallback);
    }

    /**
     * 上传单个文件
     */
    public static boolean setUploadRequest(String url, Map<String, String> params, String moduleTag,
                                           String fileKey, String fileName, File file,
                                           int what, Object sign, boolean needLogin,
                                           HttpUploadCallback processCallback,
                                           HttpJsonCallback requestCallback) {
        ArrayList<HttpRequestBean.HttpFileBean> fileList = new ArrayList<>();
        HttpRequestBean.HttpFileBean fileBean = new HttpRequestBean.HttpFileBean(fileKey,
                fileName, file, 0);
        fileList.add(fileBean);
        ArrayList<String> fileNameList = new ArrayList<>();
        fileNameList.add(fileName);
        return setUploadRequest(url, params, moduleTag, null, fileList, what,
                sign, needLogin, processCallback, requestCallback);
    }

    /**
     * 上传多个文件
     */
    public static boolean setUploadRequest(String url, Map<String, String> params,
                                           List<HttpRequestBean.HttpFileBean> httpFileList,
                                           int what, Object sign,
                                           HttpUploadCallback processCallback,
                                           HttpJsonCallback requestCallback) {
        return setUploadRequest(url, params, null, null, httpFileList,
                what, sign, false, processCallback, requestCallback);
    }

    /**
     * 上传多个文件
     */
    public static boolean setUploadRequest(String url, Map<String, String> params, String fileKey,
                                           List<HttpRequestBean.HttpFileBean> httpFileList,
                                           int what, Object sign,
                                           HttpUploadCallback processCallback,
                                           HttpJsonCallback requestCallback) {
        return setUploadRequest(url, params, null, fileKey, httpFileList,
                what, sign, false, processCallback, requestCallback);
    }

    /**
     * 上传多个文件
     *
     * @param url             地址
     * @param params          普通参数
     * @param moduleTag       模块标识
     * @param fileKey         文件的key
     * @param httpFileList    上传文件集合
     * @param what            请求标识code
     * @param sign            用于取消的sign
     * @param needLogin       是否需要登陆
     * @param processCallback
     * @param requestCallback
     * @return false表示请求没有被发送出去；true表示请求正常发出
     */
    public static boolean setUploadRequest(String url, Map<String, String> params, String moduleTag,
                                           String fileKey, List<HttpRequestBean.HttpFileBean> httpFileList,
                                           int what, Object sign, boolean needLogin,
                                           HttpUploadCallback processCallback, HttpJsonCallback requestCallback) {
        if (!TextUtils.isEmpty(moduleTag)) {
            requestCallback.setModuleTag(moduleTag);
            processCallback.setModuleTag(moduleTag);
        }
        requestCallback.setUrl(url);
        processCallback.setUrl(url);
        requestCallback.setWhat(what);
        processCallback.setWhat(what);

        HttpRequestBean requestBean = new HttpRequestBean(what, requestCallback);
        requestBean.setUrl(url);
        requestBean.setUploadFileList(httpFileList);
        requestBean.setUpLoadFileKey(fileKey);
        requestBean.setRequestMethod(HttpRequestMethod.POST);
        requestBean.setParams(params);
        requestBean.setWhat(what);
        requestBean.setSign(sign);
        if (!TextUtils.isEmpty(moduleTag)) {
            requestBean.setModuleTag(moduleTag);
        }
        requestBean.setRequestType(RequestType.UPLOAD);
        requestBean.setNeedLogin(needLogin);
        requestBean.setCallBack(requestCallback);
        if (mRequestInterceptorList != null) {
            for (int i = 0; i < mRequestInterceptorList.size(); i++) {
                if (mRequestInterceptorList.get(i).onIntercept(what, requestBean)) {
                    requestCallback.onCancel(what);
                    return false;
                }
            }
        }
        mLoadingRequestMap.put(requestBean.getKey(), requestBean);

        LogUtils.d(TAG, "Request in upload queue - " + requestBean.getModuleTag() +
                "(what:" + requestBean.getWhat() + ")" + "\r\n- url: " +
                requestBean.getUrl() + "\r\n- params: " + requestBean.getParams() +
                "\r\n- Cookies: " + getCookiesLog());
        mRequestManager.setUploadRequest(requestBean, getUploadListener(processCallback),
                getResponseListener(requestBean.getKey(), requestCallback));
        return true;
    }

    /**
     * 标准回调请求
     *
     * @param requestKey
     * @param callBack
     * @return
     */
    public static IHttpResponseListener.OnResponseListener getResponseListener(final String requestKey,
                                                                               final HttpJsonCallback callBack) {
        return new IHttpResponseListener.OnResponseListener() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, HttpResponse response) {
                LogUtils.d(TAG, "Response onSucceed in json queue - " + callBack.getModuleTag() +
                        "(what:" + what + ")" + "\r\n- url: " + callBack.getUrl() +
                        "\r\n- response: " + response.getData() +
                        "\r\n- Cookies: " + getCookiesLog());
                HttpRequestBean httpRequestBean = null;
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(requestKey)) {
                    httpRequestBean = mLoadingRequestMap.remove(requestKey);
                    httpRequestBean.setResponse(response);
                }
                if (mResponseInterceptorList != null) {
                    for (int i = 0; i < mResponseInterceptorList.size(); i++) {
                        if (mResponseInterceptorList.get(i).onIntercept(what, httpRequestBean, response)) {
                            return;
                        }
                    }
                }
                callBack.onResponse(what, response);
            }

            @Override
            public void onFailed(int what, HttpResponse response) {
                LogUtils.d(TAG, "Response onFailed in json queue - " + callBack.getModuleTag() +
                        "(what:" + what + ")" + "\r\n- url: " + callBack.getUrl() +
                        "\r\n- response: " + response.getData() +
                        "\r\n- Cookies: " + getCookiesLog());
                HttpRequestBean httpRequestBean = null;
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(requestKey)) {
                    if (mErrorRequestMap == null) {
                        mErrorRequestMap = new HashMap<>();
                    }
                    if (mErrorRequestMap.containsKey(requestKey)) {
                        mErrorRequestMap.remove(requestKey);
                    }
                    mErrorRequestMap.put(requestKey, mLoadingRequestMap.get(requestKey));
                    httpRequestBean = mLoadingRequestMap.remove(requestKey);
                    httpRequestBean.setResponse(response);
                }
                if (mResponseInterceptorList != null) {
                    for (int i = 0; i < mResponseInterceptorList.size(); i++) {
                        if (mResponseInterceptorList.get(i).onIntercept(what, httpRequestBean, response)) {
                            return;
                        }
                    }
                }
                Exception exception = response.getException();
                if (!callBack.onFail(what, exception)) {
                    defaultDeduceErrorResponse(exception);
                }
            }

            @Override
            public void onFinish(int what) {
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(requestKey)) {
                    mLoadingRequestMap.remove(requestKey);
                }
            }
        };
    }

    // 下载callback
    public static IHttpResponseListener.OnDownloadListener getDownloadListener(final String requestKey,
                                                                               final HttpDownloadCallback callBack) {
        return new IHttpResponseListener.OnDownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                LogUtils.d(TAG, "Response onDownloadError in download queue - " + callBack.getModuleTag() +
                        "(what:" + what + ")" + "\r\n- exception: " + exception.toString());
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(requestKey)) {
                    if (mErrorRequestMap == null) {
                        mErrorRequestMap = new HashMap<>();
                    }
                    if (mErrorRequestMap.containsKey(requestKey)) {
                        mErrorRequestMap.remove(requestKey);
                    }
                    mErrorRequestMap.put(requestKey, mLoadingRequestMap.get(requestKey));
                    mLoadingRequestMap.remove(requestKey);
                }
                if (!callBack.onError(what, exception)) {
                    defaultDeduceErrorResponse(exception);
                }
            }

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, long allCount) {
                LogUtils.d(TAG, "Response onStart in download queue - " + callBack.getModuleTag() +
                        "(what:" + what + ")" + "\r\n- url: " + callBack.getUrl() +
                        "\r\n- isResume: " + isResume +
                        "\r\n- rangeSize: " + rangeSize +
                        "\r\n- allCount: " + allCount +
                        "\r\n- Cookies: " + getCookiesLog());
                callBack.onStart(what, isResume, rangeSize, allCount);
            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                callBack.onProgress(what, progress, fileCount, speed);
            }

            @Override
            public void onFinish(int what, String filePath) {
                LogUtils.d(TAG, "Response onFinish in download queue - " + callBack.getModuleTag() +
                        "(what:" + what + ")" + "\r\n- filePath: " + filePath);
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(requestKey)) {
                    mLoadingRequestMap.remove(requestKey);
                }
                callBack.onFinish(what, filePath);
            }

            @Override
            public void onCancel(int what) {
                LogUtils.d(TAG, "Response onCancel in download queue - " + callBack.getModuleTag() +
                        "(what:" + what + ")");
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(requestKey)) {
                    mLoadingRequestMap.remove(requestKey);
                }
                callBack.onCancel(what);
            }
        };
    }

    public static IHttpResponseListener.OnUploadListener getUploadListener(final HttpUploadCallback callBack) {
        return new IHttpResponseListener.OnUploadListener() {
            @Override
            public void onStart(int what, HttpRequestBean.HttpFileBean fileBean) {
                LogUtils.d(TAG, "Response onStart in upload queue - " + callBack.getModuleTag() +
                        "(what:" + what + ")" + "\r\n- url: " + callBack.getUrl() +
                        "\r\n- Cookies: " + getCookiesLog());
                callBack.onStart(what, fileBean);
            }

            @Override
            public void onCancel(int what, HttpRequestBean.HttpFileBean fileBean) {
                callBack.onCancel(what, fileBean);
            }

            @Override
            public void onProgress(int what, HttpRequestBean.HttpFileBean fileBean, int progress) {
                callBack.onProgress(what, fileBean, progress);
            }

            @Override
            public void onFinish(int what, HttpRequestBean.HttpFileBean fileBean) {
                LogUtils.d(TAG, "Response onFinish in upload queue - " + callBack.getModuleTag() +
                        "(what:" + what + ")");
                callBack.onFinish(what, fileBean);
            }

            @Override
            public void onError(int what, HttpRequestBean.HttpFileBean fileBean, Exception exception) {
                LogUtils.d(TAG, "Response onError in upload queue - " + callBack.getModuleTag() +
                        "(what:" + what + ")" + "\r\n- exception: " + exception.toString());
                if (!callBack.onError(what, fileBean, exception)) {
                    defaultDeduceErrorResponse(exception);
                }
            }
        };
    }

    private static void defaultDeduceErrorResponse(Exception exception) {
        if (exception instanceof NetworkError) {
            Toast.makeText(mApplicationContext, mApplicationContext.getString(R.string.base_network_err),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mApplicationContext, mApplicationContext.getString(R.string.base_server_err),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //重新发起一次已失败的网络请求
    public static void reloadErrorRequest(String key) {
        if (mErrorRequestMap == null) {
            return;
        }
        HttpRequestBean bean = mErrorRequestMap.get(key);
        if (bean == null) {
            return;
        }
        bean.setActionType(ActionType.RETRY_WHEN_ERROR);
        if (bean.getRequestType() == RequestType.UPLOAD) {
            mRequestManager.setUploadRequest(bean, getUploadListener((HttpUploadCallback) bean.getCallBack()),
                    getResponseListener(bean.getKey(), (HttpJsonCallback) bean.getCallBack()));
        } else if (bean.getRequestType() == RequestType.DOWNLOAD) {
            mRequestManager.setDownloadRequest(bean, getDownloadListener(bean.getKey(), (HttpDownloadCallback) bean.getCallBack()));
        } else {
            mRequestManager.setJsonRequest(bean, getResponseListener(bean.getKey(), (HttpJsonCallback) bean.getCallBack()));
        }
    }

    //重新发起所有失败的网络请求
    public static void reloadAllErrorRequest() {
        if (mErrorRequestMap == null) {
            return;
        }
        Iterator<String> iterator = mErrorRequestMap.keySet().iterator();
        while (iterator.hasNext()) {
            reloadErrorRequest(iterator.next());
        }
    }

    // 根据sign标识中断对应网络请求
    public static void cancelBySign(Object sign) {
        mRequestManager.cancelBySign(sign);
    }

    // 中断所有网络请求
    public static void cancelAll() {
        mRequestManager.cancelAll();
        mLoadingRequestMap = new HashMap<>();
    }

    //获取正在进行中的网络请求数
    public static int getLoadingRequestCount() {
        return mLoadingRequestMap == null ? 0 : mLoadingRequestMap.size();
    }

    // 获取请求失败的网络请求数
    public static int getErrorRequestCount() {
        return mErrorRequestMap == null ? 0 : mErrorRequestMap.size();
    }

    //获取所有正在进行中的网络请求
    public static Map<String, HttpRequestBean> getAllLoadingRequest() {
        return mLoadingRequestMap;
    }

    //获取所有请求失败的网络请求
    public static Map<String, HttpRequestBean> getAllErrorRequest() {
        return mErrorRequestMap;
    }

    public static String getSessionId() {
        return mRequestManager.getSessionId();
    }

    public static void setSessionId(String sessionId) {
        mRequestManager.setSessionId(sessionId);
    }

    public static List<HttpCookie> getSessionCookie() {
        return mRequestManager.getSessionCookie();
    }

    public static void clearCookie() {
        mRequestManager.clearCookie();
    }

    private static String getCookiesLog() {
        List<HttpCookie> cookies = getSessionCookie();
        String cookiesStr = "";
        if (cookies != null && cookies.size() > 0) {
            cookiesStr = cookies.get(0).toString();
            for (int i = 1; i < cookies.size(); i++) {
                cookiesStr += "  " + cookies.get(i).toString();
            }
        }
        return cookiesStr;
    }
}
