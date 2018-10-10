package com.pine.mvp.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pine.base.adapter.BaseListViewHolder;
import com.pine.base.adapter.BasePaginationListAdapter;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpHomePartAEntity;
import com.pine.mvp.bean.MvpHomePartBEntity;
import com.pine.mvp.ui.activity.MvpItemDetailActivity;
import com.pine.tool.util.DecimalUtils;

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
        private Context mContext;
        private LinearLayout location_ll;
        private TextView title_tv, location_tv;

        public HomePartAViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            title_tv = itemView.findViewById(R.id.title_tv);
            location_ll = itemView.findViewById(R.id.location_ll);
            location_tv = itemView.findViewById(R.id.location_tv);
        }

        @Override
        public void updateData(MvpHomePartAEntity content, int position) {
            title_tv.setText(content.getTitle());
            String distanceStr = content.getDistance();
            if (TextUtils.isEmpty(distanceStr)) {
                location_tv.setText("");
                location_ll.setVisibility(View.GONE);
            } else {
                float distance = Float.parseFloat(distanceStr);
                if (distance >= 1000.0f) {
                    location_tv.setText(DecimalUtils.divide(distance, 1000.0f, 2) + mContext.getString(R.string.base_unit_kilometre));
                } else {
                    location_tv.setText(distance + mContext.getString(R.string.base_unit_metre));
                }
                location_ll.setVisibility(View.VISIBLE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MvpItemDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public class HomePartBViewHolder extends BaseListViewHolder<MvpHomePartBEntity> {
        private Context mContext;
        private TextView title_tv;

        public HomePartBViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            title_tv = itemView.findViewById(R.id.title_tv);
        }

        @Override
        public void updateData(MvpHomePartBEntity content, int position) {
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
