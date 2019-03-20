package com.pine.welcome.model.local;

import com.pine.welcome.model.IBundleSwitcherModel;
import com.pine.welcome.model.IVersionModel;
import com.pine.welcome.model.IWelModelManager;

public class WelLocalModelManager implements IWelModelManager {
    private static WelLocalModelManager mInstance;

    private WelLocalModelManager() {

    }

    public static synchronized WelLocalModelManager getInstance() {
        if (mInstance == null) {
            mInstance = new WelLocalModelManager();
        }
        return mInstance;
    }

    @Override
    public IBundleSwitcherModel getBundleSwitcherModel() {
        return null;
    }

    @Override
    public IVersionModel getVersionModel() {
        return null;
    }
}
