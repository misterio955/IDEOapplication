package com.example.ideo.ideoapp.models;

public class Weather {
    private String datatime;
    private double latitude;
    private double longitude;
    private double pressure;
    private double windSpeed;
    private String name;
    private int humidity;
    private double temp;

    public Weather(String datatime, double latitude, double longitude, double pressure, double windSpeed, String name, int humidity, double temp) {
        this.datatime = datatime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.name = name;
        this.humidity = humidity;
        this.temp = temp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getName() {
        return name;
    }

    public String getDataTime() {
        return datatime;
    }

    public int getHumidity() {
        return humidity;
    }
}
