package com.pine.main.model;

import com.pine.config.BuildConfig;
import com.pine.main.model.local.MainLocalModelManager;
import com.pine.main.model.net.MainNetModelManager;

public class MainModelFactory {
    public static IMainModelManager getInstance() {
        switch (BuildConfig.APP_THIRD_DATA_SOURCE_PROVIDER) {
            case "local":
                return MainLocalModelManager.getInstance();
            default:
                return MainNetModelManager.getInstance();
        }
    }

    public static IMainHomeModel getMainHomeModel() {
        return getInstance().getMainHomeModel();
    }
}
