package com.pine.mvp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.adapter.BasePaginationListAdapter;
import com.pine.base.list.bean.BaseListAdapterItemEntity;
import com.pine.base.list.bean.BaseListAdapterItemProperty;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpShopItemEntity;
import com.pine.tool.util.DecimalUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopCheckListPaginationAdapter extends BasePaginationListAdapter<MvpShopItemEntity> {
    public static final int SHOP_CHECK_VIEW_HOLDER = 1;
    private boolean mIsSearchMode;
    private ArrayList<MvpShopItemEntity> mInitCheckedList;
    private Map<String, MvpShopItemEntity> mAllCheckedMap = new HashMap<>();
    private Map<String, MvpShopItemEntity> mSearchModeCheckedMap = new HashMap<>();

    public MvpShopCheckListPaginationAdapter(ArrayList<MvpShopItemEntity> initCheckedIds, int defaultItemViewType) {
        super(defaultItemViewType);
        mInitCheckedList = initCheckedIds;
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case SHOP_CHECK_VIEW_HOLDER:
                viewHolder = new ShopCheckViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_shop_check, parent, false));
                break;
        }
        return viewHolder;
    }

    public void setData(ArrayList<MvpShopItemEntity> data, boolean isSearchMode) {
        mIsSearchMode = isSearchMode;
        mSearchModeCheckedMap.clear();
        setData(data);
    }

    @Override
    protected List<BaseListAdapterItemEntity<MvpShopItemEntity>> parseData(List<MvpShopItemEntity> data, boolean reset) {
        List<BaseListAdapterItemEntity<MvpShopItemEntity>> adapterData = new ArrayList<>();
        if (data != null) {
            BaseListAdapterItemEntity adapterEntity;
            for (int i = 0; i < data.size(); i++) {
                adapterEntity = new BaseListAdapterItemEntity();
                MvpShopItemEntity entity = data.get(i);
                adapterEntity.setData(entity);
                adapterEntity.getPropertyEntity().setItemViewType(getDefaultItemViewType());
                adapterData.add(adapterEntity);
                if (mInitCheckedList != null) {
                    for (int j = mInitCheckedList.size() - 1; j >= 0; j--) {
                        if (entity.getId().equals(mInitCheckedList.get(j).getId())) {
                            mAllCheckedMap.put(entity.getId(), entity);
                            mInitCheckedList.remove(j);
                        }
                    }
                }
            }
        }
        return adapterData;
    }

    public Map<String, MvpShopItemEntity> getAllCheckedData() {
        return mAllCheckedMap;
    }

    public void clearCheckedData() {
        if (mIsSearchMode) {
            for (String key : mSearchModeCheckedMap.keySet()) {
                mAllCheckedMap.remove(key);
            }
            mSearchModeCheckedMap.clear();
        } else {
            mSearchModeCheckedMap.clear();
            mAllCheckedMap.clear();
            if (mInitCheckedList != null) {
                mInitCheckedList.clear();
            }
        }
        notifyDataSetChanged();
    }

    public class ShopCheckViewHolder extends BaseListViewHolder<MvpShopItemEntity> {
        private Context mContext;
        private LinearLayout location_ll;
        private CheckBox item_cb;
        private ImageView photo_iv;
        private TextView title_tv, location_tv;

        public ShopCheckViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            item_cb = itemView.findViewById(R.id.item_cb);
            photo_iv = itemView.findViewById(R.id.photo_iv);
            title_tv = itemView.findViewById(R.id.title_tv);
            location_ll = itemView.findViewById(R.id.location_ll);
            location_tv = itemView.findViewById(R.id.location_tv);
        }

        @Override
        public void updateData(final MvpShopItemEntity content, BaseListAdapterItemProperty propertyEntity, final int position) {
            item_cb.setChecked(mAllCheckedMap.containsKey(content.getId()));
            if (mIsSearchMode && item_cb.isChecked()) {
                mSearchModeCheckedMap.put(content.getId(), content);
            }
            ImageLoaderManager.getInstance().loadImage(mContext, content.getMainImgUrl(), photo_iv);
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
            item_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item_cb.isChecked()) {
                        mAllCheckedMap.put(content.getId(), content);
                        if (mIsSearchMode) {
                            mSearchModeCheckedMap.put(content.getId(), content);
                        }
                    } else {
                        mAllCheckedMap.remove(content.getId());
                        if (mIsSearchMode) {
                            mSearchModeCheckedMap.remove(content.getId());
                        }
                    }
                }
            });
        }
    }
}
