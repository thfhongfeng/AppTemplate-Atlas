package com.pine.welcome.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.widget.dialog.ProgressDialog;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface ILoadingContract {
    interface Ui extends IBaseContract.Ui {
        void showVersionUpdateConfirmDialog(String newVersionName);

        void showVersionUpdateProgressDialog(ProgressDialog.IDialogActionListener listener);

        void updateVersionUpdateProgressDialog(int progress);

        void dismissVersionUpdateProgressDialog();
    }

    interface Presenter extends IBaseContract.Presenter {

        void loadBundleSwitcherData();

        void updateVersion(boolean isForce);

        void autoLogin();
    }
}
