package com.example.ideo.ideoapp.Utils;

import com.example.ideo.ideoapp.models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Utils {

    public static void makeListFromJSON(JSONObject JSON, List<Weather> weatherList) {
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


    public static boolean stringHasCoords(String value) {
        String spliting = "";
        boolean hasCoords = true;
        if (value.contains(",")) {
            spliting = ",";
        } else if (value.contains(";")) {
            spliting = ";";
        } else {
            hasCoords = false;
        }

        if (hasCoords) {
            String[] coords = value.split(spliting);
            if (coords.length != 2) hasCoords = false;

            else {
                if (!coords[0].matches("[0-9.]*") || !coords[1].matches("[0-9.]*"))
                    hasCoords = false;
            }
        }

        return hasCoords;
    }


    public static String[] splitCoords(String coords) {
        String spliting = "";
        if (coords.contains(",")) spliting = ",";
        else if (coords.contains(";")) spliting = ";";

        return coords.split(spliting);
    }
}
