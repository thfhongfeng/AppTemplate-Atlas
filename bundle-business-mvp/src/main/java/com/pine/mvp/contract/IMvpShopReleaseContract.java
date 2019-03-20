package com.pine.mvp.contract;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.bean.BaseInputParam;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpShopReleaseContract {
    interface Ui extends IBaseContract.Ui {
        @NonNull
        BaseInputParam getShopNameParam(String key);

        @NonNull
        BaseInputParam getShopTypeParam(String key);

        @NonNull
        BaseInputParam getShopTypeNameParam(String key);

        @NonNull
        BaseInputParam getShopOnlineDateParam(String key);

        @NonNull
        BaseInputParam getShopContactMobileParam(String key);

        @NonNull
        BaseInputParam getShopAddressParam(String key);

        @NonNull
        BaseInputParam getShopAddressZipCodeParam(String key);

        @NonNull
        BaseInputParam getShopLocationLonParam(String key);

        @NonNull
        BaseInputParam getShopLocationLatParam(String key);

        @NonNull
        BaseInputParam getShopDetailAddressParam(String key);

        @NonNull
        BaseInputParam getShopDescriptionParam(String key);

        @NonNull
        BaseInputParam getShopRemarkParam(String key);

        @NonNull
        BaseInputParam getShopImagesParam(String key);
    }

    interface Presenter extends IBaseContract.Presenter {
        @NonNull
        String[] getShopTypeNameArr();

        @NonNull
        HashMap<String, String> makeUploadDefaultParams();

        void addShop();
    }
}
