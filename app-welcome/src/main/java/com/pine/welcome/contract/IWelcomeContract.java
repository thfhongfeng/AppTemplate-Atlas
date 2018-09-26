package com.pine.welcome.contract;

import android.app.Activity;

import com.pine.base.mvp.contract.IBaseContract;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IWelcomeContract {
    interface Ui extends IBaseContract.Ui {

    }

    interface Presenter extends IBaseContract.Presenter {
        void goMainHomeActivity();
    }
}
