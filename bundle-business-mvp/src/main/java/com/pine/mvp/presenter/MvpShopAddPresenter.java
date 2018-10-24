package com.pine.mvp.presenter;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.contract.IMvpShopAddContract;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpShopAddPresenter extends BasePresenter<IMvpShopAddContract.Ui>
        implements IMvpShopAddContract.Presenter {

    @Override
    public boolean initDataOnUiCreate() {
        return false;
    }

    @Override
    public void onUiState(int state) {

    }

    @NonNull
    @Override
    public String[] getShopTypeArr() {
        String[] typeList = new String[3];
        typeList[0] = "景点";
        typeList[1] = "食品";
        typeList[2] = "五金";
        return typeList;
    }

    @Override
    public void addShop() {

    }
}
