package com.pine.login.manager;

import com.pine.base.BaseApplication;
import com.pine.base.http.HttpRequestBean;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.http.IHttpRequestManager;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.login.LoginConstants;
import com.pine.login.LoginUrlConstants;
import com.pine.login.callback.LoginCallback;
import com.pine.tool.util.LogUtils;
import com.pine.tool.util.SharePreferenceUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public class LoginManager {
    private final static String TAG = LogUtils.makeLogTag(LoginManager.class);
    public static int mReLoginCount = 0;
    public static volatile boolean mIsReLoginProcessing = false;
    private static String mLoginUrl = LoginUrlConstants.Query_Login_Info;
    private static Map<String, HttpRequestBean> mNoAuthRequestMap = new HashMap<String, HttpRequestBean>();

    // 登录
    public static void login(String mobile, String password, Callback callback) {
        if (BaseApplication.isLogin()) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(LoginConstants.LOGIN_MOBILE, mobile);
        params.put(LoginConstants.LOGIN_PASSWORD, password);

        HttpRequestManager.clearCookie();
        HttpRequestManager.setJsonRequest(mLoginUrl, params, TAG,
                LoginCallback.LOGIN_CODE, new LoginCallback(mobile, password, callback));
    }

    // 自动登录
    public static void autoLogin(Callback callback) {
        if (BaseApplication.isLogin()) {
            return;
        }
        String mobile = SharePreferenceUtils.readStringFromCache(LoginConstants.LOGIN_MOBILE, "");
        String password = SharePreferenceUtils.readStringFromCache(LoginConstants.LOGIN_PASSWORD, "");
        if (mobile.length() == 0 || password.length() == 0) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(LoginConstants.LOGIN_MOBILE, mobile);
        params.put(LoginConstants.LOGIN_PASSWORD, password);
        HttpRequestManager.clearCookie();
        HttpRequestManager.setJsonRequest(mLoginUrl, params, TAG,
                LoginCallback.AUTO_LOGIN_CODE, new LoginCallback(mobile, password, callback));
    }

    // 重新登录
    public static void reLogin() {
        if (BaseApplication.isLogin()) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        String mobile = SharePreferenceUtils.readStringFromCache(LoginConstants.LOGIN_MOBILE, "");
        String password = SharePreferenceUtils.readStringFromCache(LoginConstants.LOGIN_PASSWORD, "");
        if (mobile.length() == 0 || password.length() == 0) {
            return;
        }
        params.put(LoginConstants.LOGIN_MOBILE, mobile);
        params.put(LoginConstants.LOGIN_PASSWORD, password);
        HttpRequestManager.clearCookie();
        mReLoginCount++;
        mIsReLoginProcessing = true;
        HttpRequestManager.setJsonRequest(mLoginUrl, params, TAG,
                LoginCallback.RE_LOGIN_CODE, new LoginCallback(mobile, password, null));
    }

    public static Map<String, HttpRequestBean> getNoAuthRequestMap() {
        return mNoAuthRequestMap;
    }

    // 重新发起之前因401终止的指定的key的请求
    public static void reloadNoAuthRequest(String key) {
        if (mNoAuthRequestMap == null) {
            return;
        }
        HttpRequestBean bean = mNoAuthRequestMap.get(key);
        if (bean == null) {
            return;
        }
        HttpRequestManager.setJsonRequest(bean, IHttpRequestManager.ActionType.RETRY_AFTER_RE_LOGIN);
    }

    // 重新发起之前所有因401终止的请求
    public static void reloadAllNoAuthRequest() {
        if (mNoAuthRequestMap == null) {
            return;
        }
        Iterator<String> iterator = mNoAuthRequestMap.keySet().iterator();
        while (iterator.hasNext()) {
            reloadNoAuthRequest(iterator.next());
        }
    }

    public static void flushNoAuthRequest(String key) {
        if (mNoAuthRequestMap == null) {
            return;
        }
        HttpRequestBean bean = mNoAuthRequestMap.get(key);
        if (bean == null) {
            return;
        }
        if (bean.getResponse().isSucceed()) {
            ((HttpJsonCallback) bean.getCallBack()).onResponse(bean.getWhat(), bean.getResponse());
        } else {
            ((HttpJsonCallback) bean.getCallBack()).onError(bean.getWhat(), bean.getResponse().getException());
        }
    }

    public static void flushAllNoAuthRequest() {
        if (mNoAuthRequestMap == null) {
            return;
        }
        Iterator<String> iterator = mNoAuthRequestMap.keySet().iterator();
        while (iterator.hasNext()) {
            flushNoAuthRequest(iterator.next());
        }
    }

    public interface Callback {
        void onLoginResponse(boolean isSuccess, String msg);
    }
}
