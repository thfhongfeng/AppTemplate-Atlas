package com.pine.mvp.contract;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.bean.InputParamBean;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpShopReleaseContract {
    interface Ui extends IBaseContract.Ui {
        @NonNull
        InputParamBean getShopNameParam(String key);

        @NonNull
        InputParamBean getShopTypeParam(String key);

        @NonNull
        InputParamBean getShopTypeNameParam(String key);

        @NonNull
        InputParamBean getShopOnlineDateParam(String key);

        @NonNull
        InputParamBean getShopContactMobileParam(String key);

        @NonNull
        InputParamBean getShopAddressParam(String key);

        @NonNull
        InputParamBean getShopAddressZipCodeParam(String key);

        @NonNull
        InputParamBean getShopLocationParam(String key);

        @NonNull
        InputParamBean getShopDetailAddressParam(String key);

        @NonNull
        InputParamBean getShopDescriptionParam(String key);

        @NonNull
        InputParamBean getShopRemarkParam(String key);

        @NonNull
        InputParamBean getShopImagesParam(String key);

        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        @NonNull
        String[] getShopTypeNameArr();

        @NonNull
        HashMap<String, String> makeUploadDefaultParams();

        void addShop();
    }
}
