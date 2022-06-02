package com.example.galaxyalarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class AlarmActivity extends AppCompatActivity {

    private Button mDismissButton;
    private View decorView;
    private String background = "Background 1";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String BACKGROUND = "background";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // The following block of code is necessary to open the activity when the phone is locked
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        mDismissButton = findViewById(R.id.buttonDismiss);
        mDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AlarmActivity.this, ToolsActivity.class));
                finish();
            }
        });
    }
}