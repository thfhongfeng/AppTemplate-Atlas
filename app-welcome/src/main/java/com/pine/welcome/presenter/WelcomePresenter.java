package com.pine.welcome.presenter;

import android.os.Bundle;
import android.os.Handler;

import com.pine.base.BaseApplication;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.router.IRouterCallback;
import com.pine.router.command.RouterMainCommand;
import com.pine.router.impl.RouterManager;
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
                RouterManager.getMainRouter().callUiCommand(BaseApplication.mCurResumedActivity,
                        RouterMainCommand.goMainHomeActivity, null, new IRouterCallback() {
                            @Override
                            public void onSuccess(Bundle responseBundle) {
                                LogUtils.d(TAG, "onSuccess " + RouterMainCommand.goMainHomeActivity);
                                finishUi();
                                return;
                            }

                            @Override
                            public boolean onFail(int failCode, String errorInfo) {
                                return false;
                            }
                        });

            }
        }, 2000);
    }
}
