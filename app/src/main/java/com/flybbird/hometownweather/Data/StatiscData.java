package com.flybbird.hometownweather.Data;

/**
 * Created by SuyoungKang on 2016. 1. 20..
 */

// 통계 데이터
public class StatiscData {
    private  String cityName;
    private  double  avgWeather;

    public StatiscData(String name, double measureValue){
        this.cityName = name;
        this.avgWeather = measureValue;
    }

    public void setCityName(String name){
        this.cityName = name;
    }

    public String getCityName() { return this.cityName;}

    public void setAvgWeather(float avgWeather){
        this.avgWeather = avgWeather;
    }

    public double getAvgWeather(){
        return this.avgWeather;
    }

}
