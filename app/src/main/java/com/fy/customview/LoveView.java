package com.fy.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 心形图案
 */
public class LoveView extends View {

    private final String TAG = "LoveView";

    public LoveView(Context context) {
        this(context, null);
    }

    public LoveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoveView);
        int color = typedArray.getColor(R.styleable.LoveView_color, Color.RED);
        typedArray.recycle();
        paint = new Paint();
        path = new Path();
        paint.setColor(color);
//        GradientDrawable gradientDrawable = new GradientDrawable();
//        gradientDrawable.setColors(new int[]{});
//        gradientDrawable.d
//        ShapeDrawable shapeDrawable = new ShapeDrawable();
//        shapeDrawable.setShape();
//
//        paint.setShader(gradientDrawable);
        paint.setAntiAlias(true);
    }

    private Paint paint;
    private Path path;
    private float rate;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawHeart2(canvas);
        drawHeart1(canvas);
    }

    private void drawHeart1(Canvas canvas) {
        path.reset();
        int px = getWidth() / 2;
        int py = getHeight() / 2;
        rate = Math.min(px, py) / 20f;
        Log.d(TAG, "onDraw->px:" + px + ",py:" + py);
        // 路径的起始点
        path.moveTo(px, py - 5 * rate);
        LinearGradient linearGradient = new LinearGradient((float) px, (float) (py - 5 * rate),
                (float) px, (float) getHeight(), new int[]{0xFFFF6197, 0xFFFFA66A}, null, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        // 依据心形函数绘图
        for (double i = 0; i <= 2 * Math.PI; i += 0.001) {
            float x = (float) (16 * Math.sin(i) * Math.sin(i) * Math.sin(i));
            float y = (float) (13 * Math.cos(i) - 5 * Math.cos(2 * i) - 2 * Math.cos(3 * i) - Math.cos(4 * i));
            x *= rate;
            y *= rate;
            x = px - x;
            y = py - y;
            path.lineTo(x, y);
        }
        canvas.drawPath(path, paint);
    }

    private void drawHeart2(Canvas canvas) {
        path.moveTo((float) (12 * Math.sin(0)), (float) (12 * Math.cos(0)));
        for (int i = 0; i <= 60; i ++) {
            float x = (float) (0.01 * (-i * i + 40 * i + 1200) * Math.sin(Math.PI * i / 180));
            float y = (float) (0.01 * (-i * i + 40 * i + 1200) * Math.cos(Math.PI * i / 180));
            path.lineTo(x, y);
        }
        LinearGradient linearGradient = new LinearGradient((float) (0.01 * 1200 * Math.sin(0)),
                (float) (0.01 * 1200 * Math.cos(0)), (float) (0.01 * 1200 * Math.sin(0)),
                (float) (0.01 * (-60 * 60 + 40 * 60 + 1200) * Math.cos(Math.PI * 60 / 180)),
                new int[]{0xFFFF6197, 0xFFFFA66A}, null, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawPath(path, paint);
    }

}
