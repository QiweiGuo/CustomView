package com.fy.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ProgressBar1 extends View {


    private final String TAG = "ProgressBar1";

    private int orientation;//进度条方向

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    private float progress;
    private float maxProgress, minProgress;

    private Paint mPaint;
    private int backgroundColor;//背景色
    private int foregroundColor;//前景色
    private int startColor;//渐变开始颜色
    private int endColor;//渐变结束颜色
    private String gradientOrientation;//渐变方向，取值有vertical和horizontal

    private int mWidth;
    private int mHeight;

    //圆角半径，不知道为什么只设置一个的话会没有效果
    private float radiusX, radiusY;

    public ProgressBar1(Context context) {
        this(context, null);
    }

    public ProgressBar1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();

        //读取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar1);
        orientation = typedArray.getInt(R.styleable.ProgressBar1_android_orientation, LinearLayout.HORIZONTAL);//读取进度条方向，默认水平方向
        gradientOrientation = typedArray.getString(R.styleable.ProgressBar1_gradientOrientation);
        progress = typedArray.getFloat(R.styleable.ProgressBar1_progress, 0);//读取进度条进度，默认0
        maxProgress = typedArray.getFloat(R.styleable.ProgressBar1_maxProgress, 100);//读取进度条最大进度，默认100
        minProgress = typedArray.getFloat(R.styleable.ProgressBar1_minProgress, 0);//读取进度条最小进度，默认0
        radiusX = typedArray.getDimension(R.styleable.ProgressBar1_radiusX, 0);//X轴方向圆角半径，默认0
        radiusY = typedArray.getDimension(R.styleable.ProgressBar1_radiusY, 0);//Y轴方向圆角半径，默认0
        backgroundColor = typedArray.getColor(R.styleable.ProgressBar1_backgroundColor, Color.GRAY);
        foregroundColor = typedArray.getColor(R.styleable.ProgressBar1_foregroundColor, Color.BLUE);
        startColor = typedArray.getColor(R.styleable.ProgressBar1_startColor, foregroundColor);
        endColor = typedArray.getColor(R.styleable.ProgressBar1_endColor, foregroundColor);
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = 0;
        mHeight = 0;
        /*
         * 设置宽高
         */
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG,"onMeasure->widthSpecMode is:"+widthSpecMode);
        Log.d(TAG,"onMeasure->widthSpecSize is:"+widthSpecSize);
        Log.d(TAG,"onMeasure->heightSpecMode is:"+heightSpecMode);
        Log.d(TAG,"onMeasure->heightSpecSize is:"+heightSpecSize);
        switch (widthSpecMode) {
            case MeasureSpec.EXACTLY:// 明确指定了
//                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                mWidth = widthSpecSize;
//                mWidth = getPaddingLeft() + getPaddingRight();
                break;
            case MeasureSpec.UNSPECIFIED://父布局没有限制，可以为任意宽度
                break;
        }


        switch (heightSpecMode) {
            case MeasureSpec.EXACTLY:// 明确指定了
                mHeight = heightSpecSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
//                mHeight = mWidth / 10;
                mHeight = heightSpecSize;
                break;
            case MeasureSpec.UNSPECIFIED://父布局没有限制，可以为任意高度
                break;
        }

        if (orientation == LinearLayout.VERTICAL && mWidth > mHeight) {
            //如果是竖直方向，并且宽大于高，则把宽强制改为高的十分之一
            mWidth = mHeight / 10;
        } else if (orientation == LinearLayout.HORIZONTAL && mHeight > mWidth) {
            //如果是水平方向，并且高大于宽，则把高强制改为宽的十分之一
            mHeight = mWidth / 10;
        }

        Log.d(TAG, "onMeasure-> mWidth=" + mWidth + " mHeight=" + mHeight);
        setMeasuredDimension(mWidth, mHeight);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(backgroundColor);//设置背景进度条颜色

        RectF rectFBack = new RectF();
        rectFBack.left = 0.0f;
        rectFBack.top = 0.0f;
        rectFBack.right = mWidth;
        rectFBack.bottom = mHeight;
        canvas.drawRoundRect(rectFBack, radiusX, radiusY, mPaint);


        Rect rectFore = new Rect();
        rectFore.left = 0;
        GradientDrawable mDrawable;
        if (orientation == LinearLayout.VERTICAL) {
            //竖直方向

            rectFore.bottom = mHeight;
            rectFore.top = (int) (mHeight * (1 - progress / maxProgress));
            rectFore.right = mWidth;
            mDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{startColor, endColor});
        } else {
            //水平方向

            rectFore.bottom = mHeight;
            rectFore.top = 0;
            rectFore.right = (int) (mWidth * (progress / maxProgress));
            mDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{startColor, endColor});
        }
        //如果有设置渐变方向则按照设定的方向渐变，如果没有则跟进度条的方向保持一致
        if ("vertical".equalsIgnoreCase(gradientOrientation)){
            mDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{startColor, endColor});
        }else if ("horizontal".equalsIgnoreCase(gradientOrientation)){
            mDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{startColor, endColor});
        }

        mDrawable.setShape(GradientDrawable.RECTANGLE);//设置形状为矩形
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);//设置渐变方式为线性渐变
        mDrawable.setCornerRadii(new float[]{radiusX, radiusY, radiusX, radiusY, radiusX, radiusY, radiusX, radiusY});//设置4角的圆角半径值
        mDrawable.setBounds(rectFore);//设置位置大小
        mDrawable.draw(canvas);//绘制到canvas上
    }
}
