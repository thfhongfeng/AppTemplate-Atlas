package com.pine.mvp.presenter;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.contract.IMvpShopAddContract;
import com.pine.mvp.model.MvpShopModel;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpShopAddPresenter extends BasePresenter<IMvpShopAddContract.Ui>
        implements IMvpShopAddContract.Presenter {
    private MvpShopModel mModel;
    private boolean mIsLoadProcessing;


    public MvpShopAddPresenter() {
        mModel = new MvpShopModel();
    }

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
    public void addShop() {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();

        InputParamBean nameBean = getUi().getShopNameParam("name");
        if (nameBean.checkIsEmpty(R.string.mvp_shop_add_name_need)) {
            return;
        } else {
            params.put("name", nameBean.getValue());
        }

        InputParamBean typeBean = getUi().getShopTypeParam("type");
        if (typeBean.checkIsEmpty(R.string.mvp_shop_add_type_need)) {
            return;
        } else {
            params.put("type", typeBean.getValue());
        }

        InputParamBean typeNameBean = getUi().getShopTypeNameParam("typeName");
        if (typeNameBean.checkIsEmpty(R.string.mvp_shop_add_type_need)) {
            return;
        } else {
            params.put("typeName", typeNameBean.getValue());
        }

        InputParamBean onlineDateBean = getUi().getShopOnlineDateParam("onlineDate");
        if (onlineDateBean.checkIsEmpty(R.string.mvp_shop_add_online_date_need)) {
            return;
        } else {
            params.put("onlineDate", onlineDateBean.getValue());
        }

        InputParamBean mobileBean = getUi().getShopContactMobileParam("mobile");
        if (mobileBean.checkIsEmpty(R.string.mvp_shop_add_contact_need) ||
                !mobileBean.checkIsPhone(R.string.mvp_shop_add_mobile_incorrect_format)) {
            return;
        } else {
            params.put("mobile", mobileBean.getValue());
        }

        InputParamBean addressBean = getUi().getShopAddressParam("address");
        if (addressBean.checkIsEmpty(R.string.mvp_shop_add_address_need)) {
            return;
        } else {
            params.put("address", addressBean.getValue());
        }

        InputParamBean addressZipCodeBean = getUi().getShopAddressZipCodeParam("addressZipCode");
        if (addressZipCodeBean.checkIsEmpty(R.string.mvp_shop_add_address_need)) {
            return;
        } else {
            params.put("addressZipCode", addressZipCodeBean.getValue());
        }

        InputParamBean locationBean = getUi().getShopLocationParam("location");
        if (locationBean.checkIsEmpty(R.string.mvp_shop_add_address_location_need)) {
            return;
        } else {
            params.put("location", locationBean.getValue());
        }

        params.put("detailAddress", getUi().getShopDetailAddressParam("detailAddress").getValue());
        params.put("description", getUi().getShopDescriptionParam("description").getValue());
        params.put("remark", getUi().getShopRemarkParam("remark").getValue());

        InputParamBean imagesBean = getUi().getShopImagesParam("images");
        if (imagesBean.checkIsEmpty(R.string.mvp_shop_add_photo_image_need)) {
            return;
        } else {
            params.put("images", imagesBean.getValue());
        }

        startDataLoadUi();
        if (!mModel.requestAddShop(params, new IModelAsyncResponse<MvpShopDetailEntity>() {
            @Override
            public void onResponse(MvpShopDetailEntity entity) {
                finishDataLoadUi();
                if (isUiAlive()) {
                    Toast.makeText(getContext(), R.string.mvp_shop_add_success, Toast.LENGTH_SHORT).show();
                    finishUi();
                    return;
                }
            }

            @Override
            public boolean onFail(Exception e) {
                finishDataLoadUi();
                return false;
            }
        })) {
            finishDataLoadUi();
        }
    }

    private void startDataLoadUi() {
        mIsLoadProcessing = true;
        if (isUiAlive()) {
            getUi().setSwipeRefreshLayoutRefresh(true);
        }
    }

    private void finishDataLoadUi() {
        mIsLoadProcessing = false;
        if (isUiAlive()) {
            getUi().setSwipeRefreshLayoutRefresh(false);
        }
    }
}
