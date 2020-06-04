package com.fy.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.fy.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 绘制粗细渐变的线，不平滑，暂时弃用
 */
public class Line1 extends View {

    //参考：https://blog.csdn.net/u011102153/article/details/52679866
//    https://blog.csdn.net/itermeng/article/details/80291361

    private final String TAG = "Line1";

    public Line1(Context context) {
        this(context, null);
    }

    public Line1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Line1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Line1);
        count = typedArray.getInt(R.styleable.Line1_count, 10);
        scale = typedArray.getFloat(R.styleable.Line1_scale, 0.4f);
        typedArray.recycle();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        //-------------------------------------------------
        mFingerPath = new Path();
        pathSegments = new ArrayList<>();

        paths = new ArrayList<>();
    }

    private Paint paint;
    private Path mFingerPath;

    private List<PathSegment> pathSegments;

    private class PathSegment {
        Path path;
        float width;
        int alpha;

        PathSegment(Path path) {
            this.path = path;
        }

        Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = width;
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

    }

    private float length;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged->w:" + w + ",h:" + h);

    }

    private List<Path> paths;

    private int count;
    private float scale;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        length = getWidth();
        Path path1 = new Path();
        path1.moveTo(getRight(), getHeight() / 2f);
        path1.lineTo(length * scale, getHeight() / 2f);
//        path1.quadTo(getRight(), getHeight() / 2f,length * 0.4f, getHeight() / 2f);
        path1.close();
        paths.add(path1);
        for (int i = 0; i < count; i++) {
            Path path = new Path();
            float x0 = length * scale * (count - i) / count;
            float y0 = getHeight() / 2f;
            float x1 = length * scale * (count - i - 1) / count;
            float y1 = getHeight() / 2f;
            path.moveTo(x0, y0);
            path.lineTo(x1, y1);
//            path.quadTo(x0, y0, x1, y1);
            path.close();
            paths.add(path);
        }
//        for (PathSegment p : pathSegments) {
//            paint.setAlpha(p.getAlpha());
//            paint.setStrokeWidth(p.getWidth());
//            canvas.drawPath(p.getPath(), paint);
//        }

        for (int i = 0; i < paths.size(); i++) {
//            paint.reset();
//            paint.setColor(Color.RED);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setFlags(Paint.LINEAR_TEXT_FLAG);
            float width = getHeight() * (paths.size() - (float) i) / paths.size();
            Log.d(TAG, "onDraw->width:" + width);
            paint.setStrokeWidth(width);
//            paint.setAlpha(255);
            canvas.drawPath(paths.get(i), paint);
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                pathSegments.clear();
//                mFingerPath.reset();
//                mFingerPath.moveTo(x, y);
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                getPaths(mFingerPath);
//                mFingerPath.lineTo(x, y);
//                break;
//
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        invalidate();
//        return true;
//    }


    /**
     * 越小，线条锯齿度越小
     */
    private static final float DEFAULT_SEGMENT_LENGTH = 10F;
    private static final float DEFAULT_WIDTH = 3F;
    private static final float MAX_WIDTH = 45F;

    /**
     * 截取path
     *
     * @param path
     */
    private void getPaths(Path path) {
        PathMeasure pm = new PathMeasure(path, false);
        float length = pm.getLength();
        int segmentSize = (int) Math.ceil(length / DEFAULT_SEGMENT_LENGTH);
        Path ps = null;
        PathSegment pe = null;
        int nowSize = pathSegments.size();//集合中已经有的
        if (nowSize == 0) {
            ps = new Path();
            pm.getSegment(0, length, ps, true);
            pe = new PathSegment(ps);
            pe.setAlpha(255);
            pe.setWidth(DEFAULT_WIDTH);
            pathSegments.add(pe);
        } else {
            for (int i = nowSize; i < segmentSize; i++) {
                ps = new Path();
                pm.getSegment((i - 1) * DEFAULT_SEGMENT_LENGTH - 0.4f, Math.min(i * DEFAULT_SEGMENT_LENGTH, length), ps, true);
                pe = new PathSegment(ps);
                pe.setAlpha(255);
                pe.setWidth((float) Math.min(MAX_WIDTH, i * 0.3 + DEFAULT_WIDTH));
                pathSegments.add(pe);
            }
        }
    }

}
