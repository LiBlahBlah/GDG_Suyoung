package com.flybbird.hometownweather.DB;

import android.location.LocationManager;

import com.flybbird.hometownweather.Data.WeatherData;

import io.realm.RealmObject;

/**
 * Created by SuyoungKang on 2016. 1. 20..
 *
 * Add 20150120 측정때마다 DB에 ADD 한다
 */
public class WeatherSimpleData extends RealmObject {
    // 측정시간
    private long  measureTime;

    // 위경도
    private double latitude;
    private double longtitude;

//    private WeatherData weatherData;
    private String cityID = null;
    private String cityName = null;
    private String weatherIconName = null;
    private String weatherStateDesc = null;
    private float weatherTempature ;

    public long getMeasureTime() {return this.measureTime; }
    public void setMeasureTime(long currentTime) { this.measureTime = currentTime; }

    public double getLatitude() { return  this.latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongtitude() { return this.longtitude;}
    public void setLongtitude(double longtitude) { this.longtitude = longtitude; }

    public String getCityID() {return  this.cityID;};
    public void setCityID(String id) { this.cityID = id; }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String name ){
        this.cityName = name;
    }

    public String getWeatherIconName(){
        return this.weatherIconName;
    }

    public void setWeatherIconName(String name){
        this.weatherIconName = name;
    }

    public String getWeatherStateDesc(){
        return this.weatherStateDesc;
    }

    public void setWeatherStateDesc(String desc){
        this.weatherStateDesc = desc;
    }

    public void setWeatherTempature(float kelvin){
        weatherTempature = kelvin - 273.15f;
    }
    public float getWeatherTempature(){
        return weatherTempature;
    }


}
