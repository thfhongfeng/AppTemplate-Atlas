package com.pine.base.architecture.mvp.ui.activity;

import android.app.Activity;
import android.support.annotation.CallSuper;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.ui.BaseActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseMvpActivity<V extends IBaseContract.Ui, P extends BasePresenter<V>>
        extends BaseActivity implements IBaseContract.Ui {
    protected P mPresenter;

    @CallSuper
    @Override
    protected void beforeInitOnCreate() {
        // 创建并绑定presenter
        mPresenter = createPresenter();
        if (mPresenter == null) {
            Class presenterClazz;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                presenterClazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
                try {
                    mPresenter = (P) presenterClazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (mPresenter != null) {
            mPresenter.attachUi((V) this);
        } else {
            throw new RuntimeException("must initialize a presenter!");
        }
    }

    protected P createPresenter() {
        return null;
    }

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
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UiState.UI_STATE_ON_CREATE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UiState.UI_STATE_ON_START);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UiState.UI_STATE_ON_RESUME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UiState.UI_STATE_ON_PAUSE);
        }
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UiState.UI_STATE_ON_STOP);
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
