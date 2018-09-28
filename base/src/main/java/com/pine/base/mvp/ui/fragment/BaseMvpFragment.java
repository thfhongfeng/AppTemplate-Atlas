package com.pine.base.mvp.ui.fragment;

import android.content.Context;

import com.pine.base.BaseFragment;
import com.pine.base.mvp.contract.IBaseContract;
import com.pine.base.mvp.presenter.BasePresenter;

public abstract class BaseMvpFragment<V extends IBaseContract.Ui, P extends BasePresenter<V>>
        extends BaseFragment implements IBaseContract.Ui {
    protected P mPresenter;

    protected final void beforeInit() {
        // 创建并绑定presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachUi((V) this);
        }
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
    public Context getContext() {
        return getActivity();
    }

    protected abstract P createPresenter();
}
