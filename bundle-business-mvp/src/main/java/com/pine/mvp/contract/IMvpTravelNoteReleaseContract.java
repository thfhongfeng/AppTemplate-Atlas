package com.pine.mvp.contract;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.base.bean.BaseInputParam;
import com.pine.base.component.uploader.ui.UploadFileLinearLayout;
import com.pine.mvp.bean.MvpShopItemEntity;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public interface IMvpTravelNoteReleaseContract {
    interface Ui extends IBaseContract.Ui {

        void setBelongShop(String ids, String names);

        @NonNull
        BaseInputParam getNoteTitleParam(String key);

        @NonNull
        BaseInputParam getNoteSetOutDateParam(String key);

        @NonNull
        BaseInputParam getNoteTravelDayCountParam(String key);

        @NonNull
        BaseInputParam getNoteBelongShopsParam(String key, ArrayList<MvpShopItemEntity> list);

        @NonNull
        BaseInputParam getNotePrefaceParam(String key);

        @NonNull
        BaseInputParam getNoteContentParam(String key);
    }

    interface Presenter extends IBaseContract.Presenter {
        @NonNull
        UploadFileLinearLayout.OneByOneUploadAdapter getUploadAdapter();

        void selectBelongShop();

        void onBelongShopSelected(Intent data);

        void addNote();
    }
}
