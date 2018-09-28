package com.pine.tool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.pine.tool.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/27
 */

public class RingScaleDiagramView extends View {
    // 坐标系分区，按drawArc方向（-90为上方，顺时针增加）
    private final int ZONE_N90_TO_N45 = 1;
    private final int ZONE_N45_TO_0 = 2;
    private final int ZONE_0_TO_45 = 3;
    private final int ZONE_45_TO_90 = 4;
    private final int ZONE_90_TO_135 = 5;
    private final int ZONE_135_TO_180 = 6;
    private final int ZONE_180_TO_225 = 7;
    private final int ZONE_225_TO_270 = 8;

    private Context mContext;

    // 内圆半径
    private int mInnerRadius;
    // 外圆半径
    private int mOuterRadius;
    // 指示折线宽度
    private int mFoldLineWidth;
    // 指示折线第一段长度
    private int mFoldFirstLineLength;
    // 指示折线第二段水平长度（第二段用于调整布局，长度不固定。此长度为参考值）
    private int mFoldSecondLineLength;
    // 比例名称和比例数值的字体颜色
    private int mNameTextColor, mScaleTextColor;
    // 比例名称和比例数值的字体大小
    private int mNameTextSize, mScaleTextSize;
    // 比例名称和比例数值是否分行显示
    private boolean mIsTwoTextLine;
    // 比例圆环背景圆环颜色
    private int mRingBackgroundColor;
    // 比例绘图的起始角度（-90为从圆环正上方开始绘制）
    private BigDecimal mRingStartAngle;
    // 第二段折线偏转角度
    private int mFoldLineAngle;
    // 文本左右两端的间隔
    private int mTextHorizontalInterval = 2;
    // 文本的上下间隔
    private int mTextVerticalHeightInterval = 2;
    // 元素的上下间隔
    private int mItemVerticalHeightInterval = 4;
    // 控件宽度和高度
    private int mWidth, mHeight;
    // 内圆经过缩放后的实际半径
    private int mActualInnerRadius;
    // 外圆经过缩放后的实际半径
    private int mActualOuterRadius;
    // 缩放过后的比例名称和比例数值的字体大小
    private int mActualNameTextSize, mActualScaleTextSize;
    // 圆环绘制矩形区域
    private RectF mRecF;
    // 折线绘制路径
    private Path mFoldLinePath;
    // 背景，圆环，折线，比例名称文本和比例数值文本的绘制画笔
    private Paint mBgRingPaint, mScaleRingPaint, mFoldLinePaint, mNameTextPaint, mScaleTextPaint;
    // 比例数据列表
    private List<ScaleData> mScaleDataList;
    // -90到0度比例数据列表
    private List<ScaleData> mN90To0ScaleDataList = new ArrayList<ScaleData>();
    // 0到90度比例数据列表
    private List<ScaleData> m0To90ScaleDataList = new ArrayList<ScaleData>();
    // 90到180度比例数据列表
    private List<ScaleData> m90To180ScaleDataList = new ArrayList<ScaleData>();
    // 180到270度比例数据列表
    private List<ScaleData> m180To270ScaleDataList = new ArrayList<ScaleData>();
    // 补齐背景数据（比例列表无法填充整个圆环时的补齐数据）
    private ScaleData mOverPlusBgData;
    // 控件坐标X值最大值和坐标X值最小值
    private float mMaxX, mMinX;
    // 控件坐标Y值最大值和坐标Y值最小值
    private float mMaxY, mMinY;
    // 测量过后应该进行缩放的比例
    private float mViewZoomScaleAfterMeasure = 1.0f;
    // 是否需要进行缩放
    private boolean mNeedZoom = false;
    private boolean mEnableComplementOverPlus;

    public RingScaleDiagramView(Context context) {
        this(context, null);
    }

    public RingScaleDiagramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingScaleDiagramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingScaleDiagramView);
        mInnerRadius = typedArray.getDimensionPixelOffset(R.styleable.RingScaleDiagramView_innerRadius, 40);
        mOuterRadius = typedArray.getDimensionPixelOffset(R.styleable.RingScaleDiagramView_outerRadius, 80);
        mFoldLineWidth = typedArray.getDimensionPixelOffset(R.styleable.RingScaleDiagramView_foldLineWidth, 3);
        mFoldFirstLineLength = typedArray.getDimensionPixelOffset(R.styleable.RingScaleDiagramView_foldFirstLineLength, (int) (mOuterRadius * 0.5f));
        mFoldSecondLineLength = (int) (mFoldFirstLineLength * 0.4f);
        mScaleTextColor = typedArray.getColor(R.styleable.RingScaleDiagramView_nameTextColor, Color.BLACK);
        mScaleTextSize = typedArray.getDimensionPixelSize(R.styleable.RingScaleDiagramView_nameTextSize, 28);
        mNameTextColor = typedArray.getColor(R.styleable.RingScaleDiagramView_nameTextColor, Color.BLACK);
        mNameTextSize = typedArray.getDimensionPixelSize(R.styleable.RingScaleDiagramView_nameTextSize, 28);
        mIsTwoTextLine = typedArray.getBoolean(R.styleable.RingScaleDiagramView_isTwoTextLine, true);
        mRingBackgroundColor = typedArray.getColor(R.styleable.RingScaleDiagramView_ringBackgroundColor, Color.LTGRAY);
        int startAngle = typedArray.getInteger(R.styleable.RingScaleDiagramView_startAngle, -90);
        mRingStartAngle = new BigDecimal(String.valueOf((startAngle % 360 + 450) % 360 - 90));
        int foldLineAngles = 30;
        mFoldLineAngle = (foldLineAngles % 360 + 360) % 360;
        mActualOuterRadius = mOuterRadius;
        mActualInnerRadius = mInnerRadius;
        mActualNameTextSize = mNameTextSize;
        mActualScaleTextSize = mScaleTextSize;
        mMaxX = mOuterRadius;
        mMinX = -mOuterRadius;
        mMaxY = mOuterRadius;
        mMinY = -mOuterRadius;
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        mBgRingPaint = new Paint();
        mBgRingPaint.setAntiAlias(true);
        mBgRingPaint.setStyle(Paint.Style.STROKE);
        mBgRingPaint.setColor(mRingBackgroundColor);

        mScaleRingPaint = new Paint();
        mScaleRingPaint.setAntiAlias(true);
        mScaleRingPaint.setStyle(Paint.Style.STROKE);

        mFoldLinePaint = new Paint();
        mFoldLinePaint.setAntiAlias(true);

        mNameTextPaint = new Paint();
        mNameTextPaint.setAntiAlias(true);
        mNameTextPaint.setColor(mNameTextColor);
        mNameTextPaint.setTextSize(mNameTextSize);

        mScaleTextPaint = new Paint();
        mScaleTextPaint.setAntiAlias(true);
        mScaleTextPaint.setColor(mScaleTextColor);
        mScaleTextPaint.setTextSize(mScaleTextSize);

        mFoldLinePath = new Path();
        mFoldLinePaint.setStyle(Paint.Style.STROKE);
        mFoldLinePaint.setStrokeWidth(mFoldLineWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.AT_MOST) {
            if (hasData()) {
                mWidth = 2 * (int) Math.max(Math.abs(mMaxX), Math.abs(mMinX));
            } else {
                mWidth = 0;
            }
        } else {
            mWidth = specSize;
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.AT_MOST) {
            if (hasData()) {
                mHeight = 2 * (int) Math.max(Math.abs(mMaxY), Math.abs(mMinY));
            } else {
                mHeight = 0;
            }
        } else {
            mHeight = specSize;
        }

        if (resetViewZoomScale()) {
            mNeedZoom = true;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    private boolean resetViewZoomScale() {
        if (mWidth == 0 || mHeight == 0) {
            return true;
        }
        if (!hasData()) {
            return false;
        }
        float oldViewScaleAfterMeasure = mViewZoomScaleAfterMeasure;
        float newWidthRateAfterMeasure = 1.0f;
        float newHeightRateAfterMeasure = 1.0f;
        int contentWidth = 2 * (int) Math.max(Math.abs(mMaxX), Math.abs(mMinX));
        if (mWidth < contentWidth) {
            newWidthRateAfterMeasure = (float) mWidth / contentWidth;
        }
        int contentHeight = 2 * (int) Math.max(Math.abs(mMaxY), Math.abs(mMaxY));
        if (mHeight < contentHeight) {
            newHeightRateAfterMeasure = (float) mHeight / contentHeight;
        }
        mViewZoomScaleAfterMeasure = Math.min(newWidthRateAfterMeasure, newHeightRateAfterMeasure);
        if (mViewZoomScaleAfterMeasure != oldViewScaleAfterMeasure) {
            mViewZoomScaleAfterMeasure = Math.min(newWidthRateAfterMeasure, newHeightRateAfterMeasure);
            mActualOuterRadius = (int) Math.floor(mViewZoomScaleAfterMeasure * mOuterRadius);
            mActualInnerRadius = (int) Math.floor(mViewZoomScaleAfterMeasure * mInnerRadius);
            mActualNameTextSize = (int) Math.floor(mViewZoomScaleAfterMeasure * mNameTextSize);
            mActualScaleTextSize = (int) Math.floor(mViewZoomScaleAfterMeasure * mScaleTextSize);
            return true;
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int recFRadius = (mActualOuterRadius + mActualInnerRadius) / 2;
        mRecF = new RectF(centerX - recFRadius, centerY - recFRadius, centerX + recFRadius, centerY + recFRadius);
        mBgRingPaint.setStrokeWidth(mActualOuterRadius - mActualInnerRadius);
        canvas.drawArc(mRecF, mRingStartAngle.floatValue(), 360, false, mBgRingPaint);
        if (!hasData()) {
            return;
        }
        if (mNeedZoom) {
            zoomDataPosition(mViewZoomScaleAfterMeasure);
            mNeedZoom = false;
        }
        mScaleRingPaint.setStrokeWidth(mActualOuterRadius - mActualInnerRadius);
        ScaleData scaleData = null;
        for (int i = 0; i < mScaleDataList.size(); i++) {
            scaleData = mScaleDataList.get(i);
            mScaleRingPaint.setColor(Color.parseColor(scaleData.getRingColor()));
            canvas.drawArc(mRecF, scaleData.getStartAngle(), scaleData.getSweepAngle(), false, mScaleRingPaint);

            mFoldLinePath.reset();
            mFoldLinePaint.setColor(Color.parseColor(scaleData.getFoldLineColor() == null ? scaleData.getRingColor() : scaleData.getFoldLineColor()));
            float[] lineXPath = scaleData.getLineXPaths();
            float[] lineYPath = scaleData.getLineYPaths();

            mFoldLinePath.moveTo(lineXPath[0] + centerX, lineYPath[0] + centerY);
            mFoldLinePath.lineTo(lineXPath[1] + centerX, lineYPath[1] + centerY);
            mFoldLinePath.lineTo(lineXPath[2] + centerX, lineYPath[2] + centerY);
            canvas.drawPath(mFoldLinePath, mFoldLinePaint);

            mNameTextPaint.setTextSize(mActualNameTextSize);
            canvas.drawText(scaleData.getNameText(), scaleData.getNameTextX() + centerX, scaleData.getNameTextY() + centerY, mNameTextPaint);

            mScaleTextPaint.setTextSize(mActualScaleTextSize);
            canvas.drawText(scaleData.getScaleText(), scaleData.getScaleTextX() + centerX, scaleData.getScaleTextY() + centerY, mScaleTextPaint);
        }
        if (mOverPlusBgData != null && mEnableComplementOverPlus) {
            canvas.drawArc(mRecF, mOverPlusBgData.getStartAngle(), mOverPlusBgData.getSweepAngle(), false, mBgRingPaint);
        }
    }

    private boolean hasData() {
        return mScaleDataList != null && mScaleDataList.size() > 0;
    }

    public void setData(final List<ScaleData> dataList) {
        this.setData(dataList, false);
    }

    public void setData(final List<ScaleData> dataList, final boolean enableComplementOverPlus) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mEnableComplementOverPlus = enableComplementOverPlus;
                mScaleDataList = dataList;
                mViewZoomScaleAfterMeasure = 1;
                mNeedZoom = false;
                if (!hasData()) {
                    setVisibility(GONE);
                    return;
                }
                measureTextAndDivideZone();
                parseDataPosition();
                if (resetViewZoomScale()) {
                    mNeedZoom = true;
                }
                setVisibility(VISIBLE);
                requestLayout();
                postInvalidate();
            }
        });
    }

    private void measureTextAndDivideZone() {
        if (!hasData()) {
            return;
        }
        ScaleData scaleData = null;
        BigDecimal ringStartAngle = new BigDecimal(mRingStartAngle.floatValue());
        BigDecimal correctionAngle = new BigDecimal("5");
        mOverPlusBgData = null;
        mN90To0ScaleDataList.clear();
        m0To90ScaleDataList.clear();
        m90To180ScaleDataList.clear();
        m180To270ScaleDataList.clear();
        for (int i = 0; i < mScaleDataList.size(); i++) {
            scaleData = mScaleDataList.get(i);

            Rect nameRect = new Rect();
            mNameTextPaint.getTextBounds(scaleData.getNameText(), 0, scaleData.getNameText().length(), nameRect);
            int nameTextWidth = nameRect.width();
            Rect scaleRect = new Rect();
            mScaleTextPaint.getTextBounds(scaleData.getScaleText(), 0, scaleData.getScaleText().length(), scaleRect);
            int scaleTextWidth = scaleRect.width();
            int maxTextWidth = Math.max(nameTextWidth, scaleTextWidth);
            if (!mIsTwoTextLine) {
                maxTextWidth = nameTextWidth + scaleTextWidth + mTextHorizontalInterval * 3;
                scaleData.setMaxTextHeight(Math.max(mScaleTextSize, mNameTextSize) + mTextVerticalHeightInterval * 2);
            } else {
                scaleData.setMaxTextHeight(mScaleTextSize + mNameTextSize + mTextVerticalHeightInterval * 3);
            }
            scaleData.setMaxTextWidth(maxTextWidth);
            scaleData.setNameTextWidth(nameTextWidth);
            scaleData.setScaleTextWidth(scaleTextWidth);

            BigDecimal scaleAngle = new BigDecimal(scaleData.getScale()).multiply(new BigDecimal("360"));
            BigDecimal curOverPlusScale = new BigDecimal("360").add(mRingStartAngle).subtract(ringStartAngle);
            if (i == mScaleDataList.size() - 1) {
                if (ringStartAngle.add(scaleAngle).subtract(mRingStartAngle).floatValue() < 360) {
                    scaleData.setStartAngle(ringStartAngle.floatValue());
                    mOverPlusBgData = new ScaleData();
                    mOverPlusBgData.setStartAngle(ringStartAngle.add(scaleAngle).floatValue());
                    mOverPlusBgData.setSweepAngle(new BigDecimal("360").add(mRingStartAngle).subtract(ringStartAngle.add(scaleAngle)).floatValue());
                } else {
                    scaleData.setStartAngle(ringStartAngle.floatValue());
                }
            } else {
                if (i == 0) {
                    scaleData.setStartAngle(ringStartAngle.subtract(correctionAngle).floatValue());
                } else {
                    scaleData.setStartAngle(ringStartAngle.floatValue());
                }
            }
            scaleData.setSweepAngle(curOverPlusScale.floatValue());

            BigDecimal lineStartPointAngle = ringStartAngle.add(scaleAngle.divide(new BigDecimal("2")));
            scaleData.setLineStartAngle(lineStartPointAngle.floatValue());
            int times = ((lineStartPointAngle.intValue() + 90) / 45) % 8;
            switch (times) {
                case 0:
                    scaleData.setZone(ZONE_N90_TO_N45);
                    mN90To0ScaleDataList.add(scaleData);
                    break;
                case 1:
                    scaleData.setZone(ZONE_N45_TO_0);
                    mN90To0ScaleDataList.add(scaleData);
                    break;
                case 2:
                    scaleData.setZone(ZONE_0_TO_45);
                    m0To90ScaleDataList.add(scaleData);
                    break;
                case 3:
                    scaleData.setZone(ZONE_45_TO_90);
                    m0To90ScaleDataList.add(scaleData);
                    break;
                case 4:
                    scaleData.setZone(ZONE_90_TO_135);
                    m90To180ScaleDataList.add(scaleData);
                    break;
                case 5:
                    scaleData.setZone(ZONE_135_TO_180);
                    m90To180ScaleDataList.add(scaleData);
                    break;
                case 6:
                    scaleData.setZone(ZONE_180_TO_225);
                    m180To270ScaleDataList.add(scaleData);
                    break;
                case 7:
                    scaleData.setZone(ZONE_225_TO_270);
                    m180To270ScaleDataList.add(scaleData);
                    break;
            }

            ringStartAngle = ringStartAngle.add(scaleAngle);
        }
    }

    private void parseDataPosition() {
        if (hasData()) {
            ScaleData scaleData = null;
            mMaxX = mOuterRadius;
            mMinX = -mOuterRadius;
            mMaxY = mOuterRadius;
            mMinY = -mOuterRadius;
            float lastN90To0LineEndYPath = 0;
            float last0To90LineEndYPath = mIsTwoTextLine ? -(mNameTextSize + mScaleTextSize + mTextVerticalHeightInterval * 3 + mItemVerticalHeightInterval) :
                    -(Math.max(mNameTextSize, mScaleTextSize) + mTextVerticalHeightInterval * 2 + mItemVerticalHeightInterval);
            float last90To180LineEndYPath = last0To90LineEndYPath;
            float last180To270LineEndYPath = 0;
            for (int i = mN90To0ScaleDataList.size() - 1; i >= 0; i--) {
                scaleData = mN90To0ScaleDataList.get(i);
                float lineStartPointAngle = scaleData.getLineStartAngle();
                float radian = (float) Math.PI * lineStartPointAngle / 180;
                float cosVal = (float) Math.cos(radian);
                float sinVal = (float) Math.sin(radian);
                float[] lineXPath = new float[3];
                float[] lineYPath = new float[3];
                lineXPath[0] = cosVal * mOuterRadius;
                lineYPath[0] = sinVal * mOuterRadius;
                lineXPath[1] = lineXPath[0] + mFoldFirstLineLength * cosVal;
                lineYPath[1] = lineYPath[0] + mFoldFirstLineLength * sinVal;

                float radianSecond = (float) Math.PI * (lineStartPointAngle + mFoldLineAngle) / 180;
                float secondCosVal = (float) Math.cos(radianSecond);
                float secondSinTwo = (float) Math.sin(radianSecond);

                lineXPath[2] = lineXPath[1] + mFoldSecondLineLength * secondCosVal;
                lineYPath[2] = lineYPath[1] + mFoldSecondLineLength * secondSinTwo;
                if (i != mN90To0ScaleDataList.size() - 1) {
                    float thresholdY = lastN90To0LineEndYPath - scaleData.getMaxTextHeight() - mItemVerticalHeightInterval;
                    if (thresholdY < lineYPath[2]) {
                        lineYPath[2] = thresholdY;
                    }
                }

                scaleData.setLineXPaths(lineXPath);
                scaleData.setLineYPaths(lineYPath);

                scaleData.setNameTextX(lineXPath[2] + mTextHorizontalInterval);
                scaleData.setNameTextY(lineYPath[2]);
                if (mIsTwoTextLine) {
                    scaleData.setScaleTextX(lineXPath[2] + mTextHorizontalInterval);
                    scaleData.setScaleTextY(lineYPath[2] + (mNameTextSize + mTextVerticalHeightInterval));
                    int offset = Math.abs((int) ((scaleData.getScaleTextWidth() - scaleData.getNameTextWidth()) / 2));
                    if (scaleData.getScaleTextWidth() > scaleData.getNameTextWidth()) {
                        scaleData.setNameTextX(scaleData.getNameTextX() + offset);
                    } else {
                        scaleData.setScaleTextX(scaleData.getScaleTextX() + offset);
                    }
                } else {
                    scaleData.setScaleTextX(scaleData.getNameTextX() + scaleData.getNameTextWidth() + mTextHorizontalInterval);
                    scaleData.setScaleTextY(scaleData.getNameTextY());
                }

                mMaxX = Math.max(mMaxX, lineXPath[2] + scaleData.getMaxTextWidth());
                mMinY = Math.min(mMinY, lineYPath[2] - scaleData.getMaxTextHeight() - mItemVerticalHeightInterval);

                lastN90To0LineEndYPath = lineYPath[2];
                if (i == 0) {
                    last0To90LineEndYPath = lineYPath[2];
                }
            }
            for (int i = 0; i < m0To90ScaleDataList.size(); i++) {
                scaleData = m0To90ScaleDataList.get(i);
                float lineStartPointAngle = scaleData.getLineStartAngle();
                float radian = (float) Math.PI * lineStartPointAngle / 180;
                float cosVal = (float) Math.cos(radian);
                float sinVal = (float) Math.sin(radian);
                float[] lineXPath = new float[3];
                float[] lineYPath = new float[3];
                lineXPath[0] = cosVal * mOuterRadius;
                lineYPath[0] = sinVal * mOuterRadius;
                lineXPath[1] = lineXPath[0] + mFoldFirstLineLength * cosVal;
                lineYPath[1] = lineYPath[0] + mFoldFirstLineLength * sinVal;

                float radianSecond = (float) Math.PI * (lineStartPointAngle - mFoldLineAngle) / 180;
                float secondCosVal = (float) Math.cos(radianSecond);
                float secondSinTwo = (float) Math.sin(radianSecond);

                lineXPath[2] = lineXPath[1] + mFoldSecondLineLength * secondCosVal;
                lineYPath[2] = lineYPath[1] + mFoldSecondLineLength * secondSinTwo;
                if (last0To90LineEndYPath > -(scaleData.getMaxTextHeight() + mItemVerticalHeightInterval)) {
                    float thresholdY = last0To90LineEndYPath + scaleData.getMaxTextHeight() + mItemVerticalHeightInterval;
                    if (thresholdY > lineYPath[2]) {
                        lineYPath[2] = thresholdY;
                    }
                }

                scaleData.setLineXPaths(lineXPath);
                scaleData.setLineYPaths(lineYPath);

                scaleData.setNameTextX(lineXPath[2] + mTextHorizontalInterval);
                scaleData.setNameTextY(lineYPath[2]);
                if (mIsTwoTextLine) {
                    scaleData.setScaleTextX(lineXPath[2] + mTextHorizontalInterval);
                    scaleData.setScaleTextY(lineYPath[2] + (mNameTextSize + mTextVerticalHeightInterval));
                    int offset = Math.abs((int) ((scaleData.getScaleTextWidth() - scaleData.getNameTextWidth()) / 2));
                    if (scaleData.getScaleTextWidth() > scaleData.getNameTextWidth()) {
                        scaleData.setNameTextX(scaleData.getNameTextX() + offset);
                    } else {
                        scaleData.setScaleTextX(scaleData.getScaleTextX() + offset);
                    }
                } else {
                    scaleData.setScaleTextX(scaleData.getNameTextX() + scaleData.getNameTextWidth() + mTextHorizontalInterval);
                    scaleData.setScaleTextY(scaleData.getNameTextY());
                }

                mMaxX = Math.max(mMaxX, lineXPath[2] + scaleData.getMaxTextWidth());
                mMaxY = Math.max(mMaxY, lineYPath[2] + scaleData.getMaxTextHeight() + mItemVerticalHeightInterval);

                last0To90LineEndYPath = lineYPath[2];
            }
            for (int i = 0; i < m180To270ScaleDataList.size(); i++) {
                scaleData = m180To270ScaleDataList.get(i);
                float lineStartPointAngle = scaleData.getLineStartAngle();
                float radian = (float) Math.PI * lineStartPointAngle / 180;
                float cosVal = (float) Math.cos(radian);
                float sinVal = (float) Math.sin(radian);
                float[] lineXPath = new float[3];
                float[] lineYPath = new float[3];
                lineXPath[0] = cosVal * mOuterRadius;
                lineYPath[0] = sinVal * mOuterRadius;
                lineXPath[1] = lineXPath[0] + mFoldFirstLineLength * cosVal;
                lineYPath[1] = lineYPath[0] + mFoldFirstLineLength * sinVal;

                float radianSecond = (float) Math.PI * (lineStartPointAngle - mFoldLineAngle) / 180;
                float secondCosVal = (float) Math.cos(radianSecond);
                float secondSinTwo = (float) Math.sin(radianSecond);

                lineXPath[2] = lineXPath[1] + mFoldSecondLineLength * secondCosVal;
                lineYPath[2] = lineYPath[1] + mFoldSecondLineLength * secondSinTwo;
                if (i > 0) {
                    float thresholdY = last180To270LineEndYPath - scaleData.getMaxTextHeight() - mItemVerticalHeightInterval;
                    if (thresholdY < lineYPath[2]) {
                        lineYPath[2] = thresholdY;
                    }
                }
                scaleData.setLineXPaths(lineXPath);
                scaleData.setLineYPaths(lineYPath);

                scaleData.setNameTextX(lineXPath[2] - scaleData.getMaxTextWidth());
                scaleData.setNameTextY(lineYPath[2]);
                if (mIsTwoTextLine) {
                    scaleData.setScaleTextX(lineXPath[2] - scaleData.getMaxTextWidth());
                    scaleData.setScaleTextY(lineYPath[2] + (mNameTextSize + mTextVerticalHeightInterval));
                    int offset = Math.abs((int) ((scaleData.getScaleTextWidth() - scaleData.getNameTextWidth()) / 2));
                    if (scaleData.getScaleTextWidth() > scaleData.getNameTextWidth()) {
                        scaleData.setNameTextX(scaleData.getNameTextX() + offset);
                    } else {
                        scaleData.setScaleTextX(scaleData.getScaleTextX() + offset);
                    }
                } else {
                    scaleData.setScaleTextX(scaleData.getNameTextX() + scaleData.getNameTextWidth() + mTextHorizontalInterval);
                    scaleData.setScaleTextY(scaleData.getNameTextY());
                }

                mMinX = Math.min(mMinX, lineXPath[2] - scaleData.getMaxTextWidth());
                mMinY = Math.min(mMinY, lineYPath[2] - scaleData.getMaxTextHeight() - mItemVerticalHeightInterval);

                last180To270LineEndYPath = lineYPath[2];
                if (i == 0) {
                    last90To180LineEndYPath = lineYPath[2];
                }
            }
            for (int i = m90To180ScaleDataList.size() - 1; i >= 0; i--) {
                scaleData = m90To180ScaleDataList.get(i);
                float lineStartPointAngle = scaleData.getLineStartAngle();
                float radian = (float) Math.PI * lineStartPointAngle / 180;
                float cosVal = (float) Math.cos(radian);
                float sinVal = (float) Math.sin(radian);
                float[] lineXPath = new float[3];
                float[] lineYPath = new float[3];
                lineXPath[0] = cosVal * mOuterRadius;
                lineYPath[0] = sinVal * mOuterRadius;
                lineXPath[1] = lineXPath[0] + mFoldFirstLineLength * cosVal;
                lineYPath[1] = lineYPath[0] + mFoldFirstLineLength * sinVal;

                float radianSecond = (float) Math.PI * (lineStartPointAngle + mFoldLineAngle) / 180;
                float secondCosVal = (float) Math.cos(radianSecond);
                float secondSinTwo = (float) Math.sin(radianSecond);

                lineXPath[2] = lineXPath[1] + mFoldSecondLineLength * secondCosVal;
                lineYPath[2] = lineYPath[1] + mFoldSecondLineLength * secondSinTwo;
                if (last90To180LineEndYPath > -(scaleData.getMaxTextHeight() + mItemVerticalHeightInterval)) {
                    float thresholdY = last90To180LineEndYPath + scaleData.getMaxTextHeight() + mItemVerticalHeightInterval;
                    if (thresholdY > lineYPath[2]) {
                        lineYPath[2] = thresholdY;
                    }
                }
                scaleData.setLineXPaths(lineXPath);
                scaleData.setLineYPaths(lineYPath);

                scaleData.setNameTextX(lineXPath[2] - scaleData.getMaxTextWidth());
                scaleData.setNameTextY(lineYPath[2]);
                if (mIsTwoTextLine) {
                    scaleData.setScaleTextX(lineXPath[2] - scaleData.getMaxTextWidth());
                    scaleData.setScaleTextY(lineYPath[2] + (mNameTextSize + mTextVerticalHeightInterval));
                    int offset = Math.abs((int) ((scaleData.getScaleTextWidth() - scaleData.getNameTextWidth()) / 2));
                    if (scaleData.getScaleTextWidth() > scaleData.getNameTextWidth()) {
                        scaleData.setNameTextX(scaleData.getNameTextX() + offset);
                    } else {
                        scaleData.setScaleTextX(scaleData.getScaleTextX() + offset);
                    }
                } else {
                    scaleData.setScaleTextX(scaleData.getNameTextX() + scaleData.getNameTextWidth() + mTextHorizontalInterval);
                    scaleData.setScaleTextY(scaleData.getNameTextY());
                }

                mMinX = Math.min(mMinX, lineXPath[2] - scaleData.getMaxTextWidth());
                mMaxY = Math.max(mMaxY, lineYPath[2] + scaleData.getMaxTextHeight() + mItemVerticalHeightInterval);

                last90To180LineEndYPath = lineYPath[2];
            }
        }
    }

    private void zoomDataPosition(float viewScaleAfterMeasure) {
        if (!hasData()) {
            return;
        }
        ScaleData scaleData = null;
        for (int i = 0; i < mScaleDataList.size(); i++) {
            scaleData = mScaleDataList.get(i);
            float[] lineXPath = scaleData.getLineXPaths();
            float[] lineYPath = scaleData.getLineYPaths();
            for (int j = 0; j < lineXPath.length; j++) {
                lineXPath[j] = lineXPath[j] * viewScaleAfterMeasure;
                lineYPath[j] = lineYPath[j] * viewScaleAfterMeasure;
            }
            scaleData.setNameTextX(scaleData.getNameTextX() * viewScaleAfterMeasure);
            scaleData.setNameTextY(scaleData.getNameTextY() * viewScaleAfterMeasure);
            scaleData.setScaleTextX(scaleData.getScaleTextX() * viewScaleAfterMeasure);
            scaleData.setScaleTextY(scaleData.getScaleTextY() * viewScaleAfterMeasure);
        }
    }

    public static class ScaleData {
        // 比例名称文本
        private String nameText;
        // 比例数值文本
        private String scaleText;
        // 比例值
        private String scale;
        // 比例环的颜色
        private String ringColor = "#89DBCB";
        // 折线的颜色
        private String foldLineColor;
        // 比例名称和比例数值文本的宽度
        private int nameTextWidth, scaleTextWidth;
        // 该比例文本的最大宽度
        private int maxTextWidth;
        // 该比例文本的最大高度
        private int maxTextHeight;
        // 改比例数据所在的分区
        private int zone;
        // 指示线开始的角度
        private float lineStartAngle;
        // 开始角度
        private float startAngle;
        // 覆盖度数
        private float sweepAngle;
        // 折线X坐标轨迹
        private float[] lineXPaths;
        // 折线Y坐标轨迹
        private float[] lineYPaths;
        // 比例名称文本X坐标，Y坐标
        private float nameTextX, nameTextY;
        // 比例数值文本X坐标，Y坐标
        private float scaleTextX, scaleTextY;

        public String getNameText() {
            return nameText;
        }

        public void setNameText(String nameText) {
            this.nameText = nameText;
        }

        public String getScaleText() {
            return scaleText;
        }

        public void setScaleText(String scaleText) {
            this.scaleText = scaleText;
        }

        public String getScale() {
            return scale;
        }

        public void setScale(String scale) {
            this.scale = scale;
        }

        public String getRingColor() {
            return ringColor;
        }

        public void setRingColor(String ringColor) {
            this.ringColor = ringColor;
        }

        public String getFoldLineColor() {
            return foldLineColor;
        }

        public void setFoldLineColor(String foldLineColor) {
            this.foldLineColor = foldLineColor;
        }

        public int getNameTextWidth() {
            return nameTextWidth;
        }

        public void setNameTextWidth(int nameTextWidth) {
            this.nameTextWidth = nameTextWidth;
        }

        public int getScaleTextWidth() {
            return scaleTextWidth;
        }

        public void setScaleTextWidth(int scaleTextWidth) {
            this.scaleTextWidth = scaleTextWidth;
        }

        public int getMaxTextWidth() {
            return maxTextWidth;
        }

        public void setMaxTextWidth(int maxTextWidth) {
            this.maxTextWidth = maxTextWidth;
        }

        public int getMaxTextHeight() {
            return maxTextHeight;
        }

        public void setMaxTextHeight(int maxTextHeight) {
            this.maxTextHeight = maxTextHeight;
        }

        public int getZone() {
            return zone;
        }

        public void setZone(int zone) {
            this.zone = zone;
        }

        public float getLineStartAngle() {
            return lineStartAngle;
        }

        public void setLineStartAngle(float lineStartAngle) {
            this.lineStartAngle = lineStartAngle;
        }

        public float getStartAngle() {
            return startAngle;
        }

        public void setStartAngle(float startAngle) {
            this.startAngle = startAngle;
        }

        public float getSweepAngle() {
            return sweepAngle;
        }

        public void setSweepAngle(float sweepAngle) {
            this.sweepAngle = sweepAngle;
        }

        public float[] getLineXPaths() {
            return lineXPaths;
        }

        public void setLineXPaths(float[] lineXPaths) {
            this.lineXPaths = lineXPaths;
        }

        public float[] getLineYPaths() {
            return lineYPaths;
        }

        public void setLineYPaths(float[] lineYPaths) {
            this.lineYPaths = lineYPaths;
        }

        public float getNameTextX() {
            return nameTextX;
        }

        public void setNameTextX(float nameTextX) {
            this.nameTextX = nameTextX;
        }

        public float getNameTextY() {
            return nameTextY;
        }

        public void setNameTextY(float nameTextY) {
            this.nameTextY = nameTextY;
        }

        public float getScaleTextX() {
            return scaleTextX;
        }

        public void setScaleTextX(float scaleTextX) {
            this.scaleTextX = scaleTextX;
        }

        public float getScaleTextY() {
            return scaleTextY;
        }

        public void setScaleTextY(float scaleTextY) {
            this.scaleTextY = scaleTextY;
        }
    }
}

