package com.pine.mvp.model.local;

import com.pine.mvp.model.IMvpModelManager;
import com.pine.mvp.model.IMvpShopModel;
import com.pine.mvp.model.IMvpTravelNoteModel;

public class MvpLocalModelManager implements IMvpModelManager {
    private static MvpLocalModelManager mInstance;

    private MvpLocalModelManager() {

    }

    public static synchronized MvpLocalModelManager getInstance() {
        if (mInstance == null) {
            mInstance = new MvpLocalModelManager();
        }
        return mInstance;
    }

    @Override
    public IMvpShopModel getMvpShopModel() {
        return null;
    }

    @Override
    public IMvpTravelNoteModel getMvpTravelNoteModel() {
        return null;
    }
}
