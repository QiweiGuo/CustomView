package com.fy.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;

public class SubSurfaceView extends SurfaceView {

    private final String TAG = "SubSurfaceView";

    private Paint textPaint, linePaint;

    public SubSurfaceView(Context context) {
        this(context, null);
    }

    public SubSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        linePaint = new Paint();
        init(context);
    }

    private int mWidth, mHeight;
    private int defaultWidth, defaultHeight;
    private int SCREEN_WIDTH = 0;
    private int SCREEN_HEIGHT = 0;

    private static final int INVALID_POINTER = -1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;

    private int mScrollState = SCROLL_STATE_IDLE;
    private int mScrollPointerId = INVALID_POINTER;
    private VelocityTracker mVelocityTracker;
    private int mLastTouchY;
    private int mTouchSlop;
    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;
    private final ViewFlinger mViewFlinger = new ViewFlinger();

    //f(x) = (x-1)^5 + 1
    private static final Interpolator sQuinticInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        for (int i = 0; i < 20; i++) {
            int width = SCREEN_WIDTH;
            int height = SCREEN_HEIGHT / 2;
            int left = 0;
            int right = left + width;
            int bottom = top + height;

            //撑大边界
            if (bottom > mHeight) {
                mHeight = bottom;
            }
            if (right > mWidth) {
                mWidth = right;
            }

//            TextView textView = new TextView(getContext());
//            if (i % 2 == 0) {
//                textView.setBackgroundColor(Color.CYAN);
//            } else {
//                textView.setBackgroundColor(Color.GREEN);
//            }
//            textView.setText("item:" + i);
////            addView(textView);
//            textView.layout(left, top, right, bottom);
            top += height;
            top += 20;
        }
    }

    private void init(Context context) {
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        SCREEN_WIDTH = metric.widthPixels;
        SCREEN_HEIGHT = metric.heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        boolean eventAddedToVelocityTracker = false;
        final int action = event.getAction();
//        final int action = MotionEventCompat.getActionMasked(event);
        final int actionIndex = event.getActionIndex();
//        final int actionIndex = MotionEventCompat.getActionIndex(event);
        final MotionEvent vtev = MotionEvent.obtain(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                setScrollState(SCROLL_STATE_IDLE);
                mScrollPointerId = event.getPointerId(0);
                mLastTouchY = (int) (event.getY() + 0.5f);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                mScrollPointerId = event.getPointerId(actionIndex);
                mLastTouchY = (int) (event.getY(actionIndex) + 0.5f);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int index = event.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    Log.e("zhufeng", "Error processing scroll; pointer index for id " + mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }

                final int y = (int) (event.getY(index) + 0.5f);
                int dy = mLastTouchY - y;

                if (mScrollState != SCROLL_STATE_DRAGGING) {
                    boolean startScroll = false;

                    if (Math.abs(dy) > mTouchSlop) {
                        if (dy > 0) {
                            dy -= mTouchSlop;
                        } else {
                            dy += mTouchSlop;
                        }
                        startScroll = true;
                    }
                    if (startScroll) {
                        setScrollState(SCROLL_STATE_DRAGGING);
                    }
                }

                if (mScrollState == SCROLL_STATE_DRAGGING) {
                    mLastTouchY = y;
                    constrainScrollBy(0, dy);
                }

                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                if (event.getPointerId(actionIndex) == mScrollPointerId) {
                    // Pick a new pointer to pick up the slack.
                    final int newIndex = actionIndex == 0 ? 1 : 0;
                    mScrollPointerId = event.getPointerId(newIndex);
                    mLastTouchY = (int) (event.getY(newIndex) + 0.5f);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                mVelocityTracker.addMovement(vtev);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                float yVelocity = -VelocityTrackerCompat.getYVelocity(mVelocityTracker, mScrollPointerId);
                Log.i("zhufeng", "速度取值：" + yVelocity);
                if (Math.abs(yVelocity) < mMinFlingVelocity) {
                    yVelocity = 0F;
                } else {
                    yVelocity = Math.max(-mMaxFlingVelocity, Math.min(yVelocity, mMaxFlingVelocity));
                }
                if (yVelocity != 0) {
                    mViewFlinger.fling((int) yVelocity);
                } else {
                    setScrollState(SCROLL_STATE_IDLE);
                }
                resetTouch();
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                resetTouch();
                break;
            }
        }
        if (!eventAddedToVelocityTracker) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;

    }

    private void resetTouch() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
    }

    private void setScrollState(int state) {
        if (state == mScrollState) {
            return;
        }
        mScrollState = state;
        if (state != SCROLL_STATE_SETTLING) {
            mViewFlinger.stop();
        }
    }

    private class ViewFlinger implements Runnable {

        private int mLastFlingY = 0;
        private OverScroller mScroller;
        private boolean mEatRunOnAnimationRequest = false;
        private boolean mReSchedulePostAnimationCallback = false;

        public ViewFlinger() {
            mScroller = new OverScroller(getContext(), sQuinticInterpolator);
        }

        @Override
        public void run() {
            disableRunOnAnimationRequests();
            final OverScroller scroller = mScroller;
            if (scroller.computeScrollOffset()) {
                final int y = scroller.getCurrY();
                int dy = y - mLastFlingY;
                mLastFlingY = y;
                constrainScrollBy(0, dy);
                postOnAnimation();
            }
            enableRunOnAnimationRequests();
        }

        public void fling(int velocityY) {
            mLastFlingY = 0;
            setScrollState(SCROLL_STATE_SETTLING);
            mScroller.fling(0, 0, 0, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        public void stop() {
            removeCallbacks(this);
            mScroller.abortAnimation();
        }

        private void disableRunOnAnimationRequests() {
            mReSchedulePostAnimationCallback = false;
            mEatRunOnAnimationRequest = true;
        }

        private void enableRunOnAnimationRequests() {
            mEatRunOnAnimationRequest = false;
            if (mReSchedulePostAnimationCallback) {
                postOnAnimation();
            }
        }

        void postOnAnimation() {
            if (mEatRunOnAnimationRequest) {
                mReSchedulePostAnimationCallback = true;
            } else {
                removeCallbacks(this);
                ViewCompat.postOnAnimation(SubSurfaceView.this, this);
            }
        }
    }

    private void constrainScrollBy(int dx, int dy) {
        Rect viewport = new Rect();
        getGlobalVisibleRect(viewport);
        int height = viewport.height();
        int width = viewport.width();

        int scrollX = getScrollX();
        int scrollY = getScrollY();

        //右边界
        if (mWidth - scrollX - dx < width) {
            dx = mWidth - scrollX - width;
        }
        //左边界
        if (-scrollX - dx > 0) {
            dx = -scrollX;
        }
        //下边界
        if (mHeight - scrollY - dy < height) {
            dy = mHeight - scrollY - height;
        }
        //上边界
        if (scrollY + dy < 0) {
            dy = -scrollY;
        }
        scrollBy(dx, dy);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.d(TAG, "onWindowFocusChanged->mWidth:" + getWidth() + ",mHeight:" + getHeight() + ",x:" + getX() + ",y:" + getY());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量控件宽高
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMeasureMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                mWidth = defaultWidth;
//                mWidth.set(defaultWidth);
                break;
            case MeasureSpec.EXACTLY:
//                mWidth.set(measureWidth - getPaddingEnd() - getPaddingStart());
                mWidth = measureWidth - getPaddingEnd() - getPaddingStart();
                break;
        }
        switch (heightMeasureMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
//                mHeight.set(defaultHeight);
                mHeight = defaultHeight;
                break;
            case MeasureSpec.EXACTLY:
//                mHeight.set(measureHeight - getPaddingBottom() - getPaddingTop());
                mHeight = measureHeight - getPaddingBottom() - getPaddingTop();
                break;
        }
        Log.d(TAG, "onMeasure->mWidth:" + mWidth + ",mHeight:" + mHeight + ",x:" + getX() + ",y:" + getY());
//        Log.d(TAG, "onMeasure->mWidth:" + mWidth + ",mHeight:" + mHeight);//这里打印了三次，进行了三次测量，看来是跟布局的层次有关？
//        setMeasuredDimension(mWidth.get(), mHeight.get());
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "--onDraw--");
        textPaint.setTextSize(20);
        textPaint.setStrokeWidth(10);
        linePaint.setStrokeWidth(5);
        canvas.drawLine(10, 10, 200, 10, linePaint);
        canvas.drawLine(10, 30, 200, 30, linePaint);
        canvas.drawText("0123456789AaBb啊一二", 10, 30, textPaint);
    }
}
