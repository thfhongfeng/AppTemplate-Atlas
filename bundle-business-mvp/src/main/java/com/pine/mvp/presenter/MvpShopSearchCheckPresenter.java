package com.pine.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.MvpConstants;
import com.pine.mvp.R;
import com.pine.mvp.adapter.MvpShopCheckListPaginationAdapter;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.mvp.contract.IMvpShopSearchCheckContract;
import com.pine.mvp.model.MvpShopModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/11/15
 */

public class MvpShopSearchCheckPresenter extends BasePresenter<IMvpShopSearchCheckContract.Ui>
        implements IMvpShopSearchCheckContract.Presenter {
    public final static String RESULT_CHECKED_IDS_KEY = "result_checked_ids_key";
    public final static String RESULT_CHECKED_NAMES_KEY = "result_checked_names_key";
    public final static String REQUEST_CHECKED_IDS_KEY = "request_checked_ids_key";

    private MvpShopModel mModel;
    private MvpShopCheckListPaginationAdapter mAdapter;
    private boolean mSearchMode;
    private boolean mIsLoadProcessing;

    public MvpShopSearchCheckPresenter() {
        mModel = new MvpShopModel();
    }

    @Override
    public boolean parseIntentDataOnCreate() {
        return false;
    }

    @Override
    public void onUiState(int state) {

    }

    @Override
    public MvpShopCheckListPaginationAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new MvpShopCheckListPaginationAdapter(getStringArrayListExtra(REQUEST_CHECKED_IDS_KEY),
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
            pageNo = mAdapter.getPageNo() + 1;
        }
        params.put(MvpConstants.PAGE_NO, String.valueOf(pageNo));
        params.put(MvpConstants.PAGE_SIZE, String.valueOf(mAdapter.getPageSize()));

        InputParamBean<String> searchKey = getUi().getSearchKey("searchKey");
        if (TextUtils.isEmpty(searchKey.getValue())) {
            mSearchMode = false;
        } else {
            mSearchMode = true;
            params.put(searchKey.getKey(), searchKey.getValue());
        }

        startDataLoadUi();
        mModel.requestShopListData(params, new IModelAsyncResponse<ArrayList<MvpShopItemEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpShopItemEntity> mvpShopItemEntities) {
                finishDataLoadUi();
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
            ArrayList<String> ids = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            for (MvpShopItemEntity entity : checkedData.values()) {
                ids.add(entity.getId());
                names.add(entity.getName());
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra(RESULT_CHECKED_IDS_KEY, ids);
            intent.putStringArrayListExtra(RESULT_CHECKED_NAMES_KEY, names);
            getActivity().setResult(Activity.RESULT_OK, intent);
            finishUi();
        }
    }

    @Override
    public void clearCurCheck() {
        mAdapter.clearCheckedData();
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
