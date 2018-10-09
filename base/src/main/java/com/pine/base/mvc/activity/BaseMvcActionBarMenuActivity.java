package com.pine.base.mvc.activity;

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
import com.pine.base.ui.BaseActivity;

public abstract class BaseMvcActionBarMenuActivity extends BaseActivity {
    private ImmersionBar mImmersionBar;

    @Override
    protected final void setContentView() {
        setContentView(R.layout.base_mvc_activity_actionbar_menu);
    }

    @CallSuper
    @Override
    protected boolean beforeInit() {
        ViewStub content_layout = (ViewStub) findViewById(R.id.content_layout);
        content_layout.setLayoutResource(getActivityLayoutResId());
        content_layout.inflate();

        initImmersionBar();

        return false;
    }

    @CallSuper
    @Override
    protected void afterInit() {
        View action_bar_ll = findViewById(R.id.action_bar_ll);
        initActionBar((ImageView) action_bar_ll.findViewById(R.id.go_back_iv),
                (TextView) action_bar_ll.findViewById(R.id.title),
                (ImageView) action_bar_ll.findViewById(R.id.menu_iv));
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

    protected abstract void initActionBar(ImageView goBackIv, TextView titleTv, ImageView menuBtnIv);
}
