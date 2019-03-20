package com.pine.mvp.contract;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.mvp.bean.MvpShopDetailEntity;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpShopDetailContract {
    interface Ui extends IBaseContract.Ui {
        void setupShopDetail(MvpShopDetailEntity entity);
    }

    interface Presenter extends IBaseContract.Presenter {
        void loadShopDetailData();

        void showMarkerInMap();

        void goToShopH5Activity();

        void goToTravelNoteListActivity();
    }
}
