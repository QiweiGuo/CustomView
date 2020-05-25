package com.fy.customview.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fy.customview.R;

public class ConstrainSetTestFragment extends Fragment implements View.OnClickListener {

    private ConstraintLayout cl_fragment_constrain_set_root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_constrain_set_test, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (view != null) {
            cl_fragment_constrain_set_root = view.findViewById(R.id.cl_fragment_constrain_set_root);
            view.findViewById(R.id.tv_fragment_constrain_set_test_text1).setOnClickListener(this);
            view.findViewById(R.id.tv_fragment_constrain_set_test_text2).setOnClickListener(this);
            view.findViewById(R.id.btn_constrain_test_add_view).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl_fragment_constrain_set_root);
        switch (v.getId()) {
            case R.id.tv_fragment_constrain_set_test_text1:
                constraintSet.connect(R.id.tv_fragment_constrain_set_test_text1, ConstraintSet.TOP, R.id.tv_fragment_constrain_set_test_text2,
                        ConstraintSet.BOTTOM);
                constraintSet.constrainHeight(R.id.tv_fragment_constrain_set_test_text1, 100);
                constraintSet.clear(R.id.tv_fragment_constrain_set_test_text1, ConstraintSet.BOTTOM);
                constraintSet.applyTo(cl_fragment_constrain_set_root);
                break;
            case R.id.tv_fragment_constrain_set_test_text2:
                constraintSet.connect(R.id.tv_fragment_constrain_set_test_text1, ConstraintSet.BOTTOM, R.id.tv_fragment_constrain_set_test_text2,
                        ConstraintSet.TOP);
                constraintSet.clear(R.id.tv_fragment_constrain_set_test_text1, ConstraintSet.TOP);
                constraintSet.applyTo(cl_fragment_constrain_set_root);
                break;
            case R.id.btn_constrain_test_add_view:
                //添加一个控件
//                TextView textView = new TextView(getActivity());
//                textView.setText("我是新来的控件");
//                textView.setId(R.id.custom_note1);
//                cl_fragment_constrain_set_root.addView(textView);
                if (count<customIds.length){
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageResource(R.mipmap.ukulele_home_tutorial_prepare_bg);
                    imageView.setId(customIds[count]);
                    cl_fragment_constrain_set_root.addView(imageView);
                    if (count == 0){
                        constraintSet.connect(imageView.getId(), ConstraintSet.TOP, R.id.btn_constrain_test_add_view, ConstraintSet.BOTTOM);
                        constraintSet.connect(imageView.getId(), ConstraintSet.START, R.id.btn_constrain_test_add_view, ConstraintSet.START);
                    }else {
                        constraintSet.connect(imageView.getId(), ConstraintSet.TOP, customIds[count-1], ConstraintSet.BOTTOM);
                        constraintSet.connect(imageView.getId(), ConstraintSet.START, customIds[count-1], ConstraintSet.START,50);
                    }
                    constraintSet.constrainHeight(imageView.getId(), 100);
                    constraintSet.constrainWidth(imageView.getId(), 100);
                    constraintSet.applyTo(cl_fragment_constrain_set_root);
                    count++;
                }
                break;
        }
    }

    private int count;
    private int[] customIds = new int[]{R.id.custom_note1, R.id.custom_note2, R.id.custom_note3, R.id.custom_note4, R.id.custom_note5,
            R.id.custom_note6, R.id.custom_note7, R.id.custom_note8, R.id.custom_note9, R.id.custom_note10,
            R.id.custom_note11, R.id.custom_note12, R.id.custom_note13, R.id.custom_note14, R.id.custom_note15,
            R.id.custom_note16, R.id.custom_note17, R.id.custom_note18, R.id.custom_note19, R.id.custom_note20,};

}
