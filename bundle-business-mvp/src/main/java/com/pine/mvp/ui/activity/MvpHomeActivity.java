package com.pine.mvp.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarImageMenuActivity;
import com.pine.base.permission.PermissionsAnnotation;
import com.pine.config.switcher.ConfigFuncSwitcher;
import com.pine.mvp.R;
import com.pine.mvp.contract.IMvpHomeContract;
import com.pine.mvp.presenter.MvpHomePresenter;
import com.pine.mvp.ui.fragment.MvpShopNoPaginationListFragment;
import com.pine.mvp.ui.fragment.MvpShopPaginationListFragment;
import com.pine.mvp.ui.fragment.MvpShopTreeListFragment;
import com.pine.mvp.ui.fragment.MvpWebViewFragment;
import com.pine.tool.adapter.TabFragmentPagerAdapter;
import com.pine.tool.widget.ViewPagerTabLayout;

/**
 * Created by tanghongfeng on 2018/9/13
 */

@PermissionsAnnotation(Permissions = {Manifest.permission.ACCESS_FINE_LOCATION})
public class MvpHomeActivity extends BaseMvpActionBarImageMenuActivity<IMvpHomeContract.Ui, MvpHomePresenter>
        implements IMvpHomeContract.Ui {
    private ViewPagerTabLayout view_pager_tab_layout;
    private ViewPager view_pager;

    @Override
    protected void beforeInitOnCreate(@Nullable Bundle savedInstanceState) {
        super.beforeInitOnCreate(savedInstanceState);
        setActionBarTag(ACTION_BAR_CENTER_TITLE_TAG | ACTION_BAR_NO_GO_BACK_TAG);
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_home;
    }

    @Override
    protected void findViewOnCreate() {
        view_pager_tab_layout = findViewById(R.id.view_pager_tab_layout);
        view_pager = findViewById(R.id.view_pager);
    }

    @Override
    protected void init() {
        setupViewPage();
    }

    private void setupViewPage() {
        view_pager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(),
                new Fragment[]{
                        new MvpShopPaginationListFragment(), new MvpShopTreeListFragment(),
                        new MvpShopNoPaginationListFragment(), new MvpWebViewFragment()},
                new String[]{"PartA", "PartB", "PartC", "PartD"}));
        view_pager_tab_layout.setupWithViewPager(view_pager);
    }

    @Override
    protected void setupActionBar(ImageView goBackIv, TextView titleTv, ImageView menuBtnIv) {
        titleTv.setText(R.string.mvp_home_title);
        if (ConfigFuncSwitcher.getInstance().canAddProduct()) {
            menuBtnIv.setImageResource(R.mipmap.base_ic_add);
            menuBtnIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.goToAddShopActivity();
                }
            });
            menuBtnIv.setVisibility(View.VISIBLE);
        } else {
            menuBtnIv.setVisibility(View.GONE);
        }
    }
}
