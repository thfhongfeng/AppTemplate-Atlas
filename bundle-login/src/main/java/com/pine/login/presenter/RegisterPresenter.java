package com.pine.login.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.bean.BaseInputParam;
import com.pine.login.LoginConstants;
import com.pine.login.R;
import com.pine.login.bean.AccountBean;
import com.pine.login.contract.IRegisterContract;
import com.pine.login.model.AccountModel;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/11/15
 */

public class RegisterPresenter extends BasePresenter<IRegisterContract.Ui>
        implements IRegisterContract.Presenter {
    private AccountModel mAccountModel = new AccountModel();

    @Override
    public void register() {
        if (mIsLoadProcessing) {
            return;
        }
        BaseInputParam<String> mobileBean = getUi().getUserMobileParam(LoginConstants.LOGIN_MOBILE);
        BaseInputParam<String> pwdBean = getUi().getUserPasswordParam(LoginConstants.LOGIN_PASSWORD);
        if (mobileBean.checkIsEmpty(R.string.login_input_empty_msg) ||
                pwdBean.checkIsEmpty(R.string.login_input_empty_msg) ||
                !mobileBean.checkIsPhone(R.string.login_mobile_incorrect_format)) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(mobileBean.getKey(), mobileBean.getValue());
        params.put(pwdBean.getKey(), pwdBean.getValue());
        setUiLoading(true);
        mAccountModel.requestRegister(params, new IModelAsyncResponse<AccountBean>() {
            @Override
            public void onResponse(AccountBean accountBean) {
                setUiLoading(false);
                showShortToast(R.string.login_register_success);
                finishUi();
            }

            @Override
            public boolean onFail(Exception e) {
                setUiLoading(false);
                showShortToast(R.string.login_register_fail);
                return false;
            }

            @Override
            public void onCancel() {
                setUiLoading(false);
                showShortToast(R.string.login_register_fail);
            }
        });
    }
}
