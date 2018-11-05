package com.pine.base.component.map.baidu;

import android.content.Context;
import android.content.Intent;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.pine.base.component.map.ILocationListener;
import com.pine.base.component.map.IMapManager;
import com.pine.base.component.map.LocationInfo;
import com.pine.base.component.map.MapSdkManager;
import com.pine.base.component.map.baidu.location.BdLocationManager;
import com.pine.base.component.map.baidu.ui.BaiduMapActivity;
import com.pine.tool.util.LogUtils;

/**
 * Created by tanghongfeng on 2018/10/31
 */

public class BaiduMapManager implements IMapManager {
    private final static String TAG = LogUtils.makeLogTag(BaiduMapManager.class);

    private static BaiduMapManager mInstance;
    private ILocationListener mLocationCallback;
    private BDAbstractLocationListener mLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null && (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                    || bdLocation.getLocType() == BDLocation.TypeOffLineLocation)) {
                BdLocationManager.getInstance().unregisterListener(mLocationListener);
                BdLocationManager.getInstance().stop();
                BdLocationManager.getInstance().setLocation(bdLocation);
                if (mLocationCallback != null) {
                    LocationInfo locationInfo = new LocationInfo();
                    locationInfo.setLatitude(bdLocation.getLatitude());
                    locationInfo.setLongitude(bdLocation.getLongitude());
                    mLocationCallback.onReceiveLocation(locationInfo);
                }
            } else if (mLocationCallback != null) {
                mLocationCallback.onReceiveFail();
            }
        }
    };

    public static IMapManager getInstance() {
        if (mInstance == null) {
            synchronized (BaiduMapManager.class) {
                if (mInstance == null) {
                    LogUtils.releaseLog(TAG, "use third map: baidu");
                    mInstance = new BaiduMapManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void startLocation() {
        BdLocationManager.getInstance().start();
    }

    @Override
    public void stopLocation() {
        BdLocationManager.getInstance().stop();
    }

    @Override
    public void registerLocationListener(ILocationListener locationListener) {
        mLocationCallback = locationListener;
        BdLocationManager.getInstance().registerListener(mLocationListener);
    }

    @Override
    public void unregisterLocationListener(ILocationListener locationListener) {
        mLocationCallback = null;
        BdLocationManager.getInstance().unregisterListener(mLocationListener);
    }

    @Override
    public LocationInfo getLocation() {
        BDLocation location = BdLocationManager.getInstance().getLocation();
        if (location == null) {
            return null;
        }
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setLatitude(location.getLatitude());
        locationInfo.setLongitude(location.getLongitude());
        return locationInfo;
    }

    @Override
    public Intent getMapActivityIntent(Context context) {
        return getMapActivityIntent(context, MapSdkManager.MapType.MAP_TYPE_NORMAL, -1, -1);
    }

    @Override
    public Intent getMapActivityIntent(Context context, MapSdkManager.MapType mapType) {
        return getMapActivityIntent(context, mapType, -1, -1);
    }

    @Override
    public Intent getMapActivityIntent(Context context, MapSdkManager.MapType mapType,
                                       double latitude, double longitude) {
        Intent intent = new Intent(context, BaiduMapActivity.class);
        intent.putExtra("mapTypeOrdinal", mapType.ordinal());
        if (latitude != -1 && longitude != -1) {
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
        }
        return intent;
    }
}
