package com.pine.main.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMainHomeContract {
    interface Ui extends IBaseContract.Ui {
        void setBusinessBundleAdapter(String[] names);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadBusinessBundleData();

        void onBusinessItemClick(int position);
    }
}
