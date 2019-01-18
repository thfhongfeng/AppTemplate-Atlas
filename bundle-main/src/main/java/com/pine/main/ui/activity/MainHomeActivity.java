package com.pine.main.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpNoActionBarActivity;
import com.pine.base.widget.rv_space.GridSpacingItemDecoration;
import com.pine.main.R;
import com.pine.main.adapter.MainBusinessAdapter;
import com.pine.main.bean.MainBusinessItemEntity;
import com.pine.main.contract.IMainHomeContract;
import com.pine.main.presenter.MainHomePresenter;

import java.util.ArrayList;

public class MainHomeActivity extends BaseMvpNoActionBarActivity<IMainHomeContract.Ui, MainHomePresenter>
        implements IMainHomeContract.Ui {

    private RecyclerView business_rv;
    private MainBusinessAdapter mMainBusinessAdapter;

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.main_activity_home;
    }

    @Override
    protected void findViewOnCreate() {
        business_rv = findViewById(R.id.business_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        business_rv.setLayoutManager(layoutManager);
        business_rv.addItemDecoration(new GridSpacingItemDecoration(3,
                getResources().getDimensionPixelOffset(R.dimen.dp_10), true));
        business_rv.setHasFixedSize(true);
        mMainBusinessAdapter = new MainBusinessAdapter(
                MainBusinessAdapter.BUSINESS_VIEW_HOLDER);
        mMainBusinessAdapter.showEmptyComplete(true, false);
        business_rv.setAdapter(mMainBusinessAdapter);
    }

    @Override
    protected void init() {
        mPresenter.loadBusinessBundleData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setBusinessBundleData(ArrayList<MainBusinessItemEntity> list) {
        mMainBusinessAdapter.setData(list);
    }
}
