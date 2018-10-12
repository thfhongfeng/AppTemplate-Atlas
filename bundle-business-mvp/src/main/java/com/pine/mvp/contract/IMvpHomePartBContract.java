package com.pine.mvp.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.mvp.adapter.MvpHomeItemPaginationComplexAdapter;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpHomePartBContract {
    interface Ui extends IBaseContract.Ui {
        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadHomePartBListData(boolean refresh);

        MvpHomeItemPaginationComplexAdapter getRecycleViewAdapter();
    }
}
