package com.pine.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseListViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseListViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void updateData(T content, BaseListAdapterItemPropertyEntity propertyEntity, int position);
}
