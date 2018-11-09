package com.pine.login.presenter;

import android.widget.Toast;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.login.LoginConstants;
import com.pine.login.R;
import com.pine.login.contract.ILoginContract;
import com.pine.login.manager.LoginManager;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class LoginPresenter extends BasePresenter<ILoginContract.Ui> implements ILoginContract.Presenter {

    @Override
    public boolean parseIntentDataOnCreate() {
        return false;
    }

    @Override
    public void onUiState(int state) {

    }

    @Override
    public void login() {
        InputParamBean mobileBean = getUi().getUserMobileParam(LoginConstants.LOGIN_MOBILE);
        InputParamBean pwdBean = getUi().getUserPasswordParam(LoginConstants.LOGIN_PASSWORD);
        if (mobileBean.checkIsEmpty(R.string.login_input_empty_msg) ||
                pwdBean.checkIsEmpty(R.string.login_input_empty_msg) ||
                !mobileBean.checkIsPhone(R.string.login_mobile_incorrect_format)) {
            return;
        }
        LoginManager.login(mobileBean.getValue(), pwdBean.getValue(), new LoginManager.Callback() {
            @Override
            public void onLoginResponse(boolean isSuccess, String msg) {
                if (isUiAlive()) {
                    if (!isSuccess) {
                        Toast.makeText(getContext(), R.string.login_login_fail, Toast.LENGTH_SHORT).show();
                    } else {
                        finishUi();
                    }
                }
            }
        });
    }
}
