package com.pine.main.ui.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.pine.base.mvp.ui.activity.BaseNoActionBarActivity;
import com.pine.main.R;
import com.pine.main.contract.IMainHomeContract;
import com.pine.main.presenter.MainHomePresenter;

public class MainHomeActivity extends BaseNoActionBarActivity<IMainHomeContract.Ui, MainHomePresenter>
        implements IMainHomeContract.Ui {

    private GridView business_gv;

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.main_activity_home;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        business_gv = (GridView) findViewById(R.id.business_gv);
    }

    @Override
    protected void afterInit() {
        mPresenter.loadBusinessBundleData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected MainHomePresenter createPresenter() {
        return new MainHomePresenter();
    }

    @Override
    public void setBusinessBundleAdapter(String[] names) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
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
