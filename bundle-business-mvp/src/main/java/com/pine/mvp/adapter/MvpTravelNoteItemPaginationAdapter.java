package com.pine.mvp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.adapter.BasePaginationListAdapter;
import com.pine.base.list.bean.BaseListAdapterItemPropertyEntity;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpTravelNoteItemEntity;
import com.pine.mvp.ui.activity.MvpTravelNoteDetailActivity;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpTravelNoteItemPaginationAdapter extends BasePaginationListAdapter {
    public static final int TRAVEL_NOTE_VIEW_HOLDER = 1;

    public MvpTravelNoteItemPaginationAdapter(int defaultItemViewType) {
        super(defaultItemViewType);
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case TRAVEL_NOTE_VIEW_HOLDER:
                viewHolder = new TravelNoteViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_travel_note, parent, false));
                break;
        }
        return viewHolder;
    }

    public class TravelNoteViewHolder extends BaseListViewHolder<MvpTravelNoteItemEntity> {
        private Context mContext;
        private TextView title_tv, create_time_tv;

        public TravelNoteViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            title_tv = itemView.findViewById(R.id.title_tv);
            create_time_tv = itemView.findViewById(R.id.create_time_tv);
        }

        @Override
        public void updateData(final MvpTravelNoteItemEntity content, BaseListAdapterItemPropertyEntity propertyEntity, int position) {
            title_tv.setText(content.getTitle());
            create_time_tv.setText(content.getCreateTime());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MvpTravelNoteDetailActivity.class);
                    intent.putExtra("id", content.getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
