package com.pine.mvp.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.mvp.adapter.MvpHomeItemNoPaginationAdapter;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpHomePartCContract {
    interface Ui extends IBaseContract.Ui {
        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadHomePartCListData();

        MvpHomeItemNoPaginationAdapter getRecycleViewAdapter();
    }
}
