package com.pine.user.model;

import com.pine.config.BuildConfig;
import com.pine.user.model.local.UserLocalModelManager;
import com.pine.user.model.net.UserNetModelManager;

public class UserModelFactory {
    public static IUserModelManager getInstance() {
        switch (BuildConfig.APP_THIRD_DATA_SOURCE_PROVIDER) {
            case "local":
                return UserLocalModelManager.getInstance();
            default:
                return UserNetModelManager.getInstance();
        }
    }
}
