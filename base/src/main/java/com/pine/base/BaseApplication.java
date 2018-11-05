package com.pine.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.pine.base.access.UiAccessManager;
import com.pine.base.access.UiAccessType;
import com.pine.base.access.executor.UiAccessLoginExecutor;
import com.pine.base.access.executor.UiAccessVipLevelExecutor;
import com.pine.base.component.share.manager.ShareManager;
import com.pine.base.http.HttpRequestManager;
import com.pine.tool.util.LogUtils;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class BaseApplication {
    private final static String TAG = LogUtils.makeLogTag(BaseApplication.class);
    public static volatile Activity mCurResumedActivity;
    public static Application mApplication;
    private static volatile boolean mIsLogin;

    public static boolean isLogin() {
        return mIsLogin;
    }

    public static void setLogin(boolean isLogin) {
        mIsLogin = isLogin;
    }

    public static void init(Application application) {
        mApplication = application;

        registerActivity();

        initManager();
    }

    private static void registerActivity() {
        mApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtils.d(TAG, activity + " on created");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtils.d(TAG, activity + " on started");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtils.d(TAG, activity + " on resumed");
                mCurResumedActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtils.d(TAG, activity + " on paused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtils.d(TAG, activity + " on stopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtils.d(TAG, activity + " on destroyed");
            }
        });
    }

    private static void initManager() {

        ShareManager.getInstance().init(mApplication);

        HttpRequestManager.init(mApplication);

        UiAccessManager.getInstance().addAccessExecutor(UiAccessType.LOGIN,
                new UiAccessLoginExecutor(R.string.base_ui_access_login_forbidden));
        UiAccessManager.getInstance().addAccessExecutor(UiAccessType.VIP_LEVEL,
                new UiAccessVipLevelExecutor(R.string.base_ui_access_vip_level_forbidden));

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(mApplication);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
