package com.fy.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


public class RingProgressBar extends View {


    private Paint mPaint;
    private int width, height;
    private float progress, maxProgress;
    private int backgroundColor;//背景色
    private int foregroundColor;//前景色
    private int startColor;//渐变开始颜色
    private int endColor;//渐变结束颜色
    private float roundWidth;//圆环宽度

    public RingProgressBar(Context context) {
        this(context, null);
    }


    public RingProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public RingProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressBar);
        progress = typedArray.getFloat(R.styleable.RingProgressBar_progress, 0f);
        maxProgress = typedArray.getFloat(R.styleable.RingProgressBar_maxProgress, 100f);
        startAngle = typedArray.getFloat(R.styleable.RingProgressBar_startAngle, 270f);
        backgroundColor = typedArray.getColor(R.styleable.RingProgressBar_backgroundColor, Color.GRAY);
        foregroundColor = typedArray.getColor(R.styleable.RingProgressBar_foregroundColor, Color.BLUE);
        startColor = typedArray.getColor(R.styleable.RingProgressBar_startColor, foregroundColor);
        endColor = typedArray.getColor(R.styleable.RingProgressBar_endColor, foregroundColor);
        roundWidth = typedArray.getDimension(R.styleable.RingProgressBar_ringWidth,5f);
        typedArray.recycle();
        mPaint = new Paint();
        progressRectF = new RectF();
        sweepAngle = 360 * progress / maxProgress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

    }

    private RectF progressRectF;
    private float startAngle, sweepAngle;


    @Override
    protected void onDraw(Canvas canvas) {
        float cx = width / 2f;
        float cy = height / 2f;
        float radius = width / 2f - roundWidth / 2f;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setColor(backgroundColor);
        canvas.drawCircle(cx, cy, radius, mPaint);

        progressRectF.set(roundWidth / 2f, roundWidth / 2f, width - roundWidth / 2f, height - roundWidth / 2f);
        mPaint.setColor(foregroundColor);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);//这个圆角方法会让圆弧的两端都是圆角

        canvas.drawArc(progressRectF, startAngle, sweepAngle, false, mPaint);

    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        postInvalidate();
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
        postInvalidate();
    }
}
