package com.example.galaxyalarmclock;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    private Button mDismissButton;
    //private String mSelectedTime;
    private boolean mAlarmOn;
    private int mHourOfDay;
    private int mMinuteOfDay;

    public static final String SHARED_PREFS = "sharedPrefs";
    //public static final String SELECTED_TIME = "selectedTime";
    public static final String ALARM_ON = "alarmOn";
    public static final String HOUR_OF_DAY = "hourOfDay";
    public static final String MINUTE_OF_DAY = "minuteOfDay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        loadData();

        // The following block of code is necessary to open the activity when the phone is locked
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        // Start the vibrator of the phone
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Create a vibration pattern
        long[] pattern = {0, 2000, 1000};
        vibrator.vibrate(pattern, 0);
        // When the Dismiss button is clicked, the alarm is first cancelled, then set to exact the same time, the next day
        mDismissButton = findViewById(R.id.buttonDismiss);
        mDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlarmOn = false;
                saveData();
                vibrator.cancel();
                loadData();
                setAlarmNextDay();
                startActivity(new Intent(AlarmActivity.this, ToolActivity.class));
                finish();
            }
        });
    }

    private void setAlarmNextDay() {
        // Get the saved alarm time from sharedPreferences
        loadData();
        // Load the saved time in the calendar
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, mHourOfDay);
        c.set(Calendar.MINUTE, mMinuteOfDay);
        c.set(Calendar.SECOND, 0);
        // Start the alarm
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarmReceiver = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intentAlarmReceiver, PendingIntent.FLAG_IMMUTABLE);
        // If the selected time is set in the past, we will set the time for the next day
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        mAlarmOn = true;
        saveData();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        mHourOfDay = sharedPreferences.getInt(HOUR_OF_DAY, 1);
        mMinuteOfDay = sharedPreferences.getInt(MINUTE_OF_DAY, 1);
        mAlarmOn = sharedPreferences.getBoolean(ALARM_ON, false);
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(HOUR_OF_DAY, mHourOfDay);
        editor.putInt(MINUTE_OF_DAY, mMinuteOfDay);
        editor.putBoolean(ALARM_ON, mAlarmOn);

        editor.apply();
    }
}