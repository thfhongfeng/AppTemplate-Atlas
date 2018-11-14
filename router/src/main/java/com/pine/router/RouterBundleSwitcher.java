package com.pine.router;

import com.pine.tool.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public class RouterBundleSwitcher {
    private static final String TAG = LogUtils.makeLogTag(RouterBundleSwitcher.class);

    private static Map<String, Boolean> mBundleSwitcherStateMap = new HashMap<String, Boolean>();

    static {
        RouterBundleSwitcher.setBundleSwitchState(RouterBundleKey.LOGIN_BUNDLE_KEY, true);
        RouterBundleSwitcher.setBundleSwitchState(RouterBundleKey.MAIN_BUNDLE_KEY, true);
        RouterBundleSwitcher.setBundleSwitchState(RouterBundleKey.USER_BUNDLE_KEY, true);
    }

    public static void setBundleSwitchState(String key, boolean isOpen) {
        mBundleSwitcherStateMap.put(key, isOpen);
        LogUtils.releaseLog(TAG, "Set " + key + " bundle " + (isOpen ? "open" : "close"));
    }

    public static boolean isBundleOpen(String key) {
        return mBundleSwitcherStateMap.containsKey(key) && mBundleSwitcherStateMap.get(key);
    }
}
