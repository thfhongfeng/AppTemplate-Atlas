package com.pine.mvvm.remote.atlas;

import android.os.Bundle;
import android.taobao.atlas.remote.IRemote;

import com.pine.mvvm.remote.MvvmUiRemoteService;
import com.pine.router.impl.atlas.AtlasRouterBundleRemote;

import java.lang.reflect.Method;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class MvvmAtlasUiRemote extends AtlasRouterBundleRemote<MvvmUiRemoteService> implements IRemote {

    private static MvvmUiRemoteService mRemoteService;
    private static Method[] mMethods;

    static {
        mRemoteService = new MvvmUiRemoteService();
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
