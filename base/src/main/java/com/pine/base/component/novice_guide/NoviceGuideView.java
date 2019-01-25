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
    private int mCurPosition = -1;
    private IGuideViewListener mListener;

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
        init(guideBeans, null, false, true);
    }

    public void init(ArrayList<GuideBean> guideBeans, IGuideViewListener listener) {
        init(guideBeans, listener, false, true);
    }

    public void init(ArrayList<GuideBean> guideBeans, boolean canBelowClick, boolean showSkip) {
        init(guideBeans, null, canBelowClick, showSkip);
    }

    public void init(ArrayList<GuideBean> guideBeans, IGuideViewListener listener, boolean canBelowClick, boolean showSkip) {
        if (guideBeans == null || guideBeans.size() < 1) {
            setVisibility(GONE);
            return;
        }
        mListener = listener;
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
            textView.setPadding(getResources().getDimensionPixelOffset(R.dimen.dp_8),
                    getResources().getDimensionPixelOffset(R.dimen.dp_3),
                    getResources().getDimensionPixelOffset(R.dimen.dp_8),
                    getResources().getDimensionPixelOffset(R.dimen.dp_3));
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
    }

    private void goNext(int position) {
        mCurPosition = position + 1;
        if (position < mGuideItemViewList.size() - 1) {
            if (position >= 0) {
                mGuideItemViewList.get(position).cancelAnim();
                mGuideItemViewList.get(position).setVisibility(GONE);
            }
            mGuideItemViewList.get(position + 1).setVisibility(VISIBLE);
            mGuideItemViewList.get(position + 1).startAnim();
            if (mListener != null) {
                mListener.onItemViewShow(mGuideItemViewList.get(mCurPosition), mCurPosition);
            }
        } else {
            finalFinish();
        }
    }

    public void finalFinish() {
        setVisibility(GONE);
        if (mListener != null) {
            mListener.onFinish();
        }
    }

    public void startAnim(int position) {
        if (position >= 0 && position < mGuideItemViewList.size()) {
            mGuideItemViewList.get(position).startAnim();
        }
    }

    public void cancelAnim(int position) {
        if (position >= 0 && position < mGuideItemViewList.size()) {
            mGuideItemViewList.get(position).cancelAnim();
        }
    }

    public void cancelAllAnim() {
        for (int i = 0; i < mGuideItemViewList.size(); i++) {
            mGuideItemViewList.get(i).cancelAnim();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        goNext(mCurPosition);
    }

    public interface IGuideViewListener {
        void onItemViewShow(NoviceGuideItemView view, int position);

        void onFinish();
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

        private float mScale = 1f;

        public void setScale(float scale) {
            mScale = scale;
        }

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
            if (mScale != 1f) {
                return (int) (guideViewOffsetMarginX * mScale);
            }
            return guideViewOffsetMarginX;
        }

        public void setGuideViewOffsetMarginX(int guideViewOffsetMarginX) {
            this.guideViewOffsetMarginX = guideViewOffsetMarginX;
        }

        public int getGuideViewOffsetMarginY() {
            if (mScale != 1f) {
                return (int) (guideViewOffsetMarginY * mScale);
            }
            return guideViewOffsetMarginY;
        }

        public void setGuideViewOffsetMarginY(int guideViewOffsetMarginY) {
            this.guideViewOffsetMarginY = guideViewOffsetMarginY;
        }

        public int getDescToActionViewGapPx() {
            if (mScale != 1f) {
                return (int) (descToActionViewGapPx * mScale);
            }
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
