package com.pine.mvp.model.local;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.mvp.bean.MvpTravelNoteCommentEntity;
import com.pine.mvp.bean.MvpTravelNoteDetailEntity;
import com.pine.mvp.bean.MvpTravelNoteItemEntity;
import com.pine.mvp.model.IMvpTravelNoteModel;

import java.util.ArrayList;
import java.util.Map;

public class MvpLocalTravelNoteModel implements IMvpTravelNoteModel {
    @Override
    public void requestAddTravelNote(Map<String, String> params,
                                     @NonNull IModelAsyncResponse<MvpTravelNoteDetailEntity> callback) {

    }

    @Override
    public void requestTravelNoteDetailData(Map<String, String> params,
                                            @NonNull IModelAsyncResponse<MvpTravelNoteDetailEntity> callback) {

    }

    @Override
    public void requestTravelNoteListData(Map<String, String> params,
                                          @NonNull IModelAsyncResponse<ArrayList<MvpTravelNoteItemEntity>> callback) {

    }

    @Override
    public void requestTravelNoteCommentData(Map<String, String> params,
                                             @NonNull IModelAsyncResponse<ArrayList<MvpTravelNoteCommentEntity>> callback) {

    }
}
