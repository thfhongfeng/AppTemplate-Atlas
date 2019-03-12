package com.pine.login.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.pine.base.BaseApplication;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.bean.InputParamBean;
import com.pine.login.LoginConstants;
import com.pine.login.R;
import com.pine.login.contract.ILoginContract;
import com.pine.login.manager.LoginManager;
import com.pine.login.ui.activity.RegisterActivity;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class LoginPresenter extends BasePresenter<ILoginContract.Ui> implements ILoginContract.Presenter {

    @Override
    public boolean parseInitData(Bundle bundle) {
        return false;
    }

    @Override
    public void onUiState(BasePresenter.UiState state) {

    }

    @Override
    public void login() {
        if (BaseApplication.isLogin() || mIsLoadProcessing) {
            return;
        }
        InputParamBean<String> mobileBean = getUi().getUserMobileParam(LoginConstants.LOGIN_MOBILE);
        InputParamBean<String> pwdBean = getUi().getUserPasswordParam(LoginConstants.LOGIN_PASSWORD);
        if (mobileBean.checkIsEmpty(R.string.login_input_empty_msg) ||
                pwdBean.checkIsEmpty(R.string.login_input_empty_msg) ||
                !mobileBean.checkIsPhone(R.string.login_mobile_incorrect_format)) {
            return;
        }
        setUiLoading(true);
        LoginManager.login(mobileBean.getValue(), pwdBean.getValue(), new LoginManager.Callback() {
            @Override
            public boolean onLoginResponse(boolean isSuccess, String msg) {
                if (isUiAlive()) {
                    setUiLoading(false);
                    if (!isSuccess) {
                        if (TextUtils.isEmpty(msg)) {
                            return false;
                        } else {
                            showShortToast(msg);
                        }
                    } else {
                        finishUi();
                    }
                }
                return true;
            }

            @Override
            public void onCancel() {
                setUiLoading(false);
            }
        });
    }

    @Override
    public void goRegister() {
        getContext().startActivity(new Intent(getContext(), RegisterActivity.class));
        finishUi();
    }
}
