package com.pine.mvvm.remote.atlas;

import android.os.Bundle;
import android.taobao.atlas.remote.IRemote;

import com.pine.router.RouterConstants;
import com.pine.router.annotation.RouterAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class MvvmBundleRemote implements IRemote {

    private static MvvmBundleRemoteService mRemoteService;
    private static Method[] mMethods;

    static {
        mRemoteService = new MvvmBundleRemoteService();
        Class clazz = mRemoteService.getClass();
        mMethods = clazz.getMethods();
    }

    @Override
    public Bundle call(String commandName, Bundle args, IResponse callback) {
        Bundle responseBundle = null;
        for (int i = 0; i < mMethods.length; i++) {
            RouterAnnotation annotation = mMethods[i].getAnnotation(RouterAnnotation.class);
            if (annotation != null && annotation.CommandName().equals(commandName)) {
                mMethods[i].setAccessible(true);
                responseBundle = new Bundle();
                try {
                    responseBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_SUCCEED);
                    Bundle resultBundle = (Bundle) mMethods[i].invoke(mRemoteService, args);
                    if (resultBundle == null) {
                        resultBundle = new Bundle();
                    }
                    responseBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    responseBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString(RouterConstants.REMOTE_CALL_FAIL_MESSAGE_KEY, e.toString());
                    responseBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    responseBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString(RouterConstants.REMOTE_CALL_FAIL_MESSAGE_KEY, e.toString());
                    responseBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
                }
                break;
            }
        }
        if (responseBundle == null) {
            responseBundle = new Bundle();
            responseBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
            Bundle resultBundle = new Bundle();
            resultBundle.putString(RouterConstants.REMOTE_CALL_FAIL_MESSAGE_KEY, "");
            responseBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
        }
        if (callback != null) {
            callback.OnResponse(responseBundle);
        }
        return responseBundle;
    }

    @Override
    public <T> T getRemoteInterface(Class<T> interfaceClass, Bundle args) {
        return null;
    }
}
