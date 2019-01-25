package com.pine.router.manager.atlas;

import com.pine.router.RouterBundleKey;
import com.pine.router.manager.IRouterManager;

/**
 * Created by tanghongfeng on 2019/1/14
 */

public class AtlasRouterManagerFactory {

    public static IRouterManager getManager(String bundleKey) {
        switch (bundleKey) {
            case RouterBundleKey.LOGIN_BUNDLE_KEY:
                return getLoginManager();
            case RouterBundleKey.MAIN_BUNDLE_KEY:
                return getMainManager();
            case RouterBundleKey.USER_BUNDLE_KEY:
                return getUserManager();
            case RouterBundleKey.BUSINESS_MVC_BUNDLE_KEY:
                return getBusinessMvcManager();
            case RouterBundleKey.BUSINESS_MVP_BUNDLE_KEY:
                return getBusinessMvpManager();
            case RouterBundleKey.BUSINESS_MVVM_BUNDLE_KEY:
                return getBusinessMvvmManager();
            case RouterBundleKey.BUSINESS_DEMO_BUNDLE_KEY:
                return getBusinessDemoManager();
            default:
                return null;
        }
    }

    public static IRouterManager getLoginManager() {
        return AtlasRouterLoginManager.getInstance();
    }

    public static IRouterManager getMainManager() {
        return AtlasRouterMainManager.getInstance();
    }

    public static IRouterManager getUserManager() {
        return AtlasRouterUserManager.getInstance();
    }

    public static IRouterManager getBusinessMvcManager() {
        return AtlasRouterBusinessMvcManager.getInstance();
    }

    public static IRouterManager getBusinessMvpManager() {
        return AtlasRouterBusinessMvpManager.getInstance();
    }

    public static IRouterManager getBusinessMvvmManager() {
        return AtlasRouterBusinessMvvmManager.getInstance();
    }

    public static IRouterManager getBusinessDemoManager() {
        return AtlasRouterBusinessDemoManager.getInstance();
    }
}
