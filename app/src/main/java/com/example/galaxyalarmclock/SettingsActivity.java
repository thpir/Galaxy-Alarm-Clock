package com.example.galaxyalarmclock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.galaxyalarmclock.utils.SharedPreference;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private Button button;
    private Switch switch1;
    private String background;
    private boolean switch1OnOff;
    private ArrayList<BackgroundItem> mBackgroundList;
    private BackgroundAdapter mAdapter;
    private Spinner spinnerBackgrounds;

    //private SharedPreference sharedPreference;
    //Activity context = this;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";
    public static final String BACKGROUND = "background";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initList();

        button = findViewById(R.id.SaveSettings);
        switch1 = findViewById(R.id.switch1);
        spinnerBackgrounds = findViewById(R.id.spinner_backgrounds);
        mAdapter = new BackgroundAdapter(this, mBackgroundList);
        spinnerBackgrounds.setAdapter(mAdapter);

        spinnerBackgrounds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BackgroundItem clickedItem = (BackgroundItem) parent.getItemAtPosition(position);
                String clickedBackgroundName = clickedItem.getBackgroundName();
                background = clickedBackgroundName;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveDataSwitches();
                returnToMain();
            }
        });

        loadData();
        updateViews();
    }

    private void returnToMain() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void initList() {
        mBackgroundList = new ArrayList<>();
        mBackgroundList.add(new BackgroundItem("Background 1", R.drawable.background01));
        mBackgroundList.add(new BackgroundItem("Background 2", R.drawable.background02));
        mBackgroundList.add(new BackgroundItem("Background 3", R.drawable.background03));
        mBackgroundList.add(new BackgroundItem("Background 4", R.drawable.background04));
        mBackgroundList.add(new BackgroundItem("Background 5", R.drawable.background05));
        mBackgroundList.add(new BackgroundItem("Background 6", R.drawable.background06));
        mBackgroundList.add(new BackgroundItem("Background 7", R.drawable.background07));
    }

    public void saveDataSwitches() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1, switch1.isChecked());
        editor.putString(BACKGROUND, background);

        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switch1OnOff = sharedPreferences.getBoolean(SWITCH1, false);
    }

    public void updateViews() {
        switch1.setChecked(switch1OnOff);
    }
}