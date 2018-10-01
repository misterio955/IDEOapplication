package com.example.ideo.ideoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.ideo.ideoapp.R;

public class HomeScreenActivity extends AppCompatActivity {

    private Button startButton;
    private ToggleButton changeLang;
    private SharedPreferences sharedPreferences;
    private Spinner changeUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        startButton = findViewById(R.id.startButton);
        changeLang = findViewById(R.id.changeLanguage);
        changeUnit = findViewById(R.id.changeUnit);
        setChangeUnitSpinner();
        setClicks();
        setFlag();
    }

    private void setClicks() {
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rememberUnit();
                startActivity(new Intent(getBaseContext(), CheckForecastActivity.class));
            }
        });

        changeLang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (changeLang.isChecked()) {
                    sharedPreferences = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("language", "pl").apply();
                    changeLang.setBackgroundResource(R.drawable.polish_flag);
                } else {
                    sharedPreferences = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("language", "en").apply();
                    changeLang.setBackgroundResource(R.drawable.english_flag);
                }
            }
        });
    }

    private void rememberUnit() {
        int unitTempID = (int) changeUnit.getSelectedItemId();
        sharedPreferences = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("unitTemp", unitTempID).apply();
    }

    private void setChangeUnitSpinner() {
        String[] unitTempList = {getResources().getString(R.string.celsius), getResources().getString(R.string.kelvin), getResources().getString(R.string.fahrenheit)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, unitTempList);
        changeUnit.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        int unitToLoad = sharedPreferences.getInt("unitTemp", 0);
        changeUnit.setSelection(unitToLoad);
    }

    private void setFlag() {
        sharedPreferences = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        String languageToLoad = sharedPreferences.getString("language", "en");
        if (languageToLoad.equals("pl")) changeLang.setBackgroundResource(R.drawable.polish_flag);
    }
}
