package com.pine.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.component.editor.ui.TextImageDisplayView;
import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.adapter.BaseComplexListAdapter;
import com.pine.base.list.bean.BaseListAdapterItemEntity;
import com.pine.base.list.bean.BaseListAdapterItemProperty;
import com.pine.mvp.R;
import com.pine.mvp.bean.MvpTravelNoteCommentEntity;
import com.pine.mvp.bean.MvpTravelNoteDetailEntity;
import com.pine.tool.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpTravelNoteDetailComplexAdapter extends BaseComplexListAdapter<MvpTravelNoteDetailEntity, MvpTravelNoteCommentEntity> {
    public static final int TRAVEL_NOTE_HEAD_VIEW_HOLDER = 1;
    public static final int TRAVEL_NOTE_DAY_VIEW_HOLDER = 2;
    public static final int TRAVEL_NOTE_COMMENT_HEAD_VIEW_HOLDER = 3;
    public static final int TRAVEL_NOTE_COMMENT_VIEW_HOLDER = 4;

    public BaseListViewHolder<String> getEmptyBackgroundViewHolder(ViewGroup parent) {
        return new EmptyBackgroundViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(com.pine.base.R.layout.base_item_empty_background, parent, false));
    }

    @Override
    public List<BaseListAdapterItemEntity<MvpTravelNoteDetailEntity>> parseHeadData(List<MvpTravelNoteDetailEntity> data) {
        List<BaseListAdapterItemEntity<MvpTravelNoteDetailEntity>> adapterData = new ArrayList<>();
        if (data != null) {
            BaseListAdapterItemEntity adapterEntity;
            for (int i = 0; i < data.size(); i++) {
                MvpTravelNoteDetailEntity entity = data.get(i);
                adapterEntity = new BaseListAdapterItemEntity();
                adapterEntity.setData(entity);
                adapterEntity.getPropertyEntity().setItemViewType(TRAVEL_NOTE_HEAD_VIEW_HOLDER);
                List<MvpTravelNoteDetailEntity.DayBean> dayList = entity.getDays();
                adapterData.add(adapterEntity);
                if (dayList != null) {
                    for (int j = 0; j < dayList.size(); j++) {
                        adapterEntity = new BaseListAdapterItemEntity();
                        adapterEntity.setData(dayList.get(j));
                        adapterEntity.getPropertyEntity().setItemViewType(TRAVEL_NOTE_DAY_VIEW_HOLDER);
                        adapterData.add(adapterEntity);
                    }
                }
            }
            adapterEntity = new BaseListAdapterItemEntity();
            adapterEntity.setData("");
            adapterEntity.getPropertyEntity().setItemViewType(TRAVEL_NOTE_COMMENT_HEAD_VIEW_HOLDER);
            adapterData.add(adapterEntity);
        }
        return adapterData;
    }

    @Override
    public List<BaseListAdapterItemEntity<MvpTravelNoteCommentEntity>> parseTailData(List<MvpTravelNoteCommentEntity> data) {
        List<BaseListAdapterItemEntity<MvpTravelNoteCommentEntity>> adapterData = new ArrayList<>();
        if (data != null) {
            BaseListAdapterItemEntity adapterEntity;
            for (int i = 0; i < data.size(); i++) {
                adapterEntity = new BaseListAdapterItemEntity();
                adapterEntity.setData(data.get(i));
                adapterEntity.getPropertyEntity().setItemViewType(TRAVEL_NOTE_COMMENT_VIEW_HOLDER);
                adapterData.add(adapterEntity);
            }
        }
        return adapterData;
    }

    @Override
    public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
        BaseListViewHolder viewHolder = null;
        switch (viewType) {
            case TRAVEL_NOTE_HEAD_VIEW_HOLDER:
                viewHolder = new TravelNoteHeadViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_travel_note_head, parent, false));
                break;
            case TRAVEL_NOTE_DAY_VIEW_HOLDER:
                viewHolder = new TravelNoteDayViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_travel_note_day, parent, false));
                break;
            case TRAVEL_NOTE_COMMENT_HEAD_VIEW_HOLDER:
                viewHolder = new TravelNoteCommentHeadViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_travel_note_comment_head, parent, false));
                break;
            case TRAVEL_NOTE_COMMENT_VIEW_HOLDER:
                viewHolder = new TravelNoteCommentViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mvp_item_travel_note_comment, parent, false));
                break;
        }
        return viewHolder;
    }

    public class TravelNoteHeadViewHolder extends BaseListViewHolder<MvpTravelNoteDetailEntity> {
        private Context mContext;
        private CircleImageView person_civ;
        private ImageView is_like_iv;
        private TextView title_tv, set_out_date_tv, author_tv, create_time_tv, like_count_tv, read_count_tv, preface_tv;

        public TravelNoteHeadViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            person_civ = itemView.findViewById(R.id.person_civ);
            is_like_iv = itemView.findViewById(R.id.is_like_iv);
            title_tv = itemView.findViewById(R.id.title_tv);
            set_out_date_tv = itemView.findViewById(R.id.set_out_date_tv);
            author_tv = itemView.findViewById(R.id.author_tv);
            create_time_tv = itemView.findViewById(R.id.create_time_tv);
            like_count_tv = itemView.findViewById(R.id.like_count_tv);
            read_count_tv = itemView.findViewById(R.id.read_count_tv);
            preface_tv = itemView.findViewById(R.id.preface_tv);
        }

        @Override
        public void updateData(MvpTravelNoteDetailEntity content, BaseListAdapterItemProperty propertyEntity, int position) {
            ImageLoaderManager.getInstance().loadImage(mContext, content.getHeadImg(),
                    R.mipmap.base_iv_portrait_default, -1, person_civ);
            title_tv.setText(content.getTitle());
            set_out_date_tv.setText(mContext.getString(R.string.mvp_travel_note_detail_set_out_date, content.getSetOutDate()));
            author_tv.setText(content.getAuthor());
            create_time_tv.setText(content.getCreateTime());
            is_like_iv.setSelected(content.isLike());
            like_count_tv.setText(String.valueOf(content.getLikeCount()));
            read_count_tv.setText(String.valueOf(content.getReadCount()));
            preface_tv.setText(content.getPreface());
        }
    }

    public class TravelNoteDayViewHolder extends BaseListViewHolder<MvpTravelNoteDetailEntity.DayBean> {
        private Context mContext;
        private TextImageDisplayView day_tv;

        public TravelNoteDayViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            day_tv = itemView.findViewById(R.id.day_tidv);
        }

        @Override
        public void updateData(MvpTravelNoteDetailEntity.DayBean content, BaseListAdapterItemProperty propertyEntity, int position) {
            day_tv.setTitle(content.getDay());
            day_tv.setContent(content.getTextImageContentList());
        }
    }

    public class TravelNoteCommentHeadViewHolder extends BaseListViewHolder<String> {
        private Context mContext;

        public TravelNoteCommentHeadViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
        }

        @Override
        public void updateData(String content, BaseListAdapterItemProperty propertyEntity, int position) {
        }
    }

    public class TravelNoteCommentViewHolder extends BaseListViewHolder<MvpTravelNoteCommentEntity> {
        private Context mContext;
        private CircleImageView person_civ;
        private TextView author_tv, create_time_tv, content_tv;

        public TravelNoteCommentViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            person_civ = itemView.findViewById(R.id.person_civ);
            author_tv = itemView.findViewById(R.id.author_tv);
            create_time_tv = itemView.findViewById(R.id.create_time_tv);
            content_tv = itemView.findViewById(R.id.content_tv);
        }

        @Override
        public void updateData(MvpTravelNoteCommentEntity content, BaseListAdapterItemProperty propertyEntity, int position) {
            ImageLoaderManager.getInstance().loadImage(mContext, content.getImgUrl(), person_civ);
            author_tv.setText(content.getAuthor());
            create_time_tv.setText(content.getCreateTime());
            content_tv.setText(content.getContent());
        }
    }
}
