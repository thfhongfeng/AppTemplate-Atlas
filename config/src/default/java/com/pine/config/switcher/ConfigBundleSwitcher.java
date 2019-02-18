package com.pine.config.switcher;

import com.pine.config.ConfigBundleKey;
import com.pine.tool.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public class ConfigBundleSwitcher {
    private static final String TAG = LogUtils.makeLogTag(ConfigBundleSwitcher.class);

    private static Map<String, Boolean> mBundleStateMap = new HashMap();

    static {
        setBundleState(ConfigBundleKey.LOGIN_BUNDLE_KEY, true);
        setBundleState(ConfigBundleKey.MAIN_BUNDLE_KEY, true);
        setBundleState(ConfigBundleKey.USER_BUNDLE_KEY, true);
    }

    public static void setBundleState(String key, boolean isOpen) {
        mBundleStateMap.put(key, isOpen);
        LogUtils.releaseLog(TAG, "Set " + key + " bundle " + (isOpen ? "open" : "close"));
    }

    public static boolean isBundleOpen(String key) {
        return mBundleStateMap.containsKey(key) && mBundleStateMap.get(key);
    }
}
