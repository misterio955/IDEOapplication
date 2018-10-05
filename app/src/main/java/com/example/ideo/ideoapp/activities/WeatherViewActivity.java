package com.example.ideo.ideoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ideo.ideoapp.R;
import com.example.ideo.ideoapp.models.Weather;
import com.example.ideo.ideoapp.Utils.CustomPagerAdapter;

import java.util.List;

public class WeatherViewActivity extends AppCompatActivity {

    private Button homeButton;
    private ViewPager viewPager;
    private List<Weather> weathers;
    private TextView transparent;
    private LinearLayout layout;
    private TextView locName;
    private TextView locCoords;
    private int unitTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckForecastActivity.alert.hide();
        setContentView(R.layout.activity_weather_view);
        transparent = findViewById(R.id.transparent);
        layout = findViewById(R.id.layout);
        viewPager = findViewById(R.id.viewPager);
        homeButton = findViewById(R.id.backToHome);
        locName = findViewById(R.id.locName);
        locCoords = findViewById(R.id.locCoords);
        whatUnit();
        weathers = CheckForecastActivity.getWeatherList();
        if (weathers.size() == 0) {
            transparent.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            homeButton.setVisibility(View.GONE);
        } else viewPager.setAdapter(new CustomPagerAdapter(this, weathers, unitTemp));

        setClicks();
        setTextValues();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void whatUnit() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        unitTemp = sharedPreferences.getInt("unitTemp", 0);
    }

    private void setTextValues() {
        String name = null;
        String lat = null;
        String lon = null;
        for (Weather weather : weathers) {
            name = weather.getName();
            lat = String.valueOf(weather.getLatitude());
            lon = String.valueOf(weather.getLongitude());
            break;
        }
        locName.setText(name);
        locCoords.setText("(" + lat + " ; " + lon + ")");
    }


    private void setClicks() {
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), HomeScreenActivity.class));
            }
        });
    }
}
