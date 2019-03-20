package com.pine.welcome.model.net;

import com.pine.welcome.model.IBundleSwitcherModel;
import com.pine.welcome.model.IVersionModel;
import com.pine.welcome.model.IWelModelManager;

public class WelNetModelManager implements IWelModelManager {
    private static WelNetModelManager mInstance;

    private WelNetModelManager() {

    }

    public static synchronized WelNetModelManager getInstance() {
        if (mInstance == null) {
            mInstance = new WelNetModelManager();
        }
        return mInstance;
    }

    @Override
    public IBundleSwitcherModel getBundleSwitcherModel() {
        return new BundleSwitcherModel();
    }

    @Override
    public IVersionModel getVersionModel() {
        return new VersionModel();
    }
}
