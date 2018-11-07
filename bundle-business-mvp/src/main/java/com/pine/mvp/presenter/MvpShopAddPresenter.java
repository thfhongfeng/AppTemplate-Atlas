package com.pine.mvp.presenter;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.contract.IMvpShopAddContract;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpShopAddPresenter extends BasePresenter<IMvpShopAddContract.Ui>
        implements IMvpShopAddContract.Presenter {

    @Override
    public boolean parseIntentDataOnCreate() {
        return false;
    }

    @Override
    public void onUiState(int state) {

    }

    @NonNull
    @Override
    public String[] getShopTypeNameArr() {
        return getContext().getResources().getStringArray(R.array.mvp_shop_type);
    }

    @NonNull
    @Override
    public HashMap<String, String> makeUploadParams() {
        HashMap<String, String> params = new HashMap<>();
        // Test code begin
        params.put("bizType", "10");
        params.put("orderNum", "100");
        params.put("orderNum", "100");
        params.put("descr", "desc");
        params.put("fileType", "1");
        // Test code end
        return params;
    }

    @Override
    public void addShop(MvpShopDetailEntity entity) {

    }
}
