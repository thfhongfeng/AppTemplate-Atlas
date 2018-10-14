package com.pine.mvp.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pine.base.adapter.BaseListAdapterItemPropertyEntity;
import com.pine.base.adapter.BaseListViewHolder;
import com.pine.base.adapter.BasePaginationListAdapter;
import com.pine.base.image.ImageLoaderManager;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopEntity;
import com.pine.mvp.ui.activity.MvpShopDetailActivity;
import com.pine.tool.util.DecimalUtils;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomeItemPaginationAdapter extends BasePaginationListAdapter {
    public static final int HOME_SHOP_VIEW_HOLDER = 1;

    public MvpHomeItemPaginationAdapter(int defaultItemViewType) {
        super(defaultItemViewType);
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case HOME_SHOP_VIEW_HOLDER:
                viewHolder = new HomeShopViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_home_shop, parent, false));
                break;
        }
        return viewHolder;
    }

    public class HomeShopViewHolder extends BaseListViewHolder<MvpShopEntity> {
        private Context mContext;
        private LinearLayout location_ll;
        private ImageView photo_iv;
        private TextView title_tv, location_tv;

        public HomeShopViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            photo_iv = itemView.findViewById(R.id.photo_iv);
            title_tv = itemView.findViewById(R.id.title_tv);
            location_ll = itemView.findViewById(R.id.location_ll);
            location_tv = itemView.findViewById(R.id.location_tv);
        }

        @Override
        public void updateData(MvpShopEntity content, BaseListAdapterItemPropertyEntity propertyEntity, int position) {
            ImageLoaderManager.getInstance().loadImage(mContext, R.mipmap.base_ic_launcher, photo_iv);
            title_tv.setText(content.getName());
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
                    Intent intent = new Intent(mContext, MvpShopDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
