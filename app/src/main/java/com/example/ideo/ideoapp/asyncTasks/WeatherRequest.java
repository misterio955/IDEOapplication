package com.example.ideo.ideoapp.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ideo.ideoapp.interfaces.OnWeatherRequestListener;
import com.example.ideo.ideoapp.models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.ideo.ideoapp.interfaces.OnWeatherRequestListener.forecastStatus.*;

public class WeatherRequest extends AsyncTask<String, Object, Object> implements OnWeatherRequestListener {

    private OnWeatherRequestListener onWeatherRequestListener;
    private String cityName;
    private String appID;
    private static List<Weather> weatherList;
    private double latitude;
    private double longitude;
    private boolean checkByName;
    forecastStatus status = GETTING_FORECAST_SUCCESS;

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
        onWeatherRequestListener.onGetting(GETTING_FORECAST_STARTED);
        weatherList = new ArrayList<>();
    }

    @Override
    protected Object doInBackground(String... code) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.openweathermap.org/data/2.5/forecast").newBuilder();
        urlBuilder.addQueryParameter("q", "warsaw");
        urlBuilder.addQueryParameter("appid", "dc024d65cbbe45a60808edc20edeb342");
        String url = urlBuilder.build().toString();

        Log.e("aaa", url);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                status = GETTING_FORECAST_FAILURE;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                switch (response.code()) {
                    case 200:
                        try {
                            String myResponse = response.body().string();
                            makeListFromJSON(new JSONObject(myResponse));
                            Log.e("aaa", response.code() + weatherList.toString());
                            status = GETTING_FORECAST_SUCCESS;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    case 404:
                        status = CITY_NOT_FOUND;
                    default:
                        status = GETTING_FORECAST_FAILURE;

                }
            }
        });
        Log.e("aaa", status.toString());
        return status;

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
