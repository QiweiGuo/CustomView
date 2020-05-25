package com.fy.customview.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.fy.customview.R;
import com.fy.customview.fragments.BlankFragment;
import com.fy.customview.fragments.ConstrainSetTestFragment;

import java.util.ArrayList;
import java.util.List;

public class TestFragmentActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager fl_fragment_container;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        fragments.add(new BlankFragment());
        fragments.add(new ConstrainSetTestFragment());
        fl_fragment_container = findViewById(R.id.fl_fragment_container);
        fl_fragment_container.setAdapter(fragmentPagerAdapter);
        findViewById(R.id.btn_test_view).setOnClickListener(this);
        findViewById(R.id.btn_test_constrain_set).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_view:
                fl_fragment_container.setCurrentItem(0);
                break;
            case R.id.btn_test_constrain_set:
                fl_fragment_container.setCurrentItem(1);
                break;
        }
    }
}
