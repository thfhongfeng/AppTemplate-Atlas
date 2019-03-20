package com.pine.welcome.model;

import com.pine.config.BuildConfig;
import com.pine.welcome.model.local.WelLocalModelManager;
import com.pine.welcome.model.net.WelNetModelManager;

public class WelModelFactory {
    public static IWelModelManager getInstance() {
        switch (BuildConfig.APP_THIRD_DATA_SOURCE_PROVIDER) {
            case "local":
                return WelLocalModelManager.getInstance();
            default:
                return WelNetModelManager.getInstance();
        }
    }

    public static IBundleSwitcherModel getBundleSwitcherModel() {
        return getInstance().getBundleSwitcherModel();
    }

    public static IVersionModel getVersionModel() {
        return getInstance().getVersionModel();
    }
}
