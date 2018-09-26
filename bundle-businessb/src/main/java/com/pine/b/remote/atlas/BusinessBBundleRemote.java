package com.pine.b.remote.atlas;

import android.os.Bundle;
import android.taobao.atlas.remote.IRemote;

import com.pine.router.RouterConstants;
import com.pine.router.annotation.RouterAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class BusinessBBundleRemote implements IRemote {

    private static BusinessBBundleRemoteService mRemoteService;
    private static Method[] mMethods;

    static {
        mRemoteService = new BusinessBBundleRemoteService();
        Class clazz = mRemoteService.getClass();
        mMethods = clazz.getMethods();
    }

    @Override
    public Bundle call(String commandName, Bundle args, IResponse callback) {
        Bundle returnBundle = null;
        for (int i = 0; i < mMethods.length; i++) {
            RouterAnnotation annotation = mMethods[i].getAnnotation(RouterAnnotation.class);
            if (annotation != null && annotation.CommandName().equals(commandName)) {
                mMethods[i].setAccessible(true);
                try {
                    returnBundle = (Bundle) mMethods[i].invoke(mRemoteService, args);
                    if (returnBundle == null) {
                        returnBundle = new Bundle();
                    }
                    returnBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_SUCCEED);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    returnBundle = new Bundle();
                    returnBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    returnBundle = new Bundle();
                    returnBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
                }
                break;
            }
        }
        if (returnBundle == null) {
            returnBundle = new Bundle();
            returnBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
        }
        if (callback != null) {
            callback.OnResponse(returnBundle);
        }
        return returnBundle;
    }

    @Override
    public <T> T getRemoteInterface(Class<T> interfaceClass, Bundle args) {
        return null;
    }
}
