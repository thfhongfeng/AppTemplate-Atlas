package com.pine.mvc.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.mvc.activity.BaseMvcActionBarActivity;
import com.pine.mvc.R;
import com.pine.mvc.ui.fragment.MvcHomePartAFragment;
import com.pine.mvc.ui.fragment.MvcHomePartBFragment;
import com.pine.tool.adapter.TabFragmentPagerAdapter;
import com.pine.tool.widget.ViewPagerTabLayout;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvcHomeActivity extends BaseMvcActionBarActivity {
    private ViewPagerTabLayout view_pager_tab_layout;
    private ViewPager view_pager;

    private TabFragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.mvc_home_title);
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
        return R.layout.mvc_activity_home;
    }

    @Override
    protected boolean initData() {
        return false;
    }

    @Override
    protected void initView() {
        view_pager_tab_layout = (ViewPagerTabLayout) findViewById(R.id.view_pager_tab_layout);
        view_pager = (ViewPager) findViewById(R.id.view_pager);

        view_pager.setAdapter(mFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(),
                new Fragment[]{new MvcHomePartAFragment(), new MvcHomePartBFragment()},
                new String[]{"PartA", "PartB"}));
        view_pager_tab_layout.setupWithViewPager(view_pager);
    }
}
