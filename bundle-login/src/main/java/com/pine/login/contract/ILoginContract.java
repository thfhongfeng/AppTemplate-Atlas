package com.pine.login.contract;

import android.support.annotation.NonNull;

import com.pine.base.bean.InputParamBean;
import com.pine.base.architecture.mvp.contract.IBaseContract;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface ILoginContract {
    interface Ui extends IBaseContract.Ui {
        @NonNull
        InputParamBean getUserMobileParam(String key);

        @NonNull
        InputParamBean getUserPasswordParam(String key);
    }

    interface Presenter extends IBaseContract.Presenter {
        void login();

        void goRegister();
    }
}
