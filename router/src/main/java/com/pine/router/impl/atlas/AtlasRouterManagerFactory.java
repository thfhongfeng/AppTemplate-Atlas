package com.pine.router.impl.atlas;

import com.pine.config.ConfigBundleKey;
import com.pine.router.impl.IRouterManager;
import com.pine.router.impl.atlas.manager.AtlasRouterBusinessDemoManager;
import com.pine.router.impl.atlas.manager.AtlasRouterBusinessMvcManager;
import com.pine.router.impl.atlas.manager.AtlasRouterBusinessMvpManager;
import com.pine.router.impl.atlas.manager.AtlasRouterBusinessMvvmManager;
import com.pine.router.impl.atlas.manager.AtlasRouterLoginManager;
import com.pine.router.impl.atlas.manager.AtlasRouterMainManager;
import com.pine.router.impl.atlas.manager.AtlasRouterUserManager;

/**
 * Created by tanghongfeng on 2019/1/14
 */

public class AtlasRouterManagerFactory {

    public static IRouterManager getManager(String bundleKey) {
        switch (bundleKey) {
            case ConfigBundleKey.LOGIN_BUNDLE_KEY:
                return getLoginRouter();
            case ConfigBundleKey.MAIN_BUNDLE_KEY:
                return getMainRouter();
            case ConfigBundleKey.USER_BUNDLE_KEY:
                return getUserRouter();
            case ConfigBundleKey.BUSINESS_MVC_BUNDLE_KEY:
                return getBusinessMvcRouter();
            case ConfigBundleKey.BUSINESS_MVP_BUNDLE_KEY:
                return getBusinessMvpRouter();
            case ConfigBundleKey.BUSINESS_MVVM_BUNDLE_KEY:
                return getBusinessMvvmRouter();
            case ConfigBundleKey.BUSINESS_DEMO_BUNDLE_KEY:
                return getBusinessDemoRouter();
            default:
                return null;
        }
    }

    public static IRouterManager getLoginRouter() {
        return AtlasRouterLoginManager.getInstance();
    }

    public static IRouterManager getMainRouter() {
        return AtlasRouterMainManager.getInstance();
    }

    public static IRouterManager getUserRouter() {
        return AtlasRouterUserManager.getInstance();
    }

    public static IRouterManager getBusinessMvcRouter() {
        return AtlasRouterBusinessMvcManager.getInstance();
    }

    public static IRouterManager getBusinessMvpRouter() {
        return AtlasRouterBusinessMvpManager.getInstance();
    }

    public static IRouterManager getBusinessMvvmRouter() {
        return AtlasRouterBusinessMvvmManager.getInstance();
    }

    public static IRouterManager getBusinessDemoRouter() {
        return AtlasRouterBusinessDemoManager.getInstance();
    }
}
