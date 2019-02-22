package com.pine.login.remote.atlas;

import android.os.Bundle;
import android.taobao.atlas.remote.IRemote;

import com.pine.login.remote.LoginOpRemoteService;
import com.pine.router.impl.atlas.AtlasRouterBundleRemote;

import java.lang.reflect.Method;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class LoginAtlasOpRemote extends AtlasRouterBundleRemote<LoginOpRemoteService> implements IRemote {

    private static LoginOpRemoteService mRemoteService;
    private static Method[] mMethods;

    static {
        mRemoteService = new LoginOpRemoteService();
        Class clazz = mRemoteService.getClass();
        mMethods = clazz.getMethods();
    }

    @Override
    public Bundle call(String commandName, Bundle args, final IResponse callback) {
        return call(mRemoteService, mMethods, commandName, args, callback);
    }

    @Override
    public <T> T getRemoteInterface(Class<T> interfaceClass, Bundle args) {
        return null;
    }
}
