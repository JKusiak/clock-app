package com.example.alarmclockapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Handler;


public class ClockActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    TimePicker timePicker;
    Button scheduleAlarmButton;
    Button receiveDataButton;
    String alarmToArduino;
    TextView timeDataArduino;
    TextView weatherDataArduino;

    BluetoothAdapter bluetoothAdapter = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String deviceAddress = null;

    boolean stopThread;
    byte buffer[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        timePicker = findViewById(R.id.alarmTimePicker);
        scheduleAlarmButton = findViewById(R.id.scheduleAlarmBtn);
        constraintLayout = findViewById(R.id.clockLayout);

        receiveDataButton = findViewById(R.id.receiveDataBtn);

        timeDataArduino = findViewById(R.id.timeDataArduino);
        weatherDataArduino = findViewById(R.id.weatherDataArduino);

        Intent msgIntent = getIntent();
        deviceAddress = msgIntent.getStringExtra(DeviceListActivity.EXTRA_ADDRESS);

        new ConnectBT().execute(); //Call the class to connect

        scheduleAlarmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addAlarm();
                sendAlarmToArduino();

                Toast alarmToast = Toast.makeText(ClockActivity.this, "Alarm added", Toast.LENGTH_SHORT);
                alarmToast.show();
            }
        });


        receiveDataButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                beginListenForData();

                Toast alarmToast = Toast.makeText(ClockActivity.this, "Received data", Toast.LENGTH_SHORT);
                alarmToast.show();
            }
        });


    }


    private void sendAlarmToArduino() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(alarmToArduino.getBytes());
            } catch (IOException e) {
                Toast alarmToast = Toast.makeText(ClockActivity.this,
                        "Didn't send information but was connected", Toast.LENGTH_SHORT);
                alarmToast.show();
            }
        }
    }



    private void addAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                timePicker.getHour(),
                timePicker.getMinute(),
                false
        );


        TextView alarmTextView = new TextView(ClockActivity.this);
        alarmTextView.setId(alarm.getAlarmId());
        alarmTextView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT));
        alarmTextView.setTextSize(30);
        constraintLayout.addView(alarmTextView);

        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(constraintLayout);
        constraints.connect(alarm.getAlarmId(), ConstraintSet.TOP, R.id.scheduleAlarmBtn, ConstraintSet.BOTTOM, 0);
        constraints.connect(alarm.getAlarmId(), ConstraintSet.BOTTOM, R.id.clockLayout, ConstraintSet.BOTTOM, 550);
        constraints.connect(alarm.getAlarmId(), ConstraintSet.LEFT, R.id.clockLayout, ConstraintSet.LEFT, 220);
        constraints.connect(alarm.getAlarmId(), ConstraintSet.RIGHT, R.id.clockLayout, ConstraintSet.RIGHT, 0);
        constraints.applyTo(constraintLayout);

        String alarmToDisplay;
        if (alarm.getHour() < 10 & alarm.getMinute() < 10) {
            alarmToDisplay = "0" + alarm.getHour() + ":0" + alarm.getMinute();
        } else if (alarm.getHour() > 10 & alarm.getMinute() < 10) {
            alarmToDisplay = alarm.getHour() + ":0" + alarm.getMinute();
        } else if (alarm.getHour() < 10 & alarm.getMinute() > 10) {
            alarmToDisplay = "0" + alarm.getHour() + ":" + alarm.getMinute();
        } else {
            alarmToDisplay = alarm.getHour() + ":" + alarm.getMinute();
        }

        alarmToArduino = alarm.getHour() + ":" + alarm.getMinute();

        alarmTextView.setText("Alarm set at " + alarmToDisplay);
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            ;
        }

        @Override
        //while the progress dialog is shown, the connection is done in background
        protected Void doInBackground(Void... devices)
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice myDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
                    // create a RFCOMM (SPP) connection
                    btSocket = myDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
                Toast alarmToast = Toast.makeText(ClockActivity.this, "Failed to connect", Toast.LENGTH_SHORT);
                alarmToast.show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
        }
    }

    void beginListenForData() {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopThread) {
                    try {
                        int byteCount = btSocket.getInputStream().available();
                        byteCount += byteCount;

                        if (byteCount >= 56){
                            byte[] rawBytes = new byte[byteCount];
                            byteCount = 0;
                            btSocket.getInputStream().read(rawBytes);
                            final String arduinoData = new String(rawBytes, "UTF-8");
                            Pattern p = Pattern.compile("(?<=a)(\\d{2}:\\d{2}:\\d{2})([ -]\\d{2})(\\d{2})(?=a)");
                            Matcher m = p.matcher(arduinoData);

                            while (m.find()) {
                                String time = m.group(1);
                                String temperature = m.group(2);
                                String humidity = m.group(3);

                                handler.post(new Runnable() {
                                    public void run() {
                                        timeDataArduino.setText(time);
                                        weatherDataArduino.setText(temperature + " °C" + "     " + humidity + " %");
                                    }
                                });
                            }
                        }
                    } catch (IOException ex) {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }
}
