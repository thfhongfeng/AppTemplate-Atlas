package com.pine.main.model.net;

import com.pine.main.model.IMainHomeModel;
import com.pine.main.model.IMainModelManager;

public class MainNetModelManager implements IMainModelManager {
    private static MainNetModelManager mInstance;

    private MainNetModelManager() {

    }

    public static synchronized MainNetModelManager getInstance() {
        if (mInstance == null) {
            mInstance = new MainNetModelManager();
        }
        return mInstance;
    }

    @Override
    public IMainHomeModel getMainHomeModel() {
        return new MainHomeModel();
    }
}
