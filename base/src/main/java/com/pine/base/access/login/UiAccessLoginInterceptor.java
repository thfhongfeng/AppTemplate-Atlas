package com.pine.base.access.login;

import android.content.Context;

import com.pine.base.BaseApplication;
import com.pine.base.access.IUiAccessInterceptor;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class UiAccessLoginInterceptor implements IUiAccessInterceptor {
    @Override
    public boolean onInterceptor(Context context, int level) {
        return !BaseApplication.isLogin();
    }
}
