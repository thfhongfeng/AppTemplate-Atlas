package com.pine.login.model.net;

import com.pine.login.model.ILoginAccountModel;
import com.pine.login.model.ILoginModelManager;

public class LoginNetModelManager implements ILoginModelManager {
    private static LoginNetModelManager mInstance;

    private LoginNetModelManager() {

    }

    public static synchronized LoginNetModelManager getInstance() {
        if (mInstance == null) {
            mInstance = new LoginNetModelManager();
        }
        return mInstance;
    }

    @Override
    public ILoginAccountModel getLoginAccountModel() {
        return new LoginAccountModel();
    }
}
