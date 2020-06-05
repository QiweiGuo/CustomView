package com.fy.customview.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fy.customview.R;
import com.fy.customview.fragments.PianoKeyboardFragment;

import java.util.ArrayList;
import java.util.List;

public class ShowWidgetActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] barViewIds = new int[]{R.id.tv_show_piano_keyboard};
    private TextView[] barViews = new TextView[barViewIds.length];

    private ViewPager vp_show_widget;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_widget);
        initData();
        initView();
    }

    private void initData() {
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        fragments.add(new PianoKeyboardFragment());
    }

    private void initView() {
        for (int i = 0; i < barViewIds.length; i++) {
            barViews[i] = findViewById(barViewIds[i]);
            barViews[i].setOnClickListener(this);
        }
        vp_show_widget = findViewById(R.id.vp_show_widget);
        vp_show_widget.setAdapter(fragmentPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < barViewIds.length; i++) {
            if (barViewIds[i] == v.getId()) {
                vp_show_widget.setCurrentItem(i);
                return;
            }
        }
    }
}
