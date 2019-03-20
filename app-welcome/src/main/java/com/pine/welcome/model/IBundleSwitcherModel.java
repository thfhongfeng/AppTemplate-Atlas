package com.pine.welcome.model;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.welcome.bean.BundleSwitcherEntity;

import java.util.ArrayList;

public interface IBundleSwitcherModel {
    boolean requestBundleSwitcherData(@NonNull IModelAsyncResponse<ArrayList<BundleSwitcherEntity>> callback);
}
