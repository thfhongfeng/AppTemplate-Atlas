package com.pine.mvp.model;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.mvp.bean.MvpShopAndProductEntity;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.bean.MvpShopItemEntity;

import java.util.ArrayList;
import java.util.Map;

public interface IMvpShopModel {
    void requestAddShop(final Map<String, String> params,
                        @NonNull final IModelAsyncResponse<MvpShopDetailEntity> callback);

    void requestShopDetailData(final Map<String, String> params,
                               @NonNull final IModelAsyncResponse<MvpShopDetailEntity> callback);

    void requestShopListData(final Map<String, String> params,
                             @NonNull final IModelAsyncResponse<ArrayList<MvpShopItemEntity>> callback);

    void requestShopAndProductListData(Map<String, String> params,
                                       @NonNull final IModelAsyncResponse<ArrayList<MvpShopAndProductEntity>> callback);
}
