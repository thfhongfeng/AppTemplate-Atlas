package com.pine.main.ui.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpNoActionBarActivity;
import com.pine.main.R;
import com.pine.main.contract.IMainHomeContract;
import com.pine.main.presenter.MainHomePresenter;

public class MainHomeActivity extends BaseMvpNoActionBarActivity<IMainHomeContract.Ui, MainHomePresenter>
        implements IMainHomeContract.Ui {

    private GridView business_gv;

    @Override
    protected MainHomePresenter createPresenter() {
        return new MainHomePresenter();
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.main_activity_home;
    }

    @Override
    protected void findViewOnCreate() {
        business_gv = findViewById(R.id.business_gv);
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
    public void setBusinessBundleAdapter(String[] names) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.main_item_home_gridview, R.id.title_tv, names);
        business_gv.setAdapter(arrayAdapter);
        business_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onBusinessItemClick(position);
            }
        });
    }
}
