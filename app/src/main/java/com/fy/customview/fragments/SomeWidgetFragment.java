package com.fy.customview.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fy.customview.R;
import com.fy.customview.widget.BitmapView;

/**
 * 钢琴键盘展示界面
 */
public class SomeWidgetFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "SomeWidgetFragment";

    private BitmapView bv_fragment_some_widget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_some_widget, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView()!=null){
            bv_fragment_some_widget = getView().findViewById(R.id.bv_fragment_some_widget);
            bv_fragment_some_widget.setOnClickListener(this);
        }else {
            Log.e(TAG,"onActivityCreated->The view is null.");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(bv_fragment_some_widget)){
            bv_fragment_some_widget.startAnimation();
        }
    }
}
