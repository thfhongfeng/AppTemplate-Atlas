package com.pine.welcome.ui.activity;

import com.pine.base.mvp.ui.activity.BaseNoActionBarActivity;
import com.pine.welcome.R;
import com.pine.welcome.contract.IWelcomeContract;
import com.pine.welcome.presenter.WelcomePresenter;

public class WelcomeActivity extends BaseNoActionBarActivity<IWelcomeContract.Ui, WelcomePresenter>
        implements IWelcomeContract.Ui {

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initData() {

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
