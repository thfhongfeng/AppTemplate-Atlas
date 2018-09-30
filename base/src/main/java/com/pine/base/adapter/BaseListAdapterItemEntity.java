package com.pine.base.adapter;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class BaseListAdapterItemEntity {
    // item类别
    private int itemViewType = -10000;
    // item是否显示
    private boolean isItemViewShow = true;
    // item下属数量(用于多级列表)
    private boolean subItemViewCount;
    // item下属是否展开
    private boolean isItemViewSpread;

    public int getItemViewType() {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public boolean isItemViewShow() {
        return isItemViewShow;
    }

    public void setItemViewShow(boolean itemViewShow) {
        isItemViewShow = itemViewShow;
    }

    public boolean isSubItemViewCount() {
        return subItemViewCount;
    }

    public void setSubItemViewCount(boolean subItemViewCount) {
        this.subItemViewCount = subItemViewCount;
    }

    public boolean isItemViewSpread() {
        return isItemViewSpread;
    }

    public void setItemViewSpread(boolean itemViewSpread) {
        isItemViewSpread = itemViewSpread;
    }
}
