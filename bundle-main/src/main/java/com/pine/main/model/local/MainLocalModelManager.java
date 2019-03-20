package com.pine.main.model.local;

import com.pine.main.model.IMainHomeModel;
import com.pine.main.model.IMainModelManager;

public class MainLocalModelManager implements IMainModelManager {
    private static MainLocalModelManager mInstance;

    private MainLocalModelManager() {

    }

    public static synchronized MainLocalModelManager getInstance() {
        if (mInstance == null) {
            mInstance = new MainLocalModelManager();
        }
        return mInstance;
    }

    @Override
    public IMainHomeModel getMainHomeModel() {
        return null;
    }
}
