package com.pine.mvp.presenter;

import com.pine.base.mvp.model.IModelAsyncResponse;
import com.pine.base.mvp.presenter.BasePresenter;
import com.pine.mvp.bean.MvpHomePartCEntity;
import com.pine.mvp.contract.IMvpHomePartCContract;
import com.pine.mvp.model.MvpHomeModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomePartCPresenter extends BasePresenter<IMvpHomePartCContract.Ui>
        implements IMvpHomePartCContract.Presenter {
    private MvpHomeModel mModel = new MvpHomeModel();
    private boolean mIsLoadProcessing;

    public MvpHomePartCPresenter() {
        mModel = new MvpHomeModel();
    }

    @Override
    public void loadHomePartCListData() {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        mIsLoadProcessing = true;
        mModel.requestHomePartCListData(params, new IModelAsyncResponse<ArrayList<MvpHomePartCEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpHomePartCEntity> mvpHomePartCEntities) {
                if (isUiAlive()) {
                    getUi().setHomePartCListAdapter(mvpHomePartCEntities);
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
}
