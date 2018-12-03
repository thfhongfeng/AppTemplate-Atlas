package com.pine.login.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
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
    public boolean parseIntentData() {
        return false;
    }

    @Override
    public void onUiState(int state) {

    }

    @Override
    public void login() {
        InputParamBean<String> mobileBean = getUi().getUserMobileParam(LoginConstants.LOGIN_MOBILE);
        InputParamBean<String> pwdBean = getUi().getUserPasswordParam(LoginConstants.LOGIN_PASSWORD);
        if (mobileBean.checkIsEmpty(R.string.login_input_empty_msg) ||
                pwdBean.checkIsEmpty(R.string.login_input_empty_msg) ||
                !mobileBean.checkIsPhone(R.string.login_mobile_incorrect_format)) {
            return;
        }
        getUi().startLoadingUi();
        LoginManager.login(mobileBean.getValue(), pwdBean.getValue(), new LoginManager.Callback() {
            @Override
            public boolean onLoginResponse(boolean isSuccess, String msg) {
                if (isUiAlive()) {
                    getUi().finishLoadingUi();
                    if (!isSuccess) {
                        if (TextUtils.isEmpty(msg)) {
                            return false;
                        } else {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        finishUi();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void goRegister() {
        getContext().startActivity(new Intent(getContext(), RegisterActivity.class));
        finishUi();
    }
}
