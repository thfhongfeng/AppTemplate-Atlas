package com.pine.mvp.presenter;

import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.contract.IMvpHomeContract;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpHomePresenter extends BasePresenter<IMvpHomeContract.Ui>
        implements IMvpHomeContract.Presenter {

    @Override
    public boolean initDataOnUiCreate() {
        return false;
    }

    @Override
    public void onUiState(int state) {

    }
}
