package com.pine.main.presenter;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.main.bean.MainBusinessItemEntity;
import com.pine.main.contract.IMainHomeContract;
import com.pine.main.model.IMainHomeModel;
import com.pine.main.model.MainModelFactory;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MainHomePresenter extends BasePresenter<IMainHomeContract.Ui> implements IMainHomeContract.Presenter {
    private IMainHomeModel mHomeModel;
    private String[] mGridViewNames;
    private ArrayList<String> mGridViewBundleList;
    private ArrayList<String> mGridViewCommandList;

    public MainHomePresenter() {
        mHomeModel = MainModelFactory.getMainHomeModel();
    }

    @Override
    public void loadBusinessBundleData() {
        mHomeModel.requestBusinessListData(new IModelAsyncResponse<ArrayList<MainBusinessItemEntity>>() {
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

            @Override
            public void onCancel() {

            }
        });
    }
}
