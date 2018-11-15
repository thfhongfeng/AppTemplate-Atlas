package com.pine.mvp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.adapter.BasePaginationTreeListAdapter;
import com.pine.base.list.bean.BaseListAdapterItemEntity;
import com.pine.base.list.bean.BaseListAdapterItemPropertyEntity;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopAndProductEntity;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.mvp.ui.activity.MvpShopDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopListPaginationTreeAdapter extends BasePaginationTreeListAdapter<MvpShopAndProductEntity> {
    public static final int SHOP_VIEW_HOLDER = 1;
    public static final int SHOP_PRODUCT_VIEW_HOLDER = 2;

    @Override
    public List<BaseListAdapterItemEntity<MvpShopAndProductEntity>> parseTreeData(List<MvpShopAndProductEntity> data,
                                                                                  boolean reset) {
        List<BaseListAdapterItemEntity<MvpShopAndProductEntity>> adapterData = new ArrayList<>();
        if (data != null) {
            BaseListAdapterItemEntity adapterEntity;
            for (int i = 0; i < data.size(); i++) {
                MvpShopAndProductEntity entity = data.get(i);
                adapterEntity = new BaseListAdapterItemEntity();
                adapterEntity.setData(entity);
                adapterEntity.getPropertyEntity().setItemViewType(SHOP_VIEW_HOLDER);
                List<MvpShopAndProductEntity.ProductsBean> productList = entity.getProducts();
                adapterEntity.getPropertyEntity().setSubItemViewCount(productList == null ? 0 : productList.size());
                adapterData.add(adapterEntity);
                if (productList != null) {
                    for (int j = 0; j < productList.size(); j++) {
                        adapterEntity = new BaseListAdapterItemEntity();
                        adapterEntity.setData(productList.get(j));
                        adapterEntity.getPropertyEntity().setItemViewType(SHOP_PRODUCT_VIEW_HOLDER);
                        adapterData.add(adapterEntity);
                    }
                }
            }
        }
        return adapterData;
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case SHOP_VIEW_HOLDER:
                viewHolder = new ShopViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_shop_tree, parent, false));
                break;
            case SHOP_PRODUCT_VIEW_HOLDER:
                viewHolder = new ShopProductViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_shop_product, parent, false));
                break;
        }
        return viewHolder;
    }

    public class ShopViewHolder extends BaseListViewHolder<MvpShopItemEntity> {
        private Context mContext;
        private LinearLayout container;
        private ImageView photo_iv;
        private TextView title_tv, toggle_btn_tv;

        public ShopViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            container = itemView.findViewById(R.id.container);
            photo_iv = itemView.findViewById(R.id.photo_iv);
            title_tv = itemView.findViewById(R.id.title_tv);
            toggle_btn_tv = itemView.findViewById(R.id.toggle_btn_tv);
        }

        @Override
        public void updateData(final MvpShopItemEntity content, final BaseListAdapterItemPropertyEntity propertyEntity, final int position) {
            ImageLoaderManager.getInstance().loadImage(mContext, content.getImgUrl(), photo_iv);
            if (!propertyEntity.isItemViewNeedShow()) {
                container.setVisibility(View.GONE);
                return;
            }
            container.setVisibility(View.VISIBLE);
            title_tv.setText(content.getName());
            toggle_btn_tv.setText(propertyEntity.isItemViewSpread() ? R.string.mvp_fold : R.string.mvp_spread);
            toggle_btn_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean subWillShow = !propertyEntity.isItemViewSpread();
                    propertyEntity.setItemViewSpread(subWillShow);
                    for (int i = position + 1; i < propertyEntity.getSubItemViewCount() + position + 1; i++) {
                        mData.get(i).getPropertyEntity().setItemViewNeedShow(subWillShow);
                    }
                    notifyItemRangeChanged(position, propertyEntity.getSubItemViewCount() + 1);
                }
            });
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

    public class ShopProductViewHolder extends BaseListViewHolder<MvpShopAndProductEntity.ProductsBean> {
        private Context mContext;
        private LinearLayout container;
        private TextView title_tv;

        public ShopProductViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            container = itemView.findViewById(R.id.container);
            title_tv = itemView.findViewById(R.id.title_tv);
        }

        @Override
        public void updateData(final MvpShopAndProductEntity.ProductsBean content,
                               BaseListAdapterItemPropertyEntity propertyEntity, int position) {
            if (!propertyEntity.isItemViewNeedShow()) {
                container.setVisibility(View.GONE);
                return;
            }
            container.setVisibility(View.VISIBLE);
            title_tv.setText(content.getName());
        }
    }
}
