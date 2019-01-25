package com.pine.welcome.presenter;

import android.os.Bundle;
import android.os.Handler;

import com.pine.base.BaseApplication;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.router.IRouterCallback;
import com.pine.router.RouterBundleKey;
import com.pine.router.command.RouterCommand;
import com.pine.router.manager.RouterManager;
import com.pine.tool.util.LogUtils;
import com.pine.welcome.contract.IWelcomeContract;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class WelcomePresenter extends BasePresenter<IWelcomeContract.Ui> implements IWelcomeContract.Presenter {

    public WelcomePresenter() {

    }

    @Override
    public boolean parseIntentData() {
        return false;
    }

    @Override
    public void onUiState(BasePresenter.UiState state) {

    }

    @Override
    public void goMainHomeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RouterManager.getInstance(RouterBundleKey.MAIN_BUNDLE_KEY).callUiCommand(BaseApplication.mCurResumedActivity,
                        RouterCommand.MAIN_goMainHomeActivity, null, new IRouterCallback() {
                            @Override
                            public void onSuccess(Bundle responseBundle) {
                                LogUtils.d(TAG, "onSuccess " + RouterCommand.MAIN_goMainHomeActivity);
                                finishUi();
                                return;
                            }

                            @Override
                            public boolean onFail(String errorInfo) {
                                return false;
                            }
                        });

            }
        }, 2000);
    }
}
