package com.pine.base.map;

import com.pine.base.BuildConfig;
import com.pine.base.map.baidu.BaiduMapManager;

/**
 * Created by tanghongfeng on 2018/10/31
 */

public class MapSdkManager {
    private MapSdkManager() {

    }

    public static IMapManager getInstance() {
        switch (BuildConfig.APP_THIRD_MAP_PROVIDER) {
            case "baidu":
                return BaiduMapManager.getInstance();
            default:
                return BaiduMapManager.getInstance();
        }
    }

    public enum MapType {
        MAP_TYPE_NORMAL,
        MAP_TYPE_SATELLITE,
        MAP_TYPE_NONE
    }
}
