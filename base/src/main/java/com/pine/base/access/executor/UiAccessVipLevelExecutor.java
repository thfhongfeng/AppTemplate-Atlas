package com.pine.base.access.executor;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.pine.base.access.IUiAccessExecutor;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class UiAccessVipLevelExecutor implements IUiAccessExecutor {
    private int mForbiddenToastResId = -1;

    public UiAccessVipLevelExecutor(int forbiddenToastResId) {
        mForbiddenToastResId = forbiddenToastResId;
    }

    @Override
    public boolean onExecute(Activity activity, String args, boolean isResumeUi) {
        return true;
    }

    @Override
    public boolean onExecute(Fragment fragment, String args, boolean isResumeUi) {
        return true;
    }
}
