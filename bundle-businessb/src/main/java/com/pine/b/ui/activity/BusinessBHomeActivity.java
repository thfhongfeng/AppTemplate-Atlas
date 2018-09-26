package com.pine.b.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.b.R;
import com.pine.b.contract.IBusinessBHomeContract;
import com.pine.b.presenter.BusinessBHomePresenter;
import com.pine.base.mvp.ui.activity.BaseActionBarActivity;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class BusinessBHomeActivity extends BaseActionBarActivity<IBusinessBHomeContract.Ui, BusinessBHomePresenter>
        implements IBusinessBHomeContract.Ui {
    @Override
    protected int getActivityLayoutResId() {
        return R.layout.business_b_activity_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.business_b_home_title);
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
    protected BusinessBHomePresenter createPresenter() {
        return new BusinessBHomePresenter();
    }
}
