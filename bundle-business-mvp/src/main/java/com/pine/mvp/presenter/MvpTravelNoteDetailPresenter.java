package com.pine.mvp.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.MvpConstants;
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

    public MvpTravelNoteDetailPresenter() {
        mModel = new MvpTravelNoteModel();
    }

    @Override
    public boolean parseInitData(Bundle bundle) {
        mId = bundle.getString("id", "");
        if (TextUtils.isEmpty(mId)) {
            finishUi();
            return true;
        }
        return false;
    }

    @Override
    public void onUiState(BasePresenter.UiState state) {

    }

    @Override
    public MvpTravelNoteDetailComplexAdapter getListAdapter() {
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
        setUiLoading(true);
        mModel.requestTravelNoteDetailData(params, new IModelAsyncResponse<MvpTravelNoteDetailEntity>() {
            @Override
            public void onResponse(MvpTravelNoteDetailEntity entity) {
                setUiLoading(false);
                if (refresh) {
                    if (isUiAlive()) {
                        List<MvpTravelNoteDetailEntity> list = new ArrayList<>();
                        list.add(entity);
                        mTravelNoteDetailAdapter.setHeadData(list);
                    }
                    loadTravelNoteCommentData(true);
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
        params.put(MvpConstants.PAGE_NO, String.valueOf(pageNo));
        params.put(MvpConstants.PAGE_SIZE, String.valueOf(mTravelNoteDetailAdapter.getPageSize()));
        params.put("id", mId);
        setUiLoading(true);
        mModel.requestTravelNoteCommentData(params, new IModelAsyncResponse<ArrayList<MvpTravelNoteCommentEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpTravelNoteCommentEntity> list) {
                setUiLoading(false);
                if (isUiAlive()) {
                    if (refresh) {
                        mTravelNoteDetailAdapter.setTailData(list);
                    } else {
                        mTravelNoteDetailAdapter.addTailData(list);
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
