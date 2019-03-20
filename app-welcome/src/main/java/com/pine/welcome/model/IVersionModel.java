package com.pine.welcome.model;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.welcome.bean.VersionEntity;

public interface IVersionModel {
    boolean requestUpdateVersionData(@NonNull IModelAsyncResponse<VersionEntity> callback);
}
