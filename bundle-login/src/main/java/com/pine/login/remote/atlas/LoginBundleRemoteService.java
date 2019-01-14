package com.pine.login.remote.atlas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.pine.login.manager.LoginManager;
import com.pine.login.ui.activity.LoginActivity;
import com.pine.router.RouterCommand;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.tool.util.AppUtils;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class LoginBundleRemoteService {

    @RouterAnnotation(CommandName = RouterCommand.LOGIN_goLoginActivity)
    public Bundle goLoginActivity(Bundle args) {
        Bundle responseBundle = new Bundle();
        Intent intent = new Intent(AppUtils.getApplicationByReflect(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getApplicationByReflect().startActivity(intent);
        return responseBundle;
    }

    @RouterAnnotation(CommandName = RouterCommand.LOGIN_autoLogin)
    public Bundle autoLogin(Bundle args) {
        final Bundle responseBundle = new Bundle();
        LoginManager.autoLogin(new LoginManager.Callback() {
            @Override
            public boolean onLoginResponse(boolean isSuccess, String msg) {
                responseBundle.putBoolean("success", isSuccess);
                responseBundle.putString("msg", msg);
                return true;
            }
        });
        return responseBundle;
    }

    @RouterAnnotation(CommandName = RouterCommand.LOGIN_logout)
    public Bundle logout(Bundle args) {
        final Bundle responseBundle = new Bundle();
        LoginManager.logout();
        return responseBundle;
    }
}
