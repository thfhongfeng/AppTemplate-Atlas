package com.pine.base.list.bean;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class BaseListAdapterItemProperty {
    // item类别
    private int itemViewType = -10000;
    // item是否需要显示
    private boolean isItemViewNeedShow = true;
    // item下属数量(用于多级列表)
    private int subItemViewCount;
    // item下属是否展开
    private boolean isItemViewSpread = true;

    public int getItemViewType() {
        return itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public boolean isItemViewNeedShow() {
        return isItemViewNeedShow;
    }

    public void setItemViewNeedShow(boolean itemViewNeedShow) {
        isItemViewNeedShow = itemViewNeedShow;
    }

    public int getSubItemViewCount() {
        return subItemViewCount;
    }

    public void setSubItemViewCount(int subItemViewCount) {
        this.subItemViewCount = subItemViewCount;
    }

    public boolean isItemViewSpread() {
        return isItemViewSpread;
    }

    public void setItemViewSpread(boolean itemViewSpread) {
        isItemViewSpread = itemViewSpread;
    }
}
