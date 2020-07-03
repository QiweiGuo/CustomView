package com.fy.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.fy.customview.R;

public class BitmapView extends View {

    private final String TAG = "BitmapView";

    private int[] defaultImgIds = new int[]{R.mipmap.widget_keyboard_play_2_02, R.mipmap.widget_keyboard_play_2_03,
            R.mipmap.widget_keyboard_play_2_04, R.mipmap.widget_keyboard_play_2_05, R.mipmap.widget_keyboard_play_2_06,
            /*R.mipmap.widget_keyboard_play_2_07, R.mipmap.widget_keyboard_play_2_08, R.mipmap.widget_keyboard_play_2_09,*/
            R.mipmap.widget_keyboard_play_1_1, R.mipmap.widget_keyboard_play_1_2,
            R.mipmap.widget_keyboard_play_1_3, R.mipmap.widget_keyboard_play_1_4, R.mipmap.widget_keyboard_play_1_5,
            R.mipmap.widget_keyboard_play_1_6};

    private Bitmap[] defaultBitmap = new Bitmap[defaultImgIds.length];

    private int bitmapIndex = -1;

    private Paint paint;

    private int width, height;

    public BitmapView(Context context) {
        this(context, null);
    }

    public BitmapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadRes();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure->widthMode:" + widthMode + ",measureWidth:" + measureWidth);
        Log.d(TAG, "onMeasure->heightMode:" + heightMode + ",measureHeight:" + measureHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        Log.d(TAG, "onSizeChanged->w:" + w + ",h:" + h + ",oldw:" + oldw + ",oldh:" + oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw->bitmapIndex:" + bitmapIndex + ",isLoadResSuccess:" + isLoadResSuccess);
        if (bitmapIndex >= 0 && bitmapIndex < defaultBitmap.length && isLoadResSuccess) {
            canvas.drawBitmap(defaultBitmap[bitmapIndex], 0, 0, paint);//第二个参数为裁剪图片的区域，不需要裁剪直接置空
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.d(TAG, "onWindowFocusChanged->hasWindowFocus:" + hasWindowFocus);
    }

    public void startAnimation() {
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "startAnimation->run->bitmapIndex:" + bitmapIndex);
//                if (bitmapIndex < defaultBitmap.length) {
//                    bitmapIndex++;
//                    invalidate();
//                    postDelayed(this, 50);
//                }else {
//                    bitmapIndex = 0;
//                }
//            }
//        }, 50);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < defaultBitmap.length; i++) {
                    bitmapIndex = i;
                    invalidate();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //资源加载是否完成
    private boolean isLoadResSuccess;

    private void loadRes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "loadRes->run->start time:" + System.currentTimeMillis());
                for (int i = 0; i < defaultImgIds.length; i++) {
                    defaultBitmap[i] = BitmapFactory.decodeResource(getResources(), defaultImgIds[i]);
                }
                isLoadResSuccess = true;
                Log.d(TAG, "loadRes->run->end time:" + System.currentTimeMillis());
            }
        }).start();
    }
}
