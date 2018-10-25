package com.pine.base.access.executor;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.pine.base.BaseApplication;
import com.pine.base.access.IUiAccessExecutor;
import com.pine.router.RouterCommand;
import com.pine.router.RouterFactory;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class UiAccessLoginExecutor implements IUiAccessExecutor {
    private int mForbiddenToastResId = -1;

    public UiAccessLoginExecutor(int forbiddenToastResId) {
        mForbiddenToastResId = forbiddenToastResId;
    }

    @Override
    public boolean onExecute(Activity activity, String args) {
        boolean canAccess = BaseApplication.isLogin();
        if (!canAccess) {
            if (mForbiddenToastResId > 0) {
                Toast.makeText(activity, mForbiddenToastResId, Toast.LENGTH_SHORT).show();
            }
            RouterFactory.getLoginBundleManager().callUiCommand(BaseApplication.mCurResumedActivity,
                    RouterCommand.LOGIN_goLoginActivity, null, null);
        }
        return canAccess;
    }

    @Override
    public boolean onExecute(Fragment fragment, String args) {
        return true;
    }
}
