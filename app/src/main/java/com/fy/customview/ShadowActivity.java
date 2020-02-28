package com.fy.customview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sxu.shadowdrawable.ShadowDrawable;

/**
 * 尝试各种阴影效果
 */
public class ShadowActivity extends AppCompatActivity {

    private TextView tv;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);
        tv = findViewById(R.id.tv);
//        tv.setOutlineProvider(new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                outline.setRoundRect(0,0,view.getWidth()+50,view.getHeight(),50);
//            }
//        });

        //引用ShadowDrawable框架，看其实现原理是使用LinearGradient绘制渐变背景来实现阴影
        ShadowDrawable.setShadowDrawable(tv,5,R.color.blue_1_3, 10,0,0);
    }
}
