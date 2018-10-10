package com.pine.mvp.presenter;

import com.pine.base.mvp.model.IModelAsyncResponse;
import com.pine.base.mvp.presenter.BasePresenter;
import com.pine.mvp.adapter.MvpHomeItemPaginationAdapter;
import com.pine.mvp.bean.MvpHomePartBEntity;
import com.pine.mvp.contract.IMvpHomePartBContract;
import com.pine.mvp.model.MvpHomeModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomePartBPresenter extends BasePresenter<IMvpHomePartBContract.Ui>
        implements IMvpHomePartBContract.Presenter {
    private MvpHomeModel mModel = new MvpHomeModel();
    private MvpHomeItemPaginationAdapter mMvpHomeItemAdapter;
    private boolean mIsLoadProcessing;

    public MvpHomePartBPresenter() {
        mModel = new MvpHomeModel();
    }

    @Override
    public MvpHomeItemPaginationAdapter getRecycleViewAdapter() {
        if (mMvpHomeItemAdapter == null) {
            mMvpHomeItemAdapter = new MvpHomeItemPaginationAdapter(MvpHomeItemPaginationAdapter.HOME_PART_B_VIEW_HOLDER);
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
            pageNo = mMvpHomeItemAdapter.getPageNo();
        }
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(mMvpHomeItemAdapter.getPageSize()));
        mIsLoadProcessing = true;
        mModel.requestHomePartBListData(params, new IModelAsyncResponse<ArrayList<MvpHomePartBEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpHomePartBEntity> mvpHomePartBEntities) {
                if (isUiAlive()) {
                    if (refresh) {
                        mMvpHomeItemAdapter.setData(mvpHomePartBEntities);
                    } else {
                        mMvpHomeItemAdapter.addData(mvpHomePartBEntities);
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
