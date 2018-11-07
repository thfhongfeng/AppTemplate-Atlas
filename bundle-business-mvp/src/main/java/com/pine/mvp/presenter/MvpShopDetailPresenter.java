package com.pine.mvp.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.contract.IMvpShopDetailContract;
import com.pine.mvp.model.MvpHomeShopModel;
import com.pine.mvp.ui.activity.MvpTravelNoteListActivity;
import com.pine.mvp.ui.activity.MvpWebViewActivity;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpShopDetailPresenter extends BasePresenter<IMvpShopDetailContract.Ui>
        implements IMvpShopDetailContract.Presenter {
    private String mId;
    private MvpHomeShopModel mModel;
    private boolean mIsLoadProcessing;

    public MvpShopDetailPresenter() {
        mModel = new MvpHomeShopModel();
    }

    @Override
    public boolean parseIntentDataOnCreate() {
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
    public void loadShopDetailData() {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("id", mId);
        startDataLoadUi();
        mModel.requestShopDetailData(params, new IModelAsyncResponse<MvpShopDetailEntity>() {
            @Override
            public void onResponse(MvpShopDetailEntity entity) {
                finishDataLoadUi();
                if (isUiAlive()) {
                    getUi().setupShopDetail(entity);
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
    public void goToShopH5Activity() {
        Intent intent = new Intent(getContext(), MvpWebViewActivity.class);
        intent.putExtra("url", "");
        getContext().startActivity(intent);
    }

    @Override
    public void goToTravelNoteListActivity() {
        Intent intent = new Intent(getContext(), MvpTravelNoteListActivity.class);
        intent.putExtra("id", mId);
        getContext().startActivity(intent);
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
