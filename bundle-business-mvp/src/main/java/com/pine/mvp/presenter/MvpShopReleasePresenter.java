package com.pine.mvp.presenter;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.bean.BaseInputParam;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.contract.IMvpShopReleaseContract;
import com.pine.mvp.model.IMvpShopModel;
import com.pine.mvp.model.MvpModelFactory;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpShopReleasePresenter extends BasePresenter<IMvpShopReleaseContract.Ui>
        implements IMvpShopReleaseContract.Presenter {
    private IMvpShopModel mShopModel;

    public MvpShopReleasePresenter() {
        mShopModel = MvpModelFactory.getMvpShopModel();
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

        BaseInputParam<String> name = getUi().getShopNameParam("name");
        if (name.checkIsEmpty(R.string.mvp_shop_release_name_need)) {
            return;
        } else {
            params.put(name.getKey(), name.getValue());
        }

        BaseInputParam<String> type = getUi().getShopTypeParam("type");
        if (type.checkIsEmpty(R.string.mvp_shop_release_type_need)) {
            return;
        } else {
            params.put(type.getKey(), type.getValue());
        }

        BaseInputParam<String> typeName = getUi().getShopTypeNameParam("typeName");
        if (typeName.checkIsEmpty(R.string.mvp_shop_release_type_need)) {
            return;
        } else {
            params.put(typeName.getKey(), typeName.getValue());
        }

        BaseInputParam<String> onlineDate = getUi().getShopOnlineDateParam("onlineDate");
        if (onlineDate.checkIsEmpty(R.string.mvp_shop_release_online_date_need)) {
            return;
        } else {
            params.put(onlineDate.getKey(), onlineDate.getValue());
        }

        BaseInputParam<String> mobile = getUi().getShopContactMobileParam("mobile");
        if (mobile.checkIsEmpty(R.string.mvp_shop_release_contact_need) ||
                !mobile.checkIsPhone(R.string.mvp_shop_release_mobile_incorrect_format)) {
            return;
        } else {
            params.put(mobile.getKey(), mobile.getValue());
        }

        BaseInputParam<String> address = getUi().getShopAddressParam("address");
        if (address.checkIsEmpty(R.string.mvp_shop_release_address_need)) {
            return;
        } else {
            params.put(address.getKey(), address.getValue());
        }

        BaseInputParam<String> addressZipCode = getUi().getShopAddressZipCodeParam("addressZipCode");
        if (addressZipCode.checkIsEmpty(R.string.mvp_shop_release_address_need)) {
            return;
        } else {
            params.put(addressZipCode.getKey(), addressZipCode.getValue());
        }

        BaseInputParam<String> latitude = getUi().getShopLocationLatParam("latitude");
        if (latitude.checkIsEmpty(R.string.mvp_shop_release_address_location_need)) {
            return;
        } else {
            params.put(latitude.getKey(), latitude.getValue());
        }

        BaseInputParam<String> longitude = getUi().getShopLocationLonParam("longitude");
        if (longitude.checkIsEmpty(R.string.mvp_shop_release_address_location_need)) {
            return;
        } else {
            params.put(longitude.getKey(), longitude.getValue());
        }

        params.put("detailAddress", getUi().getShopDetailAddressParam("detailAddress").getValue().toString());
        params.put("description", getUi().getShopDescriptionParam("description").getValue().toString());
        params.put("remark", getUi().getShopRemarkParam("remark").getValue().toString());

        BaseInputParam<String> images = getUi().getShopImagesParam("imgUrls");
        if (images.checkIsEmpty(R.string.mvp_shop_release_photo_image_need)) {
            return;
        } else {
            params.put(images.getKey(), images.getValue());
        }

        setUiLoading(true);
        mShopModel.requestAddShop(params, new IModelAsyncResponse<MvpShopDetailEntity>() {
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
