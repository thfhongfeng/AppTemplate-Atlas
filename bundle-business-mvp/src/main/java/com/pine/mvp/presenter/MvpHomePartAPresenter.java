package com.pine.mvp.presenter;

import android.widget.Toast;

import com.pine.base.mvp.model.IModelAsyncResponse;
import com.pine.base.mvp.presenter.BasePresenter;
import com.pine.mvp.bean.MvpHomePartAEntity;
import com.pine.mvp.contract.IMvpHomePartAContract;
import com.pine.mvp.model.MvpHomeModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomePartAPresenter extends BasePresenter<IMvpHomePartAContract.Ui>
        implements IMvpHomePartAContract.Presenter {
    private MvpHomeModel mModel = new MvpHomeModel();
    private boolean mIsLoadProcessing;

    public MvpHomePartAPresenter() {
        mModel = new MvpHomeModel();
    }

    @Override
    public void loadHomePartAListData(final boolean refresh, int pageNo, int pageSize) {
        if (mIsLoadProcessing) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        mIsLoadProcessing = true;
        mModel.requestHomePartAListData(params, new IModelAsyncResponse<ArrayList<MvpHomePartAEntity>>() {
            @Override
            public void onResponse(ArrayList<MvpHomePartAEntity> mvpHomePartAEntities) {
                if (isUiAlive()) {
                    getUi().setHomePartAListAdapter(mvpHomePartAEntities, refresh);
                }
                finishDataLoad();
            }

            @Override
            public boolean onFail(Exception e) {
                if (e instanceof JSONException) {
                    if (isUiAlive()) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                    }
                }
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
