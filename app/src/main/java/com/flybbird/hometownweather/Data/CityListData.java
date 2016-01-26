package com.flybbird.hometownweather.Data;

import io.realm.RealmObject;

/**
 * Created by SuyoungKang on 2016. 1. 25..
 */
public class CityListData extends RealmObject {

//        { "_id":1283710,
//          "name":"Bāgmatī Zone",
//          "country":"NP",
//          "coord":
//                  {"lon":85.416664,"lat":28}}
//        {"_id":529334,"name":"Mar’ina Roshcha","country":"RU",
//          "coord":{"lon":37.611111,"lat":55.796391}}
//        {"_id":1269750,"name":"Republic of India","country":"IN","coord":{"lon":77,"lat":20}}
//
    private String _id;
    private String name;
    private String country;

    private LocationData  coord;

    public String get_id() {
        return _id;
    }

    public void set_id(String tID) {
        this._id = tID;
    }

    public String getName() {
        return name;
    }

    public void setName(String tName) {
        this.name = tName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String tCountry) {
        this.country = tCountry;
    }

    public LocationData getCoord()
    {
        return this.coord;
    }

    public void setCoord(LocationData data){
        this.coord = data;
    }

}

