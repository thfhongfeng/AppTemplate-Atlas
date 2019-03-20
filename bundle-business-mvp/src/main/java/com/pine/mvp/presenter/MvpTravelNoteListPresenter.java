package com.pine.mvp.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.MvpConstants;
import com.pine.mvp.adapter.MvpTravelNoteListPaginationAdapter;
import com.pine.mvp.bean.MvpTravelNoteItemEntity;
import com.pine.mvp.contract.IMvpTravelNoteListContract;
import com.pine.mvp.model.IMvpTravelNoteModel;
import com.pine.mvp.model.MvpModelFactory;
import com.pine.mvp.ui.activity.MvpTravelNoteReleaseActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpTravelNoteListPresenter extends BasePresenter<IMvpTravelNoteListContract.Ui>
        implements IMvpTravelNoteListContract.Presenter {
    private String mId;
    private IMvpTravelNoteModel mTravelNoteModel;
    private MvpTravelNoteListPaginationAdapter mMvpTravelNoteItemAdapter;

    public MvpTravelNoteListPresenter() {
        mTravelNoteModel = MvpModelFactory.getMvpTravelNoteModel();
    }

    @Override
    public boolean parseIntentData(Bundle bundle) {
        mId = bundle.getString("id", "");
        if (TextUtils.isEmpty(mId)) {
            finishUi();
            return true;
        }
        return false;
    }

    @Override
    public MvpTravelNoteListPaginationAdapter getListAdapter() {
        if (mMvpTravelNoteItemAdapter == null) {
            mMvpTravelNoteItemAdapter = new MvpTravelNoteListPaginationAdapter(
                    MvpTravelNoteListPaginationAdapter.TRAVEL_NOTE_VIEW_HOLDER);
        }
        return mMvpTravelNoteItemAdapter;
    }

    @Override
    public void goToAddTravelNoteActivity() {
        Intent intent = new Intent(getContext(), MvpTravelNoteReleaseActivity.class);
        getContext().startActivity(intent);
    }

    @Override
    public void loadTravelNoteListData(final boolean refresh) {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        int pageNo = 1;
        if (!refresh) {
            pageNo = mMvpTravelNoteItemAdapter.getNextPageNo();
        }
        params.put(MvpConstants.PAGE_NO, String.valueOf(pageNo));
        params.put(MvpConstants.PAGE_SIZE, String.valueOf(mMvpTravelNoteItemAdapter.getPageSize()));
        params.put("id", mId);
        setUiLoading(true);
        mTravelNoteModel.requestTravelNoteListData(params, new IModelAsyncResponse<ArrayList<MvpTravelNoteItemEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpTravelNoteItemEntity> list) {
                setUiLoading(false);
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
