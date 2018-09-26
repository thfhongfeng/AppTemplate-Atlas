package com.pine.a.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.a.R;
import com.pine.a.contract.IBusinessAHomeContract;
import com.pine.a.presenter.BusinessAHomePresenter;
import com.pine.base.mvp.ui.activity.BaseActionBarActivity;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class BusinessAHomeActivity extends BaseActionBarActivity<IBusinessAHomeContract.Ui, BusinessAHomePresenter>
        implements IBusinessAHomeContract.Ui {
    @Override
    protected int getActivityLayoutResId() {
        return R.layout.business_a_activity_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.business_a_home_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void afterInit() {

    }

    @Override
    protected BusinessAHomePresenter createPresenter() {
        return new BusinessAHomePresenter();
    }
}
