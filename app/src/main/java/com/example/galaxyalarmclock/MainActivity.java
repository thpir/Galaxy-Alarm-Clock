package com.example.galaxyalarmclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private View decorView;
    private boolean switch1OnOff;
    private String background = "Background 1";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";
    public static final String BACKGROUND = "background";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check the saved state of the settings
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switch1OnOff = sharedPreferences.getBoolean(SWITCH1, false);
        background = sharedPreferences.getString(BACKGROUND, "background");

        //Select the saved background
        toggleBackground();

        //Create full screen view
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        //Keep the screen on when activity is active
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Display actual time with a specific font
        Handler handler = new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                TextView clockDisplay = findViewById(R.id.clock);
                Typeface face = Typeface.createFromAsset(getAssets(),"fonts/alarmclock.ttf");
                clockDisplay.setTypeface(face);
                String currentTimeString = java.text.DateFormat.getTimeInstance().format(new Date());
                clockDisplay.setText(currentTimeString);
                handler.postDelayed(this,500); // set time here to refresh textView
            }
        });

        //Display actual date with a specific font
        if(switch1OnOff) {
            Handler handler2 = new Handler();
            handler2.post(new Runnable(){
                @Override
                public void run() {
                    TextView dateDisplay = findViewById(R.id.displayDate);
                    Typeface face = Typeface.createFromAsset(getAssets(),"fonts/alarmclock.ttf");
                    dateDisplay.setTypeface(face);
                    String currentDateString = java.text.DateFormat.getDateInstance().format(new Date());
                    dateDisplay.setText(currentDateString);
                    handler2.postDelayed(this,500); // set time here to refresh textView
                }
            });
        }

        //log no° of button clicks for single/double click feature
        button = findViewById(R.id.backgroundChange);

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openToolsActivity();
                return true;
            }
        });

        //Inform user to tap the background when they want to change the background image
        Toast.makeText(getApplication(), "Long click on anywhere on screen to open settings", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    public void toggleBackground() {
        if (background.equals("Background 1")) {

            ImageView image = findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.background01);

        } else if (background.equals("Background 2")) {

            ImageView image = findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.background02);

        } else if (background.equals("Background 3")) {

            ImageView image = findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.background03);

        } else if (background.equals("Background 4")) {

            ImageView image = findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.background04);

        } else if (background.equals("Background 5")) {

            ImageView image = findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.background05);

        } else if (background.equals("Background 6")) {

            ImageView image = findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.background06);

        } else {

            ImageView image = findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.background07);

        }
    }

    public void openToolsActivity() {
        Intent intent = new Intent(this, ToolsActivity.class);
        startActivity(intent);
    }
}



