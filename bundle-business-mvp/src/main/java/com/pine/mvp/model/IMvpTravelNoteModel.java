package com.pine.mvp.model;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.mvp.bean.MvpTravelNoteCommentEntity;
import com.pine.mvp.bean.MvpTravelNoteDetailEntity;
import com.pine.mvp.bean.MvpTravelNoteItemEntity;

import java.util.ArrayList;
import java.util.Map;

public interface IMvpTravelNoteModel {
    void requestAddTravelNote(final Map<String, String> params,
                              @NonNull final IModelAsyncResponse<MvpTravelNoteDetailEntity> callback);

    void requestTravelNoteDetailData(final Map<String, String> params,
                                     @NonNull final IModelAsyncResponse<MvpTravelNoteDetailEntity> callback);

    void requestTravelNoteListData(final Map<String, String> params,
                                   @NonNull final IModelAsyncResponse<ArrayList<MvpTravelNoteItemEntity>> callback);


    void requestTravelNoteCommentData(final Map<String, String> params,
                                      @NonNull final IModelAsyncResponse<ArrayList<MvpTravelNoteCommentEntity>> callback);
}
