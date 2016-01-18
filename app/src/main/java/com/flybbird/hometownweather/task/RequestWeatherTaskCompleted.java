package com.flybbird.hometownweather.task;

import com.flybbird.hometownweather.Data.WeatherData;

/**
 * Created by SuyoungKang on 2016. 1. 15..
 */
public interface RequestWeatherTaskCompleted {
    void onResponseTaskCompleted(WeatherData data);
}
