package com.pine.router.impl.atlas;

import android.app.Activity;
import android.os.Bundle;
import android.taobao.atlas.remote.IRemoteTransactor;

import com.pine.router.IServiceCallback;
import com.pine.router.RouterConstants;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.tool.util.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tanghongfeng on 2019/2/21
 */

public abstract class AtlasRouterBundleRemote<T> {
    protected final String TAG = LogUtils.makeLogTag(this.getClass());

    protected Bundle call(T t, Method[] methods, Activity activity, String commandName,
                          Bundle args, final IRemoteTransactor.IResponse callback) {
        for (int i = 0; i < methods.length; i++) {
            RouterAnnotation annotation = methods[i].getAnnotation(RouterAnnotation.class);
            if (annotation != null && annotation.CommandName().equals(commandName)) {
                methods[i].setAccessible(true);
                final Bundle responseBundle = new Bundle();
                try {
                    responseBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_SUCCEED);
                    methods[i].invoke(t, activity, args, new IServiceCallback() {
                        @Override
                        public void onResponse(Bundle bundle) {
                            if (bundle == null) {
                                bundle = new Bundle();
                            }
                            responseBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, bundle);
                            if (callback != null) {
                                callback.OnResponse(responseBundle);
                            }
                        }
                    });
                    return responseBundle;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    responseBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString(RouterConstants.REMOTE_CALL_FAIL_MESSAGE_KEY, e.toString());
                    responseBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
                    if (callback != null) {
                        callback.OnResponse(responseBundle);
                    }
                    return responseBundle;
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    responseBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString(RouterConstants.REMOTE_CALL_FAIL_MESSAGE_KEY, e.toString());
                    responseBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
                    if (callback != null) {
                        callback.OnResponse(responseBundle);
                    }
                    return responseBundle;
                }
            }
        }
        Bundle responseBundle = new Bundle();
        responseBundle.putString(RouterConstants.REMOTE_CALL_STATE_KEY, RouterConstants.ON_EXCEPTION);
        Bundle resultBundle = new Bundle();
        resultBundle.putString(RouterConstants.REMOTE_CALL_FAIL_MESSAGE_KEY, "");
        responseBundle.putBundle(RouterConstants.REMOTE_CALL_RESULT_KEY, resultBundle);
        if (callback != null) {
            callback.OnResponse(responseBundle);
        }
        return responseBundle;
    }
}