package com.example.galaxyalarmclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class TabFragment3 extends Fragment {

    private View mView;
    private ImageButton mButtonPlay;
    private ImageButton mButtonReset;
    private Button mButtonSet;
    private EditText mEditTextInput;
    private TextView mTextviewCountdown;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private boolean keepScreenOn = false;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis = mStartTimeInMillis;
    private long mEndTime;
    private Switch mSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.tab_fragment3, container, false);
        mSwitch = (Switch) mView.findViewById(R.id.switch3);
        mButtonPlay = (ImageButton) mView.findViewById(R.id.imagebuttonPlay);
        mButtonReset = (ImageButton) mView.findViewById(R.id.imagebuttonReset);
        mButtonSet = (Button) mView.findViewById(R.id.buttonSet);
        mEditTextInput = (EditText) mView.findViewById(R.id.edittextInput);
        mTextviewCountdown = (TextView) mView.findViewById(R.id.textviewCountdown);
        configureButtonClicks();
        //updateCountDownText(); is already called in onStart function
        return mView;
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(getSharedPreferences(), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        mEditor.putLong("startTimeInMillis", mStartTimeInMillis);
        mEditor.putLong(getMillisLeft(), mTimeLeftInMillis());
        mEditor.putBoolean(getTimerRunning(), mTimerRunning());
        mEditor.putLong(getEndTime(), mEndTime());
        mEditor.putBoolean(getKeepScreenOn(), keepScreenOn());

        mEditor.apply(); // save instances

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel(); // stop the countdown timer
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(getSharedPreferences(), Context.MODE_PRIVATE);

        mStartTimeInMillis = mSharedPreferences.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = mSharedPreferences.getLong(getMillisLeft(), mStartTimeInMillis);
        mTimerRunning = mSharedPreferences.getBoolean(getTimerRunning(), false);
        keepScreenOn = mSharedPreferences.getBoolean(getKeepScreenOn(), false);

        updateCountDownText();

        if(mTimerRunning) {
            mEndTime = mSharedPreferences.getLong(getEndTime(), 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
            } else {
                startTimer();
            }
        }
    }

    private void configureButtonClicks() {

        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(getActivity(), "Fill in any number of minutes", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;

                if (millisInput == 0) {
                    Toast.makeText(getActivity(), "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                resetTimer(); //if the timer is running we need to reset the running timer to set it to a new value
                setTime(millisInput);
                mEditTextInput.setText("");
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
                mButtonPlay.setImageResource(R.drawable.icon_play);
                resetTimer();
            }
        }.start();

        mTimerRunning = true;
        mButtonPlay.setImageResource(R.drawable.icon_pause);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonPlay.setImageResource(R.drawable.icon_play);
    }

    private void resetTimer() {
        if(mTimerRunning) {
            mCountDownTimer.cancel();
        }
        mTimerRunning = false;
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        mTextviewCountdown.setText(timeLeftFormatted);
    }

    private final String getMillisLeft() {return "MILLIS_LEFT" + getId();};

    private final String getTimerRunning() {return  "TIMER_RUNNING" + getId();}

    private final String getEndTime() {return "END_TIME" + getId();}

    private final String getKeepScreenOn() {return "KEEP_SCREEN_ON" + getId();}

    private final String getSharedPreferences() {return "GET_SHARED_PREFERENCES" + getId();}

    private long mTimeLeftInMillis() {return mTimeLeftInMillis;}

    private boolean mTimerRunning() {return mTimerRunning;}

    private long mEndTime() {return mEndTime;}

    public boolean keepScreenOn() {
        return keepScreenOn;
    }
}