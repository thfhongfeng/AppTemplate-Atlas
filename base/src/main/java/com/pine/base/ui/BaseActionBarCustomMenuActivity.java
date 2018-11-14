package com.pine.base.ui;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.pine.base.R;

public abstract class BaseActionBarCustomMenuActivity extends BaseActivity {
    private ImmersionBar mImmersionBar;

    @Override
    protected void beforeInitOnCreate() {

    }

    @Override
    protected final void setContentView() {
        setContentView(R.layout.base_activity_actionbar_custom_menu);

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
    protected void afterInitOnCreate() {
        View action_bar_ll = findViewById(R.id.action_bar_ll);
        ViewStub base_content_layout = findViewById(R.id.custom_menu_container_vs);
        base_content_layout.setLayoutResource(getMenuBarLayoutResId());
        initActionBar((ImageView) action_bar_ll.findViewById(R.id.go_back_iv),
                (TextView) action_bar_ll.findViewById(R.id.title), base_content_layout.inflate());
    }

    /**
     * onCreate中获取当前MenuBar的内容布局资源id
     *
     * @return MenuBar的内容布局资源id
     */
    protected abstract int getMenuBarLayoutResId();

    protected abstract void initActionBar(ImageView goBackIv, TextView titleTv, View menuContainer);

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
    }

    public void setKeyboardListener(OnKeyboardListener listener) {
        mImmersionBar.setOnKeyboardListener(listener);
    }

    protected int getLoadingUiResId() {
        return R.layout.base_loading;
    }

    public void startLoadingUi() {
        findViewById(R.id.base_loading_layout).setVisibility(View.VISIBLE);
    }

    public void finishLoadingUi() {
        findViewById(R.id.base_loading_layout).setVisibility(View.GONE);
    }
}
