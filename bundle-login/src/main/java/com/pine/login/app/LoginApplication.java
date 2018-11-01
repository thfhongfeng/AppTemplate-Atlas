package com.pine.login.app;

import android.app.Application;
import android.content.Context;

import com.pine.base.http.HttpRequestManager;
import com.pine.login.interceptor.LoginResponseInterceptor;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public class LoginApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HttpRequestManager.setGlobalResponseInterceptor(new LoginResponseInterceptor());
    }

    @Override
    public void attachBaseContext(Context baseContext) {
        super.attachBaseContext(baseContext);
    }
}
