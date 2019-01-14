package com.pine.mvp.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.adapter.MvpShopListNoPaginationAdapter;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.mvp.contract.IMvpShopNoPaginationListContract;
import com.pine.mvp.model.MvpShopModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopNoPaginationListPresenter extends BasePresenter<IMvpShopNoPaginationListContract.Ui>
        implements IMvpShopNoPaginationListContract.Presenter {
    private String mId;
    private MvpShopModel mModel;
    private MvpShopListNoPaginationAdapter mMvpHomeItemAdapter;
    private boolean mIsLoadProcessing;

    public MvpShopNoPaginationListPresenter() {
        mModel = new MvpShopModel();
    }

    @Override
    public boolean parseIntentData() {
        mId = getStringExtra("id", "");
        return false;
    }

    @Override
    public void onUiState(BasePresenter.UiState state) {

    }

    @Override
    public MvpShopListNoPaginationAdapter getListAdapter() {
        if (mMvpHomeItemAdapter == null) {
            mMvpHomeItemAdapter = new MvpShopListNoPaginationAdapter(
                    MvpShopListNoPaginationAdapter.SHOP_VIEW_HOLDER);
        }
        return mMvpHomeItemAdapter;
    }

    @Override
    public void loadShopNoPaginationListData() {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("id", mId);
        startDataLoadUi();
        if (!mModel.requestShopListData(params, new IModelAsyncResponse<ArrayList<MvpShopItemEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopItemEntity> list) {
                finishDataLoadUi();
                if (isUiAlive()) {
                    mMvpHomeItemAdapter.setData(list);
                }
            }

            @Override
            public boolean onFail(Exception e) {
                finishDataLoadUi();
                return false;
            }
        })) {
            finishDataLoadUi();
        }
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
