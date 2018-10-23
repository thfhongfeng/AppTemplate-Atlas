package com.pine.mvp.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarImageMenuActivity;
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

public class MvpHomeActivity extends BaseMvpActionBarImageMenuActivity<IMvpHomeContract.Ui, MvpHomePresenter>
        implements IMvpHomeContract.Ui {
    private ViewPagerTabLayout view_pager_tab_layout;
    private ViewPager view_pager;

    /**
     * 获取actionbar类别
     */
    protected int getActionBarType() {
        return ACTION_BAR_CENTER_TITLE_TAG | ACTION_BAR_NO_GO_BACK_TAG;
    }

    @Override
    protected MvpHomePresenter createPresenter() {
        return new MvpHomePresenter();
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv, ImageView menuBtnIv) {
        titleTv.setText(R.string.mvp_home_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
        menuBtnIv.setImageResource(R.mipmap.base_ic_add);
        menuBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.goToAddShopActivity();
            }
        });
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_home;
    }

    @Override
    protected void initViewOnCreate() {
        view_pager_tab_layout = findViewById(R.id.view_pager_tab_layout);
        view_pager = findViewById(R.id.view_pager);

        view_pager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(),
                new Fragment[]{
                        new MvpShopPaginationListFragment(), new MvpShopTreeListFragment(),
                        new MvpShopNoPaginationListFragment(), new MvpWebViewFragment()},
                new String[]{"PartA", "PartB", "PartC", "PartD"}));
        view_pager_tab_layout.setupWithViewPager(view_pager);
    }
}
