package com.fy.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class RoundedImageView extends AppCompatImageView {


    public RoundedImageView(Context context) {
        this(context, null);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPath = new Path();
        mPath.setFillType(Path.FillType.EVEN_ODD);
    }

    private Path mPath;
    private int mWidth,mHeight;

    @Override
    protected void onDraw(Canvas canvas) {
        if (mWidth != getWidth()){
            mWidth = getWidth();
            mHeight = getHeight();
//        if(mRoundMode != MODE_NONE){
            int saveCount = canvas.save();

//            checkPathChanged();

            mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight), 20f, 20f, Path.Direction.CW);
            canvas.clipPath(mPath);
            super.draw(canvas);

            canvas.restoreToCount(saveCount);
//        }else {
//            super.draw(canvas);
//        }
        }else {
            super.onDraw(canvas);
        }
    }
}
