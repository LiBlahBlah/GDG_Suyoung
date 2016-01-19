package com.flybbird.hometownweather;

import android.Manifest;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flybbird.hometownweather.Data.WeatherData;
import com.flybbird.hometownweather.task.RequestWeatherTask;
import com.flybbird.hometownweather.task.RequestWeatherTaskCompleted;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener,
                                                                LocationListener,
                                                                RequestWeatherTaskCompleted

{
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 2;
    private static final int GPS_LOCATION_INTERVAL = 10 * 1000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest = new LocationRequest();
    private TextView mInfoTextView;
    private Location mGpsLocationInfo;
    private ProgressBar mProgressBar;
    private ImageView mWeatherIconImageView;
    private TextView mWeatherDescTextView;
    private TextView mWeatherTempTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mInfoTextView = (TextView) findViewById(R.id.INFO_TEXT_VIEW);
        mProgressBar = (ProgressBar) findViewById(R.id.LOADING_BAR);
        mWeatherIconImageView = (ImageView)findViewById(R.id.WEATHER_IMAGE);
        mWeatherDescTextView = (TextView)findViewById(R.id.WEATHER_DESC);
        mWeatherTempTextView = (TextView)findViewById(R.id.WEATHER_TEMP);



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

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged Latitude= "+ location.getLatitude() + " / Longtitude="+ location.getLongitude());
        mGpsLocationInfo = location;

        updateUI();

        requestWhether();
    }





    private void requestWhether(){
        new RequestWeatherTask(this).execute(mGpsLocationInfo);
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
        }
    }
}
