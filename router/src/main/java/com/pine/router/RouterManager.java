package com.pine.router;

import com.pine.router.atlas.AtlasRouterManagerFactory;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class RouterManager {
    public static IRouterManager getBundleManager(String bundleKey) {
        return AtlasRouterManagerFactory.getBundleManager(bundleKey);
    }
}
