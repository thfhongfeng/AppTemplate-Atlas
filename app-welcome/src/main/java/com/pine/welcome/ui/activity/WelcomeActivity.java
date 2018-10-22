package com.pine.welcome.ui.activity;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpNoActionBarActivity;
import com.pine.welcome.R;
import com.pine.welcome.contract.IWelcomeContract;
import com.pine.welcome.presenter.WelcomePresenter;

public class WelcomeActivity extends BaseMvpNoActionBarActivity<IWelcomeContract.Ui, WelcomePresenter>
        implements IWelcomeContract.Ui {

    @Override
    protected WelcomePresenter createPresenter() {
        return new WelcomePresenter();
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected boolean initDataOnCreate() {
        return false;
    }

    @Override
    protected void initViewOnCreate() {

    }

    @Override
    protected void afterInitOnCreate() {
        super.afterInitOnCreate();
        mPresenter.goMainHomeActivity();
    }
}
