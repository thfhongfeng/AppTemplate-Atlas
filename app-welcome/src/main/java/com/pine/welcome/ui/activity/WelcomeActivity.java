package com.pine.welcome.ui.activity;

import com.pine.base.mvp.ui.activity.BaseMvpNoActionBarActivity;
import com.pine.welcome.R;
import com.pine.welcome.contract.IWelcomeContract;
import com.pine.welcome.presenter.WelcomePresenter;

public class WelcomeActivity extends BaseMvpNoActionBarActivity<IWelcomeContract.Ui, WelcomePresenter>
        implements IWelcomeContract.Ui {

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected boolean initData() {
        return true;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void afterInit() {
        mPresenter.goMainHomeActivity();
    }

    @Override
    protected WelcomePresenter createPresenter() {
        return new WelcomePresenter();
    }
}
