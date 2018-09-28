package com.pine.router;

import com.pine.router.atlas.AtlasRouterBusinessMvcManager;
import com.pine.router.atlas.AtlasRouterBusinessMvpManager;
import com.pine.router.atlas.AtlasRouterLoginManager;
import com.pine.router.atlas.AtlasRouterMainManager;
import com.pine.router.atlas.AtlasRouterUserManager;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class RouterFactory {

    public static IRouterManager getBundleManager(String bundleKey) {
        switch (bundleKey) {
            case RouterBundleKey.LOGIN_BUNDLE_KEY:
                return getLoginBundleManager();
            case RouterBundleKey.MAIN_BUNDLE_KEY:
                return getMainBundleManager();
            case RouterBundleKey.USER_BUNDLE_KEY:
                return getUserBundleManager();
            case RouterBundleKey.BUSINESS_MVC_BUNDLE_KEY:
                return getBusinessABundleManager();
            case RouterBundleKey.BUSINESS_MVP_BUNDLE_KEY:
                return getBusinessBBundleManager();
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

    public static IRouterManager getBusinessABundleManager() {
        return AtlasRouterBusinessMvcManager.getInstance();
    }

    public static IRouterManager getBusinessBBundleManager() {
        return AtlasRouterBusinessMvpManager.getInstance();
    }
}
