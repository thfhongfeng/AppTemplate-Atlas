package com.pine.base.list.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseNoPaginationListAdapter<T> extends RecyclerView.Adapter<BaseListViewHolder> {
    protected final static int EMPTY_BACKGROUND_VIEW_HOLDER = -10000;
    protected final static int COMPLETE_VIEW_HOLDER = -10001;
    protected List<BaseListAdapterItemEntity<T>> mData = null;
    private boolean mIsInitState = true;
    private boolean mShowEmpty = true;
    private boolean mShowComplete = true;
    private int mDefaultItemViewType = EMPTY_BACKGROUND_VIEW_HOLDER;

    public BaseNoPaginationListAdapter(int defaultItemViewType) {
        mDefaultItemViewType = defaultItemViewType;
    }

    public void showEmptyComplete(boolean showEmptyView, boolean showCompleteView) {
        mShowEmpty = showEmptyView;
        mShowComplete = showCompleteView;
    }

    public BaseListViewHolder<String> getCompleteViewHolder(ViewGroup parent) {
        return new CompleteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.base_item_complete, parent, false));
    }

    public BaseListViewHolder<String> getEmptyBackgroundViewHolder(ViewGroup parent) {
        return new EmptyBackgroundViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.base_item_empty_background, parent, false));
    }

    @NonNull
    @Override
    public BaseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case EMPTY_BACKGROUND_VIEW_HOLDER:
                viewHolder = getEmptyBackgroundViewHolder(parent);
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
        if (hasCompleteView()) {
            return actualSize + 1;
        }
        return actualSize;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null || mData.size() == 0) {
            return EMPTY_BACKGROUND_VIEW_HOLDER;
        }
        if (isCompleteView(position)) {
            return COMPLETE_VIEW_HOLDER;
        }
        BaseListAdapterItemEntity itemEntity = mData.get(position);
        return itemEntity.getPropertyEntity().getItemViewType();
    }

    private boolean hasCompleteView() {
        return mShowComplete && mData != null && mData.size() != 0;
    }

    private boolean isCompleteView(int position) {
        return mShowComplete && position != 0 && position == mData.size();
    }

    public final void setData(List<T> data) {
        mIsInitState = false;
        mData = parseData(data, true);
        notifyDataSetChanged();
    }

    public final void addData(List<T> newData) {
        List<BaseListAdapterItemEntity<T>> parseData = parseData(newData, false);
        if (parseData == null || parseData.size() == 0) {
            notifyDataSetChanged();
            return;
        }
        if (mData == null) {
            mIsInitState = false;
            mData = parseData;
        } else {
            for (int i = 0; i < parseData.size(); i++) {
                mData.add(parseData.get(i));
            }
        }
        notifyDataSetChanged();
    }

    protected List<BaseListAdapterItemEntity<T>> parseData(List<T> data, boolean reset) {
        List<BaseListAdapterItemEntity<T>> adapterData = new ArrayList<>();
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

    public List<BaseListAdapterItemEntity<T>> getAdapterData() {
        return mData;
    }

    public int getDefaultItemViewType() {
        return mDefaultItemViewType;
    }

    public final boolean mIsInitState() {
        return mIsInitState;
    }

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
