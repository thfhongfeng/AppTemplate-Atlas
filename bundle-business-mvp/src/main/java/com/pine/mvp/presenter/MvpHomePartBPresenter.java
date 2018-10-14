package com.pine.mvp.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.adapter.MvpHomeItemPaginationTreeAdapter;
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
    private MvpHomeItemPaginationTreeAdapter mMvpHomeItemAdapter;
    private boolean mIsLoadProcessing;

    public MvpHomePartBPresenter() {
        mModel = new MvpHomeModel();
    }

    @Override
    public MvpHomeItemPaginationTreeAdapter getRecycleViewAdapter() {
        if (mMvpHomeItemAdapter == null) {
            mMvpHomeItemAdapter = new MvpHomeItemPaginationTreeAdapter(
                    MvpHomeItemPaginationTreeAdapter.HOME_SHOP_AND_PRODUCT_TREE_LIST_ITEM);
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
        mIsLoadProcessing = true;
        mModel.requestShopAndProductListData(params, new IModelAsyncResponse<ArrayList<MvpShopAndProductEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopAndProductEntity> shopAndProductEntities) {
                if (isUiAlive()) {
                    if (refresh) {
                        mMvpHomeItemAdapter.setData(shopAndProductEntities);
                    } else {
                        mMvpHomeItemAdapter.addData(shopAndProductEntities);
                    }
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
