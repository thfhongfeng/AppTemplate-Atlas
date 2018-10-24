package com.pine.mvp.contract;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.contract.IBaseContract;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpShopAddContract {
    interface Ui extends IBaseContract.Ui {

    }

    interface Presenter extends IBaseContract.Presenter {
        @NonNull
        String[] getShopTypeArr();

        void addShop();
    }
}
