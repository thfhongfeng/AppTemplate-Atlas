package com.pine.mvp.contract;

import com.pine.base.bean.InputParamBean;
import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.mvp.adapter.MvpShopCheckListPaginationAdapter;

/**
 * Created by tanghongfeng on 2018/11/15
 */

public interface IMvpShopSearchCheckContract {
    interface Ui extends IBaseContract.Ui {
        InputParamBean getSearchKey(String key);

        void goAllSelectMode();

        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        MvpShopCheckListPaginationAdapter getListAdapter();

        void postSearch(boolean refresh);

        void completeAction();

        void clearCurCheck();
    }
}
