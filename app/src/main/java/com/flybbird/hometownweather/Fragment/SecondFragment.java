package com.flybbird.hometownweather.Fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flybbird.hometownweather.R;

/**
 * Created by SuyoungKang on 2016. 1. 24..
 */
public class SecondFragment extends Fragment {

    public static Fragment newInstance(Context context) {
        SecondFragment secondFragment = new SecondFragment();

        return secondFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("DEBUG", "* SecondFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("DEBUG", "* SecondFragment onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("DEBUG", "* SecondFragment onStop");
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.d("DEBUG", "* SecondFragment onPause");
    }

}
