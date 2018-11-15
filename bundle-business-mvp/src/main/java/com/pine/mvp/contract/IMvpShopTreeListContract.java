package com.pine.mvp.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.mvp.adapter.MvpShopListPaginationTreeAdapter;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpShopTreeListContract {
    interface Ui extends IBaseContract.Ui {
        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadShopTreeListData(boolean refresh);

        MvpShopListPaginationTreeAdapter getListAdapter();
    }
}
