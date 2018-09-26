package com.pine.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.pine.base.access.UiAccessInterceptorType;
import com.pine.base.access.UiAccessManager;
import com.pine.base.access.login.UiAccessLoginInterceptor;
import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.tool.util.LogUtils;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class BaseApplication {
    private final static String TAG = LogUtils.makeLogTag(BaseApplication.class);
    public static volatile Activity mLastCreatedActivity, mLastStartedActivity, mCurResumedActivity;
    public static volatile Activity mLastPausedActivity, mLastStoppedActivity, mLastDestroyedActivity;
    private static volatile boolean mIsLogin;
    private static Application mApplication;

    public static boolean isLogin() {
        return mIsLogin;
    }

    public static void setLogin(boolean isLogin) {
        mIsLogin = isLogin;
    }

    public static void init(Application application) {
        mApplication = application;

        registerActivity();

        HttpRequestManagerProxy.init(application);

        UiAccessManager.getInstance().addAccessInterceptor(UiAccessInterceptorType.LOGIN, new UiAccessLoginInterceptor());
    }

    private static void registerActivity() {
        mApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtils.d(TAG, activity + " on created");
                mLastCreatedActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtils.d(TAG, activity + " on started");
                mLastStartedActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtils.d(TAG, activity + " on resumed");
                mCurResumedActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtils.d(TAG, activity + " on paused");
                mLastPausedActivity = activity;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtils.d(TAG, activity + " on stopped");
                mLastStoppedActivity = activity;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtils.d(TAG, activity + " on destroyed");
                mLastDestroyedActivity = activity;
            }
        });
    }

}
