package com.fy.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.fy.customview.R;

import java.util.HashMap;
import java.util.Map;

public class KeyboardView extends View implements /*SurfaceHolder.Callback,Runnable,*/ View.OnTouchListener  {

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

    //白键的间隔
    private int whiteKeyDivider, whiteKeyDivider0;

    /*自定义属性*/
    //字体大小
    private int textSize;
    //字体颜色
    private int textColor;

    private Paint paint;

    private Thread drawThread;
    private SurfaceHolder holder;

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyboardView);
        textSize = typedArray.getDimensionPixelSize(R.styleable.KeyboardView_android_textSize, getResources().getDimensionPixelSize(R.dimen.sp_12));
        textColor = typedArray.getColor(R.styleable.KeyboardView_android_textColor, getResources().getColor(R.color.black));
        typedArray.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        initKeyModel();
        whiteKeyDivider0 = getResources().getDimensionPixelSize(R.dimen.dp_2);
        whiteKeyDivider = whiteKeyDivider0;
        setClickable(true);
//        holder = getHolder();
//        holder.addCallback(this);
//        drawThread = new Thread(this);

        setOnTouchListener(this);
    }

    private void initKeyModel() {
        for (int i = 0; i < musicalAlphabetArray.length; i++) {
            KeyModel keyModel = new KeyModel();
            keyModel.keyName = musicalAlphabetArray[i];
            if (keyBoardTypeArr[i].equals("0")) {
                keyModel.pressResId = R.mipmap.propiano_white_keyboard_btn_2;
                keyModel.freeResId = R.mipmap.propiano_white_keyboard_btn_1;
                keyModel.keyType = KeyType.White;
            } else {
                keyModel.freeResId = R.mipmap.propiano_black_keyboard_btn_1;
                keyModel.pressResId = R.mipmap.propiano_black_keyboard_btn_2;
                keyModel.keyType = KeyType.Black;
            }
            keyModel.rect = new Rect();
            keyModelMap.put(keyModel.keyName, keyModel);
        }
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
        calculateKeyCount();
        calculateKeySize();
        drawWhiteKey(canvas);
        drawBlackKey(canvas);
    }

    //显示的白键数量
    private int whiteKeyCount;
    //显示的第一个白键的索引
    private int startWhiteKeyIndex = 0;
    //显示的最后一个白键的索引
    private int endWhiteKeyIndex = 50;

    private Map<String, KeyModel> keyModelMap = new HashMap<>();

    private void drawWhiteKey(Canvas canvas) {
        //TODO 画白键
        int keyLeft = getPaddingStart();
        for (int i = startWhiteKeyIndex; i < endWhiteKeyIndex + 1; i++) {
            if (keyBoardTypeArr[i].equalsIgnoreCase("0")) {
                KeyModel keyModel = keyModelMap.get(musicalAlphabetArray[i]);
                int bitmapWidth = keyModel.keyWidth;//琴键图片宽度
                int bitmapHeight = keyModel.keyHeight;//琴键图片高度
                if (i > startWhiteKeyIndex) {
                    keyLeft = bitmapWidth + whiteKeyDivider + keyLeft;//琴键左边坐标
                }
                int keyTop = getPaddingTop();//琴键顶部坐标
                float textLeft = keyLeft + bitmapWidth * 0.2f;//键名左边坐标
                float textTop = bitmapHeight * 0.9f + getPaddingTop();//键名顶部坐标
                Bitmap bitmap;//琴键图片
                Log.d(TAG, "drawWhiteKey->isClick:" + keyModel.isClick);
                if (keyModel.isClick) {
                    bitmap = BitmapFactory.decodeResource(getResources(), keyModel.pressResId);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), keyModel.freeResId);
                }
                keyModel.rect.set(keyLeft, keyTop, keyLeft + bitmapWidth, keyTop + bitmapHeight);
                canvas.drawBitmap(bitmap, null, keyModel.rect, paint);//第二个参数为裁剪图片的区域，不需要裁剪直接置空

                paint.setTextSize(textSize);
                paint.setColor(textColor);
                canvas.drawText(keyModel.keyName, textLeft, textTop, paint);
            }
        }
    }

    private void drawBlackKey(Canvas canvas) {
        //TODO 画黑键
        for (int i = startWhiteKeyIndex; i < endWhiteKeyIndex; i++) {
            if (keyBoardTypeArr[i].equalsIgnoreCase("1")) {
                KeyModel keyModel = keyModelMap.get(musicalAlphabetArray[i]);
                int bitmapWidth = keyModel.keyWidth;//琴键图片宽度
                int bitmapHeight = keyModel.keyHeight;//琴键图片高度
                int offset = getResources().getDimensionPixelSize(R.dimen.dp_1);//黑键相对于白键位置的偏移量
                int keyLeft = keyModelMap.get(musicalAlphabetArray[i - 1]).rect.left + bitmapWidth / 2 - offset;//琴键左边坐标
                int keyTop = getPaddingTop();//琴键顶部坐标
                Bitmap bitmap;//琴键图片
                Log.d(TAG, "drawBlackKey->isClick:" + keyModel.isClick);
                if (keyModel.isClick) {
                    bitmap = BitmapFactory.decodeResource(getResources(), keyModel.pressResId);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), keyModel.freeResId);
                }
                keyModel.rect.set(keyLeft, keyTop, keyLeft + bitmapWidth, keyTop + bitmapHeight);
                canvas.drawBitmap(bitmap, null, keyModel.rect, paint);
            }
        }
    }


    private void calculateKeySize() {
        //TODO 计算每个键的大小
        int keyWidth = (width - getPaddingStart() - getPaddingEnd()) / whiteKeyCount - whiteKeyDivider;
        for (int i = 0; i < keyModelMap.size(); i++) {
            KeyModel keyModel = keyModelMap.get(musicalAlphabetArray[i]);
            keyModel.keyWidth = keyWidth;
            if (keyModel.keyType == KeyType.White) {
                keyModel.keyHeight = height - getPaddingTop() - getPaddingBottom();
            } else {
                keyModel.keyHeight = (int) ((height - getPaddingTop() - getPaddingBottom()) * 0.6f);
            }
        }
    }

    private void calculateKeyCount() {
        //TODO 计算显示多少个键
        Log.d(TAG, "calculateKeyCount->startWhiteKeyIndex:" + startWhiteKeyIndex + ",endWhiteKeyIndex:" + endWhiteKeyIndex);
        whiteKeyCount = 0;
        for (int i = startWhiteKeyIndex; i < endWhiteKeyIndex + 1; i++) {
            if (keyBoardTypeArr[i].equalsIgnoreCase("0")) {
                whiteKeyCount++;
            }
        }
        Log.d(TAG, "calculateKeyCount->whiteKeyCount:" + whiteKeyCount);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //TODO 点击弹奏时改变琴键
//        Log.d(TAG, "onTouchEvent->action:" + event.getAction() + " time:" + System.currentTimeMillis());
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            Log.d(TAG, "onTouchEvent->up time:" + System.currentTimeMillis());
//            for (int i = startWhiteKeyIndex; i < endWhiteKeyIndex + 1; i++) {
//                keyModelMap.get(musicalAlphabetArray[i]).isClick = false;
//            }
////            requestLayout();
//            invalidate();
//            performClick();
//            return true;
//        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            Log.d(TAG, "onTouchEvent->down time:" + System.currentTimeMillis());
//        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            Log.d(TAG, "onTouchEvent->move time:" + System.currentTimeMillis());
//        }
//
//        int eventX = (int) event.getX();//手指按下的位置相对于View的左边的距离
//        int eventY = (int) event.getY();//手指按下的位置相对于View的上边的距离
//        Log.d(TAG, "onTouchEvent->action:" + event.getAction() + ",eventX:" + eventX + ",eventY:" + eventY);
//        KeyModel pressKey = null;
//        //因黑键和白键的坐标有重叠的部分，所以先判断是否点击了黑键，没有点击黑键再判断白键，避免黑键被白键拦截了
//        boolean clickBlack = false;
//        for (int i = startWhiteKeyIndex; i < endWhiteKeyIndex; i++) {
//            KeyModel keyModel = keyModelMap.get(musicalAlphabetArray[i]);
//            keyModel.isClick = false;
//            if (keyModel.keyType == KeyType.Black) {
//                Log.d(TAG, "onTouchEvent->black key " + i + ".rect:" + keyModel.rect);
//                if (keyModel.rect.contains(eventX, eventY)) {
//                    if (keyModel.isClick) {
//                        //原本就点击了该键，不重复处理
//                        return true;
//                    }
//                    clickBlack = true;
//                    keyModel.isClick = true;
//                    pressKey = keyModel;
//                }
//            }
//        }
//        boolean clickWhite = false;
//        if (!clickBlack) {
//            for (int i = startWhiteKeyIndex; i < endWhiteKeyIndex; i++) {
//                KeyModel keyModel = keyModelMap.get(musicalAlphabetArray[i]);
//                keyModel.isClick = false;
//                if (keyModel.keyType == KeyType.White) {
//                    Log.d(TAG, "onTouchEvent->white key " + i + ".rect:" + keyModel.rect);
//                    if (keyModel.rect.contains(eventX, eventY)) {
//                        if (keyModel.isClick) {
//                            //原本就点击了该键，不重复处理
//                            return true;
//                        }
//                        clickWhite = true;
//                        keyModel.isClick = true;
//                        pressKey = keyModel;
//                    }
//                }
//            }
//        }
//        if (clickBlack || clickWhite) {
////            requestLayout();
//            invalidate();
//            if (onKeyboardPressListener != null) {
//                onKeyboardPressListener.onKeyPress(pressKey);
//            }
//            return true;
//        }
//        return true;
//    }

    public void setStartKeyIndex(int index) {
        setStartKey(musicalAlphabetArray[index]);
    }

    public void setStartKey(String keyName) {
        //TODO 设置显示的第一个键
        Log.d(TAG, "setStartKey->keyName:" + keyName);
        startWhiteKeyIndex = 0;
        for (int i = 0; i < musicalAlphabetArray.length; i++) {
            if (musicalAlphabetArray[i].equalsIgnoreCase(keyName)) {
                if (keyBoardTypeArr[i].equalsIgnoreCase("0")) {
                    startWhiteKeyIndex = i;
                    break;
                } else {
                    startWhiteKeyIndex = i - 1;
                    break;
                }
            }
        }
        Log.d(TAG, "setStartKey->startWhiteKeyIndex:" + startWhiteKeyIndex);
    }

    public void setEndKeyIndex(int index) {
        setEndKey(musicalAlphabetArray[index]);
    }

    public void setEndKey(String keyName) {
        //TODO 设置显示的最后一个键
        Log.d(TAG, "setEndKey->keyName:" + keyName);
        for (int i = 0; i < musicalAlphabetArray.length; i++) {
            if (musicalAlphabetArray[i].equalsIgnoreCase(keyName)) {
                if (keyBoardTypeArr[i].equalsIgnoreCase("0")) {
                    endWhiteKeyIndex = i;
                    break;
                } else {
                    endWhiteKeyIndex = i + 1;
                    break;
                }
            }
        }
        Log.d(TAG, "setEndKey->endWhiteKeyIndex:" + endWhiteKeyIndex);
    }

//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        drawThread.start();
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//    }
//
//    @Override
//    public void run() {
//        Canvas canvas = holder.lockCanvas();
//        canvas.drawColor(Color.BLACK);
//        calculateKeyCount();
//        calculateKeySize();
//        drawWhiteKey(canvas);
//        drawBlackKey(canvas);
//        holder.unlockCanvasAndPost(canvas);
//    }
//
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //TODO 点击弹奏时改变琴键
        if (event.getAction() == MotionEvent.ACTION_UP) {
            for (int i = startWhiteKeyIndex; i < endWhiteKeyIndex + 1; i++) {
                keyModelMap.get(musicalAlphabetArray[i]).isClick = false;
            }
//            requestLayout();
            invalidate();
            performClick();
            return true;
        }
        int eventX = (int) event.getX();//手指按下的位置相对于View的左边的距离
        int eventY = (int) event.getY();//手指按下的位置相对于View的上边的距离
        Log.d(TAG, "onTouch->action:" + event.getAction() + ",eventX:" + eventX + ",eventY:" + eventY);
        KeyModel pressKey = null;
        //因黑键和白键的坐标有重叠的部分，所以先判断是否点击了黑键，没有点击黑键再判断白键，避免黑键被白键拦截了
        boolean clickBlack = false;
        for (int i = startWhiteKeyIndex; i < endWhiteKeyIndex; i++) {
            KeyModel keyModel = keyModelMap.get(musicalAlphabetArray[i]);

            if (keyModel.keyType == KeyType.Black) {
                Log.d(TAG, "onTouch->black key " + i + ".rect:" + keyModel.rect);
                if (keyModel.rect.contains(eventX, eventY)) {
                    if (keyModel.isClick) {
                        //原本就点击了该键，不重复处理
                        return true;
                    }
                    clickBlack = true;
                    keyModel.isClick = true;
                    pressKey = keyModel;
                }else {
                    keyModel.isClick = false;
                }
            }
        }
        boolean clickWhite = false;
        if (!clickBlack) {
            for (int i = startWhiteKeyIndex; i < endWhiteKeyIndex; i++) {
                KeyModel keyModel = keyModelMap.get(musicalAlphabetArray[i]);

                if (keyModel.keyType == KeyType.White) {
                    Log.d(TAG, "onTouch->white key " + i + ".rect:" + keyModel.rect);
                    if (keyModel.rect.contains(eventX, eventY)) {
                        if (keyModel.isClick) {
                            //原本就点击了该键，不重复处理
                            return true;
                        }
                        clickWhite = true;
                        keyModel.isClick = true;
                        pressKey = keyModel;
                    }else {
                        keyModel.isClick = false;
                    }
                }
            }
        }
        if (clickBlack || clickWhite) {
//            requestLayout();
            invalidate();
            if (onKeyboardPressListener != null) {
                onKeyboardPressListener.onKeyPress(pressKey);
            }
            return true;
        }
        return true;
    }

    public static class KeyModel {
        private String keyName;
        private int keyWidth, keyHeight;
        private int pressResId, freeResId;
        private Rect rect;
        private boolean isClick;
        private KeyType keyType;

        public String getKeyName() {
            return keyName;
        }

        public Rect getRect() {
            return rect;
        }

        public KeyType getKeyType() {
            return keyType;
        }
    }

    public enum KeyType {
        White, Black
    }

    private OnKeyboardPressListener onKeyboardPressListener;

    public void setOnKeyboardPressListener(OnKeyboardPressListener onKeyboardPressListener) {
        this.onKeyboardPressListener = onKeyboardPressListener;
    }

    public interface OnKeyboardPressListener {
        void onKeyPress(KeyModel key);
    }
}
