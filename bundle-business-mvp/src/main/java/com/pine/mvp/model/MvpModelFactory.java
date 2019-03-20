package com.pine.mvp.model;

import com.pine.config.BuildConfig;
import com.pine.mvp.model.local.MvpLocalModelManager;
import com.pine.mvp.model.net.MvpNetModelManager;

public class MvpModelFactory {
    public static IMvpModelManager getInstance() {
        switch (BuildConfig.APP_THIRD_DATA_SOURCE_PROVIDER) {
            case "local":
                return MvpLocalModelManager.getInstance();
            default:
                return MvpNetModelManager.getInstance();
        }
    }

    public static IMvpShopModel getMvpShopModel() {
        return getInstance().getMvpShopModel();
    }

    public static IMvpTravelNoteModel getMvpTravelNoteModel() {
        return getInstance().getMvpTravelNoteModel();
    }
}
