package com.example.alarmclockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    TimePicker timePicker;
    Button scheduleBtn;

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
        constraintLayout = findViewById(R.id.mainLayout);

        scheduleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addAlarm();

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


        TextView alarmTextView = new TextView(MainActivity.this);
        alarmTextView.setId(alarm.getAlarmId());
        alarmTextView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT));
        alarmTextView.setTextSize(25);
        constraintLayout.addView(alarmTextView);

        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(constraintLayout);
        constraints.connect(alarm.getAlarmId(), ConstraintSet.TOP, R.id.scheduleAlarmBtn, ConstraintSet.BOTTOM,0);
        constraints.connect(alarm.getAlarmId(), ConstraintSet.RIGHT, R.id.mainLayout, ConstraintSet.RIGHT,0);
        constraints.connect(alarm.getAlarmId(), ConstraintSet.LEFT, R.id.mainLayout, ConstraintSet.LEFT,0);
        constraints.applyTo(constraintLayout);

        String alarmToDisplay = "Alarm at: " + alarm.getHour() + " : " + alarm.getMinute();
        alarmTextView.setText(alarmToDisplay);
    }

}