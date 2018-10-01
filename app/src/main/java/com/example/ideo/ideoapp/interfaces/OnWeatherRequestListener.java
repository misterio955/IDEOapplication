package com.example.ideo.ideoapp.interfaces;

public interface OnWeatherRequestListener {

    enum forecastStatus {
        GETTING_FORECAST_STARTED, GETTING_FORECAST_SUCCESS, GETTING_FORECAST_FAILURE, CITY_NOT_FOUND
    }
    void onGetting(forecastStatus getStatus);

}
