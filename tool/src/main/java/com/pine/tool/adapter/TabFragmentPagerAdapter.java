package com.pine.tool.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mPagers;
    private List<String> mTitles;

    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> pagers) {
        this(fm, pagers, new ArrayList<String>());
    }

    public TabFragmentPagerAdapter(FragmentManager fm, Fragment[] pagers) {
        this(fm, Arrays.asList(pagers), new ArrayList<String>());
    }

    public TabFragmentPagerAdapter(FragmentManager fm, Fragment[] pagers, String[] titles) {
        this(fm, Arrays.asList(pagers), Arrays.asList(titles));
    }

    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> pagers, List<String> titles) {
        super(fm);
        this.mPagers = pagers;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mPagers.get(position);
    }

    @Override
    public int getCount() {
        return mPagers == null ? 0 : mPagers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null) {
            if (mTitles.size() > position) {
                return mTitles.get(position);
            }
        }
        return super.getPageTitle(position);
    }
}

