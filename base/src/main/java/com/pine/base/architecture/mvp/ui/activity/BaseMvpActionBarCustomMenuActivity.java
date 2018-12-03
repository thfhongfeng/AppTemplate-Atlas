package com.pine.base.architecture.mvp.ui.activity;

import android.app.Activity;
import android.support.annotation.CallSuper;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.ui.BaseActionBarCustomMenuActivity;

public abstract class BaseMvpActionBarCustomMenuActivity<V extends IBaseContract.Ui, P extends BasePresenter<V>>
        extends BaseActionBarCustomMenuActivity implements IBaseContract.Ui {
    protected P mPresenter;

    @CallSuper
    @Override
    protected void beforeInitOnCreate() {
        // 创建并绑定presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachUi((V) this);
        }
    }

    protected abstract P createPresenter();

    @Override
    protected final boolean parseIntentData() {
        if (mPresenter != null) {
            return mPresenter.parseIntentData();
        }
        return false;
    }

    @CallSuper
    @Override
    protected void afterInit() {
        super.afterInit();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_CREATE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_START);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_RESUME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_PAUSE);
        }
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_STOP);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定
        if (mPresenter != null) {
            mPresenter.detachUi();
        }
    }

    @Override
    public Activity getContextActivity() {
        return this;
    }
}
