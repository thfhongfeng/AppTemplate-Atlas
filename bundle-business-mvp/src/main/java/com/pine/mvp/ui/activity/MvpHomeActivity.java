package com.pine.mvp.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.mvp.ui.activity.BaseMvpActionBarActivity;
import com.pine.mvp.R;
import com.pine.mvp.contract.IMvpHomeContract;
import com.pine.mvp.presenter.MvpHomePresenter;
import com.pine.mvp.ui.fragment.MvpHomePartAFragment;
import com.pine.mvp.ui.fragment.MvpHomePartBFragment;
import com.pine.mvp.ui.fragment.MvpHomePartCFragment;
import com.pine.mvp.ui.fragment.MvpHomePartDFragment;
import com.pine.tool.adapter.TabFragmentPagerAdapter;
import com.pine.tool.widget.ViewPagerTabLayout;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpHomeActivity extends BaseMvpActionBarActivity<IMvpHomeContract.Ui, MvpHomePresenter>
        implements IMvpHomeContract.Ui {
    private ViewPagerTabLayout view_pager_tab_layout;
    private ViewPager view_pager;

    @Override
    protected MvpHomePresenter createPresenter() {
        return new MvpHomePresenter();
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.mvp_home_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_home;
    }

    @Override
    protected boolean initData() {
        return false;
    }

    @Override
    protected void initView() {
        view_pager_tab_layout = (ViewPagerTabLayout) findViewById(R.id.view_pager_tab_layout);
        view_pager = (ViewPager) findViewById(R.id.view_pager);

        view_pager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(),
                new Fragment[]{
                        new MvpHomePartAFragment(), new MvpHomePartBFragment(),
                        new MvpHomePartCFragment(), new MvpHomePartDFragment()},
                new String[]{"PartA", "PartB", "PartC", "PartD"}));
        view_pager_tab_layout.setupWithViewPager(view_pager);
    }
}
