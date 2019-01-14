package com.pine.mvc.ui.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvc.activity.BaseMvcActionBarActivity;
import com.pine.mvc.R;

/**
 * Created by tanghongfeng on 2019/1/14
 */

public class MvcHomeActivity extends BaseMvcActionBarActivity {

    @Override
    protected void beforeInitOnCreate() {
        super.beforeInitOnCreate();
        setActionBarTag(ACTION_BAR_CENTER_TITLE_TAG | ACTION_BAR_NO_GO_BACK_TAG);
    }

    @Override
    protected void setupActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.mvc_home_title);
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvc_activity_home;
    }

    @Override
    protected void findViewOnCreate() {

    }

    @Override
    protected boolean parseIntentData() {
        return false;
    }

    @Override
    protected void init() {

    }
}
