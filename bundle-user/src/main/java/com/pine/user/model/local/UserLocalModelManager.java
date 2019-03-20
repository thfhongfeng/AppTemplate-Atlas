package com.pine.user.model.local;

import com.pine.user.model.IUserModelManager;

public class UserLocalModelManager implements IUserModelManager {
    private static UserLocalModelManager mInstance;

    private UserLocalModelManager() {

    }

    public static synchronized UserLocalModelManager getInstance() {
        if (mInstance == null) {
            mInstance = new UserLocalModelManager();
        }
        return mInstance;
    }
}
