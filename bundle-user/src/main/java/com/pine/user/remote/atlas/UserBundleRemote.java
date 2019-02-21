package com.pine.user.remote.atlas;

import android.os.Bundle;
import android.taobao.atlas.remote.IRemote;

import com.pine.router.impl.atlas.AtlasRouterBundleRemote;
import com.pine.user.remote.UserRemoteService;

import java.lang.reflect.Method;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class UserBundleRemote extends AtlasRouterBundleRemote<UserRemoteService> implements IRemote {

    private static UserRemoteService mRemoteService;
    private static Method[] mMethods;

    static {
        mRemoteService = new UserRemoteService();
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
