package com.pine.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pine.base.adapter.BaseListViewHolder;
import com.pine.base.adapter.BasePaginationListAdapter;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpHomePartAEntity;
import com.pine.mvp.bean.MvpHomePartBEntity;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomeItemPaginationAdapter extends BasePaginationListAdapter {
    public static final int HOME_PART_A_VIEW_HOLDER = 1;
    public static final int HOME_PART_B_VIEW_HOLDER = 2;

    public MvpHomeItemPaginationAdapter(int defaultItemViewType) {
        super(defaultItemViewType);
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case HOME_PART_A_VIEW_HOLDER:
                viewHolder = new HomePartAViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_home_part_a, parent, false));
                break;
            case HOME_PART_B_VIEW_HOLDER:
                viewHolder = new HomePartBViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_home_part_b, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public List parseData(List data) {
        return data;
    }

    public class HomePartAViewHolder extends BaseListViewHolder<MvpHomePartAEntity> {
        private TextView title_tv;

        public HomePartAViewHolder(Context context, View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
        }

        @Override
        public void updateData(MvpHomePartAEntity content, int position) {
            title_tv.setText(content.getTitle());
        }
    }

    public class HomePartBViewHolder extends BaseListViewHolder<MvpHomePartBEntity> {
        private TextView title_tv;

        public HomePartBViewHolder(Context context, View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
        }

        @Override
        public void updateData(MvpHomePartBEntity content, int position) {
            title_tv.setText(content.getTitle());
        }
    }
}
