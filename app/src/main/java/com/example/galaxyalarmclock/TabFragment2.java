package com.example.galaxyalarmclock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

public class TabFragment2 extends Fragment{

    private long timeWhenStopped = 0;
    private boolean isRunning = false;
    private boolean keepScreenOn = false;
    private View mView;
    private Chronometer mChronometer;
    private ImageButton mPlay;
    private ImageButton mReset;
    private Switch mSwitch;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // restore instances
        if (savedInstanceState != null) {
            keepScreenOn = savedInstanceState.getBoolean(getKeepScreenOn());
            isRunning = savedInstanceState.getBoolean(getIsRunningKey());
            setCurrentTime(savedInstanceState.getLong(getTimeKey()));
            timeWhenStopped = SystemClock.elapsedRealtime() - mChronometer.getBase();
            if (isRunning) {
                mChronometer.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.tab_fragment2, container, false);
        mSwitch = (Switch) mView.findViewById(R.id.switch2);
        mChronometer = (Chronometer) mView.findViewById(R.id.chronometer);

        // Reset chronometer after one hour or 3 600 000
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 3600000) {
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                }
            }
        });

        mPlay = (ImageButton) mView.findViewById(R.id.imagebuttonPlay);
        mReset = (ImageButton) mView.findViewById(R.id.imagebuttonReset);
        configureImagebuttons();
        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save instances
        if (isRunning) {
            timeWhenStopped = SystemClock.elapsedRealtime() - mChronometer.getBase();
        }
        outState.putLong(getTimeKey(), getCurrentTime());
        outState.putBoolean(getIsRunningKey(), isRunning());
        outState.putBoolean(getKeepScreenOn(), keepScreenOn());
    }

    private void configureImagebuttons() {

        mPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    mChronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
                    isRunning = true;
                    mChronometer.start();
                    mPlay.setImageResource(R.drawable.icon_pause);
                } else {
                    isRunning = false;
                    timeWhenStopped = SystemClock.elapsedRealtime() - mChronometer.getBase();
                    mChronometer.stop();
                    mPlay.setImageResource(R.drawable.icon_play);
                }
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mChronometer.stop();
                isRunning = false;
                mChronometer.setBase(SystemClock.elapsedRealtime());
                timeWhenStopped = 0;
                mPlay.setImageResource(R.drawable.icon_play);
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    keepScreenOn = true;
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Activate screen always on
                } else {
                    keepScreenOn = false;
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Deactivate screen always on
                }
            }
        });
    }

    private final String getTimeKey() {
        return "KEY_TIMER_TIME" + getId();
    }

    private final String getIsRunningKey() {
        return "KEY_TIMER_RUNNING" + getId();
    }

    private final String getKeepScreenOn() {
        return "KEEP_SCREEN_ON" + getId();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public long getCurrentTime() {
        return timeWhenStopped;
    }

    public boolean keepScreenOn() {
        return keepScreenOn;
    }

    public void setCurrentTime(long time) {
        timeWhenStopped = time;
        mChronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
    }
}