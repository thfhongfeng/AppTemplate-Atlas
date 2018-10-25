package com.pine.mvp.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarActivity;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopDetailEntity;
import com.pine.mvp.contract.IMvpShopDetailContract;
import com.pine.mvp.presenter.MvpShopDetailPresenter;

/**
 * Created by tanghongfeng on 2018/10/9
 */

public class MvpShopDetailActivity extends BaseMvpActionBarActivity<IMvpShopDetailContract.Ui, MvpShopDetailPresenter>
        implements IMvpShopDetailContract.Ui, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipe_refresh_layout;
    private TextView description_tv;
    private TextView go_shop_h5_btn_tv;
    private TextView go_travel_note_list_btn_tv;


    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_shop_detail;
    }

    @Override
    protected MvpShopDetailPresenter createPresenter() {
        return new MvpShopDetailPresenter();
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.mvp_shop_detail_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    @Override
    protected void initViewOnCreate() {
        description_tv = findViewById(R.id.description_tv);
        go_shop_h5_btn_tv = findViewById(R.id.go_shop_h5_btn_tv);
        go_travel_note_list_btn_tv = findViewById(R.id.go_travel_note_list_btn_tv);

        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );
        swipe_refresh_layout.setDistanceToTriggerSync(250);
        if (swipe_refresh_layout != null) {
            swipe_refresh_layout.setRefreshing(true);
        }
        swipe_refresh_layout.setEnabled(true);
    }

    @Override
    protected void onAllAccessRestrictionReleased() {
        swipe_refresh_layout.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh_layout.setRefreshing(true);
                onRefresh();
            }
        });
        initEvent();
    }

    private void initEvent() {
        go_shop_h5_btn_tv.setOnClickListener(this);
        go_travel_note_list_btn_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.go_shop_h5_btn_tv) {
            mPresenter.goToShopH5Activity();
        } else if (v.getId() == R.id.go_travel_note_list_btn_tv) {
            mPresenter.goToTravelNoteListActivity();
        }
    }

    @Override
    public void setupShopDetail(MvpShopDetailEntity entity) {
        description_tv.setText(entity.getDescription());
    }

    @Override
    public void onRefresh() {
        mPresenter.loadShopDetailData();
    }

    @Override
    public void setSwipeRefreshLayoutRefresh(boolean processing) {
        if (swipe_refresh_layout == null) {
            return;
        }
        if (processing) {
            if (swipe_refresh_layout.isRefreshing()) {
                swipe_refresh_layout.setRefreshing(processing);
            }
        } else {
            swipe_refresh_layout.setRefreshing(processing);
        }
    }
}
