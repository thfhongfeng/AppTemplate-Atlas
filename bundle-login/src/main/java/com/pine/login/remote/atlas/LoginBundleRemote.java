package com.pine.login.remote.atlas;

import android.os.Bundle;
import android.taobao.atlas.remote.IRemote;

import com.pine.router.RouterConstants;
import com.pine.router.annotation.RouterAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class LoginBundleRemote implements IRemote {

    private static LoginBundleRemoteService mRemoteService;
    private static Method[] mMethods;

    static {
        mRemoteService = new LoginBundleRemoteService();
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
                returnBundle = new Bundle();
                try {
                    returnBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_SUCCEED);
                    Bundle resultBundle = (Bundle) mMethods[i].invoke(mRemoteService, args);
                    if (resultBundle == null) {
                        resultBundle = new Bundle();
                    }
                    returnBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    returnBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString(RouterConstants.REMOTE_CALL_FAIL_MESSAGE_KEY, e.toString());
                    returnBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    returnBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString(RouterConstants.REMOTE_CALL_FAIL_MESSAGE_KEY, e.toString());
                    returnBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
                }
                break;
            }
        }
        if (returnBundle == null) {
            returnBundle = new Bundle();
            returnBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
            Bundle resultBundle = new Bundle();
            resultBundle.putString(RouterConstants.REMOTE_CALL_FAIL_MESSAGE_KEY, "");
            returnBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
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
