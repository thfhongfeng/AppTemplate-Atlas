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
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BasePaginationTreeListAdapter<T> extends RecyclerView.Adapter<BaseListViewHolder> {
    protected final static int EMPTY_BACKGROUND_VIEW_HOLDER = -10000;
    protected final static int MORE_VIEW_HOLDER = -10001;
    protected final static int COMPLETE_VIEW_HOLDER = -10002;
    // 1: 表示第一页（计数从1开始）
    protected AtomicInteger mPageNo = new AtomicInteger(1);
    protected AtomicInteger mPageSize = new AtomicInteger(10);
    protected Boolean mHasMore = true;
    protected List<BaseListAdapterItemEntity<T>> mData = null;
    private boolean mShowEmpty = true;
    private boolean mShowMore = true;
    private boolean mShowComplete = true;
    private boolean mIsInitState = true;

    public BasePaginationTreeListAdapter() {

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
    public void onBindViewHolder(BaseListViewHolder holder, int position) {
        if (mData == null || mData.size() == 0) {
            holder.updateData("", new BaseListAdapterItemPropertyEntity(), position);
            return;
        }
        if (isMoreView(position)) {
            holder.updateData("", new BaseListAdapterItemPropertyEntity(), position);
            return;
        }
        if (isCompleteView(position)) {
            holder.updateData("", new BaseListAdapterItemPropertyEntity(), position);
            return;
        }
        holder.updateData(mData.get(position).getData(), mData.get(position).getPropertyEntity(), position);
    }

    @Override
    public int getItemCount() {
        if (mIsInitState()) {
            return 0;
        }
        if ((mData == null || mData.size() == 0) && mShowEmpty) {
            return 1;
        }
        int actualSize = mData.size();
        if (hasMoreView() || hasCompleteView()) {
            return actualSize + 1;
        }
        return actualSize;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null || mData.size() == 0) {
            return EMPTY_BACKGROUND_VIEW_HOLDER;
        }
        if (isMoreView(position)) {
            return MORE_VIEW_HOLDER;
        }
        if (isCompleteView(position)) {
            return COMPLETE_VIEW_HOLDER;
        }
        BaseListAdapterItemEntity itemEntity = mData.get(position);
        return itemEntity.getPropertyEntity().getItemViewType();
    }

    private boolean hasMoreView() {
        return mShowMore && mHasMore && mData != null && mData.size() != 0;
    }

    private boolean hasCompleteView() {
        return mShowComplete && !mHasMore && mData != null && mData.size() != 0;
    }

    private boolean isMoreView(int position) {
        return mShowMore && mHasMore && position != 0 && position == mData.size();
    }

    private boolean isCompleteView(int position) {
        return mShowComplete && !mHasMore && position != 0 && position == mData.size();
    }

    public final void addData(List<T> newData) {
        List<BaseListAdapterItemEntity<T>> parseData = parseTreeData(newData, false);
        if (parseData == null || parseData.size() == 0) {
            mHasMore = false;
            notifyDataSetChanged();
            return;
        }
        if (mData == null) {
            mIsInitState = false;
            mData = parseData;
            resetAndGetPageNo();
        } else {
            for (int i = 0; i < parseData.size(); i++) {
                mData.add(parseData.get(i));
            }
            mPageNo.incrementAndGet();
        }
        mHasMore = parseData.size() >= getPageSize();
        notifyDataSetChanged();
    }

    public final void setData(List<T> data) {
        mIsInitState = false;
        mData = parseTreeData(data, true);
        resetAndGetPageNo();
        mHasMore = mData != null && mData.size() >= getPageSize();
        notifyDataSetChanged();
    }

    public List<BaseListAdapterItemEntity<T>> getAdapterData() {
        return mData;
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

    public abstract List<BaseListAdapterItemEntity<T>> parseTreeData(List<T> data, boolean reset);

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
        public void updateData(String tipsValue, BaseListAdapterItemPropertyEntity propertyEntity, int position) {
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
        public void updateData(String content, BaseListAdapterItemPropertyEntity propertyEntity, int position) {

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
        public void updateData(String content, BaseListAdapterItemPropertyEntity propertyEntity, int position) {

        }
    }
}
