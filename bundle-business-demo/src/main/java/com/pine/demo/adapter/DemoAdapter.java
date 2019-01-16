package com.pine.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pine.base.BaseApplication;
import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.adapter.BaseNoPaginationListAdapter;
import com.pine.base.list.bean.BaseListAdapterItemPropertyEntity;
import com.pine.demo.R;
import com.pine.demo.bean.DemoItemEntity;

/**
 * Created by tanghongfeng on 2019/1/16
 */

public class DemoAdapter extends BaseNoPaginationListAdapter {
    public static final int DEMO_VIEW_HOLDER = 1;

    public DemoAdapter(int defaultItemViewType) {
        super(defaultItemViewType);
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case DEMO_VIEW_HOLDER:
                viewHolder = new DemoViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.demo_item, parent, false));
                break;
        }
        return viewHolder;
    }

    public class DemoViewHolder extends BaseListViewHolder<DemoItemEntity> {
        private Context mContext;
        private TextView name_tv;

        public DemoViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            name_tv = itemView.findViewById(R.id.name_tv);
        }

        @Override
        public void updateData(final DemoItemEntity content,
                               BaseListAdapterItemPropertyEntity propertyEntity, int position) {
            name_tv.setText(content.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseApplication.mCurResumedActivity.startActivity(new Intent(
                            BaseApplication.mCurResumedActivity, content.getClazz()));
                }
            });
        }
    }
}
