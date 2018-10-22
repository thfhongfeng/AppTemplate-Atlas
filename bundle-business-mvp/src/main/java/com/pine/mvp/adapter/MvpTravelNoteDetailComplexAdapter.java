package com.pine.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.image.ImageLoaderManager;
import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.adapter.BaseComplexListAdapter;
import com.pine.base.list.bean.BaseListAdapterItemEntity;
import com.pine.base.list.bean.BaseListAdapterItemPropertyEntity;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpTravelNoteCommentEntity;
import com.pine.mvp.bean.MvpTravelNoteDetailEntity;
import com.pine.tool.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpTravelNoteDetailComplexAdapter extends BaseComplexListAdapter {
    public static final int HOME_TRAVEL_NOTE_HEAD_VIEW_HOLDER = 1;
    public static final int HOME_TRAVEL_NOTE_DAY_VIEW_HOLDER = 2;
    public static final int HOME_TRAVEL_NOTE_COMMENT_HEAD_VIEW_HOLDER = 3;
    public static final int HOME_TRAVEL_NOTE_COMMENT_VIEW_HOLDER = 4;

    public BaseListViewHolder<String> getEmptyBackgroundViewHolder(ViewGroup parent) {
        return new EmptyBackgroundViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(com.pine.base.R.layout.base_item_empty_background, parent, false));
    }

    @Override
    public List<BaseListAdapterItemEntity<? extends Object>> parseTopData(List<?> data) {
        List<BaseListAdapterItemEntity<? extends Object>> adapterData = new ArrayList<>();
        if (data != null) {
            BaseListAdapterItemEntity adapterEntity;
            for (int i = 0; i < data.size(); i++) {
                MvpTravelNoteDetailEntity entity = (MvpTravelNoteDetailEntity) data.get(i);
                adapterEntity = new BaseListAdapterItemEntity();
                adapterEntity.setData(entity);
                adapterEntity.getPropertyEntity().setItemViewType(HOME_TRAVEL_NOTE_HEAD_VIEW_HOLDER);
                List<MvpTravelNoteDetailEntity.DaysBean> dayList = entity.getDays();
                adapterData.add(adapterEntity);
                if (dayList != null) {
                    for (int j = 0; j < dayList.size(); j++) {
                        adapterEntity = new BaseListAdapterItemEntity();
                        adapterEntity.setData(dayList.get(j));
                        adapterEntity.getPropertyEntity().setItemViewType(HOME_TRAVEL_NOTE_DAY_VIEW_HOLDER);
                        adapterData.add(adapterEntity);
                    }
                }
            }
            adapterEntity = new BaseListAdapterItemEntity();
            adapterEntity.setData("");
            adapterEntity.getPropertyEntity().setItemViewType(HOME_TRAVEL_NOTE_COMMENT_HEAD_VIEW_HOLDER);
            adapterData.add(adapterEntity);
        }
        return adapterData;
    }

    @Override
    public List<BaseListAdapterItemEntity<? extends Object>> parseBottomData(List<?> data) {
        List<BaseListAdapterItemEntity<? extends Object>> adapterData = new ArrayList<>();
        if (data != null) {
            BaseListAdapterItemEntity adapterEntity;
            for (int i = 0; i < data.size(); i++) {
                adapterEntity = new BaseListAdapterItemEntity();
                adapterEntity.setData(data.get(i));
                adapterEntity.getPropertyEntity().setItemViewType(HOME_TRAVEL_NOTE_COMMENT_VIEW_HOLDER);
                adapterData.add(adapterEntity);
            }
        }
        return adapterData;
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case HOME_TRAVEL_NOTE_HEAD_VIEW_HOLDER:
                viewHolder = new HomeTravelNoteHeadViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_home_travel_note_head, parent, false));
                break;
            case HOME_TRAVEL_NOTE_DAY_VIEW_HOLDER:
                viewHolder = new HomeTravelNoteDayViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_home_travel_note_day, parent, false));
                break;
            case HOME_TRAVEL_NOTE_COMMENT_HEAD_VIEW_HOLDER:
                viewHolder = new HomeTravelNoteCommentHeadViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_home_travel_note_comment_head, parent, false));
                break;
            case HOME_TRAVEL_NOTE_COMMENT_VIEW_HOLDER:
                viewHolder = new HomeTravelNoteCommentViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_home_travel_note_comment, parent, false));
                break;
        }
        return viewHolder;
    }

    public class HomeTravelNoteHeadViewHolder extends BaseListViewHolder<MvpTravelNoteDetailEntity> {
        private Context mContext;
        private CircleImageView person_civ;
        private ImageView is_like_iv;
        private TextView title_tv, sub_title_tv, name_tv, create_time_tv, like_count_tv, read_count_tv, preface_tv;

        public HomeTravelNoteHeadViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            person_civ = itemView.findViewById(R.id.person_civ);
            is_like_iv = itemView.findViewById(R.id.is_like_iv);
            title_tv = itemView.findViewById(R.id.title_tv);
            sub_title_tv = itemView.findViewById(R.id.sub_title_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            create_time_tv = itemView.findViewById(R.id.create_time_tv);
            like_count_tv = itemView.findViewById(R.id.like_count_tv);
            read_count_tv = itemView.findViewById(R.id.read_count_tv);
            preface_tv = itemView.findViewById(R.id.preface_tv);
        }

        @Override
        public void updateData(MvpTravelNoteDetailEntity content, BaseListAdapterItemPropertyEntity propertyEntity, int position) {
            ImageLoaderManager.getInstance().loadImage(mContext, content.getImgUrl(), person_civ);
            // Test code begin
            ImageLoaderManager.getInstance().loadImage(mContext, R.mipmap.base_ic_launcher, person_civ);
            // Test code end
            title_tv.setText(content.getTitle());
            sub_title_tv.setText(content.getSubTitle());
            name_tv.setText(content.getName());
            create_time_tv.setText(content.getCreateTime());
            is_like_iv.setSelected(content.isIsLike());
            like_count_tv.setText(String.valueOf(content.getLikeCount()));
            read_count_tv.setText(String.valueOf(content.getReadCount()));
            preface_tv.setText(content.getPreface());
        }
    }

    public class HomeTravelNoteDayViewHolder extends BaseListViewHolder<MvpTravelNoteDetailEntity.DaysBean> {
        private Context mContext;
        private TextView day_tv, content_tv;

        public HomeTravelNoteDayViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            day_tv = itemView.findViewById(R.id.day_tv);
            content_tv = itemView.findViewById(R.id.content_tv);
        }

        @Override
        public void updateData(MvpTravelNoteDetailEntity.DaysBean content, BaseListAdapterItemPropertyEntity propertyEntity, int position) {
            day_tv.setText(content.getDay());
            content_tv.setText(content.getContent());
        }
    }

    public class HomeTravelNoteCommentHeadViewHolder extends BaseListViewHolder<String> {
        private Context mContext;

        public HomeTravelNoteCommentHeadViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
        }

        @Override
        public void updateData(String content, BaseListAdapterItemPropertyEntity propertyEntity, int position) {
        }
    }

    public class HomeTravelNoteCommentViewHolder extends BaseListViewHolder<MvpTravelNoteCommentEntity> {
        private Context mContext;
        private CircleImageView person_civ;
        private TextView name_tv, create_time_tv, content_tv;

        public HomeTravelNoteCommentViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            person_civ = itemView.findViewById(R.id.person_civ);
            name_tv = itemView.findViewById(R.id.name_tv);
            create_time_tv = itemView.findViewById(R.id.create_time_tv);
            content_tv = itemView.findViewById(R.id.content_tv);
        }

        @Override
        public void updateData(MvpTravelNoteCommentEntity content, BaseListAdapterItemPropertyEntity propertyEntity, int position) {
            ImageLoaderManager.getInstance().loadImage(mContext, content.getImgUrl(), person_civ);
            // Test code begin
            ImageLoaderManager.getInstance().loadImage(mContext, R.mipmap.base_ic_launcher, person_civ);
            // Test code end
            name_tv.setText(content.getName());
            create_time_tv.setText(content.getCreateTime());
            content_tv.setText(content.getContent());
        }
    }
}
