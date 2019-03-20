package com.pine.mvp.model.net;

import com.pine.mvp.model.IMvpModelManager;
import com.pine.mvp.model.IMvpShopModel;
import com.pine.mvp.model.IMvpTravelNoteModel;

public class MvpNetModelManager implements IMvpModelManager {
    private static MvpNetModelManager mInstance;

    private MvpNetModelManager() {

    }

    public static synchronized MvpNetModelManager getInstance() {
        if (mInstance == null) {
            mInstance = new MvpNetModelManager();
        }
        return mInstance;
    }

    @Override
    public IMvpShopModel getMvpShopModel() {
        return new MvpShopModel();
    }

    @Override
    public IMvpTravelNoteModel getMvpTravelNoteModel() {
        return new MvpTravelNoteModel();
    }
}
