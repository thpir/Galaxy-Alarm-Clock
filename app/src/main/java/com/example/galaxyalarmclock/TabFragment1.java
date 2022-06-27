package com.example.galaxyalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Calendar;

public class TabFragment1 extends Fragment implements TimePickerDialog.OnTimeSetListener {

    private View mView;
    private Button mSetAlarm, mCancelAlarm;
    private TextView mSelectedTime;
    //private String selectedTime;
    private boolean mAlarmOn;
    private int mHourOfDay;
    private int mMinuteOfDay;

    public static final String SHARED_PREFS = "sharedPrefs";
    //public static final String SELECTED_TIME = "selectedTime";
    public static final String ALARM_ON = "alarmOn";
    public static final String HOUR_OF_DAY = "hourOfDay";
    public static final String MINUTE_OF_DAY = "minuteOfDay";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadData();
        mView = inflater.inflate(R.layout.tab_fragment1, container, false);
        mSetAlarm = (Button) mView.findViewById(R.id.buttonSetAlarm);
        //mCancelAlarm = (Button) mView.findViewById(R.id.buttonCancelAlarm);
        mSelectedTime = (TextView) mView.findViewById(R.id.textviewSelectedTime);
        if (mAlarmOn) {
            mSelectedTime.setText(mHourOfDay + ":" + mMinuteOfDay);
            mSetAlarm.setText("Cancel Alarm");
        } else {
            mSelectedTime.setText("No Alarm");
            mSetAlarm.setText("Set Alarm");
        }
        configureButtons();
        return mView;
    }

    private void configureButtons() {

        mSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mAlarmOn) {
                    setAlarm();
                } else {
                    cancelAlarm();
                    mSetAlarm.setText("Set Alarm");
                }
            }
        });

        /*mCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { cancelAlarm(); }
        });*/
    }

    private void setAlarm() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getChildFragmentManager(), "time picker");
    }

    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        //selectedTime = sharedPreferences.getString(SELECTED_TIME, "Pick Time");
        mHourOfDay = sharedPreferences.getInt(HOUR_OF_DAY, 1);
        mMinuteOfDay = sharedPreferences.getInt(MINUTE_OF_DAY, 1);
        mAlarmOn = sharedPreferences.getBoolean(ALARM_ON, false);
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //editor.putString(SELECTED_TIME, selectedTime);
        editor.putInt(HOUR_OF_DAY, mHourOfDay);
        editor.putInt(MINUTE_OF_DAY, mMinuteOfDay);
        editor.putBoolean(ALARM_ON, mAlarmOn);

        editor.apply();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // When we set a time of day, those values will be directly send to this fragment over this method
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        mHourOfDay = hourOfDay;
        mMinuteOfDay = minute;
        updateTimeText(c);
        mSetAlarm.setText("Cancel Alarm");
        startAlarm(c);
        saveData();
    }

    private void updateTimeText(Calendar c) {
        //selectedTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        mSelectedTime.setText(mHourOfDay + ":" + mMinuteOfDay);
        //saveData();
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarmReceiver = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intentAlarmReceiver, PendingIntent.FLAG_IMMUTABLE);
        // If the selected time is set in the past, we will set the time for the next day
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        mAlarmOn = true;
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
        //selectedTime = "No Alarm Set";
        mSelectedTime.setText("No Alarm");
        mAlarmOn = false;
        saveData();
    }


}

