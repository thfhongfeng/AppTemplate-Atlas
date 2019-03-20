package com.pine.main.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pine.base.BaseApplication;
import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.adapter.BaseNoPaginationListAdapter;
import com.pine.base.list.bean.BaseListAdapterItemProperty;
import com.pine.main.R;
import com.pine.main.bean.MainBusinessItemEntity;
import com.pine.router.IRouterCallback;
import com.pine.router.impl.RouterManager;

/**
 * Created by tanghongfeng on 2019/1/16
 */

public class MainBusinessAdapter extends BaseNoPaginationListAdapter {
    public static final int BUSINESS_VIEW_HOLDER = 1;

    public MainBusinessAdapter(int defaultItemViewType) {
        super(defaultItemViewType);
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case BUSINESS_VIEW_HOLDER:
                viewHolder = new BusinessViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_item_business, parent, false));
                break;
        }
        return viewHolder;
    }

    public class BusinessViewHolder extends BaseListViewHolder<MainBusinessItemEntity> {
        private Context mContext;
        private TextView name_tv;

        public BusinessViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            name_tv = itemView.findViewById(R.id.name_tv);
        }

        @Override
        public void updateData(final MainBusinessItemEntity content,
                               BaseListAdapterItemProperty propertyEntity, int position) {
            name_tv.setText(content.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RouterManager.getInstance(content.getBundle()).callUiCommand(BaseApplication.mCurResumedActivity,
                            content.getCommand(), null, new IRouterCallback() {
                                @Override
                                public void onSuccess(Bundle responseBundle) {

                                }

                                @Override
                                public boolean onFail(int failCode, String errorInfo) {
                                    return false;
                                }
                            });
                }
            });
        }
    }
}
