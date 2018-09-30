package com.pine.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pine.base.R;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseNoPaginationListAdapter extends RecyclerView.Adapter<BaseListViewHolder> {
    protected final static int EMPTY_BACKGROUND_VIEW_HOLDER = -1000;
    protected List<BaseListAdapterItemEntity> mData = null;
    private boolean mIsInitState = true;
    private int mDefaultItemViewType = EMPTY_BACKGROUND_VIEW_HOLDER;

    public BaseNoPaginationListAdapter(int defaultItemViewType) {
        mDefaultItemViewType = defaultItemViewType;
    }

    public BaseListViewHolder<String> getEmptyBackgroundViewHolder(ViewGroup parent) {
        return new EmptyBackgroundViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.base_item_empty_background, parent, false));
    }

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
            holder.updateData("", position);
            return;
        }
        holder.updateData(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (mIsInitState) {
            return 0;
        }
        if (mData == null || mData.size() == 0) {
            return 1;
        }
        return mData.size();
    }

    @Override
    public final int getItemViewType(int position) {
        if (mData == null || mData.size() == 0) {
            return EMPTY_BACKGROUND_VIEW_HOLDER;
        }
        BaseListAdapterItemEntity itemEntity = mData.get(position);
        return itemEntity != null && itemEntity.getItemViewType() != -10000 ? itemEntity.getItemViewType() : mDefaultItemViewType;
    }

    public final void setData(List<? extends BaseListAdapterItemEntity> data) {
        mIsInitState = false;
        mData = parseData(data);
        notifyDataSetChanged();
    }

    public abstract BaseListViewHolder getViewHolder(ViewGroup parent, int viewType);

    public abstract List<BaseListAdapterItemEntity> parseData(List<? extends BaseListAdapterItemEntity> data);

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
        public void updateData(String tipsValue, int position) {
            if (!TextUtils.isEmpty(tipsValue)) {
                tips.setText(tipsValue);
            }
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT);
            container.setLayoutParams(params);
        }
    }
}
