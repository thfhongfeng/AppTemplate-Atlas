package com.pine.mvp.presenter;

import android.text.TextUtils;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.adapter.MvpTravelNoteDetailComplexAdapter;
import com.pine.mvp.bean.MvpTravelNoteCommentEntity;
import com.pine.mvp.bean.MvpTravelNoteDetailEntity;
import com.pine.mvp.contract.IMvpTravelNoteDetailContract;
import com.pine.mvp.model.MvpTravelNoteModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpTravelNoteDetailPresenter extends BasePresenter<IMvpTravelNoteDetailContract.Ui>
        implements IMvpTravelNoteDetailContract.Presenter {
    private String mId;
    private MvpTravelNoteModel mModel;
    private MvpTravelNoteDetailComplexAdapter mTravelNoteDetailAdapter;
    private boolean mIsLoadProcessing;

    public MvpTravelNoteDetailPresenter() {
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
    public MvpTravelNoteDetailComplexAdapter getRecycleViewAdapter() {
        if (mTravelNoteDetailAdapter == null) {
            mTravelNoteDetailAdapter = new MvpTravelNoteDetailComplexAdapter();
        }
        return mTravelNoteDetailAdapter;
    }

    @Override
    public void loadTravelNoteDetailData(final boolean refresh) {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("id", mId);
        startDataLoadUi();
        mModel.requestTravelNoteDetailData(params, new IModelAsyncResponse<MvpTravelNoteDetailEntity>() {
            @Override
            public void onResponse(MvpTravelNoteDetailEntity entity) {
                finishDataLoadUi();
                if (refresh) {
                    if (isUiAlive()) {
                        List<MvpTravelNoteDetailEntity> list = new ArrayList<>();
                        list.add(entity);
                        mTravelNoteDetailAdapter.setTopData(list);
                    }
                    loadTravelNoteCommentData(true);
                }
            }

            @Override
            public boolean onFail(Exception e) {
                finishDataLoadUi();
                return false;
            }
        });
    }

    @Override
    public void loadTravelNoteCommentData(final boolean refresh) {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        int pageNo = 1;
        if (!refresh) {
            pageNo = mTravelNoteDetailAdapter.getPageNo() + 1;
        }
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(mTravelNoteDetailAdapter.getPageSize()));
        params.put("id", mId);
        startDataLoadUi();
        mModel.requestTravelNoteCommentData(params, new IModelAsyncResponse<ArrayList<MvpTravelNoteCommentEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpTravelNoteCommentEntity> list) {
                finishDataLoadUi();
                if (isUiAlive()) {
                    if (refresh) {
                        mTravelNoteDetailAdapter.setBottomData(list);
                    } else {
                        mTravelNoteDetailAdapter.addBottomData(list);
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
