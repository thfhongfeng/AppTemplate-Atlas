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

import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.adapter.BaseNoPaginationListAdapter;
import com.pine.base.list.bean.BaseListAdapterItemPropertyEntity;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.mvp.ui.activity.MvpShopDetailActivity;
import com.pine.tool.util.DecimalUtils;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopListNoPaginationAdapter extends BaseNoPaginationListAdapter {
    public static final int SHOP_VIEW_HOLDER = 1;

    public MvpShopListNoPaginationAdapter(int defaultItemViewType) {
        super(defaultItemViewType);
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case SHOP_VIEW_HOLDER:
                viewHolder = new ShopViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_shop, parent, false));
                break;
        }
        return viewHolder;
    }

    public class ShopViewHolder extends BaseListViewHolder<MvpShopItemEntity> {
        private Context mContext;
        private LinearLayout location_ll;
        private ImageView photo_iv;
        private TextView title_tv, location_tv;

        public ShopViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            photo_iv = itemView.findViewById(R.id.photo_iv);
            title_tv = itemView.findViewById(R.id.title_tv);
            location_ll = itemView.findViewById(R.id.location_ll);
            location_tv = itemView.findViewById(R.id.location_tv);
        }

        @Override
        public void updateData(final MvpShopItemEntity content, BaseListAdapterItemPropertyEntity propertyEntity, int position) {
            ImageLoaderManager.getInstance().loadImage(mContext, content.getImgUrl(), photo_iv);
            title_tv.setText(content.getName());
            String distanceStr = content.getDistance();
            if (TextUtils.isEmpty(distanceStr)) {
                location_tv.setText("");
                location_ll.setVisibility(View.GONE);
            } else {
                float distance = Float.parseFloat(distanceStr);
                if (distance >= 1000.0f) {
                    location_tv.setText(DecimalUtils.divide(distance, 1000.0f, 2) + mContext.getString(R.string.unit_kilometre));
                } else {
                    location_tv.setText(distance + mContext.getString(R.string.unit_metre));
                }
                location_ll.setVisibility(View.VISIBLE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MvpShopDetailActivity.class);
                    intent.putExtra("id", content.getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
