package com.pine.mvp.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.bean.BaseInputParam;
import com.pine.mvp.adapter.MvpShopCheckListPaginationAdapter;

/**
 * Created by tanghongfeng on 2018/11/15
 */

public interface IMvpShopSearchCheckContract {
    interface Ui extends IBaseContract.Ui {
        BaseInputParam getSearchKey(String key);

        void goAllSelectMode();
    }

    interface Presenter extends IBaseContract.Presenter {
        MvpShopCheckListPaginationAdapter getListAdapter();

        void postSearch(boolean refresh);

        void completeAction();

        void clearCurCheck();
    }
}
