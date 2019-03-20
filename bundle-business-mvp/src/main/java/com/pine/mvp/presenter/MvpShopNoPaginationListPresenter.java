package com.pine.mvp.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.adapter.MvpShopListNoPaginationAdapter;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.mvp.contract.IMvpShopNoPaginationListContract;
import com.pine.mvp.model.IMvpShopModel;
import com.pine.mvp.model.MvpModelFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopNoPaginationListPresenter extends BasePresenter<IMvpShopNoPaginationListContract.Ui>
        implements IMvpShopNoPaginationListContract.Presenter {
    private IMvpShopModel mShopModel;
    private MvpShopListNoPaginationAdapter mMvpHomeItemAdapter;

    public MvpShopNoPaginationListPresenter() {
        mShopModel = MvpModelFactory.getMvpShopModel();
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
        setUiLoading(true);
        mShopModel.requestShopListData(params, new IModelAsyncResponse<ArrayList<MvpShopItemEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopItemEntity> list) {
                setUiLoading(false);
                if (isUiAlive()) {
                    mMvpHomeItemAdapter.setData(list);
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
