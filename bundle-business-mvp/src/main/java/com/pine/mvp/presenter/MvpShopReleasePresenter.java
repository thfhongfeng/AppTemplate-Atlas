package com.pine.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.bean.InputParamBean;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.contract.IMvpShopReleaseContract;
import com.pine.mvp.model.MvpShopModel;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpShopReleasePresenter extends BasePresenter<IMvpShopReleaseContract.Ui>
        implements IMvpShopReleaseContract.Presenter {
    private MvpShopModel mModel;

    public MvpShopReleasePresenter() {
        mModel = new MvpShopModel();
    }

    @Override
    public boolean parseInitData(Bundle bundle) {
        return false;
    }

    @Override
    public void onUiState(BasePresenter.UiState state) {

    }

    @NonNull
    @Override
    public String[] getShopTypeNameArr() {
        return getContext().getResources().getStringArray(R.array.mvp_shop_type);
    }

    @NonNull
    @Override
    public HashMap<String, String> makeUploadDefaultParams() {
        HashMap<String, String> params = new HashMap<>();
        // Test code begin
        params.put("bizType", "10");
        params.put("orderNum", "100");
        params.put("orderNum", "100");
        params.put("descr", "");
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

        InputParamBean<String> name = getUi().getShopNameParam("name");
        if (name.checkIsEmpty(R.string.mvp_shop_release_name_need)) {
            return;
        } else {
            params.put(name.getKey(), name.getValue());
        }

        InputParamBean<String> type = getUi().getShopTypeParam("type");
        if (type.checkIsEmpty(R.string.mvp_shop_release_type_need)) {
            return;
        } else {
            params.put(type.getKey(), type.getValue());
        }

        InputParamBean<String> typeName = getUi().getShopTypeNameParam("typeName");
        if (typeName.checkIsEmpty(R.string.mvp_shop_release_type_need)) {
            return;
        } else {
            params.put(typeName.getKey(), typeName.getValue());
        }

        InputParamBean<String> onlineDate = getUi().getShopOnlineDateParam("onlineDate");
        if (onlineDate.checkIsEmpty(R.string.mvp_shop_release_online_date_need)) {
            return;
        } else {
            params.put(onlineDate.getKey(), onlineDate.getValue());
        }

        InputParamBean<String> mobile = getUi().getShopContactMobileParam("mobile");
        if (mobile.checkIsEmpty(R.string.mvp_shop_release_contact_need) ||
                !mobile.checkIsPhone(R.string.mvp_shop_release_mobile_incorrect_format)) {
            return;
        } else {
            params.put(mobile.getKey(), mobile.getValue());
        }

        InputParamBean<String> address = getUi().getShopAddressParam("address");
        if (address.checkIsEmpty(R.string.mvp_shop_release_address_need)) {
            return;
        } else {
            params.put(address.getKey(), address.getValue());
        }

        InputParamBean<String> addressZipCode = getUi().getShopAddressZipCodeParam("addressZipCode");
        if (addressZipCode.checkIsEmpty(R.string.mvp_shop_release_address_need)) {
            return;
        } else {
            params.put(addressZipCode.getKey(), addressZipCode.getValue());
        }

        InputParamBean<String> location = getUi().getShopLocationParam("location");
        if (location.checkIsEmpty(R.string.mvp_shop_release_address_location_need)) {
            return;
        } else {
            params.put(location.getKey(), location.getValue());
        }

        params.put("detailAddress", getUi().getShopDetailAddressParam("detailAddress").getValue().toString());
        params.put("description", getUi().getShopDescriptionParam("description").getValue().toString());
        params.put("remark", getUi().getShopRemarkParam("remark").getValue().toString());

        InputParamBean<String> images = getUi().getShopImagesParam("images");
        if (images.checkIsEmpty(R.string.mvp_shop_release_photo_image_need)) {
            return;
        } else {
            params.put(images.getKey(), images.getValue());
        }

        setUiLoading(true);
        mModel.requestAddShop(params, new IModelAsyncResponse<MvpShopDetailEntity>() {
            @Override
            public void onResponse(MvpShopDetailEntity entity) {
                setUiLoading(false);
                showShortToast(R.string.mvp_shop_release_success);
                finishUi();
            }

            @Override
            public boolean onFail(Exception e) {
                setUiLoading(false);
                return false;
            }

            @Override
            public void onCancel() {
                setUiLoading(false);
            }
        });
    }
}
