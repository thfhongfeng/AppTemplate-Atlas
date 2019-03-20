package com.pine.base.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pine.base.list.bean.BaseListAdapterItemProperty;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseListViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseListViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void updateData(T content, BaseListAdapterItemProperty propertyEntity, int position);
}
