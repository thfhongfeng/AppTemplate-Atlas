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
import com.pine.base.list.bean.BaseListAdapterItemProperty;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tanghongfeng on 2018/10/22
 */

public abstract class BaseComplexListAdapter<T, B> extends RecyclerView.Adapter<BaseListViewHolder> {
    protected final static int EMPTY_BACKGROUND_VIEW_HOLDER = -10000;
    protected final static int MORE_VIEW_HOLDER = -10001;
    protected final static int COMPLETE_VIEW_HOLDER = -10002;
    // 1: 表示第一页（计数从1开始）
    protected AtomicInteger mPageNo = new AtomicInteger(1);
    protected AtomicInteger mPageSize = new AtomicInteger(10);
    protected Boolean mHasMore = true;
    protected List<BaseListAdapterItemEntity<T>> mHeadNoPaginationData = null;
    protected List<BaseListAdapterItemEntity<B>> mTailPaginationData = null;
    private boolean mIsInitState = true;
    private boolean mShowEmpty = true;
    private boolean mShowMore = true;
    private boolean mShowComplete = true;

    public BaseComplexListAdapter() {

    }

    public static boolean isLastViewMoreView(RecyclerView recyclerView) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        return recyclerView.getAdapter().getItemViewType(manager.findLastVisibleItemPosition()) == MORE_VIEW_HOLDER;
    }

    public void showEmptyMoreComplete(boolean showEmptyView, boolean showMoreView, boolean showCompleteView) {
        mShowEmpty = showEmptyView;
        mShowMore = showMoreView;
        mShowComplete = showCompleteView;
    }

    public BaseListViewHolder<String> getCompleteViewHolder(ViewGroup parent) {
        return new CompleteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.base_item_complete, parent, false));
    }

    public BaseListViewHolder<String> getMoreViewHolder(ViewGroup parent) {
        return new MoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.base_item_more, parent, false));
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
            case EMPTY_BACKGROUND_VIEW_HOLDER:
                viewHolder = getEmptyBackgroundViewHolder(parent);
                break;
            case MORE_VIEW_HOLDER:
                viewHolder = getMoreViewHolder(parent);
                break;
            case COMPLETE_VIEW_HOLDER:
                viewHolder = getCompleteViewHolder(parent);
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
            holder.updateData("", new BaseListAdapterItemProperty(), position);
            return;
        }
        if (isMoreView(position)) {
            holder.updateData("", new BaseListAdapterItemProperty(), position);
            return;
        }
        if (isCompleteView(position)) {
            holder.updateData("", new BaseListAdapterItemProperty(), position);
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
        if (topSize == 0 && bottomSize == 0 && mShowEmpty) {
            return 1;
        }
        int actualSize = topSize + bottomSize;
        if (hasMoreView() || hasCompleteView()) {
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
        if (isMoreView(position)) {
            return MORE_VIEW_HOLDER;
        }
        if (isCompleteView(position)) {
            return COMPLETE_VIEW_HOLDER;
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
            notifyDataSetChanged();
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

    private boolean hasMoreView() {
        return mShowMore && mHasMore && mTailPaginationData != null && mTailPaginationData.size() != 0;
    }

    private boolean hasCompleteView() {
        return mShowComplete && !mHasMore && mTailPaginationData != null && mTailPaginationData.size() != 0;
    }

    private boolean isMoreView(int position) {
        int topSize = mHeadNoPaginationData == null ? 0 : mHeadNoPaginationData.size();
        int bottomSize = mTailPaginationData == null ? 0 : mTailPaginationData.size();
        return mShowMore && mHasMore && position != 0 && position == topSize + bottomSize;
    }

    private boolean isCompleteView(int position) {
        int topSize = mHeadNoPaginationData == null ? 0 : mHeadNoPaginationData.size();
        int bottomSize = mTailPaginationData == null ? 0 : mTailPaginationData.size();
        return mShowComplete && !mHasMore && position != 0 && position == topSize + bottomSize;
    }

    public void resetAndGetPageNo() {
        mPageNo.set(1);
    }

    public int getPageNo() {
        return mPageNo.get();
    }

    public int getNextPageNo() {
        return mPageNo.get() + 1;
    }

    public int getPageSize() {
        return mPageSize.get();
    }

    public final boolean mIsInitState() {
        return mIsInitState;
    }

    public abstract List<BaseListAdapterItemEntity<T>> parseHeadData(List<T> data);

    public abstract List<BaseListAdapterItemEntity<B>> parseTailData(List<B> data);

    public abstract BaseListViewHolder getViewHolder(ViewGroup parent, int viewType);

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
            container = itemView.findViewById(R.id.container);
        }

        @Override
        public void updateData(String tipsValue, BaseListAdapterItemProperty propertyEntity, int position) {
            if (!TextUtils.isEmpty(tipsValue)) {
                tips.setText(tipsValue);
            }
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT);
            container.setLayoutParams(params);
        }
    }

    /**
     * 加载更多holder
     *
     * @param
     */
    public class MoreViewHolder extends BaseListViewHolder<String> {
        private TextView more_tv;

        public MoreViewHolder(View itemView) {
            super(itemView);
            itemView.setTag("more");
            more_tv = itemView.findViewById(R.id.more_tv);
        }

        @Override
        public void updateData(String content, BaseListAdapterItemProperty propertyEntity, int position) {

        }
    }

    /**
     * 全部加载完成holder
     *
     * @param
     */
    public class CompleteViewHolder extends BaseListViewHolder<String> {
        private TextView complete_tv;

        public CompleteViewHolder(View itemView) {
            super(itemView);
            itemView.setTag("complete");
            complete_tv = itemView.findViewById(R.id.complete_tv);
        }

        @Override
        public void updateData(String content, BaseListAdapterItemProperty propertyEntity, int position) {

        }
    }
}
