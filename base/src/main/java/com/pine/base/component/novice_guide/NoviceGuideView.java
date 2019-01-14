package com.pine.base.component.novice_guide;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pine.base.R;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2019/1/11
 */

public class NoviceGuideView extends RelativeLayout {
    private Context mContext;
    private ArrayList<NoviceGuideItemView> mGuideItemViewList = new ArrayList<>();
    private int mCurPosition;

    public NoviceGuideView(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public NoviceGuideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public NoviceGuideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void init(ArrayList<GuideBean> guideBeans) {
        init(guideBeans, false, true);
    }

    public void init(ArrayList<GuideBean> guideBeans, boolean canBelowClick, boolean showSkip) {
        if (guideBeans == null || guideBeans.size() < 1) {
            setVisibility(GONE);
            return;
        }
        removeAllViews();
        canBelowClick = guideBeans.size() == 1 && canBelowClick;
        for (int i = 0; i < guideBeans.size(); i++) {
            NoviceGuideItemView guideView = new NoviceGuideItemView(mContext);
            guideView.init(i, guideBeans.get(i), canBelowClick
                    , new NoviceGuideItemView.IActionListener() {
                        @Override
                        public void onNext(NoviceGuideItemView view, int position) {
                            goNext(position);
                        }
                    });
            guideView.setVisibility(GONE);
            LayoutParams itemParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            addView(guideView, itemParams);
            mGuideItemViewList.add(guideView);
        }
        if (showSkip) {
            TextView textView = new TextView(mContext);
            textView.setText("跳过");
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setTextSize(12);
            textView.setPadding(getResources().getDimensionPixelOffset(R.dimen.dp_5),
                    getResources().getDimensionPixelOffset(R.dimen.dp_5),
                    getResources().getDimensionPixelOffset(R.dimen.dp_5),
                    getResources().getDimensionPixelOffset(R.dimen.dp_5));
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = getResources().getDimensionPixelOffset(R.dimen.dp_16);
            params.topMargin = getResources().getDimensionPixelOffset(R.dimen.dp_30);
            params.addRule(ALIGN_PARENT_TOP);
            params.addRule(ALIGN_PARENT_RIGHT);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalFinish();
                }
            });
            addView(textView, params);
        }
        mGuideItemViewList.get(0).setVisibility(VISIBLE);
        mCurPosition = 0;
    }

    private void goNext(int position) {
        if (position < mGuideItemViewList.size() - 1) {
            mGuideItemViewList.get(position).setVisibility(GONE);
            mGuideItemViewList.get(position + 1).setVisibility(VISIBLE);
        } else {
            finalFinish();
        }
        mCurPosition = position + 1;
    }

    public void finalFinish() {
        setVisibility(GONE);
    }

    public void startAnim() {
        if (mCurPosition < mGuideItemViewList.size()) {
            mGuideItemViewList.get(mCurPosition).startAnim();
        }
    }

    public void cancelAnim() {
        if (mCurPosition < mGuideItemViewList.size()) {
            mGuideItemViewList.get(mCurPosition).cancelAnim();
        }
    }

    public static class GuideBean {
        public static final int LOCATION_ON_TOP_LEFT = 1;
        public static final int LOCATION_ON_TOP_RIGHT = 2;
        public static final int LOCATION_ON_BOTTOM_LEFT = 3;
        public static final int LOCATION_ON_BOTTOM_RIGHT = 4;
        public static final int LOCATION_ON_TOP_CENTER = 5;
        public static final int LOCATION_ON_BOTTOM_CENTER = 6;
        private String name;
        private View targetView;
        private int descImageViewResId;
        private int guideViewLocation; // guideView相对于targetView的位置：LOCATION_ON_TOP_LEFT、LOCATION_ON_TOP_RIGHT ...
        private int guideViewOffsetMarginX; // 用于修正描述view的位置
        private int guideViewOffsetMarginY; // 用于修正描述view的位置
        private int descToActionViewGapPx;
        private Animator animator;
        private int actionImageViewResId;
        private IDoActionListener listener;
        private int bgColor = Color.parseColor("#99000000");
        private boolean hasHole = true;
        private float holdRadius = -1f;  //px

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public View getTargetView() {
            return targetView;
        }

        public void setTargetView(View targetView) {
            this.targetView = targetView;
        }

        public int getDescImageViewResId() {
            return descImageViewResId;
        }

        public void setDescImageViewResId(int descImageViewResId) {
            this.descImageViewResId = descImageViewResId;
        }

        public int getGuideViewLocation() {
            return guideViewLocation;
        }

        public void setGuideViewLocation(int guideViewLocation) {
            this.guideViewLocation = guideViewLocation;
        }

        public int getGuideViewOffsetMarginX() {
            return guideViewOffsetMarginX;
        }

        public void setGuideViewOffsetMarginX(int guideViewOffsetMarginX) {
            this.guideViewOffsetMarginX = guideViewOffsetMarginX;
        }

        public int getGuideViewOffsetMarginY() {
            return guideViewOffsetMarginY;
        }

        public void setGuideViewOffsetMarginY(int guideViewOffsetMarginY) {
            this.guideViewOffsetMarginY = guideViewOffsetMarginY;
        }

        public int getDescToActionViewGapPx() {
            return descToActionViewGapPx;
        }

        public void setDescToActionViewGapPx(int descToActionViewGapPx) {
            this.descToActionViewGapPx = descToActionViewGapPx;
        }

        public Animator getAnimator() {
            return animator;
        }

        public void setAnimator(Animator animator) {
            this.animator = animator;
        }

        public int getActionImageViewResId() {
            return actionImageViewResId;
        }

        public void setActionImageViewResId(int actionImageViewResId) {
            this.actionImageViewResId = actionImageViewResId;
        }

        public IDoActionListener getListener() {
            return listener;
        }

        public void setListener(IDoActionListener listener) {
            this.listener = listener;
        }

        public int getBgColor() {
            return bgColor;
        }

        public void setBgColor(int bgColor) {
            this.bgColor = bgColor;
        }

        public boolean isHasHole() {
            return hasHole;
        }

        public void setHasHole(boolean hasHole) {
            this.hasHole = hasHole;
        }

        public float getHoldRadius() {
            return holdRadius;
        }

        public void setHoldRadius(float holdRadius) {
            this.holdRadius = holdRadius;
        }

        public interface IDoActionListener {
            void doAction(NoviceGuideItemView view, int position);
        }
    }
}
