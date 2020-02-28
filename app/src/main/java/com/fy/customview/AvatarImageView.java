package com.fy.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;

public class AvatarImageView extends AppCompatImageView {



    private final String TAG = "AvatarImageView";

    //画笔
    private Paint mPaint;
    //圆形图片的半径
    private int mRadius;
    //图片的宿放比例
    private float mScale;

    public AvatarImageView(Context context) {
        this(context, null);
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();

        Drawable drawable = getDrawable();
        Drawable backDrawable = getBackground();
        if (backDrawable instanceof BitmapDrawable) {
            backBitmap = ((BitmapDrawable) backDrawable).getBitmap();
            //初始化BitmapShader，传入bitmap对象
            bitmapShader = new BitmapShader(backBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            matrix = new Matrix();
        }
        if (null != drawable) {
            srcBitmap = ((BitmapDrawable) drawable).getBitmap();

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //由于是圆形，宽高应保持一致
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = size / 2;
        setMeasuredDimension(size, size);
    }

    private BitmapShader bitmapShader;
    private Matrix matrix;
    private Bitmap srcBitmap;
    private Bitmap backBitmap;

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != bitmapShader) {
            //计算缩放比例
            mScale = (mRadius * 2.0f) / Math.min(backBitmap.getHeight(), backBitmap.getWidth());

            matrix.setScale(mScale, mScale);
            bitmapShader.setLocalMatrix(matrix);
            mPaint.setShader(bitmapShader);
            //画圆形，指定好坐标，半径，画笔
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    private Bitmap createBitmap(){

        return null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        for (int i = 0; i < 20; i++) {
            int x = (int) (getLeft()+(getRight()-getLeft()) * (i / 20f));
            int y = getHeight()/2;
            int color = srcBitmap.getPixel(x, y);
            Log.d(TAG, "color " + i + " is:" + color);
        }
    }
}
