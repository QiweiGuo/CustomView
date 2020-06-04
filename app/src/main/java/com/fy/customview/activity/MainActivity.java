package com.fy.customview.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fy.customview.widget.AbilityView;
import com.fy.customview.R;

public class MainActivity extends AppCompatActivity {

    private AbilityView av;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        av = findViewById(R.id.av);
        AbilityView.AbilityData[] data = new AbilityView.AbilityData[4];
        AbilityView.AbilityData lilun = new AbilityView.AbilityData();
        lilun.abilityName = "理论";
        lilun.abilityValue = 20f;
        data[0] = lilun;
        AbilityView.AbilityData tianfu = new AbilityView.AbilityData();
        tianfu.abilityName = "天赋";
        tianfu.abilityValue = 80f;
        data[1] = tianfu;
        AbilityView.AbilityData yanzou = new AbilityView.AbilityData();
        yanzou.abilityName = "演奏";
        yanzou.abilityValue = 20f;
        data[2] = yanzou;
        AbilityView.AbilityData chuangzuo = new AbilityView.AbilityData();
        chuangzuo.abilityName = "创作";
        chuangzuo.abilityValue = 20f;
        data[3] = chuangzuo;
        av.setData(data);
//        av.setLevelColor(0,0xFFF3FAFF);
        av.setLevelColor(0, R.color.blue_1_2);
        av.setLevelColor(1, R.color.blue_1_4);

        tv = findViewById(R.id.tv);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        String text = "X:" + av.getX() + ",Y:" + av.getY() + ",left:" + av.getLeft() + ",right:" + av.getRight();
//        tv.setText(text);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        String text = "X:" + av.getX() + ",Y:" + av.getY() + ",left:" + av.getLeft() + ",top:" + av.getTop() + ",right:" + av.getRight() + ",bottom:" + av.getBottom() + ",height:" + av.getHeight() + ",width:" + av.getWidth();
        tv.setText(text);
    }



}
