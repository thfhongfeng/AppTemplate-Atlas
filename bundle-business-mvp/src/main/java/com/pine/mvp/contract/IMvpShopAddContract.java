package com.pine.mvp.contract;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.contract.IBaseContract;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpShopAddContract {
    interface Ui extends IBaseContract.Ui {
        void setSwipeRefreshLayoutRefresh(boolean processing);

        InputParamBean getShopNameParam(String key);

        InputParamBean getShopTypeParam(String key);

        InputParamBean getShopTypeNameParam(String key);

        InputParamBean getShopOnlineDateParam(String key);

        InputParamBean getShopContactMobileParam(String key);

        InputParamBean getShopAddressParam(String key);

        InputParamBean getShopAddressZipCodeParam(String key);

        InputParamBean getShopLocationParam(String key);

        InputParamBean getShopDetailAddressParam(String key);

        InputParamBean getShopDescriptionParam(String key);

        InputParamBean getShopRemarkParam(String key);

        InputParamBean getShopImagesParam(String key);
    }

    interface Presenter extends IBaseContract.Presenter {
        @NonNull
        String[] getShopTypeNameArr();

        @NonNull
        HashMap<String, String> makeUploadParams();

        void addShop();
    }
}
