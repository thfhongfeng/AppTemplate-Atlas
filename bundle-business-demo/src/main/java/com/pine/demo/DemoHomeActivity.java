package com.pine.demo;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.ui.BaseActionBarActivity;
import com.pine.demo.novice_guide.DemoNoviceGuideActivity;

/**
 * Created by tanghongfeng on 2019/1/14
 */

public class DemoHomeActivity extends BaseActionBarActivity {
    private GridView business_gv;
    private String[] mDemoNameList = {
            "新手引导"
    };
    private Class[] mDemoClassList = {
            DemoNoviceGuideActivity.class
    };

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
        business_gv = findViewById(R.id.business_gv);
    }

    @Override
    protected boolean parseIntentData() {
        return false;
    }

    @Override
    protected void init() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.demo_item_home_gridview, R.id.title_tv, mDemoNameList);
        business_gv.setAdapter(arrayAdapter);
        business_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(DemoHomeActivity.this, mDemoClassList[position]));
            }
        });
    }
}
