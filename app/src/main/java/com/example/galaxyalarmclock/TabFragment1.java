package com.example.galaxyalarmclock;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class TabFragment1 extends Fragment {

    private View mView;
    private Button mSetTime, mSetAlarm, mCancelAlarm;
    private MaterialTimePicker mTimePicker;
    private TextView mSelectedTime;
    private Calendar mCalendar;
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.tab_fragment1, container, false);
        mSetTime = (Button) mView.findViewById(R.id.buttonSelectTime);
        mSetAlarm = (Button) mView.findViewById(R.id.buttonSetAlarm);
        mCancelAlarm = (Button) mView.findViewById(R.id.buttonCancelAlarm);
        mSelectedTime = (TextView) mView.findViewById(R.id.textviewSelectedTime);
        createNotificationChannel();
        configureButtons();
        return mView;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "GalaxyAlarmClockChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("GalaxyAlarmClock", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void configureButtons() {
        mSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        mSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { setAlarm(); }
        });

        mCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { cancelAlarm(); }
        });
    }

    private void cancelAlarm() {

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        }
        mAlarmManager.cancel(mPendingIntent);
        Toast.makeText(getActivity(), "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {

        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,mCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,mPendingIntent);
        Toast.makeText(getActivity(), "Alarm Set Successfully", Toast.LENGTH_SHORT).show();
    }

    private void showTimePicker() {

        mTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        mTimePicker.show(getParentFragmentManager(), "GalaxyAlarmClock");

        mTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mTimePicker.getHour() > 12) {
                    mSelectedTime.setText(
                            String.format("%02d",(mTimePicker.getHour()-12)) + ":" + String.format("%02d",mTimePicker.getMinute()) + " PM"
                    );
                } else {
                    mSelectedTime.setText(mTimePicker.getHour() + ":" + mTimePicker.getMinute() + " PM");
                }

                mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getHour());
                mCalendar.set(Calendar.MINUTE, mTimePicker.getMinute());
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 0);
            }
        });
    }
}

