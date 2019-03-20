package com.pine.mvp.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.component.map.MapSdkManager;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.contract.IMvpShopDetailContract;
import com.pine.mvp.model.IMvpShopModel;
import com.pine.mvp.model.MvpModelFactory;
import com.pine.mvp.ui.activity.MvpTravelNoteListActivity;
import com.pine.mvp.ui.activity.MvpWebViewActivity;
import com.pine.tool.util.DecimalUtils;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpShopDetailPresenter extends BasePresenter<IMvpShopDetailContract.Ui>
        implements IMvpShopDetailContract.Presenter {
    private String mId;
    private IMvpShopModel mShopModel;
    private MvpShopDetailEntity mShopDetailEntity;

    public MvpShopDetailPresenter() {
        mShopModel = MvpModelFactory.getMvpShopModel();
    }

    @Override
    public boolean parseIntentData(Bundle bundle) {
        mId = bundle.getString("id", "");
        if (TextUtils.isEmpty(mId)) {
            finishUi();
            return true;
        }
        return false;
    }

    @Override
    public void loadShopDetailData() {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("id", mId);
        setUiLoading(true);
        mShopModel.requestShopDetailData(params, new IModelAsyncResponse<MvpShopDetailEntity>() {
            @Override
            public void onResponse(MvpShopDetailEntity entity) {
                setUiLoading(false);
                if (isUiAlive()) {
                    mShopDetailEntity = entity;
                    getUi().setupShopDetail(entity);
                }
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

    @Override
    public void showMarkerInMap() {
        if (mShopDetailEntity == null ||
                TextUtils.isEmpty(mShopDetailEntity.getLatitude()) ||
                TextUtils.isEmpty(mShopDetailEntity.getLongitude())) {
            return;
        }
        getContext().startActivity(MapSdkManager.getInstance().getMarkMapActivityIntent(
                getContext(),
                DecimalUtils.format(mShopDetailEntity.getLatitude().trim(), 6),
                DecimalUtils.format(mShopDetailEntity.getLongitude().trim(), 6), false));
    }

    @Override
    public void goToShopH5Activity() {
        Intent intent = new Intent(getContext(), MvpWebViewActivity.class);
        intent.putExtra("url", MvpUrlConstants.H5_DefaultUrl);
        getContext().startActivity(intent);
    }

    @Override
    public void goToTravelNoteListActivity() {
        Intent intent = new Intent(getContext(), MvpTravelNoteListActivity.class);
        intent.putExtra("id", mId);
        getContext().startActivity(intent);
    }
}
