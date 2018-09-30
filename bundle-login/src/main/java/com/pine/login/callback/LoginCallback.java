package com.pine.login.callback;

import android.content.Intent;

import com.pine.base.BaseApplication;
import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.base.http.HttpResponse;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.login.LoginConstants;
import com.pine.login.manager.LoginManager;
import com.pine.login.ui.activity.LoginActivity;
import com.pine.tool.util.AppUtils;
import com.pine.tool.util.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;

import static com.pine.base.http.IHttpRequestManager.SESSION_ID;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public class LoginCallback extends HttpJsonCallback {
    public static final int LOGIN_CODE = 1;
    public static final int AUTO_LOGIN_CODE = 2;
    public static final int RE_LOGIN_CODE = 3;
    private String mMobile;
    private String mPassword;
    private LoginManager.Callback mCallback;

    public LoginCallback(String mobile, String password, LoginManager.Callback callback) {
        mMobile = mobile;
        mPassword = password;
        mCallback = callback;
    }

    @Override
    public void onResponse(int what, HttpResponse response) {
        List<HttpCookie> list = response.getCookies();
        for (int i = 0; i < list.size(); i++) {
            if (SESSION_ID.equals(list.get(i).getName())) {
                HttpRequestManagerProxy.setSessionId(list.get(i).getValue());
                break;
            }
        }
        super.onResponse(what, response);
    }

    @Override
    public void onResponse(int what, JSONObject jsonObject) {
        // Test code begin
        String res = "{success:true,code:200,message:'',data:{account:'" + mMobile + "', username:'pine', age:35}}";
        try {
            jsonObject = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Test code end
        if (jsonObject == null || !jsonObject.optBoolean(LoginConstants.SUCCESS, false)) {
            BaseApplication.setLogin(false);
            if (mCallback != null) {
                mCallback.onLoginResponse(false, "");
            }
            if (AUTO_LOGIN_CODE != what) {
                goLoginActivity();
            }
            return;
        }
        saveLoginInfo(jsonObject);
        BaseApplication.setLogin(true);
        if (RE_LOGIN_CODE == what) {
            LoginManager.reloadAllNoAuthRequest();
        }
        if (mCallback != null) {
            mCallback.onLoginResponse(true, "");
        }
    }

    @Override
    public boolean onError(int what, Exception e) {
        BaseApplication.setLogin(false);
        if (mCallback != null) {
            mCallback.onLoginResponse(false, e.toString());
        }
        if (AUTO_LOGIN_CODE != what) {
            goLoginActivity();
        }
        return false;
    }

    private void saveLoginInfo(JSONObject jsonObject) {
        SharePreferenceUtils.cleanCache();
        SharePreferenceUtils.saveStringToCache(LoginConstants.LOGIN_MOBILE, mMobile);
        SharePreferenceUtils.saveStringToCache(LoginConstants.LOGIN_PASSWORD, mPassword);
    }

    private void goLoginActivity() {
        Intent intent = new Intent(AppUtils.getApplicationByReflect(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getApplicationByReflect().startActivity(intent);
    }
}
