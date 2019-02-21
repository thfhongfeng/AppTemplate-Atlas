package com.pine.router;

import android.app.Application;

import com.pine.tool.util.LogUtils;

/**
 * Created by tanghongfeng on 2019/2/21
 */

public class RouterApplication {
    private final static String TAG = LogUtils.makeLogTag(RouterApplication.class);
    public static Application mApplication;

    public static void init(Application application) {
        mApplication = application;
    }
}
