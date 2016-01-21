package com.flybbird.hometownweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flybbird.hometownweather.Adapter.ListViewAdapter;
import com.flybbird.hometownweather.DB.WeatherSimpleData;
import com.flybbird.hometownweather.Data.StatiscData;
import com.flybbird.hometownweather.Data.WeatherData;
import com.flybbird.hometownweather.task.RequestWeatherTask;
import com.flybbird.hometownweather.task.RequestWeatherTaskCompleted;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener,
                                                                LocationListener,
                                                                RequestWeatherTaskCompleted

{
    public static final String TAG = MainActivity.class.getSimpleName();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mInfoTextView = (TextView) findViewById(R.id.INFO_TEXT_VIEW);
        mProgressBar = (ProgressBar) findViewById(R.id.LOADING_BAR);
        mWeatherIconImageView = (ImageView)findViewById(R.id.WEATHER_IMAGE);
        mWeatherDescTextView = (TextView)findViewById(R.id.WEATHER_DESC);
        mWeatherTempTextView = (TextView)findViewById(R.id.WEATHER_TEMP);
        mListView = (ListView) findViewById(R.id.listView);

        // 리스트 어댑터 추가
        mListAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mListAdapter);


        // Realm DB Init Instance.
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                                                .name("weather.realm")
                                                .build();
        mRealm =  Realm.getInstance(realmConfiguration);
    }

    @Override
    protected void onResume() {
        super.onResume();

        connectGoogleAPiClient();
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }


    private void connectGoogleAPiClient(){

        if ( mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
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

//        updateUI();

        requestWhether();
    }


    @Override
    public void onResponseTaskCompleted(WeatherData data) {
        if ( data != null){
            mProgressBar.setVisibility(View.GONE);
            mInfoTextView.setText(data.getCityName());
            mWeatherDescTextView.setText(data.getWeatherStateDesc());

            String temp = String.format("%.2f", data.getWeatherTempature());
            mWeatherTempTextView.setText(temp);

            Picasso.with(MainActivity.this).load(data.getWeatherIconUrl()).into(mWeatherIconImageView);

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





    private void updateUI() {
        String locationStr = "UPDATE Location:"+String.valueOf(getLocation().getLatitude()) +
                " , " +
                String.valueOf(getLocation().getLongitude()) ;
        mInfoTextView.setText(locationStr);

    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private Location getLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Execute location service call if user has explicitly granted ACCESS_FINE_LOCATION..
            Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            return lastKnownLocation;
        }

        return null;

    }


    // 오늘 00:00 를 구해오기
    private long getTodayStartMillis(){
        Calendar current = Calendar.getInstance();
        current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DATE), 0, 0, 0);

        long startTime = current.getTimeInMillis();
        return startTime;
    }
}
