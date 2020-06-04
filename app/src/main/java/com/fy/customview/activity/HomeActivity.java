package com.fy.customview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fy.customview.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(this, SpringActivity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(this, ChordTestActivity.class));
                break;
            case R.id.button4:
                startActivity(new Intent(this, TestFragmentActivity.class));
                break;
            case R.id.button5:
                startActivity(new Intent(this, ShowWidgetActivity.class));
                break;
            default:
                break;
        }
    }
}
