package com.pine.router.manager;

import com.pine.router.BuildConfig;
import com.pine.router.RouterBundleKey;
import com.pine.router.manager.atlas.AtlasRouterManagerFactory;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class RouterManager {
    public static IRouterManager getInstance(String bundleKey) {
        switch (BuildConfig.APP_THIRD_ROUTER_PROVIDER) {
            case "atlas":
                return AtlasRouterManagerFactory.getManager(bundleKey);
            default:
                return AtlasRouterManagerFactory.getManager(bundleKey);
        }
    }

    public static IRouterManager getLoginManager() {
        return getInstance(RouterBundleKey.LOGIN_BUNDLE_KEY);
    }

    public static IRouterManager getMainManager() {
        return getInstance(RouterBundleKey.MAIN_BUNDLE_KEY);
    }

    public static IRouterManager getUserManager() {
        return getInstance(RouterBundleKey.USER_BUNDLE_KEY);
    }

    public static IRouterManager getBusinessMvcManager() {
        return getInstance(RouterBundleKey.BUSINESS_MVC_BUNDLE_KEY);
    }

    public static IRouterManager getBusinessMvpManager() {
        return getInstance(RouterBundleKey.BUSINESS_MVP_BUNDLE_KEY);
    }

    public static IRouterManager getBusinessMvvmManager() {
        return getInstance(RouterBundleKey.BUSINESS_MVVM_BUNDLE_KEY);
    }

    public static IRouterManager getBusinessDemoManager() {
        return getInstance(RouterBundleKey.BUSINESS_DEMO_BUNDLE_KEY);
    }
}
