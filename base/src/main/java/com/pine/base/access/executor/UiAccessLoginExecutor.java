package com.pine.base.access.executor;

import android.content.Context;

import com.pine.base.BaseApplication;
import com.pine.base.access.IUiAccessExecutor;
import com.pine.router.RouterCommand;
import com.pine.router.RouterFactory;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class UiAccessLoginExecutor implements IUiAccessExecutor {
    @Override
    public boolean onExecute(Context context, int level) {
        boolean canAccess = BaseApplication.isLogin();
        if (!canAccess) {
            RouterFactory.getLoginBundleManager().callUiCommand(BaseApplication.mCurResumedActivity,
                    RouterCommand.LOGIN_goLoginActivity, null, null);
        }
        return canAccess;
    }
}
