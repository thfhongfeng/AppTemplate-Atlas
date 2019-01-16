package com.pine.demo;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.ui.BaseActionBarActivity;
import com.pine.base.widget.rv_space.GridSpacingItemDecoration;
import com.pine.demo.adapter.DemoAdapter;
import com.pine.demo.bean.DemoItemEntity;
import com.pine.demo.novice_guide.DemoNoviceGuideActivity;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2019/1/14
 */

public class DemoHomeActivity extends BaseActionBarActivity {
    private RecyclerView demo_rv;
    private DemoAdapter mDemoAdapter;

    @Override
    protected void beforeInitOnCreate() {
        super.beforeInitOnCreate();
        setActionBarTag(ACTION_BAR_CENTER_TITLE_TAG | ACTION_BAR_NO_GO_BACK_TAG);
    }

    @Override
    protected void setupActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.demo_home_title);
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.demo_activity_home;
    }

    @Override
    protected void findViewOnCreate() {
        demo_rv = findViewById(R.id.demo_rv);
    }

    @Override
    protected boolean parseIntentData() {
        return false;
    }

    @Override
    protected void init() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        demo_rv.setLayoutManager(layoutManager);
        demo_rv.addItemDecoration(new GridSpacingItemDecoration(3,
                getResources().getDimensionPixelOffset(R.dimen.dp_10), true));
        demo_rv.setHasFixedSize(true);
        mDemoAdapter = new DemoAdapter(
                DemoAdapter.DEMO_VIEW_HOLDER);
        mDemoAdapter.showEmptyComplete(true, false);
        ArrayList<DemoItemEntity> list = new ArrayList<>();
        DemoItemEntity entity = new DemoItemEntity();
        entity.setName("新手引导");
        entity.setClazz(DemoNoviceGuideActivity.class);
        list.add(entity);
        mDemoAdapter.setData(list);
        demo_rv.setAdapter(mDemoAdapter);
    }
}
