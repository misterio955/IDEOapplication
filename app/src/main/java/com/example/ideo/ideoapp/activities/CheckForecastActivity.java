package com.example.ideo.ideoapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.ideo.ideoapp.R;
import com.example.ideo.ideoapp.asyncTasks.WeatherRequest;
import com.example.ideo.ideoapp.interfaces.OnWeatherRequestListener;
import com.example.ideo.ideoapp.models.FavouriteLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckForecastActivity extends AppCompatActivity implements OnWeatherRequestListener {

    private final int REQUEST_LOCATION = 10;
    private Button checkButtonByName;
    private Button checkButtonByCoords;
    private Button addFavButton;
    private AutoCompleteTextView locationName;
    List<FavouriteLocation> locationList;
    List<String> dropdownList;

    private double lattitude;
    private double longitude;

    OnWeatherRequestListener listener = this;
    LocationManager locationManager;
    DatabaseReference databaseLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppLanguage();
        setContentView(R.layout.activity_check_forecast);
        databaseLocations = FirebaseDatabase.getInstance().getReference("locations");
        checkButtonByName = findViewById(R.id.checkButton);
        checkButtonByCoords = findViewById(R.id.checkButtonCoords);
        addFavButton = findViewById(R.id.addFavourite);
        locationName = findViewById(R.id.locationName);
        dropdownList = new ArrayList<>();
        locationList = new ArrayList<>();
        setClicks();
        setAutoCompleteList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseLocations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locationList.clear();
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    FavouriteLocation favLocation = locationSnapshot.getValue(FavouriteLocation.class);
                    locationList.add(favLocation);
                }
                setAutoCompleteList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setAutoCompleteList() {
        dropdownList.clear();
        for (FavouriteLocation location : locationList)
            dropdownList.add(location.getNameLocation());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dropdownList);
        locationName.setAdapter(adapter);
        locationName.setThreshold(1);
    }

    private void setAppLanguage() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        String languageToLoad = sharedPreferences.getString("language", "en");
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        invalidateOptionsMenu();
    }

    private void setClicks() {
        checkButtonByName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = locationName.getText().toString().trim();
                if (TextUtils.isEmpty(name))
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.enter_city), Toast.LENGTH_LONG).show();
                else {
                    String apiID = getResources().getString(R.string.weather_api_id);
                    new WeatherRequest(listener, name, apiID).execute();
                }
            }
        });

        checkButtonByCoords.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                    String apiID = getResources().getString(R.string.weather_api_id);

                    new WeatherRequest(listener, lattitude, longitude, apiID).execute();
                }
            }
        });

        addFavButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = databaseLocations.push().getKey();
                String name = locationName.getText().toString().trim();

                if (TextUtils.isEmpty(name))
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.enter_city), Toast.LENGTH_LONG).show();
                else {
                    FavouriteLocation favLocation = new FavouriteLocation(id, name);
                    assert id != null;
                    databaseLocations.child(id).setValue(favLocation);
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.location_added), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                lattitude = location.getLatitude();
                longitude = location.getLongitude();

            } else if (location1 != null) {
                lattitude = location1.getLatitude();
                longitude = location1.getLongitude();

            } else if (location2 != null) {
                lattitude = location2.getLatitude();
                longitude = location2.getLongitude();

            } else {
                Toast.makeText(this, getResources().getString(R.string.unable_to_trace), Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.turn_on_gps))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onGetting(forecastStatus getStatus) {
        switch (getStatus) {
            case GETTING_FORECAST_STARTED:
            case GETTING_FORECAST_SUCCESS:
                startActivity(new Intent(getBaseContext(), WeatherViewActivity.class));
                break;
            case CITY_NOT_FOUND:
                Toast.makeText(getBaseContext(), getResources().getString(R.string.city_not_found), Toast.LENGTH_LONG).show();
                break;
            case GETTING_FORECAST_FAILURE:
                Toast.makeText(getBaseContext(), getResources().getString(R.string.went_wrong_sorry), Toast.LENGTH_LONG).show();
                break;
        }
    }
}
