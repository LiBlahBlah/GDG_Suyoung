package com.flybbird.hometownweather.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flybbird.hometownweather.R;

/**
 * Created by SuyoungKang on 2016. 1. 28..
 */
public class ThirdFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View thirdView =  inflater.inflate(R.layout.fragment_third_view, container, false);

        return thirdView;
    }


    @Override
    public void onStart() {
        super.onStart();

        // 리스트 어댑터 추가


    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("DEBUG", "* FirstFragment onResume");


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("DEBUG", "* FirstFragment onStop");
    }

}
