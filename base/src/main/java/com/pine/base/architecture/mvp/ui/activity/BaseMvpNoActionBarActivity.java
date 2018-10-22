package com.pine.base.architecture.mvp.ui.activity;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.pine.base.R;
import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.ui.BaseActivity;

public abstract class BaseMvpNoActionBarActivity<V extends IBaseContract.Ui, P extends BasePresenter<V>>
        extends BaseActivity implements IBaseContract.Ui {
    protected P mPresenter;
    private ImmersionBar mImmersionBar;

    @Override
    protected final void setContentView() {
        setContentView(R.layout.base_mvp_activity_no_actionbar);
    }

    @CallSuper
    @Override
    protected boolean beforeInitOnCreate() {
        // 创建并绑定presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachUi((V) this);
        }

        ViewStub content_layout = (ViewStub) findViewById(R.id.content_layout);
        content_layout.setLayoutResource(getActivityLayoutResId());
        content_layout.inflate();

        initImmersionBar();
        return false;
    }

    @CallSuper
    @Override
    protected void afterInitOnCreate() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_SHOW);
        }
    }

    @Override
    protected void onPause() {
        //如果软键盘已弹出，收回软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_PAUSE);
        }
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onUiState(BasePresenter.UI_STATE_ON_HIDE);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        super.onDestroy();
        //解除绑定
        if (mPresenter != null) {
            mPresenter.detachUi();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void initImmersionBar() {
        findViewById(R.id.base_status_bar_view).setBackgroundResource(getStatusBarBgResId());
        mImmersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(true, 1f)
                .statusBarView(R.id.base_status_bar_view)
                .keyboardEnable(true);
        mImmersionBar.init();
    }

    public void setKeyboardListener(OnKeyboardListener listener) {
        mImmersionBar.setOnKeyboardListener(listener);
    }

    protected int getStatusBarBgResId() {
        return R.mipmap.base_iv_status_bar_bg;
    }

    protected abstract P createPresenter();
}
