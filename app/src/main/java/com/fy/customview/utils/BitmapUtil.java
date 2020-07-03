package com.fy.customview.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapUtil {

    /**
     * 创建一个缩放后的图片
     *
     * @param context    上下文
     * @param imgId      图片ID
     * @param width  图片最终宽度
     * @param height 图片最终高度
     * @return 返回一个缩放后的图片
     */
    public static Bitmap createBitmap(Context context, int imgId, int width, int height) {
        Bitmap headBitmap;
        if (imgId != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            headBitmap = BitmapFactory.decodeResource(context.getResources(), imgId, options);
            headBitmap = getScaleBitmap(headBitmap, width, height);
        } else {
            headBitmap = null;
        }
        return headBitmap;

    }

    /**
     * 按照指定的宽高对图片进行缩放
     *
     * @param sourceBitmap 要进行缩放的图片
     * @param width        缩放后的图片宽度
     * @param height       缩放后的图片高度
     * @return 返回缩放后的图片
     */
    public static Bitmap getScaleBitmap(Bitmap sourceBitmap, float width, float height) {
        Bitmap scaleBitmap;
        //定义矩阵对象
        Matrix matrix = new Matrix();
        float scale_x = width / sourceBitmap.getWidth();
        float scale_y = height / sourceBitmap.getHeight();
        matrix.postScale(scale_x, scale_y);

        try {
            scaleBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            scaleBitmap = null;
            System.gc();
        }
        return scaleBitmap;
    }

}
