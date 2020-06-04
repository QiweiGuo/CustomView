package com.fy.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.fy.customview.R;

public class KeyboardView extends View {

    private final String TAG = "KeyboardView";

    //0白键盘  1黑键
    private String[] keyBoardTypeArr = new String[]{"0", "1", "0",
            "0", "1", "0", "1", "0", "0", "1", "0", "1", "0", "1", "0",
            "0", "1", "0", "1", "0", "0", "1", "0", "1", "0", "1", "0",
            "0", "1", "0", "1", "0", "0", "1", "0", "1", "0", "1", "0",
            "0", "1", "0", "1", "0", "0", "1", "0", "1", "0", "1", "0",
            "0", "1", "0", "1", "0", "0", "1", "0", "1", "0", "1", "0",
            "0", "1", "0", "1", "0", "0", "1", "0", "1", "0", "1", "0",
            "0", "1", "0", "1", "0", "0", "1", "0", "1", "0", "1", "0",
            "0"};

    //音名
    private String[] musicalAlphabetArray = new String[]{"A0", "Bb0", "B0",
            "C1", "C#1", "D1", "Eb1", "E1", "F1", "F#1", "G1", "Ab1", "A1", "Bb1", "B1",
            "C2", "C#2", "D2", "Eb2", "E2", "F2", "F#2", "G2", "Ab2", "A2", "Bb2", "B2",
            "C3", "C#3", "D3", "Eb3", "E3", "F3", "F#3", "G3", "Ab3", "A3", "Bb3", "B3",
            "C4", "C#4", "D4", "Eb4", "E4", "F4", "F#4", "G4", "Ab4", "A4", "Bb4", "B4",
            "C5", "C#5", "D5", "Eb5", "E5", "F5", "F#5", "G5", "Ab5", "A5", "Bb5", "B5",
            "C6", "C#6", "D6", "Eb6", "E6", "F6", "F#6", "G6", "Ab6", "A6", "Bb6", "B6",
            "C7", "C#7", "D7", "Eb7", "E7", "F7", "F#7", "G7", "Ab7", "A7", "Bb7", "B7",
            "C8"};

    //黑键
    private int[] blackKeyIds = new int[]{R.id.v_widget_keyboard_black_key_1, R.id.v_widget_keyboard_black_key_2, R.id.v_widget_keyboard_black_key_3,
            R.id.v_widget_keyboard_black_key_4, R.id.v_widget_keyboard_black_key_5, R.id.v_widget_keyboard_black_key_6,
            R.id.v_widget_keyboard_black_key_7, R.id.v_widget_keyboard_black_key_8, R.id.v_widget_keyboard_black_key_9,
            R.id.v_widget_keyboard_black_key_10, R.id.v_widget_keyboard_black_key_11, R.id.v_widget_keyboard_black_key_12,
            R.id.v_widget_keyboard_black_key_13, R.id.v_widget_keyboard_black_key_14, R.id.v_widget_keyboard_black_key_15,
            R.id.v_widget_keyboard_black_key_16, R.id.v_widget_keyboard_black_key_17, R.id.v_widget_keyboard_black_key_18,
            R.id.v_widget_keyboard_black_key_19, R.id.v_widget_keyboard_black_key_20, R.id.v_widget_keyboard_black_key_21,
            R.id.v_widget_keyboard_black_key_22, R.id.v_widget_keyboard_black_key_23, R.id.v_widget_keyboard_black_key_24,
            R.id.v_widget_keyboard_black_key_25, R.id.v_widget_keyboard_black_key_26, R.id.v_widget_keyboard_black_key_27,
            R.id.v_widget_keyboard_black_key_28, R.id.v_widget_keyboard_black_key_29, R.id.v_widget_keyboard_black_key_30,
            R.id.v_widget_keyboard_black_key_31, R.id.v_widget_keyboard_black_key_35, R.id.v_widget_keyboard_black_key_33,
            R.id.v_widget_keyboard_black_key_34, R.id.v_widget_keyboard_black_key_35, R.id.v_widget_keyboard_black_key_36};
    private View[] blackKeys = new View[blackKeyIds.length];
    //黑键的宽高
    private int blackKeyWidth, blackKeyHeight;
    //黑键的坐标
    private RectF[] blackKeyRectFs = new RectF[blackKeyIds.length];

    //白键
    private int[] whiteKeyIds = new int[]{R.id.tv_widget_keyboard_white_key_1, R.id.tv_widget_keyboard_white_key_2,
            R.id.tv_widget_keyboard_white_key_3, R.id.tv_widget_keyboard_white_key_4,
            R.id.tv_widget_keyboard_white_key_5, R.id.tv_widget_keyboard_white_key_6, R.id.tv_widget_keyboard_white_key_7,
            R.id.tv_widget_keyboard_white_key_8,
            R.id.tv_widget_keyboard_white_key_9, R.id.tv_widget_keyboard_white_key_10, R.id.tv_widget_keyboard_white_key_11,
            R.id.tv_widget_keyboard_white_key_12,
            R.id.tv_widget_keyboard_white_key_13, R.id.tv_widget_keyboard_white_key_14, R.id.tv_widget_keyboard_white_key_15,
            R.id.tv_widget_keyboard_white_key_16,
            R.id.tv_widget_keyboard_white_key_17, R.id.tv_widget_keyboard_white_key_18, R.id.tv_widget_keyboard_white_key_19,
            R.id.tv_widget_keyboard_white_key_20,
            R.id.tv_widget_keyboard_white_key_21, R.id.tv_widget_keyboard_white_key_22, R.id.tv_widget_keyboard_white_key_23,
            R.id.tv_widget_keyboard_white_key_24,
            R.id.tv_widget_keyboard_white_key_25, R.id.tv_widget_keyboard_white_key_26, R.id.tv_widget_keyboard_white_key_27,
            R.id.tv_widget_keyboard_white_key_28,
            R.id.tv_widget_keyboard_white_key_29, R.id.tv_widget_keyboard_white_key_30, R.id.tv_widget_keyboard_white_key_31,
            R.id.tv_widget_keyboard_white_key_32,
            R.id.tv_widget_keyboard_white_key_33, R.id.tv_widget_keyboard_white_key_34, R.id.tv_widget_keyboard_white_key_35,
            R.id.tv_widget_keyboard_white_key_36,
            R.id.tv_widget_keyboard_white_key_37, R.id.tv_widget_keyboard_white_key_38, R.id.tv_widget_keyboard_white_key_39,
            R.id.tv_widget_keyboard_white_key_40,
            R.id.tv_widget_keyboard_white_key_41, R.id.tv_widget_keyboard_white_key_42, R.id.tv_widget_keyboard_white_key_43,
            R.id.tv_widget_keyboard_white_key_44,
            R.id.tv_widget_keyboard_white_key_45, R.id.tv_widget_keyboard_white_key_46, R.id.tv_widget_keyboard_white_key_47,
            R.id.tv_widget_keyboard_white_key_48,
            R.id.tv_widget_keyboard_white_key_49, R.id.tv_widget_keyboard_white_key_50, R.id.tv_widget_keyboard_white_key_51,
            R.id.tv_widget_keyboard_white_key_52,};
    private TextView[] whiteKeys = new TextView[whiteKeyIds.length];
    //白键的坐标
    private RectF[] whiteKeyRectFs = new RectF[whiteKeyIds.length];
    //白键的宽高
    private int whiteKeyWidth, whiteKeyHeight;
    //白键的间隔
    private int whiteKeyDivider, whiteKeyDivider0;

    private int textSize;

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.KeyboardView);
        textSize = typedArray.getDimensionPixelSize(R.styleable.KeyboardView_android_textSize,getResources().getDimensionPixelSize(R.dimen.sp_12));
        typedArray.recycle();

    }

    //键盘的宽高
    private int width, height;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG, "onMeasure->widthMode:" + widthMode + ",widthMeasure:" + widthMeasure);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                //指定了宽度，view的宽度被设定为match或者固定值，将指定的宽度保存起来
                width = widthMeasure;
                break;
            //TODO 自适应宽高待设定
            case MeasureSpec.AT_MOST:
                //包裹内容，view的宽度被设定为wrap，这时候我们需要一个默认值
            case MeasureSpec.UNSPECIFIED:
                //未定义，一般是像ListView的item这种会出现，此时我们跟AT_MOST一样使用默认值处理
                break;
        }
        Log.d(TAG, "onMeasure->heightMode:" + heightMode + ",heightMeasure:" + heightMeasure);
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                //指定了宽度，view的宽度被设定为match或者固定值，将指定的宽度保存起来
                height = heightMeasure;
                break;
            case MeasureSpec.AT_MOST:
                //包裹内容，view的宽度被设定为wrap，这时候我们需要一个默认值
            case MeasureSpec.UNSPECIFIED:
                //未定义，一般是像ListView的item这种会出现，此时我们跟AT_MOST一样使用默认值处理
                break;
        }
        Log.d(TAG, "onMeasure->width:" + width + ",height:" + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    private void drawWhiteKey(Canvas canvas) {
        //TODO 画白键
    }

    private void drawBlackKey(Canvas canvas) {
        //TODO 画黑键
    }

    private void calculateKeySize() {
        //TODO 计算每个键的大小
    }

    private void calculateKeyCount() {
        //TODO 计算显示多少个键
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //TODO 点击弹奏时改变琴键
        return super.onTouchEvent(event);
    }

    public void setStartKey(String keyName) {
        //TODO 设置显示的第一个键
    }

    public void setEndKey(String keyName) {
        //TODO 设置显示的最后一个键
    }

    private class KeyModel {
        private String keyName;
        private int keyWidth, keyHeight;
        private int pressResId, freeResId;
        private RectF rectF;
    }

}
