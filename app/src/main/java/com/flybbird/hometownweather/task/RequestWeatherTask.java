package com.flybbird.hometownweather.task;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.flybbird.hometownweather.BuildConfig;
import com.flybbird.hometownweather.Data.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * Created by SuyoungKang on 2016. 1. 14..
 */
public class RequestWeatherTask  extends AsyncTask<Location, Void, JSONObject>
{
    private final String OPEN_API_REG_KEY = BuildConfig.AUTH_KEY_WHEATHER;
    private final String OPEN_API_URL = BuildConfig.SERVER_URL;

    private RequestWeatherTaskCompleted listener;

    public RequestWeatherTask(RequestWeatherTaskCompleted listener){
        this.listener=listener;
    }


    @Override
    protected JSONObject doInBackground(Location... params) {
        // 네트워크 모듈 만들기
        String encodeQuery = makeQuery(params[0]);

        if ( encodeQuery != null){
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            JSONObject jsonObject = null;

            try {
                URL url = new URL(encodeQuery);

                httpURLConnection = (HttpURLConnection)url.openConnection();
                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                jsonObject = new JSONObject(getStringFromInputStream( inputStream ));

                // parse JSON
//                Log.d("DEBUG", "RESULT=" + jsonObject.toString());
                inputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch(JSONException e) {
                System.err.println("JSON parsing error");
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                httpURLConnection.disconnect();
            }

            return jsonObject;
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            String cityID = json.getString("id");
            String cityName = json.getString("name");
            JSONArray weatherArray = json.getJSONArray("weather");
            JSONObject tempObject = json.getJSONObject("main");

            WeatherData weatherData = new WeatherData();
            weatherData.setCityID(cityID);
            weatherData.setCityName(cityName);

            if ( weatherArray.length() > 0) {
                JSONObject weatherArrayJSONObject = weatherArray.getJSONObject(0);

                String weatherDesc = weatherArrayJSONObject.getString("description");
                String weatherIcon = weatherArrayJSONObject.getString("icon");

                weatherData.setWeatherStateDesc(weatherDesc);
                weatherData.setWeatherIconName(weatherIcon);
            }


            if (tempObject != null ){
                float temp = (float)tempObject.getDouble("temp");

                weatherData.setWeatherTempature(temp);
            }

            if ( listener != null ){
                listener.onResponseTaskCompleted(weatherData);
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private  String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    // 쿼리만들기
    private String makeQuery(Location userLocation){
        Location location = userLocation;
        String query = OPEN_API_URL + "?APPID="+OPEN_API_REG_KEY + "&lat=" + location.getLatitude() + "&lon="+location.getLongitude();

        return query;
    }
}
