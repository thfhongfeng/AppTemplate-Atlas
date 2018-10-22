package com.pine.mvp.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.mvp.adapter.MvpTravelNoteDetailComplexAdapter;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpTravelNoteDetailContract {
    interface Ui extends IBaseContract.Ui {
        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadTravelNoteDetailData(boolean refresh);

        void loadTravelNoteCommentData(boolean refresh);

        MvpTravelNoteDetailComplexAdapter getRecycleViewAdapter();
    }
}
