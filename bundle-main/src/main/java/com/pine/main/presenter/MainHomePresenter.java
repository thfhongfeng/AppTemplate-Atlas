package com.pine.main.presenter;

import android.os.Bundle;

import com.pine.base.BaseApplication;
import com.pine.base.mvp.model.IModelAsyncResponse;
import com.pine.base.mvp.presenter.BasePresenter;
import com.pine.main.bean.MainHomeGridViewEntity;
import com.pine.main.contract.IMainHomeContract;
import com.pine.main.model.MainHomeModel;
import com.pine.router.IRouterCallback;
import com.pine.router.RouterFactory;

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
    public void loadBusinessBundleData() {
        mModel.requestBusinessListData(new IModelAsyncResponse<ArrayList<MainHomeGridViewEntity>>() {
            @Override
            public void onResponse(ArrayList<MainHomeGridViewEntity> entityList) {
                if (entityList != null && entityList.size() > 0 && isUiAlive()) {
                    mGridViewNames = new String[entityList.size()];
                    mGridViewBundleList = new ArrayList<String>();
                    mGridViewCommandList = new ArrayList<String>();
                    for (int i = 0; i < entityList.size(); i++) {
                        MainHomeGridViewEntity entity = entityList.get(i);
                        mGridViewNames[i] = entity.getName();
                        mGridViewBundleList.add(entity.getBundle());
                        mGridViewCommandList.add(entity.getCommand());
                    }
                    getUi().setBusinessBundleAdapter(mGridViewNames);
                }
            }

            @Override
            public boolean onFail(Exception e) {
                return false;
            }
        });
    }

    @Override
    public void onBusinessItemClick(int position) {
        if (getUi() != null) {
            RouterFactory.getBundleManager(mGridViewBundleList.get(position)).callUiCommand(BaseApplication.mCurResumedActivity,
                    mGridViewCommandList.get(position), null, new IRouterCallback() {
                        @Override
                        public void onSuccess(Bundle returnBundle) {

                        }

                        @Override
                        public void onException(Bundle returnBundle) {

                        }

                        @Override
                        public void onFail(String errorInfo) {

                        }
                    });
        }
    }

    @Override
    public void onUiState(int state) {

    }
}
