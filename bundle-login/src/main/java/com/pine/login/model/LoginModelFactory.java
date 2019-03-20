package com.pine.login.model;

import com.pine.config.BuildConfig;
import com.pine.login.model.local.LoginLocalModelManager;
import com.pine.login.model.net.LoginNetModelManager;

public class LoginModelFactory {
    public static ILoginModelManager getInstance() {
        switch (BuildConfig.APP_THIRD_DATA_SOURCE_PROVIDER) {
            case "local":
                return LoginLocalModelManager.getInstance();
            default:
                return LoginNetModelManager.getInstance();
        }
    }

    public static ILoginAccountModel getLoginAccountModel() {
        return getInstance().getLoginAccountModel();
    }
}
