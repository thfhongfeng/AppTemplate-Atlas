package com.pine.base.component.novice_guide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pine.base.component.novice_guide.NoviceGuideView.GuideBean;

/**
 * Created by tanghongfeng on 2019/1/11
 */

public class NoviceGuideItemView extends RelativeLayout {
    private Context mContext;
    private IActionListener mListener;
    private GuideBean mBean;
    private int[] mOffsetLocation;
    private boolean mNeedReLayoutOffsetView;
    private OnLayoutChangeListener mTargetViewOnLayoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
            mOffsetLocation = new int[2];
            v.getLocationInWindow(mOffsetLocation);
            int[] location = new int[2];
            getLocationInWindow(location);
            mOffsetLocation[0] -= location[0];
            mOffsetLocation[1] -= location[1];
            mNeedReLayoutOffsetView = true;
            requestLayout();
        }
    };
    private int mHoleCenterX;
    private int mHoleCenterY;
    private float mHoleRadius;

    public NoviceGuideItemView(Context context) {
        super(context);
        mContext = context;
    }

    public NoviceGuideItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public NoviceGuideItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void init(final int position, final GuideBean bean, boolean canBelowClick,
                     IActionListener listener) {
        if (bean == null) {
            return;
        }
        mBean = bean;
        mListener = listener;
        if (!canBelowClick) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onNext(NoviceGuideItemView.this, position);
                    }
                }
            });
        }
        ImageView descIv = null;
        if (mBean.getDescImageViewResId() != 0) {
            descIv = new ImageView(mContext);
            descIv.setImageResource(mBean.getDescImageViewResId());
            descIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        ImageView actionIv = null;
        if (mBean.getActionImageViewResId() != 0) {
            actionIv = new ImageView(mContext);
            actionIv.setImageResource(mBean.getActionImageViewResId());
            actionIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            if (mBean.getListener() != null) {
                actionIv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onNext(NoviceGuideItemView.this, position);
                        }
                        mBean.getListener().doAction(NoviceGuideItemView.this, position);
                    }
                });
            } else if (mBean.getAnimator() != null) {
                mBean.getAnimator().setTarget(actionIv);
            }
        }
        if (bean.getTargetView() != null) {
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            if (descIv != null) {
                addView(descIv, layoutParams);
            }
            if (actionIv != null) {
                addView(actionIv, layoutParams);
            }
            bean.getTargetView().addOnLayoutChangeListener(mTargetViewOnLayoutChangeListener);
        } else {
            LinearLayout container = new LinearLayout(mContext);
            container.setOrientation(LinearLayout.VERTICAL);
            container.setGravity(Gravity.CENTER_HORIZONTAL);
            if (descIv != null) {
                container.addView(descIv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            if (actionIv != null) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin = mBean.getDescToActionViewGapPx();
                container.addView(actionIv, layoutParams);
            }
            LayoutParams containerParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            containerParams.addRule(CENTER_IN_PARENT);
            addView(container, containerParams);
        }
    }

    public void startAnim() {
        if (mBean.getAnimator() != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (mBean.getAnimator() != null) {
                        mBean.getAnimator().start();
                    }
                }
            });
        }
    }

    public void cancelAnim() {
        if (mBean.getAnimator() != null) {
            mBean.getAnimator().cancel();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mBean != null && mBean.getTargetView() != null) {
            mBean.getTargetView().removeOnLayoutChangeListener(mTargetViewOnLayoutChangeListener);
        }
        cancelAnim();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mBean == null || mBean.getTargetView() == null) {
            return;
        }
        View targetView = mBean.getTargetView();
        if (mNeedReLayoutOffsetView && mOffsetLocation != null && getChildCount() > 0) {
            int targetViewWidth = targetView.getRight() - targetView.getLeft();
            int targetViewHeight = targetView.getBottom() - targetView.getTop();
            int targetViewCenterX = mOffsetLocation[0] + targetViewWidth / 2;
            int targetViewCenterY = mOffsetLocation[1] + targetViewHeight / 2;
            float holeCircleRadius = mBean.getHoldRadius();
            if (holeCircleRadius <= 0f) {
                if (targetView instanceof TextView) {
                    TextPaint textPaint = ((TextView) targetView).getPaint();
                    String text = ((TextView) targetView).getText().toString();
                    if (!TextUtils.isEmpty(text)) {
                        holeCircleRadius = textPaint.measureText(text) / 2 + 10;
                    }
                } else {
                    holeCircleRadius = (targetViewWidth + targetViewHeight) / 4;
                }
            }
            holeCircleRadius = holeCircleRadius <= 0f ? 0 : holeCircleRadius;
            mHoleCenterX = targetViewCenterX;
            mHoleCenterY = targetViewCenterY;
            mHoleRadius = holeCircleRadius;
            int width = r - l;
            int height = b - t;
            float scale = calculateScale(width, height);
            mBean.setScale(scale);
            int[] curItemLocation = new int[2];
            curItemLocation[0] = mOffsetLocation[0];
            curItemLocation[1] = mOffsetLocation[1];
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int childWidth = (int) (child.getMeasuredWidth() * scale);
                int childHeight = (int) (child.getMeasuredHeight() * scale);
                int left = 0;
                int top = 0;
                int right = 0;
                int bottom = 0;
                switch (mBean.getGuideViewLocation()) {
                    case GuideBean.LOCATION_ON_TOP_LEFT:
                        bottom = targetViewCenterY
                                - (mBean.isHasHole() ? (int) holeCircleRadius : targetViewHeight / 2)
                                + mBean.getGuideViewOffsetMarginY();
                        right = curItemLocation[0] + targetViewWidth + mBean.getGuideViewOffsetMarginX();
                        top = bottom - childHeight;
                        left = right - childWidth;
                        curItemLocation[1] = curItemLocation[1] - childHeight - mBean.getDescToActionViewGapPx();
                        break;
                    case GuideBean.LOCATION_ON_TOP_RIGHT:
                        bottom = targetViewCenterY
                                - (mBean.isHasHole() ? (int) holeCircleRadius : targetViewHeight / 2)
                                + mBean.getGuideViewOffsetMarginY();
                        left = curItemLocation[0] + mBean.getGuideViewOffsetMarginX();
                        top = bottom - childHeight;
                        right = left + childWidth;
                        curItemLocation[1] = curItemLocation[1] - childHeight - mBean.getDescToActionViewGapPx();
                        break;
                    case GuideBean.LOCATION_ON_BOTTOM_LEFT:
                        top = targetViewCenterY
                                + (mBean.isHasHole() ? (int) holeCircleRadius : targetViewHeight / 2)
                                + mBean.getGuideViewOffsetMarginY();
                        right = curItemLocation[0] + targetViewWidth + mBean.getGuideViewOffsetMarginX();
                        bottom = top + childHeight;
                        left = right - childWidth;
                        curItemLocation[1] = curItemLocation[1] + childHeight + mBean.getDescToActionViewGapPx();
                        break;
                    case GuideBean.LOCATION_ON_BOTTOM_RIGHT:
                        top = targetViewCenterY
                                + (mBean.isHasHole() ? (int) holeCircleRadius : targetViewHeight / 2)
                                + mBean.getGuideViewOffsetMarginY();
                        left = curItemLocation[0] + mBean.getGuideViewOffsetMarginX();
                        bottom = top + childHeight;
                        right = left + childWidth;
                        curItemLocation[1] = curItemLocation[1] + childHeight + mBean.getDescToActionViewGapPx();
                        break;
                    case GuideBean.LOCATION_ON_TOP_CENTER:
                        bottom = curItemLocation[1] + mBean.getGuideViewOffsetMarginY();
                        left = targetViewCenterX - childWidth / 2 + mBean.getGuideViewOffsetMarginX();
                        top = bottom - childHeight;
                        right = left + childWidth;
                        curItemLocation[1] = curItemLocation[1] - childHeight - mBean.getDescToActionViewGapPx();
                        break;
                    case GuideBean.LOCATION_ON_BOTTOM_CENTER:
                        top = curItemLocation[1] + targetViewHeight + mBean.getGuideViewOffsetMarginY();
                        left = targetViewCenterX - childWidth / 2 + mBean.getGuideViewOffsetMarginX();
                        bottom = top + childHeight;
                        right = left + childWidth;
                        curItemLocation[1] = curItemLocation[1] + childHeight + mBean.getDescToActionViewGapPx();
                        break;
                }
                child.layout(left < 0 ? 0 : left, top < 0 ? 0 : top,
                        right > width ? width : right, bottom > height ? height : bottom);
            }
            mNeedReLayoutOffsetView = false;
        }
    }

    private float calculateScale(int width, int height) {
        float widthScale = 1;
        int childrenMaxWidth = 0;
        int childrenTotalHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childrenTotalHeight = childrenTotalHeight + child.getMeasuredHeight();
            childrenMaxWidth = Math.max(childrenMaxWidth, child.getMeasuredWidth());
        }
        int fitWidth = 0;
        int fitHeight = 0;
        switch (mBean.getGuideViewLocation()) {
            case GuideBean.LOCATION_ON_TOP_LEFT:
                fitWidth = mOffsetLocation[0];
                fitHeight = mOffsetLocation[1];
                break;
            case GuideBean.LOCATION_ON_TOP_RIGHT:
                fitWidth = width - mOffsetLocation[0];
                fitHeight = mOffsetLocation[1];
                break;
            case GuideBean.LOCATION_ON_BOTTOM_LEFT:
                fitWidth = mOffsetLocation[0];
                fitHeight = height - mOffsetLocation[1];
                break;
            case GuideBean.LOCATION_ON_BOTTOM_RIGHT:
                fitWidth = width - mOffsetLocation[0];
                fitHeight = height - mOffsetLocation[1];
                break;
            case GuideBean.LOCATION_ON_TOP_CENTER:
                fitWidth = width;
                fitHeight = mOffsetLocation[1];
                break;
            case GuideBean.LOCATION_ON_BOTTOM_CENTER:
                fitWidth = width;
                fitHeight = height - mOffsetLocation[1];
                break;
        }
        if (childrenMaxWidth > 0 && childrenMaxWidth > fitWidth) {
            widthScale = fitWidth / (float) childrenMaxWidth;
        }
        float heightScale = 1;
        if (childrenTotalHeight > 0 && childrenTotalHeight > fitHeight) {
            heightScale = fitHeight / (float) childrenTotalHeight;
        }
        return Math.min(widthScale, heightScale);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        if (mBean != null) {
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
            paint.setColor(mBean.getBgColor());
            canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
            if (mBean.getTargetView() != null && mBean.isHasHole()) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawCircle(mHoleCenterX, mHoleCenterY, mHoleRadius, paint);
                paint.setXfermode(null);
                canvas.restoreToCount(layerId);
            }
        }
        super.dispatchDraw(canvas);
    }

    public interface IActionListener {
        void onNext(NoviceGuideItemView view, int position);
    }
}
