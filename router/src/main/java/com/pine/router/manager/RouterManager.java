package com.pine.router.manager;

import com.pine.config.BuildConfig;
import com.pine.config.ConfigBundleKey;
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

    public static IRouterManager getLoginRouter() {
        return getInstance(ConfigBundleKey.LOGIN_BUNDLE_KEY);
    }

    public static IRouterManager getMainRouter() {
        return getInstance(ConfigBundleKey.MAIN_BUNDLE_KEY);
    }

    public static IRouterManager getUserRouter() {
        return getInstance(ConfigBundleKey.USER_BUNDLE_KEY);
    }

    public static IRouterManager getBusinessMvcRouter() {
        return getInstance(ConfigBundleKey.BUSINESS_MVC_BUNDLE_KEY);
    }

    public static IRouterManager getBusinessMvpRouter() {
        return getInstance(ConfigBundleKey.BUSINESS_MVP_BUNDLE_KEY);
    }

    public static IRouterManager getBusinessMvvmRouter() {
        return getInstance(ConfigBundleKey.BUSINESS_MVVM_BUNDLE_KEY);
    }

    public static IRouterManager getBusinessDemoRouter() {
        return getInstance(ConfigBundleKey.BUSINESS_DEMO_BUNDLE_KEY);
    }
}
