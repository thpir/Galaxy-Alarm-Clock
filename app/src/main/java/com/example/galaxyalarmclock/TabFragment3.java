package com.example.galaxyalarmclock;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class TabFragment3 extends Fragment {

    private View mView;
    private ImageButton mPlay;
    private ImageButton mReset;
    private TextView mTextviewCountdown;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private boolean keepScreenOn = false;
    private static final long startTimeInMillis = 600000;
    private long mTimeLeftInMillis = startTimeInMillis;
    private long mEndTime;
    private Switch mSwitch;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // restore instances
        if (savedInstanceState != null) {
            keepScreenOn = savedInstanceState.getBoolean(getKeepScreenOn());
            mTimeLeftInMillis = savedInstanceState.getLong(getMillisLeft());
            mTimerRunning = savedInstanceState.getBoolean(getTimerRunning());
            updateCountDownText();

            if(mTimerRunning) {
                mEndTime = savedInstanceState.getLong(getEndTime());
                mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
                startTimer();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.tab_fragment3, container, false);
        mSwitch = (Switch) mView.findViewById(R.id.switch3);
        mPlay = (ImageButton) mView.findViewById(R.id.imagebuttonPlay);
        mReset = (ImageButton) mView.findViewById(R.id.imagebuttonReset);
        mTextviewCountdown = (TextView) mView.findViewById(R.id.textviewCountdown);
        configureImagebuttons();
        updateCountDownText();
        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save instances
        outState.putLong(getMillisLeft(), mTimeLeftInMillis());
        outState.putBoolean(getTimerRunning(), mTimerRunning());
        outState.putLong(getEndTime(), mEndTime());
        outState.putBoolean(getKeepScreenOn(), keepScreenOn());
    }

    private void configureImagebuttons() {

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
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

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mPlay.setImageResource(R.drawable.icon_play);
                resetTimer();
            }
        }.start();

        mTimerRunning = true;
        mPlay.setImageResource(R.drawable.icon_pause);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mPlay.setImageResource(R.drawable.icon_play);
    }

    private void resetTimer() {
        if(mTimerRunning) {
            mCountDownTimer.cancel();
        }
        mTimerRunning = false;
        mTimeLeftInMillis = startTimeInMillis;
        updateCountDownText();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        mTextviewCountdown.setText(timeLeftFormatted);
    }

    private final String getMillisLeft() {return "MILLIS_LEFT" + getId();};

    private final String getTimerRunning() {return  "TIMER_RUNNING" + getId();}

    private final String getEndTime() {return "END_TIME" + getId();}

    private final String getKeepScreenOn() {
        return "KEEP_SCREEN_ON" + getId();
    }

    private long mTimeLeftInMillis() {return mTimeLeftInMillis;}

    private boolean mTimerRunning() {return mTimerRunning;}

    private long mEndTime() {return mEndTime;}

    public boolean keepScreenOn() {
        return keepScreenOn;
    }
}