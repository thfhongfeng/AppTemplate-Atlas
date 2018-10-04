package com.pine.login.interceptor;

import com.pine.base.BaseApplication;
import com.pine.base.http.HttpRequestBean;
import com.pine.base.http.HttpResponse;
import com.pine.base.http.IHttpRequestManager;
import com.pine.base.http.Interceptor.IHttpResponseInterceptor;
import com.pine.login.ResponseCode;
import com.pine.login.callback.LoginCallback;
import com.pine.login.manager.LoginManager;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public class LoginResponseInterceptor implements IHttpResponseInterceptor {

    @Override
    public boolean onIntercept(int what, HttpRequestBean requestBean, HttpResponse response) {
        if (response.getResponseCode() == ResponseCode.NOT_LOGIN) { // 拦截401错误
            if (LoginManager.getNoAuthRequestMap() != null &&
                    !LoginManager.getNoAuthRequestMap().containsKey(requestBean.getCallBack().getKey()) &&
                    !(requestBean.getCallBack() instanceof LoginCallback)) {
                LoginManager.getNoAuthRequestMap().put(requestBean.getCallBack().getKey(), requestBean);
            }
            if (!LoginManager.mIsReLoginProcessing) {
                BaseApplication.setLogin(false);
                if (LoginManager.mReLoginCount > 2) { // 自动登录失败
                    LoginManager.flushAllNoAuthRequest();
                    LoginManager.getNoAuthRequestMap().clear();
                    return false;
                }
                LoginManager.reLogin();
            }
            return true;
        }
        if (requestBean.getCallBack() instanceof LoginCallback) {
            LoginManager.mIsReLoginProcessing = false;
            if (!response.isSucceed() && what == LoginCallback.RE_LOGIN_CODE) {
                BaseApplication.setLogin(false);
                if (LoginManager.mReLoginCount > 2) { // 自动登录失败
                    LoginManager.flushAllNoAuthRequest();
                    LoginManager.getNoAuthRequestMap().clear();
                    return false;
                }
                LoginManager.reLogin();
                return true;
            }
        }
        if (IHttpRequestManager.ActionType.RETRY_AFTER_RE_LOGIN == requestBean.getActionType()) {
            if (LoginManager.getNoAuthRequestMap() != null && LoginManager.getNoAuthRequestMap().containsKey(requestBean.getCallBack().getKey())) {
                LoginManager.getNoAuthRequestMap().remove(requestBean);
            }
        }
        return false;
    }
}
