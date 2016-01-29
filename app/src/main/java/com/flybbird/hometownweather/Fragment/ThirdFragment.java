package com.flybbird.hometownweather.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.flybbird.hometownweather.Adapter.ListViewAdapter;
import com.flybbird.hometownweather.DB.WeatherSimpleData;
import com.flybbird.hometownweather.Data.StatiscData;
import com.flybbird.hometownweather.Data.WeatherData;
import com.flybbird.hometownweather.R;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by SuyoungKang on 2016. 1. 28..
 */
public class ThirdFragment extends Fragment {
    private ListView mListView;

    private ListViewAdapter mListAdapter;
    private Realm mRealm;

    private ArrayList<String> cityList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View thirdView =  inflater.inflate(R.layout.fragment_third_view, container, false);

        mListView = (ListView) thirdView.findViewById(R.id.listView);

        return thirdView;
    }


    @Override
    public void onStart() {
        super.onStart();

        // 리스트 어댑터 추가
        mListAdapter = new ListViewAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("DEBUG", "* ThirdFragment onResume");
        // Realm DB Init Instance.
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getActivity())
                .name("weather.realm")
                .build();

        mRealm = Realm.getInstance(realmConfiguration);


        checkCheckingCityID();

        for ( String cityID : cityList){
            calculateAvgTempatureByCityID(cityID);
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d("DEBUG", "* ThirdFragment onStop");
        mRealm.close();
    }


    private void checkCheckingCityID(){
        // TODO: GROUPBY 나 DISTINCT가 없나요
        RealmResults<WeatherSimpleData> results =
                mRealm.where(WeatherSimpleData.class).findAllSorted("cityID");

        for (WeatherSimpleData data : results){

            if ( !cityList.contains(data.getCityID())){
                cityList.add(data.getCityID());
            }
        }
    }



    private void calculateAvgTempatureByCityID(String cityID) {

        RealmResults<WeatherSimpleData> results =
                mRealm.where(WeatherSimpleData.class).equalTo("cityID",cityID)
                        .greaterThan("measureTime", getTodayStartMillis()).findAll();

        String cityName = results.get(0).getCityName();
        double avgTempature = results.average("weatherTempature");


        ArrayList<StatiscData> statiscDataArrayList = new ArrayList<>();
        statiscDataArrayList.add(new StatiscData(cityName, avgTempature));
        mListAdapter.setStatiscDataArrayList(statiscDataArrayList);
        mListAdapter.notifyDataSetChanged();
    }


    public long getTodayStartMillis(){
        Calendar current = Calendar.getInstance();
        current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DATE), 0, 0, 0);

        long startTime = current.getTimeInMillis();
        Log.d("DEBUG","@@ getTodayStartMillis=" + startTime);
        return startTime;
    }

}
