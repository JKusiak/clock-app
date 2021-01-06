package com.example.alarmclockapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

//  static
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private EditText text;

    public TimePickerFragment(EditText v) {
        this.text = v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        String time;
        if (hourOfDay < 10 & minute < 10) {
            time = "0" + hourOfDay + ":0" + minute;
        } else if (hourOfDay > 10 & minute < 10) {
            time = "" + hourOfDay + ":0" + minute;
        } else if (hourOfDay < 10 & minute > 10) {
            time = "0" + hourOfDay + ":" + minute;
        } else {
            time = "" + hourOfDay + ":" + minute;
        }
        text.setText(time);
    }
}
