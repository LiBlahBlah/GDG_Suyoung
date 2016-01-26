package com.flybbird.hometownweather.Data;

import io.realm.RealmObject;

/**
 * Created by SuyoungKang on 2016. 1. 26..
 */
public class LocationData extends RealmObject {
    private  float lon;
    private float lat;

    public float getLon(){
        return lon;
    }

    public void setLon(float lo){
        this.lon = lo;
    }

    public float getLat(){
        return lat;
    }

    public void setLat(float lat){
        this.lat = lat;
    }

}
