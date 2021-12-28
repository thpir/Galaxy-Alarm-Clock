package com.example.galaxyalarmclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button button;
    int backgroundNumber = 0;
    int memorizedNumber = 0;

    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create full screen view
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });
        //Create full screen view

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Keep the screen on when activity is active

        //Display actual time and date with a specific font
        Handler handler=new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                TextView clockDisplay = findViewById(R.id.clock);
                Typeface face = Typeface.createFromAsset(getAssets(),"fonts/alarmclock.ttf");
                clockDisplay.setTypeface(face);
                String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                clockDisplay.setText(currentDateTimeString);
                handler.postDelayed(this,500); // set time here to refresh textView
            }
        });
        //Display actual time and date with a specific font

        //Toggle between backgrounds when tapping screen
        button = findViewById(R.id.backgroundChange);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackground();
            }
        });
        //Toggle between backgrounds when tapping screen

        Toast.makeText(getApplication(), "Tap screen to change background", Toast.LENGTH_LONG).show();

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
        if (backgroundNumber == 0) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.black);
            backgroundNumber++;
            memorizedNumber = 0;

        } else if (backgroundNumber == 1) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.ag_carinae);
            backgroundNumber++;
            memorizedNumber = 1;

        } else if (backgroundNumber == 2) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.artists_illustration_of_vy_cma);
            backgroundNumber++;
            memorizedNumber = 2;

        } else if (backgroundNumber == 3) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.dark_rays_in_ic_5063);
            backgroundNumber++;
            memorizedNumber = 3;

        } else if (backgroundNumber == 4) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.double_quasars_illustration);
            backgroundNumber++;
            memorizedNumber = 4;

        } else if (backgroundNumber == 5) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.molten_ring_galaxy);
            backgroundNumber++;
            memorizedNumber = 5;

        } else {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.ngc_6397);
            backgroundNumber = 0;
            memorizedNumber = 6;

        }
    }

    public void restoreBackground() {

        if (memorizedNumber == 0) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.black);

        } else if (memorizedNumber == 1) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.ag_carinae);

        } else if (memorizedNumber == 2) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.artists_illustration_of_vy_cma);

        } else if (memorizedNumber == 3) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.dark_rays_in_ic_5063);

        } else if (memorizedNumber == 4) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.double_quasars_illustration);

        } else if (memorizedNumber == 5) {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.molten_ring_galaxy);

        } else {

            ImageView image = (ImageView) findViewById(R.id.backgroundPicture);
            image.setImageResource(R.drawable.ngc_6397);

        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("backgroundNumber", backgroundNumber);
        outState.putInt("memorizedNumber", memorizedNumber);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        backgroundNumber = savedInstanceState.getInt("backgroundNumber");
        memorizedNumber = savedInstanceState.getInt("memorizedNumber");
        restoreBackground();

    }

}



