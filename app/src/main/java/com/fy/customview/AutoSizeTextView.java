package com.fy.customview;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

public class AutoSizeTextView extends AppCompatTextView {
    private Paint mTestPaint;

    public AutoSizeTextView(Context context) {
        super(context);
        initialise();
    }

    public AutoSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        //max size defaults to the initially specified text size unless it is too small
    }

    /* Re size the font so the specified text fits in the text box
     * assuming the text box is the specified width.
     */
    private void refitText(String text, int textWidth) {
        if (textWidth <= 0)
            return;
        int targetWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
        float hi = 100;
        float lo = 2;
        //  相当于精度
        final float threshold = 0.5f;

        mTestPaint.set(this.getPaint());
        while ((hi - lo) > threshold) {
            //  二分法计算最接近精度的值
            float size = (hi + lo) / 2;
            mTestPaint.setTextSize(size);
            // 利用Paint内的measureText方法计算出text宽度
            if (mTestPaint.measureText(text) >= targetWidth ) {
                hi = size; // too big
            } else if (mTestPaint.measureText(text) < targetWidth) {
                lo = size; // too small
            }
        }
        //  用小值避免大那么一丢丢导致悲剧
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, lo);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = getMeasuredHeight();
        refitText(this.getText().toString(), parentWidth);
        this.setMeasuredDimension(parentWidth, height);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        refitText(getText().toString(),getWidth());
    }
}
