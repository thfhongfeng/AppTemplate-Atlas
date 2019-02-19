package com.pine.base.ui;

import android.support.annotation.CallSuper;
import android.view.View;
import android.view.ViewStub;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.pine.base.R;

public abstract class BaseNoActionBarActivity extends BaseActivity {
    private ImmersionBar mImmersionBar;

    @Override
    protected void beforeInitOnCreate() {

    }

    @Override
    protected final void setContentView() {
        setContentView(R.layout.base_activity_no_actionbar);

        ViewStub base_content_layout = findViewById(R.id.base_content_layout);
        base_content_layout.setLayoutResource(getActivityLayoutResId());
        base_content_layout.inflate();

        ViewStub base_loading_layout = findViewById(R.id.base_loading_layout);
        base_loading_layout.setLayoutResource(getLoadingUiResId());
        base_loading_layout.inflate();
        findViewById(R.id.base_loading_layout).setVisibility(View.GONE);

        initImmersionBar();
    }

    private void initImmersionBar() {
        findViewById(R.id.base_status_bar_view).setBackgroundResource(getStatusBarBgResId());
        mImmersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(true, 1f)
                .statusBarView(R.id.base_status_bar_view)
                .keyboardEnable(true);
        mImmersionBar.init();
    }

    protected int getStatusBarBgResId() {
        return R.mipmap.base_iv_status_bar_bg;
    }

    @CallSuper
    @Override
    protected void afterInit() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        super.onDestroy();
    }

    public void setKeyboardListener(OnKeyboardListener listener) {
        mImmersionBar.setOnKeyboardListener(listener);
    }

    protected int getLoadingUiResId() {
        return R.layout.base_loading;
    }

    public void startLoadingUi() {
        hideSoftInputFromWindow();
        findViewById(R.id.base_loading_layout).setVisibility(View.VISIBLE);
    }

    public void finishLoadingUi() {
        findViewById(R.id.base_loading_layout).setVisibility(View.GONE);
    }
}
