package com.pine.mvp.presenter;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopDetailEntity;
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
    public String[] getShopTypeNameArr() {
        return getContext().getResources().getStringArray(R.array.mvp_shop_type);
    }

    @Override
    public void addShop(MvpShopDetailEntity entity) {

    }
}
