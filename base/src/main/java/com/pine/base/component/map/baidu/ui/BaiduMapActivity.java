package com.pine.base.component.map.baidu.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.pine.base.R;
import com.pine.base.component.map.MapSdkManager.MapType;
import com.pine.base.component.map.baidu.location.BdLocationManager;
import com.pine.base.ui.BaseActionBarTextMenuActivity;
import com.pine.tool.util.GPSUtils;

/**
 * Created by tanghongfeng on 2018/10/31
 */

public class BaiduMapActivity extends BaseActionBarTextMenuActivity implements View.OnClickListener {
    private MapView map_view;
    private ImageView location_iv;

    private BaiduMap mBaiduMap;
    private boolean mWasLocated;
    private boolean mBaiduMapSetup;
    private LatLng mMarkerLatLng, mInitLatLng;
    private int mMapType = BaiduMap.MAP_TYPE_NORMAL;

    private BDAbstractLocationListener mLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null && (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                    || bdLocation.getLocType() == BDLocation.TypeOffLineLocation)) {
                BdLocationManager.getInstance().unregisterListener(mLocationListener);
                BdLocationManager.getInstance().stop();
                BdLocationManager.getInstance().setLocation(bdLocation);
                locationInMap();
            }
        }
    };

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.base_activity_baidu_map;
    }

    @Override
    protected void findViewOnCreate() {
        location_iv = findViewById(R.id.location_iv);
        map_view = findViewById(R.id.map_view);
        mBaiduMap = map_view.getMap();
    }

    @Override
    protected boolean parseIntentDataOnCreate() {
        int mapTypeOrdinal = getIntent().getIntExtra("mapTypeOrdinal", 0);
        switch (MapType.values()[mapTypeOrdinal]) {
            case MAP_TYPE_NORMAL:
                mMapType = BaiduMap.MAP_TYPE_NORMAL;
                break;
            case MAP_TYPE_SATELLITE:
                mMapType = BaiduMap.MAP_TYPE_SATELLITE;
                break;
            case MAP_TYPE_NONE:
                mMapType = BaiduMap.MAP_TYPE_NONE;
                break;
        }
        double latitude = getIntent().getDoubleExtra("latitude", -1);
        double longitude = getIntent().getDoubleExtra("longitude", -1);
        double[] latLon = GPSUtils.gcj02_To_Bd09(latitude, longitude);
        if (latitude != -1 && latitude != -1) {
            mInitLatLng = new LatLng(latLon[0], latLon[1]);
        }
        return false;
    }

    @Override
    protected void initOnCreate() {
        location_iv.setOnClickListener(this);

        setupBaiduMap();
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv, TextView menuBtnTv) {
        titleTv.setText(R.string.base_baidu_map_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setResult(RESULT_CANCELED);
                return;
            }
        });
        menuBtnTv.setText(R.string.base_done);
        menuBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMarkerLatLng != null) {
                    Intent intent = new Intent();
                    double[] latLon = GPSUtils.bd09_To_Gcj02(mMarkerLatLng.latitude, mMarkerLatLng.longitude);
                    intent.putExtra("latitude", latLon[0]);
                    intent.putExtra("longitude", latLon[1]);
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                } else {
                    Toast.makeText(BaiduMapActivity.this, R.string.base_baidu_map_marker_need,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void setupBaiduMap() {
        if (mBaiduMapSetup) {
            return;
        }
        mBaiduMap.setMapType(mMapType);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true,
                null);
        mBaiduMap.setMyLocationConfiguration(config);
        //地图点击事件响应
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarker(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                //点击地图上的poi图标获取描述信息：mapPoi.getName()，经纬度：mapPoi.getPosition()
                addMarker(mapPoi.getPosition());
                return false;
            }
        });
        if (mInitLatLng != null) {
            addMarker(mInitLatLng);
        }
        mBaiduMapSetup = true;
    }

    private void addMarker(LatLng latLng) {
        //点击地图某个位置获取经纬度latLng.latitude、latLng.longitude
        mMarkerLatLng = latLng;
        mBaiduMap.clear();
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.base_ic_map_marker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(mMarkerLatLng)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    @Override
    public void onResume() {
        super.onResume();
        map_view.onResume();
        if (BdLocationManager.getInstance().getLocation() == null) {
            BdLocationManager.getInstance().registerListener(mLocationListener);
            BdLocationManager.getInstance().start();
        } else {
            locationInMap();
        }
    }

    @Override
    public void onStop() {
        BdLocationManager.getInstance().unregisterListener(mLocationListener);
        BdLocationManager.getInstance().stop();
        mBaiduMap.setMyLocationEnabled(false);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        map_view.onDestroy();
        super.onDestroy();
    }

    private void locationInMap() {
        if (mWasLocated) {
            return;
        }
        if (!mBaiduMapSetup) {
            setupBaiduMap();
        }
        BDLocation location = BdLocationManager.getInstance().getLocation();
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

        // LocationMode为NORMAL时需要调用animateMapStatus来启动定位（其它mode则在setMyLocationData时会自动调用animateMapStatus，则不需要额外调用）
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu); //动画效果
        mWasLocated = true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.location_iv) {
            mWasLocated = false;
            locationInMap();
        }
    }
}
