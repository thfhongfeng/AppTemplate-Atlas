package com.pine.login.model.local;

import com.pine.login.model.ILoginAccountModel;
import com.pine.login.model.ILoginModelManager;

public class LoginLocalModelManager implements ILoginModelManager {
    private static LoginLocalModelManager mInstance;

    private LoginLocalModelManager() {

    }

    public static synchronized LoginLocalModelManager getInstance() {
        if (mInstance == null) {
            mInstance = new LoginLocalModelManager();
        }
        return mInstance;
    }

    @Override
    public ILoginAccountModel getLoginAccountModel() {
        return null;
    }
}
