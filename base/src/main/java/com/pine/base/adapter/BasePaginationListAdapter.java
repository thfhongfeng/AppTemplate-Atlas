package com.pine.base.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pine.base.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BasePaginationListAdapter extends RecyclerView.Adapter<BaseListViewHolder> {
    protected final static int FOOTER_VIEW_HOLDER = -100;
    protected final static int EMPTY_BACKGROUND_VIEW_HOLDER = -1000;
    // 1: 表示第一页（计数从1开始）
    protected AtomicInteger mPageNo = new AtomicInteger(1);
    protected AtomicInteger mPageSize = new AtomicInteger(10);
    protected Boolean mHasMore = true;
    protected List<BaseListAdapterItemEntity<? extends Object>> mData = null;
    private boolean mIsInitState = true;
    private int mDefaultItemViewType = EMPTY_BACKGROUND_VIEW_HOLDER;
    private boolean mIsComplexList;

    public BasePaginationListAdapter(int defaultItemViewType, boolean isComplexList) {
        mDefaultItemViewType = defaultItemViewType;
        mIsComplexList = isComplexList;
    }

    public static boolean isLastVisibleViewFooter(RecyclerView recyclerView) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        return recyclerView.getAdapter().getItemViewType(manager.findLastVisibleItemPosition()) == FOOTER_VIEW_HOLDER;
    }

    public BaseListViewHolder<Boolean> getFooterViewHolder(ViewGroup parent) {
        return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.base_item_footer, parent, false));
    }

    public BaseListViewHolder<String> getEmptyBackgroundViewHolder(ViewGroup parent) {
        return new EmptyBackgroundViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.base_item_empty_background, parent, false));
    }

    @Override
    public BaseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case FOOTER_VIEW_HOLDER:
                viewHolder = getFooterViewHolder(parent);
                break;
            case EMPTY_BACKGROUND_VIEW_HOLDER:
                viewHolder = getEmptyBackgroundViewHolder(parent);
                break;
            default:
                viewHolder = getViewHolder(parent, viewType);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseListViewHolder holder, int position) {
        if (mData == null || mData.size() == 0) {
            holder.updateData("", new BaseListAdapterPropertyEntity(), position);
            return;
        }
        if (isFooterView(mHasMore, position, mData.size())) {
            holder.updateData(mHasMore, new BaseListAdapterPropertyEntity(), position);
            return;
        }
        holder.updateData(mData.get(position).getData(), mData.get(position).getPropertyEntity(), position);
    }

    @Override
    public int getItemCount() {
        if (mIsInitState) {
            return 0;
        }
        if (mData == null || mData.size() == 0) {
            return 1;
        }
        int actualSize = mData.size();
        if (hasFooterView(mHasMore, actualSize)) {
            return actualSize + 1;
        }
        return actualSize;
    }

    @Override
    public final int getItemViewType(int position) {
        if (mData == null || mData.size() == 0) {
            return EMPTY_BACKGROUND_VIEW_HOLDER;
        }
        if (isFooterView(true, position, mData.size())) {
            return FOOTER_VIEW_HOLDER;
        }
        BaseListAdapterItemEntity itemEntity = mData.get(position);
        return itemEntity != null && itemEntity.getPropertyEntity().getItemViewType() != -10000 ?
                itemEntity.getPropertyEntity().getItemViewType() : mDefaultItemViewType;
    }

    private boolean hasFooterView(boolean hasMore, int dataSize) {
        return hasMore && dataSize >= getPageSize();
    }

    private boolean isFooterView(boolean hasMore, int position, int dataSize) {
        return hasMore && dataSize >= getPageSize() && position == dataSize;
    }

    public final void addData(List<? extends Object> newData) {
        if (newData == null || newData.size() == 0) {
            mHasMore = false;
            return;
        }
        List<BaseListAdapterItemEntity<? extends Object>> parseData;
        if (!mIsComplexList) {
            parseData = parseData(newData);
        } else {
            parseData = parseComplexData(newData);
        }
        for (int i = 0; i < parseData.size(); i++) {
            mData.add(parseData.get(i));
        }
        mHasMore = newData.size() >= getPageSize();
        mPageNo.incrementAndGet();
        notifyDataSetChanged();
    }

    public final void setData(List<? extends Object> data) {
        mIsInitState = false;
        if (!mIsComplexList) {
            mData = parseData(data);
        } else {
            mData = parseComplexData(data);
        }
        resetAndGetPageNo();
        mHasMore = data != null && data.size() >= getPageSize();
        notifyDataSetChanged();
    }

    protected List<BaseListAdapterItemEntity<? extends Object>> parseData(List<? extends Object> data) {
        List<BaseListAdapterItemEntity<? extends Object>> adapterData = new ArrayList<>();
        if (data != null) {
            BaseListAdapterItemEntity adapterEntity;
            for (int i = 0; i < data.size(); i++) {
                adapterEntity = new BaseListAdapterItemEntity();
                adapterEntity.setData(data.get(i));
                adapterEntity.getPropertyEntity().setItemViewType(mDefaultItemViewType);
                adapterData.add(adapterEntity);
            }
        }
        return adapterData;
    }

    public void resetAndGetPageNo() {
        mPageNo.set(1);
    }

    public int getPageNo() {
        return mPageNo.get();
    }

    public int getPageSize() {
        return mPageSize.get();
    }

    public int getDefaultItemViewType() {
        return mDefaultItemViewType;
    }

    public abstract List<BaseListAdapterItemEntity<? extends Object>> parseComplexData(List<? extends Object> data);

    public abstract BaseListViewHolder getViewHolder(ViewGroup parent, int viewType);

    /**
     * 底部holder
     *
     * @param
     */
    public class FooterViewHolder extends BaseListViewHolder<Boolean> {
        private TextView footer_tv;

        public FooterViewHolder(View itemView) {
            super(itemView);
            itemView.setTag("footer");
            footer_tv = (TextView) itemView.findViewById(R.id.footer_tv);
        }

        @Override
        public void updateData(Boolean show, BaseListAdapterPropertyEntity propertyEntity, int position) {
            if (show) {
                footer_tv.setVisibility(View.VISIBLE);
            } else {
                footer_tv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 空背景
     */
    public class EmptyBackgroundViewHolder extends BaseListViewHolder<String> {
        private RelativeLayout container;
        private Context context;
        private TextView tips;

        public EmptyBackgroundViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            container = (RelativeLayout) itemView.findViewById(R.id.container);
        }

        @Override
        public void updateData(String tipsValue, BaseListAdapterPropertyEntity propertyEntity, int position) {
            if (!TextUtils.isEmpty(tipsValue)) {
                tips.setText(tipsValue);
            }
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT);
            container.setLayoutParams(params);
        }
    }
}
