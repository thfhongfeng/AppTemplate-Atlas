package com.pine.login.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface ILoginContract {
    interface Ui extends IBaseContract.Ui {
        void showLoginResultToast(String msg);

        String getUserMobile();

        String getUserPassword();
    }

    interface Presenter extends IBaseContract.Presenter {
        void login();
    }
}
