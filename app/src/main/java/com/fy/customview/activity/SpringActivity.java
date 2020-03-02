package com.fy.customview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringSystemListener;
import com.fy.customview.R;
import com.fy.customview.YLListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试弹簧效果
 */
public class SpringActivity extends AppCompatActivity implements SpringSystemListener {

    private final String TAG = "SpringActivity";

//    private SpringSystem mSpringSystem;
//
//    private ImageView iv_spring_test;

    private YLListView ylv;
    private MyAdapter adapter;
    private List<ImageData> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring);
        Log.d(TAG, "--onCreate--");
//        mSpringSystem = SpringSystem.create();
//        mSpringSystem.addListener(this);
//        iv_spring_test = findViewById(R.id.iv_spring_test);
        data = new ArrayList<>();
        data.add(new ImageData(R.mipmap.gstring_scale_top_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade1_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade2_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade_icon));
        data.add(new ImageData(R.mipmap.gstring_scale_grade_icon));
        ylv = findViewById(R.id.ylv);
        adapter = new MyAdapter();
        ylv.setAdapter(adapter);
    }

    @Override
    public void onBeforeIntegrate(BaseSpringSystem springSystem) {
        Log.d(TAG, "--onBeforeIntegrate--");
//        double value = springSystem.createSpring().getCurrentValue();
//        float scale = (float) (1f - (value * 0.5f));
//        iv_spring_test.setScaleX(scale);
//        iv_spring_test.setScaleY(scale);
//        iv_spring_test.setScaleType(ImageView.ScaleType.CENTER_CROP);

    }

    @Override
    public void onAfterIntegrate(BaseSpringSystem springSystem) {
        Log.d(TAG, "--onAfterIntegrate--" + springSystem.createSpring().getEndValue());
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public ImageData getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(SpringActivity.this).inflate(R.layout.item_image, null);
                holder.iv_scale_0 = convertView.findViewById(R.id.iv_scale_0);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.iv_scale_0.setImageResource(getItem(position).imageId);
            return convertView;
        }
    }

    private class ImageData {
        int imageId;

        ImageData(int imageId) {
            this.imageId = imageId;
        }
    }

    private class Holder {
        ImageView iv_scale_0;
    }
}
