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

public abstract class BaseActionBarTextMenuActivity extends BaseActivity {
    public static final int ACTION_BAR_TYPE_DEFAULT = 0x0;
    public static final int ACTION_BAR_CENTER_TITLE_TAG = 0x0001;
    public static final int ACTION_BAR_NO_GO_BACK_TAG = 0x0002;
    private int mActionBarTag = ACTION_BAR_TYPE_DEFAULT;
    private ImmersionBar mImmersionBar;

    @Override
    protected void beforeInitOnCreate() {

    }

    @Override
    protected final void setContentView() {
        if ((getActionBarTag() & ACTION_BAR_CENTER_TITLE_TAG) == ACTION_BAR_CENTER_TITLE_TAG) {
            setContentView(R.layout.base_activity_actionbar_text_menu_center_title);
        } else {
            setContentView(R.layout.base_activity_actionbar_text_menu);
        }

        ViewStub base_content_layout = findViewById(R.id.base_content_layout);
        base_content_layout.setLayoutResource(getActivityLayoutResId());
        base_content_layout.inflate();

        ViewStub base_loading_layout = findViewById(R.id.base_loading_layout);
        base_loading_layout.setLayoutResource(getLoadingUiResId());
        base_loading_layout.inflate();
        findViewById(R.id.base_loading_layout).setVisibility(View.GONE);

        initImmersionBar();
    }

    protected int getStatusBarBgResId() {
        return R.mipmap.base_iv_status_bar_bg;
    }

    @CallSuper
    @Override
    protected void afterInit() {
        View action_bar_ll = findViewById(R.id.action_bar_ll);
        if ((getActionBarTag() & ACTION_BAR_NO_GO_BACK_TAG) == ACTION_BAR_NO_GO_BACK_TAG) {
            action_bar_ll.findViewById(R.id.go_back_iv).setVisibility(View.GONE);
        } else {
            action_bar_ll.findViewById(R.id.go_back_iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        setupActionBar((ImageView) action_bar_ll.findViewById(R.id.go_back_iv),
                (TextView) action_bar_ll.findViewById(R.id.title),
                (TextView) action_bar_ll.findViewById(R.id.menu_tv));
    }

    protected abstract void setupActionBar(ImageView goBackIv, TextView titleTv, TextView menuBtnTv);

    /**
     * 获取actionbar内容的显示方式，重载该方法改变actionBar内容的显示方式
     */
    protected int getActionBarTag() {
        return mActionBarTag;
    }

    /**
     * 设置actionbar内容的显示方式，需在{@link #beforeInitOnCreate}中设置才有效
     *
     * @param tag
     */
    protected void setActionBarTag(int tag) {
        mActionBarTag = tag;
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
