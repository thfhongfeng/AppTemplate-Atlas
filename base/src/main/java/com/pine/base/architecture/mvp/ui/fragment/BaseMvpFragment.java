package com.pine.base.architecture.mvp.ui.fragment;

import android.app.Activity;
import android.support.annotation.CallSuper;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.ui.BaseFragment;

public abstract class BaseMvpFragment<V extends IBaseContract.Ui, P extends BasePresenter<V>>
        extends BaseFragment implements IBaseContract.Ui {
    protected P mPresenter;

    @CallSuper
    @Override
    protected void beforeInitOnCreateView() {
        // 创建并绑定presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachUi((V) this);
        }
    }

    protected abstract P createPresenter();

    @Override
    protected final boolean parseArguments() {
        if (mPresenter != null) {
            return mPresenter.parseIntentData();
        }
        return false;
    }

    @CallSuper
    @Override
    protected void afterInit() {
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_CREATE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_START);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_RESUME);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_PAUSE);
        }
    }

    @Override
    public void onStop() {
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_STOP);
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        //解除绑定
        if (mPresenter != null) {
            mPresenter.detachUi();
        }
        super.onDestroyView();
    }

    @Override
    public Activity getContextActivity() {
        return getActivity();
    }
}
