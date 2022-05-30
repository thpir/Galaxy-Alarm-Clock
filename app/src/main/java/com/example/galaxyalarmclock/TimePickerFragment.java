package com.example.galaxyalarmclock;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create a Calendar instance
        Calendar c = Calendar.getInstance();
        // And store the current hour and minute in ints hour and minute
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // When the time picker fragment opens, the current hour and minute will be displayed, We have set the date format to 24h
        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getParentFragment(), hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

}
