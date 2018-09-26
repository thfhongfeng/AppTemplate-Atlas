package com.pine.login.contract;

import com.pine.base.mvp.contract.IBaseContract;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface ILoginContract {
    interface Ui extends IBaseContract.Ui {
        void showLoginResultToast(String msg);
    }

    interface Presenter extends IBaseContract.Presenter {

    }
}
