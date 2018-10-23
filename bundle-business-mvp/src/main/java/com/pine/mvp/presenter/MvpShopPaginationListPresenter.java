package com.pine.mvp.presenter;

import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.location.BdLocationManager;
import com.pine.mvp.adapter.MvpShopItemPaginationAdapter;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.mvp.contract.IMvpShopPaginationContract;
import com.pine.mvp.model.MvpHomeShopModel;
import com.pine.tool.util.GPSUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopPaginationListPresenter extends BasePresenter<IMvpShopPaginationContract.Ui>
        implements IMvpShopPaginationContract.Presenter {
    private String mId;
    private MvpHomeShopModel mModel;
    private MvpShopItemPaginationAdapter mMvpHomeItemAdapter;
    private boolean mIsLoadProcessing;
    private BDAbstractLocationListener mLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null && (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                    || bdLocation.getLocType() == BDLocation.TypeOffLineLocation)) {
                BdLocationManager.getInstance().unregisterListener(mLocationListener);
                BdLocationManager.getInstance().stop();
                BdLocationManager.getInstance().setLocation(bdLocation);
                loadShopPaginationListData(true);
            }
        }
    };

    public MvpShopPaginationListPresenter() {
        mModel = new MvpHomeShopModel();
    }


    @Override
    public boolean initDataOnUiCreate() {
        mId = getStringExtra("id", "");
        return false;
    }

    @Override
    public void onUiState(int state) {
        switch (state) {
            case UI_STATE_ON_CREATE:
                break;
            case UI_STATE_ON_RESUME:
                if (BdLocationManager.getInstance().getLocation() == null) {
                    BdLocationManager.getInstance().registerListener(mLocationListener);
                    BdLocationManager.getInstance().start();
                }
                break;
            case UI_STATE_ON_PAUSE:
                break;
            case UI_STATE_ON_STOP:
                BdLocationManager.getInstance().unregisterListener(mLocationListener);
                BdLocationManager.getInstance().stop();
                break;
            case UI_STATE_ON_DETACH:
                break;
        }
    }

    @Override
    public MvpShopItemPaginationAdapter getRecycleViewAdapter() {
        if (mMvpHomeItemAdapter == null) {
            mMvpHomeItemAdapter = new MvpShopItemPaginationAdapter(
                    MvpShopItemPaginationAdapter.HOME_SHOP_VIEW_HOLDER);
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
            pageNo = mMvpHomeItemAdapter.getPageNo() + 1;
        }
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(mMvpHomeItemAdapter.getPageSize()));
        params.put("id", mId);
        BDLocation location = BdLocationManager.getInstance().getLocation();
        if (location != null) {
            double[] locations = GPSUtils.bd09_To_gps84(location.getLatitude(), location.getLongitude());
            params.put("latitude", String.valueOf(locations[0]));
            params.put("longitude", String.valueOf(locations[1]));
        }
        startDataLoadUi();
        mModel.requestShopListData(params, new IModelAsyncResponse<ArrayList<MvpShopItemEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopItemEntity> list) {
                finishDataLoadUi();
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
                finishDataLoadUi();
                if (e instanceof JSONException) {
                    if (isUiAlive()) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                    }
                }
                return false;
            }
        });
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
