package com.pine.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.bean.BaseInputParam;
import com.pine.mvp.MvpConstants;
import com.pine.mvp.R;
import com.pine.mvp.adapter.MvpShopCheckListPaginationAdapter;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.mvp.contract.IMvpShopSearchCheckContract;
import com.pine.mvp.model.IMvpShopModel;
import com.pine.mvp.model.MvpModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/11/15
 */

public class MvpShopSearchCheckPresenter extends BasePresenter<IMvpShopSearchCheckContract.Ui>
        implements IMvpShopSearchCheckContract.Presenter {
    public final static String RESULT_CHECKED_LIST_KEY = "result_checked_list_key";
    public final static String REQUEST_CHECKED_LIST_KEY = "request_checked_list_key";

    private IMvpShopModel mShopModel;
    private MvpShopCheckListPaginationAdapter mAdapter;
    private boolean mSearchMode;
    private ArrayList<MvpShopItemEntity> mBelongShopList;

    public MvpShopSearchCheckPresenter() {
        mShopModel = MvpModelFactory.getMvpShopModel();
    }

    @Override
    public boolean parseIntentData(Bundle bundle) {
        mBelongShopList = bundle.getParcelableArrayList(REQUEST_CHECKED_LIST_KEY);
        return false;
    }

    @Override
    public MvpShopCheckListPaginationAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new MvpShopCheckListPaginationAdapter(mBelongShopList,
                    MvpShopCheckListPaginationAdapter.SHOP_CHECK_VIEW_HOLDER);
        }
        return mAdapter;
    }

    @Override
    public void postSearch(final boolean refresh) {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        int pageNo = 1;
        if (!refresh) {
            pageNo = mAdapter.getNextPageNo();
        }
        params.put(MvpConstants.PAGE_NO, String.valueOf(pageNo));
        params.put(MvpConstants.PAGE_SIZE, String.valueOf(mAdapter.getPageSize()));

        BaseInputParam<String> searchKey = getUi().getSearchKey("searchKey");
        if (TextUtils.isEmpty(searchKey.getValue())) {
            mSearchMode = false;
        } else {
            mSearchMode = true;
            params.put(searchKey.getKey(), searchKey.getValue());
        }

        setUiLoading(true);
        mShopModel.requestShopListData(params, new IModelAsyncResponse<ArrayList<MvpShopItemEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopItemEntity> mvpShopItemEntities) {
                setUiLoading(false);
                if (isUiAlive()) {
                    if (refresh) {
                        mAdapter.setData(mvpShopItemEntities, mSearchMode);
                    } else {
                        mAdapter.addData(mvpShopItemEntities);
                    }
                }
            }

            @Override
            public boolean onFail(Exception e) {
                return false;
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void completeAction() {
        if (mSearchMode) {
            getUi().goAllSelectMode();
        } else {
            Map<String, MvpShopItemEntity> checkedData = mAdapter.getAllCheckedData();
            if (checkedData == null || checkedData.size() < 1) {
                showShortToast(R.string.mvp_shop_check_item_need);
                return;
            }
            ArrayList<MvpShopItemEntity> list = new ArrayList<>();
            for (MvpShopItemEntity entity : checkedData.values()) {
                list.add(entity);
            }
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(RESULT_CHECKED_LIST_KEY, list);
            getActivity().setResult(Activity.RESULT_OK, intent);
            finishUi();
        }
    }

    @Override
    public void clearCurCheck() {
        mAdapter.clearCheckedData();
    }
}
