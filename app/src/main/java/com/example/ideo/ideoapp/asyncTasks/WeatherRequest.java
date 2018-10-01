package com.example.ideo.ideoapp.asyncTasks;

import android.os.AsyncTask;

import com.example.ideo.ideoapp.interfaces.OnWeatherRequestListener;
import com.example.ideo.ideoapp.models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.ideo.ideoapp.interfaces.OnWeatherRequestListener.forecastStatus.*;

public class WeatherRequest extends AsyncTask<String, Object, Object> implements OnWeatherRequestListener {

    private OnWeatherRequestListener onWeatherRequestListener;
    private String cityName;
    private String appID;
    private static List<Weather> weatherList;
    private double latitude;
    private double longitude;
    private boolean checkByName;

    public WeatherRequest(OnWeatherRequestListener onWeatherRequestListener, String cityName, String appID) {
        this.onWeatherRequestListener = onWeatherRequestListener;
        this.cityName = cityName;
        this.appID = appID;
        checkByName = true;

    }

    public WeatherRequest(OnWeatherRequestListener onWeatherRequestListener, double latitude, double longitude, String appID) {
        this.onWeatherRequestListener = onWeatherRequestListener;
        this.latitude = latitude;
        this.longitude = longitude;
        this.appID = appID;
        checkByName = false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        weatherList = new ArrayList<>();
        onWeatherRequestListener.onGetting(GETTING_FORECAST_STARTED);
    }

    @Override
    protected Object doInBackground(String... code) {
        HttpURLConnection conn;
        URL connURL;
        try {
            if (checkByName) {
                connURL = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=" + appID);
            } else {
                connURL = new URL("http://api.openweathermap.org/data/2.5/forecast?lat="
                        + latitude + "&lon=" + longitude + "&appid=" + appID);
            }
            conn = (HttpURLConnection) connURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            switch (conn.getResponseCode()) {
                case 200:
                    JSONObject JSON = new JSONObject(getResponseString(conn));
                    makeListFromJSON(JSON);
                    return GETTING_FORECAST_SUCCESS;
                case 404:
                    return CITY_NOT_FOUND;
                default:
                    return GETTING_FORECAST_FAILURE;
            }
        } catch (
                Exception e)

        {
            e.printStackTrace();
            return GETTING_FORECAST_FAILURE;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        onWeatherRequestListener.onGetting((OnWeatherRequestListener.forecastStatus) o);

    }

    public static List<Weather> getWeatherList() {
        return weatherList;
    }

    private String getResponseString(HttpURLConnection connection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuilder builder = new StringBuilder();
        String string;
        while ((string = br.readLine()) != null)
            builder.append(string).append("\n");
        br.close();
        return builder.toString();
    }

    private void makeListFromJSON(JSONObject JSON) {
        try {
            JSONArray listJSON = JSON.getJSONArray("list");
            JSONObject cityJSON = JSON.getJSONObject("city");
            String name = cityJSON.getString("name");
            JSONObject coordJSON = cityJSON.getJSONObject("coord");
            double latitude = coordJSON.getDouble("lat");
            double longitude = coordJSON.getDouble("lon");
            for (int i = 0; i < listJSON.length(); i++) {
                JSONObject jsonobject = listJSON.getJSONObject(i);
                String datatime = jsonobject.getString("dt_txt");
                JSONObject mainJSON = jsonobject.getJSONObject("main");
                double temp = mainJSON.getDouble("temp");
                double pressure = mainJSON.getDouble("pressure");
                int humidity = mainJSON.getInt("humidity");

                JSONObject windJSON = jsonobject.getJSONObject("wind");
                double windSpeed = windJSON.getDouble("speed");
                weatherList.add(new Weather(datatime, latitude, longitude, pressure, windSpeed, name, humidity, temp));
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public void onGetting(forecastStatus getStatus) {
        //Log.e("status", getStatus.toString());
    }
}
