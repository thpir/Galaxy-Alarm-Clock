package com.example.galaxyalarmclock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.galaxyalarmclock.utils.SharedPreference;

public class SettingsActivity extends AppCompatActivity {

    private Button button;
    private Switch switch1;
    private boolean switch1OnOff;

    private SharedPreference sharedPreference;
    Activity context = this;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        button = findViewById(R.id.SaveSettings);
        switch1 = findViewById(R.id.switch1);
        sharedPreference = new SharedPreference();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataSwitches();
            }
        });

        loadData();
        updateViews();
    }

    public void saveDataSwitches() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1, switch1.isChecked());

        editor.apply();

        Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switch1OnOff = sharedPreferences.getBoolean(SWITCH1, false);
    }

    public void updateViews() {
        switch1.setChecked(switch1OnOff);
    }
}