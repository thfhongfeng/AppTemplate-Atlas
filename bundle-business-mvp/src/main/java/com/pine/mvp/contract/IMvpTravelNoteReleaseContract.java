package com.pine.mvp.contract;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.contract.IBaseContract;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpTravelNoteReleaseContract {
    interface Ui extends IBaseContract.Ui {
        void setSwipeRefreshLayoutRefresh(boolean processing);

        @NonNull
        InputParamBean getNoteTitleParam(String key);

        @NonNull
        InputParamBean getNoteSetOutDateParam(String key);

        @NonNull
        InputParamBean getNoteTravelDayCountParam(String key);

        @NonNull
        InputParamBean getNoteBelongShopsParam(String key);

        @NonNull
        InputParamBean getNoteBelongShopNamesParam(String key);

        @NonNull
        InputParamBean getNotePrefaceParam(String key);

        @NonNull
        InputParamBean getNoteContentParam(String key);
    }

    interface Presenter extends IBaseContract.Presenter {
        @NonNull
        HashMap<String, String> makeUploadDefaultParams();

        void addNote();
    }
}
