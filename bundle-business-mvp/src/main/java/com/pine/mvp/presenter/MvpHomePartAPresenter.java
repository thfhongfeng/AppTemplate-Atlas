package com.pine.mvp.presenter;

import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.location.BdLocationManager;
import com.pine.mvp.adapter.MvpHomeItemPaginationAdapter;
import com.pine.mvp.bean.MvpShopEntity;
import com.pine.mvp.contract.IMvpHomePartAContract;
import com.pine.mvp.model.MvpHomeModel;
import com.pine.tool.util.GPSUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomePartAPresenter extends BasePresenter<IMvpHomePartAContract.Ui>
        implements IMvpHomePartAContract.Presenter {
    private MvpHomeModel mModel;
    private MvpHomeItemPaginationAdapter mMvpHomeItemAdapter;
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
                loadHomePartAListData(true);
            }
        }
    };

    public MvpHomePartAPresenter() {
        mModel = new MvpHomeModel();
    }

    @Override
    public MvpHomeItemPaginationAdapter getRecycleViewAdapter() {
        if (mMvpHomeItemAdapter == null) {
            mMvpHomeItemAdapter = new MvpHomeItemPaginationAdapter(
                    MvpHomeItemPaginationAdapter.HOME_SHOP_VIEW_HOLDER, false);
        }
        return mMvpHomeItemAdapter;
    }

    @Override
    public void loadHomePartAListData(final boolean refresh) {
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
        BDLocation location = BdLocationManager.getInstance().getLocation();
        if (location != null) {
            double[] locations = GPSUtils.bd09_To_gps84(location.getLatitude(), location.getLongitude());
            params.put("latitude", String.valueOf(locations[0]));
            params.put("longitude", String.valueOf(locations[1]));
        }
        mIsLoadProcessing = true;
        mModel.requestShopListData(params, new IModelAsyncResponse<ArrayList<MvpShopEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopEntity> homeShopItemEntity) {
                if (isUiAlive()) {
                    if (refresh) {
                        mMvpHomeItemAdapter.setData(homeShopItemEntity);
                    } else {
                        mMvpHomeItemAdapter.addData(homeShopItemEntity);
                    }
                    mMvpHomeItemAdapter.notifyDataSetChanged();
                }
                finishDataLoad();
            }

            @Override
            public boolean onFail(Exception e) {
                if (e instanceof JSONException) {
                    if (isUiAlive()) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                    }
                }
                finishDataLoad();
                return false;
            }
        });
    }

    private void finishDataLoad() {
        mIsLoadProcessing = false;
        if (isUiAlive()) {
            getUi().setSwipeRefreshLayoutRefresh(false);
        }
    }

    @Override
    public void onUiState(int state) {
        switch (state) {
            case UI_STATE_ON_ATTACH:
                break;
            case UI_STATE_ON_SHOW:
                if (BdLocationManager.getInstance().getLocation() == null) {
                    BdLocationManager.getInstance().registerListener(mLocationListener);
                    BdLocationManager.getInstance().start();
                }
                break;
            case UI_STATE_ON_PAUSE:
                break;
            case UI_STATE_ON_HIDE:
                BdLocationManager.getInstance().unregisterListener(mLocationListener);
                BdLocationManager.getInstance().stop();
                break;
            case UI_STATE_ON_DETACH:
                break;
        }
    }
}
