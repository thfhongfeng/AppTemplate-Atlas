package com.pine.user.model.net;

import com.pine.user.model.IUserModelManager;

public class UserNetModelManager implements IUserModelManager {
    private static UserNetModelManager mInstance;

    private UserNetModelManager() {

    }

    public static synchronized UserNetModelManager getInstance() {
        if (mInstance == null) {
            mInstance = new UserNetModelManager();
        }
        return mInstance;
    }
}
