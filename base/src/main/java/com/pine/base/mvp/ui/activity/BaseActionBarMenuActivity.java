package com.pine.base.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.pine.base.R;
import com.pine.base.access.UiAccessManager;
import com.pine.base.mvp.contract.IBaseContract;
import com.pine.base.mvp.presenter.BasePresenter;
import com.pine.router.IRouterCallback;
import com.pine.router.RouterCommand;
import com.pine.router.RouterFactory;
import com.pine.tool.util.LogUtils;

public abstract class BaseActionBarMenuActivity<V extends IBaseContract.Ui, P extends BasePresenter<V>>
        extends AppCompatActivity implements IBaseContract.Ui {
    protected final String TAG = LogUtils.makeLogTag(this.getClass());
    protected P mPresenter;
    private ImmersionBar mImmersionBar;
    private boolean mIsUiAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_actionbar_menu);

        beforeInit();

        mIsUiAccess = UiAccessManager.getInstance().checkAccess(this);
        if (!mIsUiAccess) {
            RouterFactory.getLoginBundleManager().callUiCommand(this, RouterCommand.LOGIN_goLoginActivity, null, new IRouterCallback() {
                @Override
                public void onSuccess(Bundle returnBundle) {
                    finish();
                }

                @Override
                public void onException(Bundle returnBundle) {
                    finish();
                }

                @Override
                public void onFail(String errorInfo) {
                    finish();
                }
            });
            return;
        }

        // 创建并绑定presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachUi((V) this);
        }

        ViewStub content_layout = (ViewStub) findViewById(R.id.content_layout);
        content_layout.setLayoutResource(getActivityLayoutResId());
        content_layout.inflate();

        initImmersionBar();

        View action_bar_ll = findViewById(R.id.action_bar_ll);
        initActionBar((ImageView) action_bar_ll.findViewById(R.id.go_back_iv),
                (TextView) action_bar_ll.findViewById(R.id.title),
                (ImageView) action_bar_ll.findViewById(R.id.menu_iv));

        initData();
        initView();

        afterInit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!mIsUiAccess) {
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mIsUiAccess) {
            return;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!mIsUiAccess) {
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsUiAccess) {
            return;
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

    protected void beforeInit() {

    }

    protected abstract int getActivityLayoutResId();

    protected abstract void initData();

    protected abstract void initActionBar(ImageView goBackIv, TextView titleTv, ImageView menuBtnIv);

    protected abstract void initView();

    protected abstract void afterInit();

    protected abstract P createPresenter();
}
