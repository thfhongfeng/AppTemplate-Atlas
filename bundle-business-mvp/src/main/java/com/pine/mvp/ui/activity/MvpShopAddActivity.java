package com.pine.mvp.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarTextMenuActivity;
import com.pine.mvp.R;
import com.pine.mvp.contract.IMvpShopAddContract;
import com.pine.mvp.presenter.MvpShopAddPresenter;

/**
 * Created by tanghongfeng on 2018/10/23
 */

public class MvpShopAddActivity extends BaseMvpActionBarTextMenuActivity<IMvpShopAddContract.Ui, MvpShopAddPresenter>
        implements IMvpShopAddContract.Ui, View.OnClickListener {
    @Override
    protected MvpShopAddPresenter createPresenter() {
        return new MvpShopAddPresenter();
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv, TextView menuBtnTv) {
        titleTv.setText(R.string.mvp_shop_add_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
        menuBtnTv.setText(R.string.mvp_shop_add_confirm_menu);
        menuBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addShop();
            }
        });
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_shop_add;
    }

    @Override
    protected void initViewOnCreate() {

    }

    @Override
    public void onClick(View v) {

    }
}
