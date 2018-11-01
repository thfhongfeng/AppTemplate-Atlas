package com.pine.base.map.baidu.location;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.pine.base.BaseApplication;
import com.pine.tool.util.LogUtils;

/**
 * Created by tanghongfeng on 2018/10/10
 */

public class BdLocationManager {
    private final static String TAG = LogUtils.makeLogTag(BdLocationManager.class);
    private static volatile BdLocationManager mInstance;
    private LocationClient mClient = null;
    private LocationClientOption mOption, mDiyOption;
    private BDLocation mLocation;

    private BdLocationManager() {
        initLocation();
    }

    public static BdLocationManager getInstance() {
        if (mInstance == null) {
            synchronized (BdLocationManager.class) {
                if (mInstance == null) {
                    mInstance = new BdLocationManager();
                }
            }
        }
        return mInstance;
    }

    private void initLocation() {
        if (mClient == null) {
            mClient = new LocationClient(BaseApplication.mApplication);
            mClient.setLocOption(getDefaultLocationClientOption());
        }
    }

    /***
     *
     * @return DefaultLocationClientOption  默认O设置
     */
    private LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
            // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setCoorType("bd09ll");
            // 可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setScanSpan(0);
            // 可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedAddress(true);
            // 可选，设置是否需要地址描述
            mOption.setIsNeedLocationDescribe(true);
            // 可选，设置是否需要设备方向结果
            mOption.setNeedDeviceDirect(false);
            // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setLocationNotify(false);
            // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIgnoreKillProcess(true);
            // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationDescribe(true);
            // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption.setIsNeedLocationPoiList(true);
            // 可选，默认false，设置是否收集CRASH信息，默认收集
            mOption.SetIgnoreCacheException(false);
            // 可选，默认false，设置是否开启Gps定位
            mOption.setOpenGps(false);
            // 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
            mOption.setIsNeedAltitude(false);

        }
        return mOption;
    }

    public boolean registerListener(BDAbstractLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            mClient.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDAbstractLocationListener listener) {
        if (listener != null) {
            mClient.unRegisterLocationListener(listener);
        }
    }

    public boolean setLocationOption(LocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (mClient.isStarted()) {
                mClient.stop();
            }
            mDiyOption = option;
            mClient.setLocOption(option);
        }
        return isSuccess;
    }

    public synchronized boolean start() {
        if (mClient != null && !mClient.isStarted()) {
            mClient.start();
            return true;
        }
        return false;
    }

    public synchronized void stop() {
        if (mClient != null && mClient.isStarted()) {
            mClient.stop();
        }
    }

    public boolean isStart() {
        return mClient.isStarted();
    }

    public boolean requestHotSpotState() {
        return mClient.requestHotSpotState();
    }

    public BDLocation getLocation() {
        return mLocation;
    }

    public void setLocation(BDLocation location) {
        mLocation = location;
    }

}
