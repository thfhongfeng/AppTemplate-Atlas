package com.pine.mvp.presenter;

import com.pine.base.mvp.model.IModelAsyncResponse;
import com.pine.base.mvp.presenter.BasePresenter;
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
    private boolean mIsLoadProcessing;

    public MvpHomePartBPresenter() {
        mModel = new MvpHomeModel();
    }

    @Override
    public void loadHomePartBListData(final boolean refresh, int pageNo, int pageSize) {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        mIsLoadProcessing = true;
        mModel.requestHomePartBListData(params, new IModelAsyncResponse<ArrayList<MvpHomePartBEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpHomePartBEntity> mvpHomePartBEntities) {
                if (isUiAlive()) {
                    getUi().setHomePartBListAdapter(mvpHomePartBEntities, refresh);
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
