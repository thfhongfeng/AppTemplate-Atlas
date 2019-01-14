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
        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.CENTER_HORIZONTAL);
        if (mBean.getDescImageViewResId() != 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            ImageView image_view = new ImageView(mContext);
            image_view.setImageResource(mBean.getDescImageViewResId());
            image_view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            container.addView(image_view, layoutParams);
        }
        if (mBean.getActionImageViewResId() != 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            if (mBean.getDescImageViewResId() != 0) {
                layoutParams.setMargins(0, mBean.getDescToActionViewGapPx(), 0, 0);
            }
            ImageView image_view = new ImageView(mContext);
            image_view.setImageResource(mBean.getActionImageViewResId());
            image_view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            image_view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onNext(NoviceGuideItemView.this, position);
                    }
                    if (mBean.getListener() != null) {
                        mBean.getListener().doAction(NoviceGuideItemView.this, position);
                    }
                }
            });
            container.addView(image_view, layoutParams);
        }
        if (mBean.getAnimator() != null) {
            mBean.getAnimator().setTarget(container);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (bean.getTargetView() == null) {
            layoutParams.addRule(CENTER_IN_PARENT);
            addView(container, layoutParams);
        } else {
            addView(container, layoutParams);
            bean.getTargetView().addOnLayoutChangeListener(mTargetViewOnLayoutChangeListener);
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
        startAnim();
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
        View child = getChildAt(0);
        if (mNeedReLayoutOffsetView && mOffsetLocation != null && child != null) {
            int[] location = new int[2];
            getLocationInWindow(location);
            mOffsetLocation[0] -= location[0];
            mOffsetLocation[1] -= location[1];
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
            int left = l;
            int top = t;
            int right = r;
            int bottom = b;
            switch (mBean.getGuideViewLocation()) {
                case GuideBean.LOCATION_ON_TOP_LEFT:
                    bottom = targetViewCenterY
                            - (mBean.isHasHole() ? (int) holeCircleRadius : targetViewHeight / 2)
                            + mBean.getGuideViewOffsetMarginY();
                    left = mOffsetLocation[0] + mBean.getGuideViewOffsetMarginX();
                    top = bottom - child.getMeasuredHeight();
                    right = left + child.getMeasuredWidth();
                    break;
                case GuideBean.LOCATION_ON_TOP_RIGHT:
                    bottom = targetViewCenterY
                            - (mBean.isHasHole() ? (int) holeCircleRadius : targetViewHeight / 2)
                            + mBean.getGuideViewOffsetMarginY();
                    right = mOffsetLocation[0] + targetViewWidth + mBean.getGuideViewOffsetMarginX();
                    top = bottom - child.getMeasuredHeight();
                    left = right - child.getMeasuredWidth();
                    break;
                case GuideBean.LOCATION_ON_BOTTOM_LEFT:
                    top = targetViewCenterY
                            + (mBean.isHasHole() ? (int) holeCircleRadius : targetViewHeight / 2)
                            + mBean.getGuideViewOffsetMarginY();
                    left = mOffsetLocation[0] + mBean.getGuideViewOffsetMarginX();
                    bottom = top + child.getMeasuredHeight();
                    right = left + child.getMeasuredWidth();
                    break;
                case GuideBean.LOCATION_ON_BOTTOM_RIGHT:
                    top = targetViewCenterY
                            + (mBean.isHasHole() ? (int) holeCircleRadius : targetViewHeight / 2)
                            + mBean.getGuideViewOffsetMarginY();
                    right = mOffsetLocation[0] + targetViewWidth + mBean.getGuideViewOffsetMarginX();
                    bottom = top + child.getMeasuredHeight();
                    left = right - child.getMeasuredWidth();
                    break;
                case GuideBean.LOCATION_ON_TOP_CENTER:
                    bottom = mOffsetLocation[1] + mBean.getGuideViewOffsetMarginY();
                    left = targetViewCenterX - child.getMeasuredWidth() / 2 + mBean.getGuideViewOffsetMarginX();
                    top = bottom - child.getMeasuredHeight();
                    right = left + child.getMeasuredWidth();
                    break;
                case GuideBean.LOCATION_ON_BOTTOM_CENTER:
                    top = mOffsetLocation[1] + targetViewHeight + mBean.getGuideViewOffsetMarginY();
                    left = targetViewCenterX - child.getMeasuredWidth() / 2 + mBean.getGuideViewOffsetMarginX();
                    bottom = top + child.getMeasuredHeight();
                    right = left + child.getMeasuredWidth();
                    break;
            }
            child.layout(left < l ? l : left, top < t ? t : top,
                    right > r ? r : right, bottom > b ? b : bottom);
            mNeedReLayoutOffsetView = false;
        }
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
