package com.flybbird.hometownweather.Data;

/**
 * Created by SuyoungKang on 2016. 1. 15..
 */
public class WeatherData {
    private String cityName = null;
    private String weatherIconName = null;
    private String weatherStateDesc = null;
    private float weatherTempature ;



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

    public String getWeatherIconUrl(){
        return "http://openweathermap.org/img/w/"+ this.weatherIconName+".png";
    }

    public void setWeatherTemp(float kelvin){
        weatherTempature = kelvin - 273.15f;
    }

    public float getWeatherTempature(){
        return weatherTempature;
    }
}
