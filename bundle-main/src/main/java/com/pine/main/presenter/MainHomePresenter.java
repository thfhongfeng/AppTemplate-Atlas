package com.pine.main.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.main.bean.MainBusinessItemEntity;
import com.pine.main.contract.IMainHomeContract;
import com.pine.main.model.MainHomeModel;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MainHomePresenter extends BasePresenter<IMainHomeContract.Ui> implements IMainHomeContract.Presenter {
    private MainHomeModel mModel;
    private String[] mGridViewNames;
    private ArrayList<String> mGridViewBundleList;
    private ArrayList<String> mGridViewCommandList;

    public MainHomePresenter() {
        mModel = new MainHomeModel();
    }

    @Override
    public boolean parseIntentData() {
        return false;
    }

    @Override
    public void onUiState(BasePresenter.UiState state) {

    }

    @Override
    public void loadBusinessBundleData() {
        mModel.requestBusinessListData(new IModelAsyncResponse<ArrayList<MainBusinessItemEntity>>() {
            @Override
            public void onResponse(ArrayList<MainBusinessItemEntity> entityList) {
                if (entityList != null && entityList.size() > 0 && isUiAlive()) {
                    getUi().setBusinessBundleData(entityList);
                }
            }

            @Override
            public boolean onFail(Exception e) {
                return false;
            }
        });
    }
}
