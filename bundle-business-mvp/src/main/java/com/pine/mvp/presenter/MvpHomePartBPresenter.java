package com.pine.mvp.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.adapter.MvpShopItemPaginationTreeAdapter;
import com.pine.mvp.bean.MvpShopAndProductEntity;
import com.pine.mvp.contract.IMvpHomePartBContract;
import com.pine.mvp.model.MvpHomeModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomePartBPresenter extends BasePresenter<IMvpHomePartBContract.Ui>
        implements IMvpHomePartBContract.Presenter {
    private MvpHomeModel mModel;
    private MvpShopItemPaginationTreeAdapter mMvpHomeItemAdapter;
    private boolean mIsLoadProcessing;

    public MvpHomePartBPresenter() {
        mModel = new MvpHomeModel();
    }

    @Override
    public MvpShopItemPaginationTreeAdapter getRecycleViewAdapter() {
        if (mMvpHomeItemAdapter == null) {
            mMvpHomeItemAdapter = new MvpShopItemPaginationTreeAdapter();
        }
        return mMvpHomeItemAdapter;
    }

    @Override
    public void loadHomePartBListData(final boolean refresh) {
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
        startDataLoadUi();
        mModel.requestShopAndProductListData(params, new IModelAsyncResponse<ArrayList<MvpShopAndProductEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopAndProductEntity> shopAndProductEntities) {
                finishDataLoadUi();
                if (isUiAlive()) {
                    if (refresh) {
                        mMvpHomeItemAdapter.setData(shopAndProductEntities);
                    } else {
                        mMvpHomeItemAdapter.addData(shopAndProductEntities);
                    }
                }
            }

            @Override
            public boolean onFail(Exception e) {
                finishDataLoadUi();
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

    @Override
    public void onUiState(int state) {

    }
}
