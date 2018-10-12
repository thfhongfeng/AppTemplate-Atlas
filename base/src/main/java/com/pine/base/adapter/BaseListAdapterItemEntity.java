package com.pine.base.adapter;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class BaseListAdapterItemEntity<T> {
    private T data;

    private BaseListAdapterPropertyEntity propertyEntity = new BaseListAdapterPropertyEntity();

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseListAdapterPropertyEntity getPropertyEntity() {
        return propertyEntity;
    }
}
