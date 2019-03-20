package com.pine.mvp.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.component.map.ILocationListener;
import com.pine.base.component.map.LocationInfo;
import com.pine.base.component.map.MapSdkManager;
import com.pine.mvp.MvpConstants;
import com.pine.mvp.adapter.MvpShopListPaginationAdapter;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.mvp.contract.IMvpShopPaginationContract;
import com.pine.mvp.model.IMvpShopModel;
import com.pine.mvp.model.MvpModelFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopPaginationListPresenter extends BasePresenter<IMvpShopPaginationContract.Ui>
        implements IMvpShopPaginationContract.Presenter {
    private IMvpShopModel mShopModel;
    private MvpShopListPaginationAdapter mMvpHomeItemAdapter;

    private ILocationListener mLocationListener = new ILocationListener() {
        @Override
        public void onReceiveLocation(LocationInfo locationInfo) {
            loadShopPaginationListData(true);
        }

        @Override
        public void onReceiveFail() {

        }
    };

    public MvpShopPaginationListPresenter() {
        mShopModel = MvpModelFactory.getMvpShopModel();
    }

    @Override
    public void onUiState(BasePresenter.UiState state) {
        super.onUiState(state);
        switch (state) {
            case UI_STATE_ON_CREATE:
                break;
            case UI_STATE_ON_RESUME:
                if (MapSdkManager.getInstance().getLocation() == null) {
                    MapSdkManager.getInstance().registerLocationListener(mLocationListener);
                    MapSdkManager.getInstance().startLocation();
                }
                break;
            case UI_STATE_ON_PAUSE:
                break;
            case UI_STATE_ON_STOP:
                MapSdkManager.getInstance().unregisterLocationListener(mLocationListener);
                MapSdkManager.getInstance().stopLocation();
                break;
            case UI_STATE_ON_DETACH:
                break;
        }
    }

    @Override
    public MvpShopListPaginationAdapter getListAdapter() {
        if (mMvpHomeItemAdapter == null) {
            mMvpHomeItemAdapter = new MvpShopListPaginationAdapter(
                    MvpShopListPaginationAdapter.SHOP_VIEW_HOLDER);
        }
        return mMvpHomeItemAdapter;
    }

    @Override
    public void loadShopPaginationListData(final boolean refresh) {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        int pageNo = 1;
        if (!refresh) {
            pageNo = mMvpHomeItemAdapter.getNextPageNo();
        }
        params.put(MvpConstants.PAGE_NO, String.valueOf(pageNo));
        params.put(MvpConstants.PAGE_SIZE, String.valueOf(mMvpHomeItemAdapter.getPageSize()));
        LocationInfo location = MapSdkManager.getInstance().getLocation();
        if (location != null) {
            params.put("latitude", String.valueOf(location.getLatitude()));
            params.put("longitude", String.valueOf(location.getLongitude()));
        }
        setUiLoading(true);
        mShopModel.requestShopListData(params, new IModelAsyncResponse<ArrayList<MvpShopItemEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopItemEntity> list) {
                setUiLoading(false);
                if (isUiAlive()) {
                    if (refresh) {
                        mMvpHomeItemAdapter.setData(list);
                    } else {
                        mMvpHomeItemAdapter.addData(list);
                    }
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
}
