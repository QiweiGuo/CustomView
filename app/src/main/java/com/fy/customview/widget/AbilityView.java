package com.fy.customview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.fy.customview.R;

import java.util.ArrayList;

/**
 * 能力多边形，可以根据能力有多少种类绘出对应的多边形
 */
public class AbilityView extends View {


    private final String TAG = "AbilityView";

    private Paint linePaint;//画线的笔
    private Paint textPaint;//画文字的笔
    private AbilityData[] allAbility;

    private int side = 6;    //边的数量或者能力的个数，默认6条边
    private int level;//间隔数量，把半径分为几段 TODO 可定义成属性
    private float radius; //最外圈的半径
    private float angle; //角度

    /*不同层次的颜色*/
    private int[] levelColor;

    /*能力块的颜色，16进制数*/
    private int abilityColor;

    /*边框的颜色，16进制数*/
    private int sideColor;

    private int viewHeight;//控件的高度
    private int viewWidth;//控件的宽度

    private float textSize;//文本字体大小，单位px

    private ArrayList<ArrayList<PointF>> pointArrayList;//存储多边形顶点数组的数组
    private ArrayList<PointF> abilityPoints;//储存能力点的数组

    public AbilityView(Context context) {
        this(context, null);
    }

    public AbilityView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbilityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.fy.customview.R.styleable.AbilityView);
        textSize = typedArray.getDimension(com.fy.customview.R.styleable.AbilityView_android_textSize, 0);
        side = typedArray.getInt(R.styleable.AbilityView_side, 6);
        level = typedArray.getInt(R.styleable.AbilityView_level, 3);
        abilityColor = typedArray.getColor(R.styleable.AbilityView_abilityColor, 0xFF51B0FB);
        sideColor = typedArray.getColor(R.styleable.AbilityView_sideColor, 0xFF3B9FF5);
        levelColor = new int[level];
        typedArray.recycle();

        initPoints();

        initPaint();
    }

    /**
     * 设置不同等级的背景块颜色
     *
     * @param level 设置颜色的级别，不能超过已设置的级别数
     * @param color 设置的颜色id
     */
    public void setLevelColor(int level, int color) {
        if (levelColor != null && level < levelColor.length) {
            levelColor[level] = color;
            invalidate();
        }
    }

    /**
     * 初始化点的位置
     */
    private void initPoints() {
        if (pointArrayList == null) {
            pointArrayList = new ArrayList<>();
        } else {
            pointArrayList.clear();
        }
        float x;
        float y;
        for (int i = 0; i < level; i++) {
            //创建一个存储点的数组
            ArrayList<PointF> points = new ArrayList<>();
            for (int j = 0; j < side; j++) {
                float r = radius * ((float) (level - i) / level);//每一圈的半径按比例减少
                //这里减去Math.PI /2 是为了让段变形逆时针旋转90度，所以后面的所有用到cos,sin的都要减
                x = (float) (r * Math.cos(j * angle - Math.PI / 2));
                y = (float) (r * Math.sin(j * angle - Math.PI / 2));
                points.add(new PointF(x, y));
            }
            pointArrayList.add(points);
        }
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //画线的笔
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //设置线笔的宽度
        linePaint.setStrokeWidth(dp2px(getContext(), 1f));

        //画文字的笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);//设置文字居中
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);

    }

    /**
     * 初始化固定数据Size
     * 设置了是几边型；
     * 设置了最外圈的半径
     * 计算出每一边型的角度
     * 获取屏幕方向
     */
    private void initSize() {
        if (allAbility != null) {
            side = allAbility.length;
        }
        //FIXME 半径由控件宽高减去文字宽高之后取最小值。暂时不知道怎么获取文字的宽高，先假定文字高15dp，宽30dp
        radius = Math.min(viewHeight / 2f - dp2px(getContext(), 15), viewWidth / 2f - dp2px(getContext(), 30));
        Log.d(TAG, "initSize->radius:" + radius);
//        level = 4; //有四层
        angle = (float) ((2 * Math.PI) / side); //2π是一周，除以n是算出平均每一个的角度是多少

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        viewHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        initSize();
//        //设置控件的最终视图大小
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSize();
        initPoints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        radius = Math.min(viewHeight/2,viewWidth/2);
        Log.d(TAG, "onDraw->radius:" + radius);
        //把画布的原点移动到控件的中心点
        canvas.translate(viewWidth / 2f, viewHeight / 2f);

        //绘制形状
        drawPolygon(canvas);

        //画出边框线
        drawOutLine(canvas);

        //画出能力线
        drawAbilityLine(canvas);

        //画出文字
        drawAbilityText(canvas);

    }

    /**
     * 在画布上画出能力文本
     *
     * @param canvas 画布
     */
    private void drawAbilityText(Canvas canvas) {
        canvas.save();

        ArrayList<PointF> textPoints = new ArrayList<>();
        for (int i = 0; i < side; i++) {
            float r = radius + textSize;
            float x = (float) (r * Math.cos(i * angle - Math.PI / 2));
            if (x < r){
                //如果文本在对称轴的左边就往左偏移半个字符的距离
                x = x-textSize/2f;
            }else if(x > r){
                //如果文本在对称轴的右边就往右偏移半个字符的距离
                x = x+textSize/2f;
            }
            float y = (float) (r * Math.sin(i * angle - Math.PI / 2));
            textPoints.add(new PointF(x, y));
        }
        //拿到字体测量器
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        for (int i = 0; i < side; i++) {
            if (allAbility != null && allAbility[i].abilityName != null) {
//            if (allAbility != null){
                float x = textPoints.get(i).x;
                //ascent:上坡度，是文字的基线到文字的最高处的距离
                //descent:下坡度,，文字的基线到文字的最低处的距离
                float y = textPoints.get(i).y - (metrics.ascent + metrics.descent) / 2;
                canvas.drawText(allAbility[i].abilityName, x, y, textPaint);
            }

        }
        canvas.restore();
    }

    /**
     * 在画布上画出能力线
     *
     * @param canvas 画布
     */
    private void drawAbilityLine(Canvas canvas) {

        if (allAbility == null) {
            return;
        }

        canvas.save();

        //先把能力点初始化出来
        abilityPoints = new ArrayList<>();
        for (int i = 0; i < side; i++) {
            float r = radius * (allAbility[i].abilityValue / 100.0f);  //能力值/100再乘以半径就是所占的比例
            float x = (float) (r * Math.cos(i * angle - Math.PI / 2));
            float y = (float) (r * Math.sin(i * angle - Math.PI / 2));
            abilityPoints.add(new PointF(x, y));
        }
        //路径
        Path path = new Path();
        for (int i = 0; i < side; i++) {
            float x = abilityPoints.get(i).x;
            float y = abilityPoints.get(i).y;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
                //先画交点
                linePaint.reset();
                linePaint.setStyle(Paint.Style.FILL);
                linePaint.setColor(Color.BLUE);
                canvas.drawCircle(x, y, 3f, linePaint);
            }
        }
        path.close();   //别忘了闭合
        //先画实心的能力颜色
        linePaint.reset();
        linePaint.setColor(abilityColor);
        linePaint.setStyle(Paint.Style.FILL);  //设置实心的
        linePaint.setAlpha(0x80);//设置透明度，传入16进制数，0x80为50%
        canvas.drawPath(path, linePaint);
        //再画能力边框颜色
        linePaint.setStrokeWidth(dp2px(getContext(), 1));//设置边框宽度
        linePaint.setColor(sideColor);//设置边框颜色
        linePaint.setStyle(Paint.Style.STROKE);//边框必须空心，否则会覆盖掉上面的颜色
        canvas.drawPath(path, linePaint);
        for (int i = 0; i < side; i++) {
            float x = abilityPoints.get(i).x;
            float y = abilityPoints.get(i).y;
            linePaint.reset();
            linePaint.setStyle(Paint.Style.FILL);
            linePaint.setColor(Color.BLUE);
//            canvas.drawPoint(x,y,linePaint);
            canvas.drawCircle(x, y, dp2px(getContext(),2f), linePaint);
        }
        canvas.restore();

    }

    /**
     * 绘制多边形的辺，轮廓线
     *
     * @param canvas 画布
     */
    private void drawOutLine(Canvas canvas) {
        //与上一个方法中的用意一样
        canvas.save();

        //设置画笔的颜色
        linePaint.setColor(sideColor);
        //设置画笔的样式为空心
        linePaint.setStyle(Paint.Style.STROKE);
        for (int j = 0; j < level; j++) {

            //先画出最外面的多边形轮库
            Path path = new Path();
            for (int i = 0; i < side; i++) {
                //只需要第一组的点
                float x = pointArrayList.get(j).get(i).x;
                float y = pointArrayList.get(j).get(i).y;
                if (i == 0) {
                    //如果是第一个点就把path的起点设置为这个点
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            //闭合路径
            path.close();
            canvas.drawPath(path, linePaint);
        }

        Path path2 = new Path();
        //再画顶点到中心的线
        for (int i = 0; i < side; i++) {
            float x = pointArrayList.get(0).get(i).x;
            float y = pointArrayList.get(0).get(i).y;
            path2.moveTo(0f, 0f);
            path2.lineTo(x, y);
            linePaint.setPathEffect(new DashPathEffect(new float[]{8f, 8f}, 0));
            //设置画笔的颜色
            linePaint.setColor(sideColor);
            //设置画笔的样式为空心
            linePaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path2, linePaint);//画虚线
//            canvas.drawLine(0, 0, x, y, linePaint); //画实线


        }

        canvas.restore();

    }

    /**
     * 在画布上绘制画出的形状
     *
     * @param canvas 画布
     */
    private void drawPolygon(Canvas canvas) {
        //保存画布当前状态（平移，缩放、旋转、裁剪）和canvas.restore()配合使用
        canvas.save();
        //填充且描边
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //设置路径
        Path path = new Path();
        //循环、一层一层的绘制
        for (int i = 0; i < level; i++) {
            //每一层的颜色更改
            switch (i) {
                case 0:
                    if (levelColor != null && levelColor.length > 0 && (levelColor[0] != 0)) {
                        linePaint.setColor(getContext().getResources().getColor(levelColor[0]));
                    } else {
                        linePaint.setColor(Color.parseColor("#DFF2FF"));
                    }

                    break;
                case 1:
                    if (levelColor != null && levelColor.length > 1 && (levelColor[1] != 0)) {
                        linePaint.setColor(getContext().getResources().getColor(levelColor[1]));
                    } else {
                        linePaint.setColor(Color.parseColor("#F3FAFF"));
                    }

                    break;
                case 2:
                    if (levelColor != null && levelColor.length > 2 && (levelColor[2] != 0)) {
                        linePaint.setColor(getContext().getResources().getColor(levelColor[2]));
                    } else {
                        linePaint.setColor(Color.parseColor("#56C1C7"));
                    }

                    break;
                case 3:
                    if (levelColor != null && levelColor.length > 3 && (levelColor[3] != 0)) {
                        linePaint.setColor(getContext().getResources().getColor(levelColor[3]));
                    } else {
                        linePaint.setColor(Color.parseColor("#278891"));
                    }

                    break;
            }
            //每一层都有n个点
            for (int j = 0; j < side; j++) {
                float x = pointArrayList.get(i).get(j).x;
                float y = pointArrayList.get(i).get(j).y;
                if (j == 0) {
                    //如果是每层的第一个点，就把它设置为path的起始点
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            //关闭路径
            path.close();
            //在画布上画出路径
            canvas.drawPath(path, linePaint);
            //清除Path存储的路径
            path.reset();
        }
        canvas.restore();
    }

    /**
     * 传入元数据
     *
     * @param data
     */
    public void setData(AbilityData[] data) {
        if (data == null) {
            return;
        }
        this.allAbility = data;

        //View本身调用迫使view重画
        invalidate();
    }

    /**
     * 下面都是工具类，dp单位转px单位
     */
    public static int dp2px(Context c, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context c, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.getResources().getDisplayMetrics());
    }

    public static float dp2pxF(Context c, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    public static float sp2pxF(Context c, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.getResources().getDisplayMetrics());
    }

    /**
     * 能力数据类
     */
    public static class AbilityData {
        /**
         * 能力名
         */
        public String abilityName;
        /**
         * 能力数值
         */
        public float abilityValue;
    }
}
