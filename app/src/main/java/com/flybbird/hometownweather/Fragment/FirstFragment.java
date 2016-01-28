package com.flybbird.hometownweather.Fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flybbird.hometownweather.Adapter.ListViewAdapter;
import com.flybbird.hometownweather.DB.WeatherSimpleData;
import com.flybbird.hometownweather.Data.StatiscData;
import com.flybbird.hometownweather.Data.WeatherData;
import com.flybbird.hometownweather.R;
import com.flybbird.hometownweather.task.RequestWeatherTask;
import com.flybbird.hometownweather.task.RequestWeatherTaskCompleted;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by SuyoungKang on 2016. 1. 24..
 */
public class FirstFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
                                                        GoogleApiClient.OnConnectionFailedListener,
                                                        LocationListener,
                                                        RequestWeatherTaskCompleted {
    private static final String TAG = FirstFragment.class.getSimpleName();

    private static final int REQUEST_LOCATION = 2;
    private static final int GPS_LOCATION_INTERVAL = 60 * 1000; //  1분

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest = new LocationRequest();
    private TextView mInfoTextView;
    private Location mGpsLocationInfo;
    private ProgressBar mProgressBar;
    private ImageView mWeatherIconImageView;
    private TextView mWeatherDescTextView;
    private TextView mWeatherTempTextView;

    // 오늘의 통계
    private ListView mListView;
    private ListViewAdapter mListAdapter;
    private Realm mRealm;
    private FloatingActionButton mFloatingActionButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View firstView =  inflater.inflate(R.layout.fragment_first_view, container, false);

        mInfoTextView = (TextView) firstView.findViewById(R.id.INFO_TEXT_VIEW);
        mProgressBar = (ProgressBar) firstView.findViewById(R.id.LOADING_BAR);
        mWeatherIconImageView = (ImageView) firstView.findViewById(R.id.WEATHER_IMAGE);
        mWeatherDescTextView = (TextView) firstView.findViewById(R.id.WEATHER_DESC);
        mWeatherTempTextView = (TextView)firstView.findViewById(R.id.WEATHER_TEMP);
        mListView = (ListView) firstView.findViewById(R.id.listView);



        mFloatingActionButton = (FloatingActionButton) firstView.findViewById(R.id.FLOATING_ACTION_BUTTON);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // LOCATION를 가지고 화면 업데이트
                mProgressBar.setVisibility(View.VISIBLE);
                mGpsLocationInfo = getLocation();

                requestWhether();
            }
        });


        return firstView;
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

        Log.d("DEBUG", "* FirstFragment onResume");

        // Realm DB Init Instance.
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getActivity())
                .name("weather.realm")
                .build();


        mRealm =  Realm.getInstance(realmConfiguration);

        connectGoogleAPiClient();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("DEBUG", "* FirstFragment onStop");
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.d("DEBUG", "* FirstFragment onPause");

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        mRealm.close();
    }



    private void connectGoogleAPiClient(){
        if ( mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest.setInterval(GPS_LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Location services connected.");

        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdate();
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged Latitude= "+ location.getLatitude() + " / Longtitude="+ location.getLongitude());
        mGpsLocationInfo = location;

        requestWhether();
    }


    @Override
    public void onResponseTaskCompleted(WeatherData data) {
        if ( data != null){
            // UI Update
            updateUI(data);

            // Weather Image Download
            Picasso.with(getActivity()).load(data.getWeatherIconUrl()).into(mWeatherIconImageView);

            // DB UPDATE
            updateDB(data);

            // 현재 측정되는 지역의 평균치를 구해보자
            double avgTemp = calculateAvgTempatureByCityID(data.getCityID());

            ArrayList<StatiscData> statiscDataArrayList = new ArrayList<>();
            statiscDataArrayList.add( new StatiscData( data.getCityName(),avgTemp) );
            mListAdapter.setStatiscDataArrayList(statiscDataArrayList);
            mListAdapter.notifyDataSetChanged();

        }
    }


    private void requestWhether(){
        new RequestWeatherTask(this).execute(mGpsLocationInfo);
    }


    private void updateDB(WeatherData data) {

        // flybbird 20150120 Use the RealM
        mRealm.beginTransaction();

        // ADD the Measure Weather Data
        WeatherSimpleData simpleData = mRealm.createObject(WeatherSimpleData.class);

        // Current Time
        simpleData.setMeasureTime(System.currentTimeMillis());
        // 측정 위치
        simpleData.setLatitude( mGpsLocationInfo.getLatitude() );
        simpleData.setLongtitude(mGpsLocationInfo.getLongitude());

        // 서버에서 온 데이터.
        simpleData.setCityID(data.getCityID());
        simpleData.setCityName(data.getCityName());
        simpleData.setWeatherTempature(data.getWeatherTempature());
        simpleData.setWeatherStateDesc(data.getWeatherStateDesc());
        simpleData.setWeatherIconName(data.getWeatherIconName());

        mRealm.commitTransaction();


    }

    // mRealm.distinct()        / GROUPby
    private double calculateAvgTempatureByCityID(String cityID) {

        RealmResults<WeatherSimpleData> results =
                mRealm.where(WeatherSimpleData.class).equalTo("cityID",cityID)
                        .greaterThan("measureTime", getTodayStartMillis()).findAll();
        double avgTempature = results.average("weatherTempature");
        Log.d("DEBUG","# 오늘 측정된 "+  results.first().getCityName() +" 지역의 날씨 데이터는   = " + results.size()
                        +" / 평균 온도는 = " + avgTempature
        );

        return  avgTempature;
    }





    private void updateUI( WeatherData data ) {
//        String locationStr = "UPDATE Location:"+String.valueOf(getLocation().getLatitude()) +
//                " , " +
//                String.valueOf(getLocation().getLongitude()) ;
//        mInfoTextView.setText(locationStr);

        mProgressBar.setVisibility(View.GONE);
        mInfoTextView.setText(data.getCityName());
        mWeatherDescTextView.setText(data.getWeatherStateDesc());

        String temp = String.format("%.2f", data.getWeatherTempature());
        mWeatherTempTextView.setText(temp);
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private Location getLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Execute location service call if user has explicitly granted ACCESS_FINE_LOCATION..
            Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            return lastKnownLocation;
        }

        return null;

    }


    // 오늘 00:00 를 구해오기
    public long getTodayStartMillis(){
        Calendar current = Calendar.getInstance();
        current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DATE), 0, 0, 0);

        long startTime = current.getTimeInMillis();
        Log.d("DEBUG","@@ getTodayStartMillis=" + startTime);
        return startTime;
    }


}

