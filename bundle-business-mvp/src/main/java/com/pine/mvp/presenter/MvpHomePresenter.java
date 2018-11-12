package com.pine.mvp.presenter;

import android.content.Intent;

import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.contract.IMvpHomeContract;
import com.pine.mvp.ui.activity.MvpShopReleaseActivity;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpHomePresenter extends BasePresenter<IMvpHomeContract.Ui>
        implements IMvpHomeContract.Presenter {

    @Override
    public boolean parseIntentDataOnCreate() {
        return false;
    }

    @Override
    public void onUiState(int state) {

    }

    @Override
    public void goToAddShopActivity() {
        Intent intent = new Intent(getContext(), MvpShopReleaseActivity.class);
        getContext().startActivity(intent);
    }
}
