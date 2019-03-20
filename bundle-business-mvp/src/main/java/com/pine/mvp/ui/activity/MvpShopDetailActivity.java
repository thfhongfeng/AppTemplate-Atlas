package com.pine.mvp.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarActivity;
import com.pine.base.component.uploader.ui.ImageUploadView;
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
    private TextView name_tv;
    private TextView type_tv;
    private TextView online_date_tv;
    private TextView contact_tv;
    private TextView address_district_tv;
    private TextView address_marker_tv;
    private TextView address_street_tv;
    private TextView description_tv;
    private TextView remark_tv;
    private ImageUploadView photo_iuv;
    private TextView go_shop_h5_btn_tv;
    private TextView go_travel_note_list_btn_tv;

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_shop_detail;
    }

    @Override
    protected void findViewOnCreate() {
        name_tv = findViewById(R.id.name_tv);
        type_tv = findViewById(R.id.type_tv);
        online_date_tv = findViewById(R.id.online_date_tv);
        contact_tv = findViewById(R.id.contact_tv);
        address_district_tv = findViewById(R.id.address_district_tv);
        address_marker_tv = findViewById(R.id.address_marker_tv);
        address_street_tv = findViewById(R.id.address_street_tv);
        description_tv = findViewById(R.id.description_tv);
        remark_tv = findViewById(R.id.remark_tv);
        photo_iuv = findViewById(R.id.photo_iuv);
        go_shop_h5_btn_tv = findViewById(R.id.go_shop_h5_btn_tv);
        go_travel_note_list_btn_tv = findViewById(R.id.go_travel_note_list_btn_tv);
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void init() {
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

        swipe_refresh_layout.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh_layout.setRefreshing(true);
                onRefresh();
            }
        });

        photo_iuv.init(this);

        initEvent();
    }

    private void initEvent() {
        address_marker_tv.setOnClickListener(this);
        go_shop_h5_btn_tv.setOnClickListener(this);
        go_travel_note_list_btn_tv.setOnClickListener(this);
    }

    @Override
    protected void setupActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.mvp_shop_detail_title);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.address_marker_tv) {
            mPresenter.showMarkerInMap();
        } else if (v.getId() == R.id.go_shop_h5_btn_tv) {
            mPresenter.goToShopH5Activity();
        } else if (v.getId() == R.id.go_travel_note_list_btn_tv) {
            mPresenter.goToTravelNoteListActivity();
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.loadShopDetailData();
    }

    @Override
    public void setupShopDetail(MvpShopDetailEntity entity) {
        name_tv.setText(entity.getName());
        type_tv.setText(entity.getTypeName());
        online_date_tv.setText(entity.getOnlineDate());
        contact_tv.setText(entity.getMobile());
        address_district_tv.setText(entity.getAddressDistrict());
        address_marker_tv.setText(entity.getLatitude() + "," + entity.getLongitude());
        address_street_tv.setText(entity.getAddressStreet());
        description_tv.setText(entity.getDescription());
        remark_tv.setText(entity.getRemark());
        photo_iuv.setRemoteImages(entity.getImgUrls(), ",");
    }

    @Override
    public void setLoadingUiVisibility(boolean processing) {
        if (swipe_refresh_layout == null) {
            return;
        }
        swipe_refresh_layout.setRefreshing(processing);
    }
}
