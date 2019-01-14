package com.pine.tool.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tanghongfeng on 2019/1/11
 */

public class CircleHoleBgView extends View {
    private int mHoleCenterX;
    private int mHoleCenterY;
    private float mHoleRadius;
    private int mBgColor = Color.parseColor("#99000000");

    public CircleHoleBgView(Context context) {
        super(context);
    }

    public CircleHoleBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleHoleBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHoleLocation(int holeCenterX, int holeCenterY, float radius) {
        mHoleCenterX = holeCenterX;
        mHoleCenterY = holeCenterY;
        mHoleRadius = radius;
    }

    public void setBackgroundColor(String colorStr) {
        mBgColor = Color.parseColor(colorStr);
    }

    public void setBackgroundColor(int color) {
        mBgColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        paint.setColor(mBgColor);
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawCircle(mHoleCenterX, mHoleCenterY, mHoleRadius, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);
        super.onDraw(canvas);
    }
}
