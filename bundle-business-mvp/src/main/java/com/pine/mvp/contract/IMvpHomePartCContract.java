package com.pine.mvp.contract;

import com.pine.base.mvp.contract.IBaseContract;
import com.pine.mvp.bean.MvpHomePartCEntity;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpHomePartCContract {
    interface Ui extends IBaseContract.Ui {
        void setHomePartCListAdapter(List<MvpHomePartCEntity> data);

        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadHomePartCListData();
    }
}
