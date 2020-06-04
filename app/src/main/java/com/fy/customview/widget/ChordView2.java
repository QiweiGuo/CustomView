package com.fy.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fy.customview.R;


public class ChordView2 extends ConstraintLayout {

    //FIXME 这个控件可能布局太复杂了，导致加载含有该控件的布局时耗时增加，看下怎么优化

    private final String TAG = "ChordSimpleView";


    //存放圆点控件id的二维数组，横坐标为弦，从左往右分别为第4到第1弦；纵坐标为品，从上到下分别为第1到第4品
    private int[][] tv_circularId = new int[][]{{R.id.tv_circular_6_1, R.id.tv_circular_5_1, R.id.tv_circular_4_1, R.id.tv_circular_3_1, R.id.tv_circular_2_1, R.id.tv_circular_1_1},
            {R.id.tv_circular_6_2, R.id.tv_circular_5_2, R.id.tv_circular_4_2, R.id.tv_circular_3_2, R.id.tv_circular_2_2, R.id.tv_circular_1_2},
            {R.id.tv_circular_6_3, R.id.tv_circular_5_3, R.id.tv_circular_4_3, R.id.tv_circular_3_3, R.id.tv_circular_2_3, R.id.tv_circular_1_3},
            {R.id.tv_circular_6_4, R.id.tv_circular_5_4, R.id.tv_circular_4_4, R.id.tv_circular_3_4, R.id.tv_circular_2_4, R.id.tv_circular_1_4}};
    //存放圆点控件的二维数组
    private TextView[][] tv_circulars = new TextView[tv_circularId.length][tv_circularId[0].length];
    //和弦小圆点所在的行和列
    private int row, column;
    //直径
    private int diameter;

    //横线
    private int[] rowIds = new int[]{R.id.v_chord_view_2_row_1, R.id.v_chord_view_2_row_2, R.id.v_chord_view_2_row_3, R.id.v_chord_view_2_row_4, R.id.v_chord_view_2_row_5};
    private View[] rows = new View[rowIds.length];

    //竖线
    private int[] columnIds = new int[]{R.id.chord_2_column_end, R.id.chord_2_column_2, R.id.chord_2_column_3, R.id.chord_2_column_4};
    private View[] columns = new View[columnIds.length];


    //和弦上面的ooxx
    private int[] textIds = new int[]{R.id.tv_chord_view_text_2_6, R.id.tv_chord_view_text_2_5, R.id.tv_chord_view_text_2_4,
            R.id.tv_chord_view_text_2_3, R.id.tv_chord_view_text_2_2, R.id.tv_chord_view_text_2_1};
    private TextView[] textViews = new TextView[textIds.length];
    //左边的数字
    private TextView tv_chord_view_2_text;
    //背景1
    private View v_chord_view_2_bg_1;
    //背景1颜色
    private int background1Color;

    public ChordView2(Context context) {
        this(context, null);
    }

    public ChordView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChordView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        float x0 = columns[0].getX();
        float x1 = columns[1].getX();
        diameter = (int) (x0 - x1);
        for (TextView[] tv_circular : tv_circulars) {
            for (TextView textView : tv_circular) {
                ViewGroup.LayoutParams params = textView.getLayoutParams();
                params.height = diameter;
                params.width = diameter;
                textView.setLayoutParams(params);
                autoFixTextView(textView);
            }
        }
        for (TextView textView : textViews) {
            ViewGroup.LayoutParams params = textView.getLayoutParams();
            params.height = diameter;
            params.width = diameter;
            textView.setLayoutParams(params);
            autoFixTextView(textView);
        }
    }

    /**
     * 按照TextView宽度对字体大小进行缩放
     *
     * @param textView 需要缩放的TextView
     */
    private void autoFixTextView(TextView textView) {
        //  缩放后文字所占的宽度
        int targetWidth = diameter / 3;

        if (targetWidth <= 0) {
            return;
        }

        // 这里主要是要用TextPaint里的getTextSize()，和measureText()方法，模拟不同textSize下textView的情况
        TextPaint textPaint = new TextPaint(textView.getPaint());
        //最大值
        float hi = textPaint.getTextSize();
        //最小值
        float lo = 2;
        //  设置精度
        final float threshold = 0.5f;
        //  二分法计算相对精度下合适的textSize 用以减少循环次数
        while ((hi - lo) > threshold) {
            float trySize = (hi + lo) / 2;
            textPaint.setTextSize(trySize);
            // 利用Paint内的measureText方法计算出text宽度
            if (textPaint.measureText(textView.getText().toString()) >= targetWidth) {
                hi = trySize; // too big
            } else if (textPaint.measureText(textView.getText().toString()) < targetWidth) {
                lo = trySize; // too small
            }
        }
        //  用相对小的值避免大那么一丢丢导致悲剧
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, lo);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_chord_view_2, this, true);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ChordView);
        if (attributes != null) {
            column = attributes.getInt(R.styleable.ChordView2_column, 0);
            row = attributes.getInt(R.styleable.ChordView2_row, 0);
            background1Color = attributes.getColor(R.styleable.ChordView2_background1Color, -1);
            attributes.recycle();
        }
        initView();

    }


    /**
     * 设置和弦
     *
     * @param row    横向位置，取值从0到5，分别是第6到第1弦
     * @param column 纵向位置，取值从0到4，分别是第1到第5品
     */
    public void setChord(int row, int column) {
        //和弦位置的圆点显示，其他的圆点隐藏
        this.row = row;
        this.column = column;
        for (int i = 0; i < tv_circulars.length; i++) {
            for (int j = 0; j < tv_circulars[i].length; j++) {
                tv_circulars[i][j].setVisibility(INVISIBLE);
            }
            textViews[i].setVisibility(INVISIBLE);
        }
        textViews[row].setVisibility(VISIBLE);
        tv_circulars[row][column].setVisibility(VISIBLE);
    }


    public void addChord(int row, int column) {
        tv_circulars[row][column].setVisibility(VISIBLE);
        textViews[row].setVisibility(VISIBLE);
    }

    /**
     * 修改整个View的颜色
     *
     * @param colorRes 修改的颜色资源id
     */
    public void setColor(int colorRes) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                ((GradientDrawable) tv_circulars[i][j].getBackground()).setColor(colorRes);
            }
        }
        for (int i = 0; i < rowIds.length; i++) {
            ((GradientDrawable) rows[i].getBackground()).setColor(colorRes);
        }
        for (int i = 0; i < columnIds.length; i++) {
            ((GradientDrawable) columns[i].getBackground()).setColor(colorRes);
        }
    }


    protected void initView() {
        for (int i = 0; i < tv_circulars.length; i++) {
            for (int j = 0; j < tv_circulars[i].length; j++) {
                //绑定圆点
                tv_circulars[i][j] = findViewById(tv_circularId[i][j]);
            }
        }
        for (int i = 0; i < rowIds.length; i++) {
            rows[i] = findViewById(rowIds[i]);
        }
        for (int i = 0; i < columnIds.length; i++) {
            columns[i] = findViewById(columnIds[i]);
        }
        for (int i = 0; i < textViews.length; i++) {
            textViews[i] = findViewById(textIds[i]);
        }
        v_chord_view_2_bg_1 = findViewById(R.id.v_chord_view_2_bg_1);
        setBackground1Color(background1Color);
        setChord(row, column);
    }


    public void setBackground1Color(int colorRes) {
        background1Color = colorRes;
        if (background1Color != -1) {
            v_chord_view_2_bg_1.setBackgroundColor(getResources().getColor(background1Color));
        }
    }
}
