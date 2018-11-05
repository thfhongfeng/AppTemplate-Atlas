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

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseNoPaginationTreeListAdapter<T> extends RecyclerView.Adapter<BaseListViewHolder> {
    protected final static int EMPTY_BACKGROUND_VIEW_HOLDER = -10000;
    protected List<BaseListAdapterItemEntity<T>> mData = null;
    private boolean mIsInitState = true;
    private int mTreeListType = -1;

    public BaseNoPaginationTreeListAdapter(int treeListType) {
        mTreeListType = treeListType;
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
        holder.updateData(mData.get(position).getData(), mData.get(position).getPropertyEntity(), position);
    }

    @Override
    public int getItemCount() {
        if (mIsInitState()) {
            return 0;
        }
        if (mData == null || mData.size() == 0) {
            return 1;
        }
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null || mData.size() == 0) {
            return EMPTY_BACKGROUND_VIEW_HOLDER;
        }
        BaseListAdapterItemEntity itemEntity = mData.get(position);
        return itemEntity.getPropertyEntity().getItemViewType();
    }

    public final void setData(List<T> data) {
        mIsInitState = false;
        mData = parseTreeData(data, true);
        notifyDataSetChanged();
    }

    public final void addData(List<T> newData) {
        List<BaseListAdapterItemEntity<T>> parseData = parseTreeData(newData, false);
        if (parseData == null || parseData.size() == 0) {
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

    public List<BaseListAdapterItemEntity<T>> getAdapterData() {
        return mData;
    }

    public int getTreeListType() {
        return mTreeListType;
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
}
