package com.pine.base.component.map;

import android.content.Context;
import android.content.Intent;

/**
 * Created by tanghongfeng on 2018/10/31
 */

public interface IMapManager {
    void startLocation();

    void stopLocation();

    void registerLocationListener(ILocationListener locationListener);

    void unregisterLocationListener(ILocationListener locationListener);

    LocationInfo getLocation();

    Intent getMapActivityIntent(Context context);

    Intent getMapActivityIntent(Context context, MapSdkManager.MapType type);

    Intent getMarkMapActivityIntent(Context context, double latitude, double longitude,
                                    boolean canMark);

    /**
     * @param context
     * @param type
     * @param latitude  Gcj_02坐标系 纬度
     * @param longitude Gcj_02坐标系 经度
     * @param canMark   是否可以标记
     * @return
     */
    Intent getMarkMapActivityIntent(Context context, MapSdkManager.MapType type,
                                    double latitude, double longitude, boolean canMark);
}
