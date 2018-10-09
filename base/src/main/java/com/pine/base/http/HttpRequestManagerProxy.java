package com.pine.base.http;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/7.
 */

public class HttpRequestManagerProxy {
    private final static String TAG = LogUtils.makeLogTag(HttpRequestManagerProxy.class);
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
            mApplicationContext = AppUtils.getApplicationByReflect();
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

    // 默认RequestMethod:POST
    // 里面的moduleTag参数在自己的模块自己封装，无cancelSign
    public static void setJsonRequest(String url, Map<String, String> params, String moduleTag, HttpJsonCallback callBack) {
        setJsonRequest(url, params, moduleTag, -1, RequestType.STRING, callBack);
    }

    // 默认RequestMethod:POST
    // 里面的moduleTag参数在自己的模块自己封装，无cancelSign
    public static void setJsonRequest(String url, Map<String, String> params, String moduleTag, int what, HttpJsonCallback callBack) {
        setJsonRequest(url, params, moduleTag, what, IHttpRequestManager.RequestType.STRING, callBack);
    }

    // 默认RequestMethod:POST
    // 里面的moduleTag参数在自己的模块自己封装，无cancelSign
    public static void setJsonRequest(String url, Map<String, String> params, String moduleTag, int what, boolean needLogin, HttpJsonCallback callBack) {
        setJsonRequest(url, params, moduleTag, what, RequestType.STRING, needLogin, callBack);
    }

    // 默认RequestMethod:POST
    public static void setJsonRequest(String url, Map<String, String> params, String moduleTag, int what, Object sign, HttpJsonCallback callBack) {
        setJsonRequest(url, HttpRequestMethod.POST, params, moduleTag, what, sign, false, RequestType.STRING, callBack);
    }

    // 默认RequestMethod:POST
    public static void setJsonRequest(String url, Map<String, String> params, String moduleTag, int what, Object sign, boolean needLogin, HttpJsonCallback callBack) {
        setJsonRequest(url, HttpRequestMethod.POST, params, moduleTag, what, sign, needLogin, RequestType.STRING, callBack);
    }

    public static void setJsonRequest(String url, HttpRequestMethod method, Map<String, String> params, String moduleTag,
                                      int what, HttpJsonCallback callBack) {
        setJsonRequest(url, method, params, moduleTag, what, null, false, RequestType.STRING, callBack);
    }

    public static void setJsonRequest(HttpRequestBean requestBean, ActionType actionType) {
        requestBean.setActionType(actionType);
        setJsonRequest(requestBean.getUrl(), requestBean.getRequestMethod(), requestBean.getParams(),
                requestBean.getModuleTag(), requestBean.getWhat(), requestBean.getSign(), requestBean.isNeedLogin(),
                requestBean.getRequestType(), (HttpJsonCallback) requestBean.getCallBack());
    }

    public static void setJsonRequest(HttpRequestBean requestBean) {
        setJsonRequest(requestBean.getUrl(), requestBean.getRequestMethod(), requestBean.getParams(),
                requestBean.getModuleTag(), requestBean.getWhat(), requestBean.getSign(), requestBean.isNeedLogin(),
                requestBean.getRequestType(), (HttpJsonCallback) requestBean.getCallBack());
    }

    /**
     * @param url         地址
     * @param method      请求方式：GET、POST等
     * @param params      参数
     * @param moduleTag   模块标识
     * @param what        请求标识code
     * @param sign        cancel标识
     * @param needLogin   是否需要登陆
     * @param requestType 请求分类，目前只区分通用和登录
     * @param callBack    回调
     */
    public static void setJsonRequest(String url, HttpRequestMethod method, Map<String, String> params, String moduleTag,
                                      int what, Object sign, boolean needLogin, RequestType requestType, HttpJsonCallback callBack) {
        //设置模块名
        callBack.setModuleTag(moduleTag);
        callBack.setUrl(url);
        callBack.setWhat(what);

        HttpRequestBean requestBean = new HttpRequestBean(what, callBack);
        requestBean.setUrl(url);
        requestBean.setRequestMethod(method);
        requestBean.setParams(params);
        requestBean.setModuleTag(moduleTag);
        requestBean.setWhat(what);
        requestBean.setSign(sign);
        requestBean.setNeedLogin(needLogin);
        requestBean.setRequestType(requestType);
        requestBean.setCallBack(callBack);
        if (mRequestInterceptorList != null) {
            for (int i = 0; i < mRequestInterceptorList.size(); i++) {
                if (mRequestInterceptorList.get(i).onIntercept(what, requestBean)) {
                    return;
                }
            }
        }
        mLoadingRequestMap.put(callBack.getKey(), requestBean);
        LogUtils.d(TAG, "Request in string queue - " + moduleTag +
                "(requestCode:" + what + ")" + " \r\n- url : " + url + " \r\n - params: " + params);
        mRequestManager.setJsonRequest(requestBean, getResponseListener(callBack));
    }

    // 下载文件
    public static void setDownloadRequest(String url, String fileFolder, String fileName, String moduleTag,
                                          int what, HttpDownloadCallback callBack) {
        setDownloadRequest(url, fileFolder, fileName, HttpRequestMethod.GET, new HashMap<String, String>(),
                moduleTag, false, true, null, what, false, callBack);
    }

    // 下载文件
    public static void setDownloadRequest(String url, String fileFolder, String fileName, String moduleTag,
                                          int what, boolean needLogin, HttpDownloadCallback callBack) {
        setDownloadRequest(url, fileFolder, fileName, HttpRequestMethod.GET, new HashMap<String, String>(),
                moduleTag, false, true, null, what, needLogin, callBack);
    }

    // 下载文件
    public static void setDownloadRequest(String url, String fileFolder, String fileName,
                                          String moduleTag, boolean isContinue, boolean isDeleteOld,
                                          int what, boolean needLogin, HttpDownloadCallback callBack) {
        setDownloadRequest(url, fileFolder, fileName, HttpRequestMethod.GET, new HashMap<String, String>(),
                moduleTag, isContinue, isDeleteOld, null, what, needLogin, callBack);
    }

    // 下载文件
    public static void setDownloadRequest(String url, String fileFolder, String fileName, HttpRequestMethod method,
                                          HashMap<String, String> params, String moduleTag, boolean isContinue, boolean isDeleteOld,
                                          int what, boolean needLogin, HttpDownloadCallback callBack) {
        setDownloadRequest(url, fileFolder, fileName, method, params, moduleTag, isContinue,
                isDeleteOld, null, what, needLogin, callBack);
    }

    // 下载文件
    public static void setDownloadRequest(String url, String fileFolder, String fileName, HttpRequestMethod method,
                                          HashMap<String, String> params, String moduleTag, boolean isContinue, boolean isDeleteOld,
                                          Object sign, int what, boolean needLogin, HttpDownloadCallback callBack) {
        //设置模块名
        callBack.setModuleTag(moduleTag);
        callBack.setUrl(url);
        callBack.setWhat(what);

        HttpRequestBean requestBean = new HttpRequestBean(what, callBack);
        requestBean.setUrl(url);
        requestBean.setFileFolder(fileFolder);
        requestBean.setFileName(fileName);
        requestBean.setRequestMethod(method);
        requestBean.setParams(params);
        requestBean.setModuleTag(moduleTag);
        requestBean.setContinue(isContinue);
        requestBean.setDeleteOld(isDeleteOld);
        requestBean.setWhat(what);
        requestBean.setSign(sign);
        requestBean.setRequestType(RequestType.DOWNLOAD);
        requestBean.setNeedLogin(needLogin);
        requestBean.setCallBack(callBack);

        if (mRequestInterceptorList != null) {
            for (int i = 0; i < mRequestInterceptorList.size(); i++) {
                if (mRequestInterceptorList.get(i).onIntercept(what, requestBean)) {
                    return;
                }
            }
        }
        mLoadingRequestMap.put(callBack.getKey(), requestBean);
        LogUtils.d(TAG, "Request in download queue - " + moduleTag +
                "(requestCode:" + what + ")" + " \r\n- url : " + url + " \r\n - params: " + params);
        mRequestManager.setDownloadRequest(requestBean, getDownloadListener(callBack));
    }

    public static void setPostFileRequest(String url, int what, Map<String, String> params,
                                          List<File> fileList, String fileKey, String moduleTag,
                                          HttpUploadCallback processCallback, HttpJsonCallback requestCallback) {
        setPostFileRequest(url, what, params, fileList, fileKey, false, moduleTag, processCallback, requestCallback);
    }

    /**
     * 上传多文件
     *
     * @param url
     * @param what
     * @param params          普通参数
     * @param fileList        文件集合
     * @param fileKey         文件的key
     * @param needLogin       是否需要登陆
     * @param moduleTag       模块标识
     * @param processCallback
     * @param requestCallback
     */
    public static void setPostFileRequest(String url, int what, Map<String, String> params, List<File> fileList,
                                          String fileKey, boolean needLogin, String moduleTag,
                                          HttpUploadCallback processCallback, HttpJsonCallback requestCallback) {
        requestCallback.setModuleTag(moduleTag);
        requestCallback.setUrl(url);
        requestCallback.setWhat(what);

        HttpRequestBean requestBean = new HttpRequestBean(what, requestCallback);
        requestBean.setUrl(url);
        requestBean.setFileList(fileList);
        requestBean.setFileKey(fileKey);
        requestBean.setRequestMethod(HttpRequestMethod.POST);
        requestBean.setParams(params);
        requestBean.setWhat(what);
        requestBean.setRequestType(RequestType.UPLOAD);
        requestBean.setNeedLogin(needLogin);
        requestBean.setCallBack(requestCallback);
        if (mRequestInterceptorList != null) {
            for (int i = 0; i < mRequestInterceptorList.size(); i++) {
                if (mRequestInterceptorList.get(i).onIntercept(what, requestBean)) {
                    return;
                }
            }
        }
        mLoadingRequestMap.put(requestCallback.getKey(), requestBean);

        LogUtils.d(TAG, "Request in upload queue - " + moduleTag +
                "(requestCode:" + what + ")" + " \r\n- url : " + url + " \r\n - params: " + params);
        mRequestManager.setUploadRequest(requestBean, getUploadListener(processCallback), getResponseListener(requestCallback));

    }

    // 添加SessionCookie
    public static void addSessionCookie(Map<String, String> headers) {
        mRequestManager.addSessionCookie(headers);
    }

    /**
     * 标准回调请求
     *
     * @param callBack
     * @return
     */
    public static IHttpResponseListener.OnResponseListener getResponseListener(final HttpJsonCallback callBack) {
        return new IHttpResponseListener.OnResponseListener() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, HttpResponse response) {
                LogUtils.d(TAG, "Response onSucceed in string queue - " + callBack.getModuleTag() +
                        "(requestCode:" + what + ")" + " \r\n- url : " + callBack.getUrl());
                HttpRequestBean httpRequestBean = null;
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(callBack.getKey())) {
                    httpRequestBean = mLoadingRequestMap.remove(callBack.getKey());
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
                LogUtils.d(TAG, "Response onFailed in string queue - " + callBack.getModuleTag() +
                        "(requestCode:" + what + ")" + " \r\n- url : " + callBack.getUrl());
                HttpRequestBean httpRequestBean = null;
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(callBack.getKey())) {
                    if (mErrorRequestMap == null) {
                        mErrorRequestMap = new HashMap<>();
                    }
                    if (mErrorRequestMap.containsKey(callBack.getKey())) {
                        mErrorRequestMap.remove(callBack.getKey());
                    }
                    mErrorRequestMap.put(callBack.getKey(), mLoadingRequestMap.get(callBack.getKey()));
                    httpRequestBean = mLoadingRequestMap.remove(callBack.getKey());
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
                if (!callBack.onError(what, exception)) {
                    defaultDeduceErrorResponse(exception);
                }
            }

            @Override
            public void onFinish(int what) {
                LogUtils.d(TAG, "Response onFinish in string queue - " + callBack.getModuleTag() +
                        "(requestCode:" + what + ")" + " \r\n- url : " + callBack.getUrl());
                // 用于清除被cancel的网络请求
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(callBack.getKey())) {
                    mLoadingRequestMap.remove(callBack.getKey());
                }
            }
        };
    }

    // 下载callback
    public static IHttpResponseListener.OnDownloadListener getDownloadListener(final HttpDownloadCallback callBack) {
        return new IHttpResponseListener.OnDownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                LogUtils.d(TAG, "Response onDownloadError in download queue - " + callBack.getModuleTag() +
                        "(requestCode:" + what + ")" + " \r\n- url : " + callBack.getUrl());
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(callBack.getKey())) {
                    if (mErrorRequestMap == null) {
                        mErrorRequestMap = new HashMap<>();
                    }
                    if (mErrorRequestMap.containsKey(callBack.getKey())) {
                        mErrorRequestMap.remove(callBack.getKey());
                    }
                    mErrorRequestMap.put(callBack.getKey(), mLoadingRequestMap.get(callBack.getKey()));
                    mLoadingRequestMap.remove(callBack.getKey());
                }
                if (!callBack.onError(what, exception)) {
                    defaultDeduceErrorResponse(exception);
                }
            }

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, long allCount) {
                LogUtils.d(TAG, "Response onStart in download queue - " + callBack.getModuleTag() +
                        "(requestCode:" + what + ")" + " \r\n- url : " + callBack.getUrl());
                callBack.onStart(what, isResume, rangeSize, allCount);
            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                callBack.onProgress(what, progress, fileCount, speed);
            }

            @Override
            public void onFinish(int what, String filePath) {
                LogUtils.d(TAG, "Response onFinish in download queue - " + callBack.getModuleTag() +
                        "(requestCode:" + what + ")" + " \r\n- url : " + callBack.getUrl());
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(callBack.getKey())) {
                    mLoadingRequestMap.remove(callBack.getKey());
                }
                callBack.onFinish(what, filePath);
            }

            @Override
            public void onCancel(int what) {
                LogUtils.d(TAG, "Response onCancel in download queue - " + callBack.getModuleTag() +
                        "(requestCode:" + what + ")" + " \r\n- url : " + callBack.getUrl());
                if (mLoadingRequestMap != null && mLoadingRequestMap.containsKey(callBack.getKey())) {
                    mLoadingRequestMap.remove(callBack.getKey());
                }
                callBack.onCancel(what);
            }
        };
    }

    public static IHttpResponseListener.OnUploadListener getUploadListener(final HttpUploadCallback callBack) {
        return new IHttpResponseListener.OnUploadListener() {
            @Override
            public void onStart(int what) {
                callBack.onStart(what);
            }

            @Override
            public void onCancel(int what) {
                callBack.onCancel(what);
            }

            @Override
            public void onProgress(int what, int progress) {
                callBack.onProgress(what, progress);
            }

            @Override
            public void onFinish(int what) {
                LogUtils.d(TAG, "Response onFinish in upload queue - " + callBack.getModuleTag() +
                        "(requestCode:" + what + ")" + " \r\n- url : " + callBack.getUrl());
                callBack.onFinish(what);
            }

            @Override
            public void onError(int what, Exception exception) {
                LogUtils.d(TAG, "Response onError in upload queue - " + callBack.getModuleTag() +
                        "(requestCode:" + what + ")" + " \r\n- url : " + callBack.getUrl());
                if (!callBack.onError(what, exception)) {
                    defaultDeduceErrorResponse(exception);
                }
            }
        };
    }

    private static void defaultDeduceErrorResponse(Exception exception) {
        if (exception instanceof NetworkError) {
            NetworkError networkError = (NetworkError) exception;
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
                    getResponseListener((HttpJsonCallback) bean.getCallBack()));
        } else if (bean.getRequestType() == RequestType.DOWNLOAD) {
            mRequestManager.setDownloadRequest(bean, getDownloadListener((HttpDownloadCallback) bean.getCallBack()));
        } else {
            mRequestManager.setJsonRequest(bean, getResponseListener((HttpJsonCallback) bean.getCallBack()));
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

    public static void clearCookie() {
        mRequestManager.setSessionId(null);
    }

    public static void resetSession() {
        mRequestManager.setSessionId(null);
    }
}
