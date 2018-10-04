package com.example.ideo.ideoapp.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ideo.ideoapp.R;
import com.example.ideo.ideoapp.models.Weather;

import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Weather> mWeathers;
    private TextView locDatatime;
    private TextView locPressure;
    private TextView locWindSpeed;
    private TextView locHumidity;
    private TextView locTemp;
    private int mUnitTemp;

    public CustomPagerAdapter(Context context, List<Weather> weathers, int unitTemp) {
        mContext = context;
        mWeathers = weathers;
        mUnitTemp = unitTemp;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View layout = mInflater.inflate(R.layout.view_pager_item, collection, false);
        collection.addView(layout);
        locDatatime = layout.findViewById(R.id.locDataTime);
        locPressure = layout.findViewById(R.id.locPressure);
        locWindSpeed = layout.findViewById(R.id.locWindSpeed);
        locHumidity = layout.findViewById(R.id.locHumidity);
        locTemp = layout.findViewById(R.id.locTemp);
        setTextValues(position);
        return layout;
    }

    private void setTextValues(int position) {
        Weather weather = mWeathers.get(position);
        String pressure = String.valueOf(weather.getPressure()) + "hPa";
        String windspeed = String.valueOf(weather.getWindSpeed()) + "m/s";
        String humidity = String.valueOf(weather.getHumidity()) + "%";
        String temperature = null;
        switch (mUnitTemp) {
            case 0:
                temperature = String.valueOf(weather.getTemp() - 273.15).substring(0, 4) + "°C";
                break;
            case 1:
                temperature = String.valueOf(weather.getTemp()).substring(0, 5) + "K";
                break;
            case 2:
                temperature = String.valueOf((weather.getTemp() * 1.8) - 459.67).substring(0, 4) + "°F";
                break;
        }
        locDatatime.setText(weather.getDataTime());
        locPressure.append(pressure);
        locWindSpeed.append(windspeed);
        locHumidity.append(humidity);
        locTemp.append(temperature);
    }


    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mWeathers.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}