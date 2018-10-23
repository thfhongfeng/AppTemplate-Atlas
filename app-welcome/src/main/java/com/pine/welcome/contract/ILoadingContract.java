package com.pine.welcome.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface ILoadingContract {
    interface Ui extends IBaseContract.Ui {
        void showVersionUpdateConfirmDialog(String newVersionName);

        void showVersionUpdateProgressDialog();

        void updateVersionUpdateProgressDialog(int progress);

        void dismissVersionUpdateProgressDialog();

        void showVersionUpdateToast(String msg);
    }

    interface Presenter extends IBaseContract.Presenter {

        void loadBundleSwitcherData();

        void updateVersion();

        void autoLogin();
    }
}
