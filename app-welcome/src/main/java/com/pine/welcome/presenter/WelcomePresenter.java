package com.pine.welcome.presenter;

import android.os.Bundle;
import android.os.Handler;

import com.pine.base.BaseApplication;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.router.IRouterCallback;
import com.pine.router.RouterCommand;
import com.pine.router.RouterFactory;
import com.pine.tool.util.LogUtils;
import com.pine.welcome.contract.IWelcomeContract;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class WelcomePresenter extends BasePresenter<IWelcomeContract.Ui> implements IWelcomeContract.Presenter {

    public WelcomePresenter() {

    }

    @Override
    public boolean initDataOnUiCreate() {
        return false;
    }

    @Override
    public void onUiState(int state) {

    }

    @Override
    public void goMainHomeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RouterFactory.getMainBundleManager().callUiCommand(BaseApplication.mCurResumedActivity,
                        RouterCommand.MAIN_goMainHomeActivity, null, new IRouterCallback() {
                            @Override
                            public void onSuccess(Bundle returnBundle) {
                                LogUtils.d(TAG, "onSuccess " + RouterCommand.MAIN_goMainHomeActivity);
                                finishUi();
                                return;
                            }

                            @Override
                            public void onException(Bundle returnBundle) {
                                LogUtils.d(TAG, "onException " + RouterCommand.MAIN_goMainHomeActivity);
                            }

                            @Override
                            public void onFail(String errorInfo) {
                                LogUtils.d(TAG, "onFail " + RouterCommand.MAIN_goMainHomeActivity);
                            }
                        });

            }
        }, 2000);
    }
}
