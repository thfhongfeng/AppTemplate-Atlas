package com.pine.mvp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pine.base.adapter.BaseListViewHolder;
import com.pine.base.adapter.BaseNoPaginationListAdapter;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpHomePartCEntity;
import com.pine.mvp.ui.activity.MvpItemDetailActivity;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomeItemNoPaginationAdapter extends BaseNoPaginationListAdapter {
    public static final int HOME_PART_C_VIEW_HOLDER = 1;

    public MvpHomeItemNoPaginationAdapter(int defaultItemViewType) {
        super(defaultItemViewType);
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case HOME_PART_C_VIEW_HOLDER:
                viewHolder = new HomePartCViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_home_part_c, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public List parseData(List data) {
        return data;
    }

    public class HomePartCViewHolder extends BaseListViewHolder<MvpHomePartCEntity> {
        private Context mContext;
        private TextView title_tv;

        public HomePartCViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            title_tv = itemView.findViewById(R.id.title_tv);
        }

        @Override
        public void updateData(MvpHomePartCEntity content, int position) {
            title_tv.setText(content.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MvpItemDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
