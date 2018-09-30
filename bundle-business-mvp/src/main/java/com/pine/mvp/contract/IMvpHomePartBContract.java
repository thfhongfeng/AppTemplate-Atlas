package com.pine.mvp.contract;

import com.pine.base.mvp.contract.IBaseContract;
import com.pine.mvp.bean.MvpHomePartBEntity;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpHomePartBContract {
    interface Ui extends IBaseContract.Ui {
        void setHomePartBListAdapter(List<MvpHomePartBEntity> data, boolean refresh);

        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadHomePartBListData(boolean refresh, int pageNo, int pageSize);
    }
}
