package com.pine.mvp.model.local;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.mvp.bean.MvpShopAndProductEntity;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.mvp.model.IMvpShopModel;

import java.util.ArrayList;
import java.util.Map;

public class MvpLocalShopModel implements IMvpShopModel {
    @Override
    public void requestAddShop(Map<String, String> params,
                               @NonNull IModelAsyncResponse<MvpShopDetailEntity> callback) {

    }

    @Override
    public void requestShopDetailData(Map<String, String> params,
                                      @NonNull IModelAsyncResponse<MvpShopDetailEntity> callback) {

    }

    @Override
    public void requestShopListData(Map<String, String> params,
                                    @NonNull IModelAsyncResponse<ArrayList<MvpShopItemEntity>> callback) {

    }

    @Override
    public void requestShopAndProductListData(Map<String, String> params,
                                              @NonNull IModelAsyncResponse<ArrayList<MvpShopAndProductEntity>> callback) {

    }
}
