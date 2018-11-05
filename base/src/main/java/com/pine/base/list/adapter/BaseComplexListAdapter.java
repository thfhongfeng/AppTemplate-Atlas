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

public abstract class BaseComplexListAdapter<T, B> extends RecyclerView.Adapter<BaseListViewHolder> {
    protected final static int EMPTY_BACKGROUND_VIEW_HOLDER = -10000;
    protected final static int FOOTER_VIEW_HOLDER = -10001;
    // 1: 表示第一页（计数从1开始）
    protected AtomicInteger mPageNo = new AtomicInteger(1);
    protected AtomicInteger mPageSize = new AtomicInteger(10);
    protected Boolean mHasMore = true;
    protected List<BaseListAdapterItemEntity<T>> mHeadNoPaginationData = null;
    protected List<BaseListAdapterItemEntity<B>> mTailPaginationData = null;
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
        int topSize = mHeadNoPaginationData == null ? 0 : mHeadNoPaginationData.size();
        int bottomSize = mTailPaginationData == null ? 0 : mTailPaginationData.size();
        if (topSize == 0 && bottomSize == 0) {
            holder.updateData("", new BaseListAdapterItemPropertyEntity(), position);
            return;
        }
        if (isFooterView(mHasMore, position, topSize, bottomSize)) {
            holder.updateData(mHasMore, new BaseListAdapterItemPropertyEntity(), position);
            return;
        }
        if (mHeadNoPaginationData != null && position < mHeadNoPaginationData.size()) {
            holder.updateData(mHeadNoPaginationData.get(position).getData(), mHeadNoPaginationData.get(position).getPropertyEntity(), position);
        } else {
            int index = position - mHeadNoPaginationData.size();
            holder.updateData(mTailPaginationData.get(index).getData(), mTailPaginationData.get(index).getPropertyEntity(), index);
        }
    }

    @Override
    public int getItemCount() {
        if (mIsInitState()) {
            return 0;
        }
        int topSize = mHeadNoPaginationData == null ? 0 : mHeadNoPaginationData.size();
        int bottomSize = mTailPaginationData == null ? 0 : mTailPaginationData.size();
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
    public int getItemViewType(int position) {
        int topSize = mHeadNoPaginationData == null ? 0 : mHeadNoPaginationData.size();
        int bottomSize = mTailPaginationData == null ? 0 : mTailPaginationData.size();
        if (topSize == 0 && bottomSize == 0) {
            return EMPTY_BACKGROUND_VIEW_HOLDER;
        }
        if (isFooterView(true, position, topSize, bottomSize)) {
            return FOOTER_VIEW_HOLDER;
        }
        if (mHeadNoPaginationData != null && position < mHeadNoPaginationData.size()) {
            return mHeadNoPaginationData.get(position).getPropertyEntity().getItemViewType();
        } else {
            int index = position - mHeadNoPaginationData.size();
            return mTailPaginationData.get(index).getPropertyEntity().getItemViewType();
        }
    }

    public final void setHeadData(List<T> data) {
        mIsInitState = false;
        mHeadNoPaginationData = parseHeadData(data);
        notifyDataSetChanged();
    }

    public final void addTailData(List<B> newData) {
        List<BaseListAdapterItemEntity<B>> parseData = parseTailData(newData);
        if (parseData == null || parseData.size() == 0) {
            mHasMore = false;
            return;
        }
        if (mTailPaginationData == null) {
            mIsInitState = false;
            mTailPaginationData = parseData;
            resetAndGetPageNo();
        } else {
            for (int i = 0; i < parseData.size(); i++) {
                mTailPaginationData.add(parseData.get(i));
            }
            mPageNo.incrementAndGet();
        }
        mHasMore = parseData.size() >= getPageSize();
        notifyDataSetChanged();
    }

    public final void setTailData(List<B> data) {
        mIsInitState = false;
        mTailPaginationData = parseTailData(data);
        resetAndGetPageNo();
        mHasMore = mTailPaginationData != null && mTailPaginationData.size() >= getPageSize();
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

    public final boolean mIsInitState() {
        return mIsInitState;
    }

    public abstract List<BaseListAdapterItemEntity<T>> parseHeadData(List<? extends Object> data);

    public abstract List<BaseListAdapterItemEntity<B>> parseTailData(List<? extends Object> data);

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
