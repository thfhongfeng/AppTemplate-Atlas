package com.pine.main.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.main.bean.MainBusinessItemEntity;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMainHomeContract {
    interface Ui extends IBaseContract.Ui {
        void setBusinessBundleData(ArrayList<MainBusinessItemEntity> list);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadBusinessBundleData();
    }
}
