package com.pine.base.list.bean;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class BaseListAdapterItemEntity<T> {
    private T data;

    private BaseListAdapterItemProperty propertyEntity = new BaseListAdapterItemProperty();

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseListAdapterItemProperty getPropertyEntity() {
        return propertyEntity;
    }
}
