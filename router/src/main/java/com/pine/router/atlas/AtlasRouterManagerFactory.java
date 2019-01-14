package com.pine.router.atlas;

import com.pine.router.IRouterManager;
import com.pine.router.RouterBundleKey;

/**
 * Created by tanghongfeng on 2019/1/14
 */

public class AtlasRouterManagerFactory {

    public static IRouterManager getBundleManager(String bundleKey) {
        switch (bundleKey) {
            case RouterBundleKey.LOGIN_BUNDLE_KEY:
                return getLoginBundleManager();
            case RouterBundleKey.MAIN_BUNDLE_KEY:
                return getMainBundleManager();
            case RouterBundleKey.USER_BUNDLE_KEY:
                return getUserBundleManager();
            case RouterBundleKey.BUSINESS_MVC_BUNDLE_KEY:
                return getBusinessMvcBundleManager();
            case RouterBundleKey.BUSINESS_MVP_BUNDLE_KEY:
                return getBusinessMvpBundleManager();
            case RouterBundleKey.BUSINESS_MVVM_BUNDLE_KEY:
                return getBusinessMvvmBundleManager();
            case RouterBundleKey.BUSINESS_DEMO_BUNDLE_KEY:
                return getBusinessDemoBundleManager();
            default:
                return null;
        }
    }

    public static IRouterManager getLoginBundleManager() {
        return AtlasRouterLoginManager.getInstance();
    }

    public static IRouterManager getMainBundleManager() {
        return AtlasRouterMainManager.getInstance();
    }

    public static IRouterManager getUserBundleManager() {
        return AtlasRouterUserManager.getInstance();
    }

    public static IRouterManager getBusinessMvcBundleManager() {
        return AtlasRouterBusinessMvcManager.getInstance();
    }

    public static IRouterManager getBusinessMvpBundleManager() {
        return AtlasRouterBusinessMvpManager.getInstance();
    }

    public static IRouterManager getBusinessMvvmBundleManager() {
        return AtlasRouterBusinessMvvmManager.getInstance();
    }

    public static IRouterManager getBusinessDemoBundleManager() {
        return AtlasRouterBusinessDemoManager.getInstance();
    }
}
