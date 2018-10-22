package com.pine.base.list.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.bean.BaseListAdapterItemEntity;
import com.pine.base.list.bean.BaseListAdapterItemPropertyEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tanghongfeng on 2018/10/22
 */

public abstract class BaseComplexListAdapter extends RecyclerView.Adapter<BaseListViewHolder> {
    protected final static int EMPTY_BACKGROUND_VIEW_HOLDER = -10000;
    protected final static int FOOTER_VIEW_HOLDER = -10001;
    // 1: 表示第一页（计数从1开始）
    protected AtomicInteger mPageNo = new AtomicInteger(1);
    protected AtomicInteger mPageSize = new AtomicInteger(10);
    protected Boolean mHasMore = true;
    protected List<BaseListAdapterItemEntity<? extends Object>> mTopNoPaginationData = null;
    protected List<BaseListAdapterItemEntity<? extends Object>> mBottomPaginationData = null;
    private boolean mIsInitState = true;

    public BaseComplexListAdapter() {

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

    @NonNull
    @Override
    public BaseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull BaseListViewHolder holder, int position) {
        int topSize = mTopNoPaginationData == null ? 0 : mTopNoPaginationData.size();
        int bottomSize = mBottomPaginationData == null ? 0 : mBottomPaginationData.size();
        if (topSize == 0 && bottomSize == 0) {
            holder.updateData("", new BaseListAdapterItemPropertyEntity(), position);
            return;
        }
        if (isFooterView(mHasMore, position, topSize, bottomSize)) {
            holder.updateData(mHasMore, new BaseListAdapterItemPropertyEntity(), position);
            return;
        }
        if (mTopNoPaginationData != null && position < mTopNoPaginationData.size()) {
            holder.updateData(mTopNoPaginationData.get(position).getData(), mTopNoPaginationData.get(position).getPropertyEntity(), position);
        } else {
            int index = position - mTopNoPaginationData.size();
            holder.updateData(mBottomPaginationData.get(index).getData(), mBottomPaginationData.get(index).getPropertyEntity(), index);
        }
    }

    @Override
    public int getItemCount() {
        if (mIsInitState) {
            return 0;
        }
        int topSize = mTopNoPaginationData == null ? 0 : mTopNoPaginationData.size();
        int bottomSize = mBottomPaginationData == null ? 0 : mBottomPaginationData.size();
        if (topSize == 0 && bottomSize == 0) {
            return 1;
        }
        int actualSize = topSize + bottomSize;
        if (hasFooterView(mHasMore, bottomSize)) {
            return actualSize + 1;
        }
        return actualSize;
    }

    @Override
    public final int getItemViewType(int position) {
        int topSize = mTopNoPaginationData == null ? 0 : mTopNoPaginationData.size();
        int bottomSize = mBottomPaginationData == null ? 0 : mBottomPaginationData.size();
        if (topSize == 0 && bottomSize == 0) {
            return EMPTY_BACKGROUND_VIEW_HOLDER;
        }
        if (isFooterView(true, position, topSize, bottomSize)) {
            return FOOTER_VIEW_HOLDER;
        }
        if (mTopNoPaginationData != null && position < mTopNoPaginationData.size()) {
            return mTopNoPaginationData.get(position).getPropertyEntity().getItemViewType();
        } else {
            int index = position - mTopNoPaginationData.size();
            return mBottomPaginationData.get(index).getPropertyEntity().getItemViewType();
        }
    }

    public final void addBottomData(List<? extends Object> newData) {
        List<BaseListAdapterItemEntity<? extends Object>> parseData = parseBottomData(newData);
        if (parseData == null || parseData.size() == 0) {
            mHasMore = false;
            return;
        }
        if (mBottomPaginationData == null) {
            mIsInitState = false;
            mBottomPaginationData = parseData;
            resetAndGetPageNo();
        } else {
            for (int i = 0; i < parseData.size(); i++) {
                mBottomPaginationData.add(parseData.get(i));
            }
            mPageNo.incrementAndGet();
        }
        mHasMore = parseData.size() >= getPageSize();
        notifyDataSetChanged();
    }

    public final void setTopData(List<? extends Object> data) {
        mIsInitState = false;
        mTopNoPaginationData = parseTopData(data);
        notifyDataSetChanged();
    }

    public final void setBottomData(List<? extends Object> data) {
        mIsInitState = false;
        mBottomPaginationData = parseBottomData(data);
        resetAndGetPageNo();
        mHasMore = mBottomPaginationData != null && mBottomPaginationData.size() >= getPageSize();
        notifyDataSetChanged();
    }

    private boolean hasFooterView(boolean hasMore, int dataSize) {
        return hasMore && dataSize >= getPageSize();
    }

    private boolean isFooterView(boolean hasMore, int position, int topDataSize, int bottomDataSize) {
        return hasMore && bottomDataSize >= getPageSize() && position == topDataSize + bottomDataSize;
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

    public abstract List<BaseListAdapterItemEntity<? extends Object>> parseTopData(List<? extends Object> data);

    public abstract List<BaseListAdapterItemEntity<? extends Object>> parseBottomData(List<? extends Object> data);

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
        public void updateData(Boolean show, BaseListAdapterItemPropertyEntity propertyEntity, int position) {
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
        public void updateData(String tipsValue, BaseListAdapterItemPropertyEntity propertyEntity, int position) {
            if (!TextUtils.isEmpty(tipsValue)) {
                tips.setText(tipsValue);
            }
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT);
            container.setLayoutParams(params);
        }
    }
}
