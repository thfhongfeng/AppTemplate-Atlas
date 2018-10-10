package com.pine.mvp.contract;

import com.pine.base.mvp.contract.IBaseContract;
import com.pine.mvp.adapter.MvpHomeItemPaginationAdapter;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpHomePartAContract {
    interface Ui extends IBaseContract.Ui {
        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadHomePartAListData(boolean refresh);

        MvpHomeItemPaginationAdapter getRecycleViewAdapter();
    }
}
