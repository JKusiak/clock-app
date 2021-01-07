package com.example.alarmclockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private EditText hourET;
    private TextView helloTV;
    Character helloChar = ' ';
    String helloString = " ";

    // Variables for connection
    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hourET = findViewById(R.id.hourET);
        hourET.setFocusable(false);

        helloTV = findViewById(R.id.tvMain);
        helloString = Character.toString(helloChar);
        helloTV.setText(helloString);

    }

    public void showTimePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment(hourET);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
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
}