package com.flybbird.hometownweather;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.Before;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import static org.junit.Assert.assertEquals;
import com.flybbird.hometownweather.DB.WeatherSimpleData;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by SuyoungKang on 2016. 1. 23..
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    private Realm mTestReam;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);


    @Before
    public void setUp() throws Exception {
        // Realm DB Init Instance.
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(mActivityTestRule.getActivity())
                .name("weather.realm")
                .build();
        mTestReam = Realm.getInstance(realmConfiguration);
    }

    @After
    public void tearDown() throws Exception {
        mTestReam.close();

    }

    @Test
    public void valideDatabase() {
        long currentTime = System.currentTimeMillis();

//        mActivityTestRule.getActivity().getTodayStartMillis();

        // DB Insert [[
        mTestReam.beginTransaction();

        // GURO 삭제
        mTestReam.where(WeatherSimpleData.class).equalTo("cityName","GURO").findAll().clear();

        WeatherSimpleData simpleData = mTestReam.createObject(WeatherSimpleData.class);
        simpleData.setMeasureTime(currentTime);
        simpleData.setLatitude(10.0f);
        simpleData.setLongtitude(10.0f);
        simpleData.setCityID("100");
        simpleData.setCityName("GURO");
        simpleData.setWeatherTempature(-15.0f);

        mTestReam.commitTransaction();
        // ]]

        WeatherSimpleData  findFirstQuery =
                mTestReam.where(WeatherSimpleData.class).equalTo("cityName", "GURO").findFirst();

        assertEquals(simpleData, findFirstQuery);

        mTestReam.beginTransaction();
        mTestReam.where(WeatherSimpleData.class).equalTo("cityName","GURO").findAll().clear();
        mTestReam.commitTransaction();

        WeatherSimpleData  deleteItem =
                mTestReam.where(WeatherSimpleData.class).equalTo("cityName","GURO").findFirst();

        assertEquals(deleteItem,null);
        // ]]
    }

    @Test
    public void validNetwork(){

    }
}