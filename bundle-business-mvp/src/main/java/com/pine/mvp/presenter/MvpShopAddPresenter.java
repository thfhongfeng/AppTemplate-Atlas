package com.pine.mvp.presenter;

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

    @Override
    public void addShop() {

    }
}
