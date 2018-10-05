//package com.example.ideo.ideoapp.asyncTasks;
//
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.example.ideo.ideoapp.interfaces.OnWeatherRequestListener;
//import com.example.ideo.ideoapp.models.Weather;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.HttpUrl;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//import static com.example.ideo.ideoapp.interfaces.OnWeatherRequestListener.forecastStatus.*;
//
//public class WeatherRequest {
//
//    private OnWeatherRequestListener onWeatherRequestListener;
//    private String cityName;
//    private String appID;
//    private static List<Weather> weatherList;
//    private double latitude;
//    private double longitude;
//    private boolean checkByName;
//    private forecastStatus status;
//
//    public WeatherRequest(OnWeatherRequestListener onWeatherRequestListener, String cityName, String appID) {
//        this.onWeatherRequestListener = onWeatherRequestListener;
//        this.cityName = cityName;
//        this.appID = appID;
//        checkByName = true;
//    }
//
//    public WeatherRequest(OnWeatherRequestListener onWeatherRequestListener, double latitude, double longitude, String appID) {
//        this.onWeatherRequestListener = onWeatherRequestListener;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.appID = appID;
//        checkByName = false;
//    }
//
//    public void execute(Context context) {
//        weatherList = new ArrayList<>();
//        doRequest(context);
//
//    }
//
//    public Object doRequest(Context context) {
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.openweathermap.org/data/2.5/forecast").newBuilder();
//
//        if(checkByName == true) {
//            urlBuilder.addQueryParameter("q", cityName);
//            urlBuilder.addQueryParameter("appid", appID);
//        } else {
//            urlBuilder.addQueryParameter("lat", String.valueOf(latitude));
//            urlBuilder.addQueryParameter("lon", String.valueOf(longitude));
//            urlBuilder.addQueryParameter("appid", appID);
//        }
//        String url = urlBuilder.build().toString();
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                setfStatus(GETTING_FORECAST_FAILURE);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                switch (response.code()) {
//                    case 200:
////                        try {
////                            String myResponse = response.body().string();
////                            //makeListFromJSON(new JSONObject(myResponse));
////                            setfStatus(GETTING_FORECAST_SUCCESS);
////                            onPostExecute(status);
////
////                        } catch (JSONException e) {
////                            setfStatus(GETTING_FORECAST_FAILURE);
////                            e.printStackTrace();
////                            onPostExecute(status);
////                        }
//
//                    case 404:
//                        setfStatus(CITY_NOT_FOUND);
//                        onPostExecute(status);
//                    default:
//                        setfStatus(GETTING_FORECAST_FAILURE);
//                        onPostExecute(status);
//                }
//            }
//        });
//
//        return status;
//    }
//
//    public void setfStatus(forecastStatus status) {
//        this.status = status;
//    }
//
//    public void onPostExecute(Object o) {
//        onWeatherRequestListener.onGetting((OnWeatherRequestListener.forecastStatus) o);
//    }
//
//    public static List<Weather> getWeatherList() {
//        return weatherList;
//    }
//
//}
