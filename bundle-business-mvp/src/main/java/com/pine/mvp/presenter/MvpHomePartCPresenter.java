package com.pine.mvp.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.adapter.MvpHomeItemNoPaginationAdapter;
import com.pine.mvp.bean.MvpShopEntity;
import com.pine.mvp.contract.IMvpHomePartCContract;
import com.pine.mvp.model.MvpHomeModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomePartCPresenter extends BasePresenter<IMvpHomePartCContract.Ui>
        implements IMvpHomePartCContract.Presenter {
    private MvpHomeModel mModel = new MvpHomeModel();
    private MvpHomeItemNoPaginationAdapter mMvpHomeItemAdapter;
    private boolean mIsLoadProcessing;

    public MvpHomePartCPresenter() {
        mModel = new MvpHomeModel();
    }

    @Override
    public MvpHomeItemNoPaginationAdapter getRecycleViewAdapter() {
        if (mMvpHomeItemAdapter == null) {
            mMvpHomeItemAdapter = new MvpHomeItemNoPaginationAdapter(
                    MvpHomeItemNoPaginationAdapter.HOME_SHOP_VIEW_HOLDER);
        }
        return mMvpHomeItemAdapter;
    }

    @Override
    public void loadHomePartCListData() {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        mIsLoadProcessing = true;
        mModel.requestShopListData(params, new IModelAsyncResponse<ArrayList<MvpShopEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopEntity> homeShopItemEntity) {
                if (isUiAlive()) {
                    mMvpHomeItemAdapter.setData(homeShopItemEntity);
                    mMvpHomeItemAdapter.notifyDataSetChanged();
                }
                finishDataLoad();
            }

            @Override
            public boolean onFail(Exception e) {
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

    }
}
