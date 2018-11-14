package com.pine.login.callback;

import android.content.Intent;

import com.pine.base.BaseApplication;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.http.HttpResponse;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.login.LoginConstants;
import com.pine.login.manager.LoginManager;
import com.pine.login.ui.activity.LoginActivity;
import com.pine.tool.util.AppUtils;

import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;

import static com.pine.base.http.IHttpRequestManager.SESSION_ID;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public class LoginCallback extends HttpJsonCallback {
    public static final int LOGIN_CODE = 1;
    public static final int LOGOUT_CODE = 2;
    public static final int AUTO_LOGIN_CODE = 3;
    public static final int RE_LOGIN_CODE = 4;
    private LoginManager.Callback mCallback;

    public LoginCallback() {

    }

    public LoginCallback(LoginManager.Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(int what, HttpResponse response) {
        List<HttpCookie> list = response.getCookies();
        for (int i = 0; i < list.size(); i++) {
            if (SESSION_ID.equals(list.get(i).getName().toUpperCase())) {
                HttpRequestManager.setSessionId(list.get(i).getValue());
                break;
            }
        }
        super.onResponse(what, response);
    }

    @Override
    public void onResponse(int what, JSONObject jsonObject) {
        if (LOGOUT_CODE == what) {
            LoginManager.clearLoginInfo();
            BaseApplication.setLogin(false);
            return;
        } else {
            if (jsonObject == null || !jsonObject.optBoolean(LoginConstants.SUCCESS, false)) {
                BaseApplication.setLogin(false);
                if (mCallback != null) {
                    mCallback.onLoginResponse(false, jsonObject == null ?
                            "" : jsonObject.optString(LoginConstants.MESSAGE));
                }
                if (AUTO_LOGIN_CODE != what) {
                    goLoginActivity();
                }
                return;
            }
            LoginManager.saveLoginInfo(jsonObject);
            BaseApplication.setLogin(true);
            if (RE_LOGIN_CODE == what) {
                LoginManager.reloadAllNoAuthRequest();
            }
            if (mCallback != null) {
                mCallback.onLoginResponse(true, "");
            }
        }
    }

    @Override
    public boolean onFail(int what, Exception e) {
        BaseApplication.setLogin(false);
        boolean consumed = false;
        if (mCallback != null) {
            consumed = mCallback.onLoginResponse(false, "");
        }
        if (AUTO_LOGIN_CODE != what) {
            goLoginActivity();
        }
        return consumed;
    }

    private void goLoginActivity() {
        Intent intent = new Intent(AppUtils.getApplicationByReflect(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getApplicationByReflect().startActivity(intent);
    }
}
