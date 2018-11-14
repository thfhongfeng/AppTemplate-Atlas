package com.pine.mvp.contract;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.component.editor.bean.EditorItemData;
import com.pine.base.component.uploader.ui.UploadFileLinearLayout;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpTravelNoteReleaseContract {
    interface Ui extends IBaseContract.Ui {
        void onDayCountSet(int dayCount, List<List<EditorItemData>> dayList);

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

        void setSwipeRefreshLayoutRefresh(boolean processing);
    }

    interface Presenter extends IBaseContract.Presenter {
        @NonNull
        UploadFileLinearLayout.OneByOneUploadAdapter getUploadAdapter();

        void addNote();
    }
}
