package com.pine.mvp.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.mvp.adapter.MvpTravelNoteListPaginationAdapter;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpTravelNoteListContract {
    interface Ui extends IBaseContract.Ui {
        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadTravelNoteListData(boolean refresh);

        MvpTravelNoteListPaginationAdapter getListAdapter();

        void goToAddTravelNoteActivity();
    }
}
