package com.pine.mvp.bean;

import com.pine.base.adapter.BaseListAdapterItemEntity;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomePartAEntity extends BaseListAdapterItemEntity {

    /**
     * title :
     * distance :
     */

    private String title;
    private String distance;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
