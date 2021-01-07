package com.example.alarmclockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    TimePicker timePicker;
    Button scheduleBtn;
    TextView alarmTextList;

    ArrayList<Alarm> alarmList = new ArrayList();


//    // Variables for connection
//    String address = null;
//    BluetoothAdapter myBluetooth = null;
//    BluetoothSocket btSocket = null;
//    private boolean isBtConnected = false;
//    //SPP UUID. Look for it
//    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.alarmTimePicker);
        scheduleBtn = findViewById(R.id.scheduleAlarmBtn);
        alarmTextList = findViewById(R.id.alarmList);

        scheduleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addAlarm();
                alarmTextList.setText(" ");

                ArrayList<String> allAlarms = new ArrayList();

                for (Alarm alarm : alarmList) {
                    allAlarms.add(alarm.getHour() + " : " + alarm.getMinute());
                }

                String allAlarmsAsText = allAlarms.toString();

                alarmTextList.setText(allAlarmsAsText);
                Toast alarmToast = Toast.makeText(MainActivity.this, "Alarm added", Toast.LENGTH_SHORT);
                alarmToast.show();


            }
        });
    }


    private void addAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                timePicker.getHour(),
                timePicker.getMinute(),
                false
        );

        alarmList.add(alarm);

    }

}