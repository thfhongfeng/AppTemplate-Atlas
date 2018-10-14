package com.pine.mvp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pine.base.adapter.BaseListAdapterItemEntity;
import com.pine.base.adapter.BaseListAdapterItemPropertyEntity;
import com.pine.base.adapter.BaseListViewHolder;
import com.pine.base.adapter.BasePaginationTreeListAdapter;
import com.pine.base.image.ImageLoaderManager;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopAndProductEntity;
import com.pine.mvp.bean.MvpShopEntity;
import com.pine.mvp.ui.activity.MvpShopDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomeItemPaginationTreeAdapter extends BasePaginationTreeListAdapter {
    public static final int HOME_SHOP_VIEW_HOLDER = 1;
    public static final int HOME_SHOP_PRODUCT_VIEW_HOLDER = 2;
    public static final int HOME_SHOP_AND_PRODUCT_TREE_LIST_ITEM = 101;

    public MvpHomeItemPaginationTreeAdapter(int defaultItemViewType) {
        super(defaultItemViewType);
    }

    @Override
    public List<BaseListAdapterItemEntity<? extends Object>> parseTreeData(List<? extends Object> data) {
        List<BaseListAdapterItemEntity<? extends Object>> adapterData = new ArrayList<>();
        if (data != null) {
            BaseListAdapterItemEntity adapterEntity;
            switch (getTreeListType()) {
                case HOME_SHOP_AND_PRODUCT_TREE_LIST_ITEM:
                    for (int i = 0; i < data.size(); i++) {
                        MvpShopAndProductEntity entity = (MvpShopAndProductEntity) data.get(i);
                        adapterEntity = new BaseListAdapterItemEntity();
                        adapterEntity.setData(entity);
                        adapterEntity.getPropertyEntity().setItemViewType(HOME_SHOP_VIEW_HOLDER);
                        List<MvpShopAndProductEntity.ProductsBean> productList = entity.getProducts();
                        adapterEntity.getPropertyEntity().setSubItemViewCount(productList == null ? 0 : productList.size());
                        adapterData.add(adapterEntity);
                        if (productList != null) {
                            for (int j = 0; j < productList.size(); j++) {
                                adapterEntity = new BaseListAdapterItemEntity();
                                adapterEntity.setData(productList.get(j));
                                adapterEntity.getPropertyEntity().setItemViewType(HOME_SHOP_PRODUCT_VIEW_HOLDER);
                                adapterData.add(adapterEntity);
                            }
                        }
                    }
                    break;
            }
        }
        return adapterData;
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case HOME_SHOP_VIEW_HOLDER:
                viewHolder = new HomeShopViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_home_shop_tree, parent, false));
                break;
            case HOME_SHOP_PRODUCT_VIEW_HOLDER:
                viewHolder = new HomeShopProductViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_home_shop_product, parent, false));
                break;
        }
        return viewHolder;
    }

    public class HomeShopViewHolder extends BaseListViewHolder<MvpShopEntity> {
        private Context mContext;
        private LinearLayout container;
        private ImageView photo_iv;
        private TextView title_tv, toggle_btn_tv;

        public HomeShopViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            container = itemView.findViewById(R.id.container);
            photo_iv = itemView.findViewById(R.id.photo_iv);
            title_tv = itemView.findViewById(R.id.title_tv);
            toggle_btn_tv = itemView.findViewById(R.id.toggle_btn_tv);
        }

        @Override
        public void updateData(MvpShopEntity content, final BaseListAdapterItemPropertyEntity propertyEntity, final int position) {
            ImageLoaderManager.getInstance().loadImage(mContext, content.getImgUrl(), photo_iv);
            ImageLoaderManager.getInstance().loadImage(mContext, "https://img.zcool.cn/community/019af55798a4090000018c1be7a078.jpg@1280w_1l_2o_100sh.webp", photo_iv);
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
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public class HomeShopProductViewHolder extends BaseListViewHolder<MvpShopAndProductEntity.ProductsBean> {
        private Context mContext;
        private LinearLayout container;
        private TextView title_tv;

        public HomeShopProductViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            container = itemView.findViewById(R.id.container);
            title_tv = itemView.findViewById(R.id.title_tv);
        }

        @Override
        public void updateData(MvpShopAndProductEntity.ProductsBean content,
                               BaseListAdapterItemPropertyEntity propertyEntity, int position) {
            if (!propertyEntity.isItemViewNeedShow()) {
                container.setVisibility(View.GONE);
                return;
            }
            container.setVisibility(View.VISIBLE);
            title_tv.setText(content.getName());
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
