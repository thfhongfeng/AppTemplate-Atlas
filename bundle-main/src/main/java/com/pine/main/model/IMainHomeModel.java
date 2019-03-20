package com.pine.main.model;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.main.bean.MainBusinessItemEntity;

import java.util.ArrayList;

public interface IMainHomeModel {
    void requestBusinessListData(@NonNull final IModelAsyncResponse<ArrayList<MainBusinessItemEntity>> callback);
}
