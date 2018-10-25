package com.pine.mvp.presenter;

import android.text.TextUtils;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.adapter.MvpTravelNoteItemPaginationAdapter;
import com.pine.mvp.bean.MvpTravelNoteItemEntity;
import com.pine.mvp.contract.IMvpTravelNoteListContract;
import com.pine.mvp.model.MvpTravelNoteModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpTravelNoteListPresenter extends BasePresenter<IMvpTravelNoteListContract.Ui>
        implements IMvpTravelNoteListContract.Presenter {
    private String mId;
    private MvpTravelNoteModel mModel;
    private MvpTravelNoteItemPaginationAdapter mMvpTravelNoteItemAdapter;
    private boolean mIsLoadProcessing;

    public MvpTravelNoteListPresenter() {
        mModel = new MvpTravelNoteModel();
    }

    @Override
    public boolean initDataOnUiCreate() {
        mId = getStringExtra("id", "");
        if (TextUtils.isEmpty(mId)) {
            finishUi();
            return true;
        }
        return false;
    }

    @Override
    public void onUiState(int state) {

    }

    @Override
    public MvpTravelNoteItemPaginationAdapter getRecycleViewAdapter() {
        if (mMvpTravelNoteItemAdapter == null) {
            mMvpTravelNoteItemAdapter = new MvpTravelNoteItemPaginationAdapter(
                    MvpTravelNoteItemPaginationAdapter.TRAVEL_NOTE_VIEW_HOLDER);
        }
        return mMvpTravelNoteItemAdapter;
    }

    @Override
    public void loadTravelNoteListData(final boolean refresh) {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        int pageNo = 1;
        if (!refresh) {
            pageNo = mMvpTravelNoteItemAdapter.getPageNo() + 1;
        }
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(mMvpTravelNoteItemAdapter.getPageSize()));
        params.put("id", mId);
        startDataLoadUi();
        mModel.requestTravelNoteListData(params, new IModelAsyncResponse<ArrayList<MvpTravelNoteItemEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpTravelNoteItemEntity> list) {
                finishDataLoadUi();
                if (isUiAlive()) {
                    if (refresh) {
                        mMvpTravelNoteItemAdapter.setData(list);
                    } else {
                        mMvpTravelNoteItemAdapter.addData(list);
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