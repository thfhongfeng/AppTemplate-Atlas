package com.pine.mvp.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.MvpConstants;
import com.pine.mvp.adapter.MvpShopListPaginationTreeAdapter;
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
    private MvpShopModel mModel;
    private MvpShopListPaginationTreeAdapter mMvpHomeItemAdapter;

    public MvpShopTreeListPresenter() {
        mModel = new MvpShopModel();
    }

    @Override
    public MvpShopListPaginationTreeAdapter getListAdapter() {
        if (mMvpHomeItemAdapter == null) {
            mMvpHomeItemAdapter = new MvpShopListPaginationTreeAdapter();
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
            pageNo = mMvpHomeItemAdapter.getNextPageNo();
        }
        params.put(MvpConstants.PAGE_NO, String.valueOf(pageNo));
        params.put(MvpConstants.PAGE_SIZE, String.valueOf(mMvpHomeItemAdapter.getPageSize()));
        setUiLoading(true);
        mModel.requestShopAndProductListData(params, new IModelAsyncResponse<ArrayList<MvpShopAndProductEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopAndProductEntity> list) {
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
