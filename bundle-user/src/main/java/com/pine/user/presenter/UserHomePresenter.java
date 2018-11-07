package com.pine.user.presenter;

import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.user.contract.IUserHomeContract;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class UserHomePresenter extends BasePresenter<IUserHomeContract.Ui> implements IUserHomeContract.Presenter {

    @Override
    public boolean parseIntentDataOnCreate() {
        return false;
    }

    @Override
    public void onUiState(int state) {

    }
}
