package com.pine.mvp.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.adapter.MvpShopItemPaginationTreeAdapter;
import com.pine.mvp.bean.MvpShopAndProductEntity;
import com.pine.mvp.contract.IMvpShopTreeListContract;
import com.pine.mvp.model.MvpShopModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopTreeListPresenter extends BasePresenter<IMvpShopTreeListContract.Ui>
        implements IMvpShopTreeListContract.Presenter {
    private String mId;
    private MvpShopModel mModel;
    private MvpShopItemPaginationTreeAdapter mMvpHomeItemAdapter;
    private boolean mIsLoadProcessing;

    public MvpShopTreeListPresenter() {
        mModel = new MvpShopModel();
    }

    @Override
    public boolean parseIntentDataOnCreate() {
        mId = getStringExtra("id", "");
        return false;
    }

    @Override
    public void onUiState(int state) {

    }

    @Override
    public MvpShopItemPaginationTreeAdapter getRecycleViewAdapter() {
        if (mMvpHomeItemAdapter == null) {
            mMvpHomeItemAdapter = new MvpShopItemPaginationTreeAdapter();
        }
        return mMvpHomeItemAdapter;
    }

    @Override
    public void loadShopTreeListData(final boolean refresh) {
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
        startDataLoadUi();
        mModel.requestShopAndProductListData(params, new IModelAsyncResponse<ArrayList<MvpShopAndProductEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopAndProductEntity> list) {
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
