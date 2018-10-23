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

public abstract class BaseActionBarImageMenuActivity extends BaseActivity {
    public static final int ACTION_BAR_TYPE_DEFAULT = 0x0;
    public static final int ACTION_BAR_CENTER_TITLE_TAG = 0x0001;
    public static final int ACTION_BAR_NO_GO_BACK_TAG = 0x0002;
    private ImmersionBar mImmersionBar;

    @Override
    protected final void setContentView() {
        if ((getActionBarType() & ACTION_BAR_CENTER_TITLE_TAG) == ACTION_BAR_CENTER_TITLE_TAG) {
            setContentView(R.layout.base_activity_actionbar_image_menu_center_title);
        } else {
            setContentView(R.layout.base_activity_actionbar_image_menu);
        }
    }

    @CallSuper
    @Override
    protected boolean beforeInitOnCreate() {
        ViewStub content_layout = findViewById(R.id.content_layout);
        content_layout.setLayoutResource(getActivityLayoutResId());
        content_layout.inflate();

        initImmersionBar();

        return false;
    }

    @CallSuper
    @Override
    protected void afterInitOnCreate() {
        View action_bar_ll = findViewById(R.id.action_bar_ll);
        if ((getActionBarType() & ACTION_BAR_NO_GO_BACK_TAG) == ACTION_BAR_NO_GO_BACK_TAG) {
            action_bar_ll.findViewById(R.id.go_back_iv).setVisibility(View.GONE);
        }
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

    /**
     * 获取actionbar类别
     */
    protected int getActionBarType() {
        return ACTION_BAR_TYPE_DEFAULT;
    }

    protected abstract void initActionBar(ImageView goBackIv, TextView titleTv, ImageView menuBtnIv);
}
