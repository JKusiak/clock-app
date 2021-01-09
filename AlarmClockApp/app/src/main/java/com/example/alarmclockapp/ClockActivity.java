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
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Handler;

import static java.lang.Thread.sleep;

public class ClockActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    TimePicker timePicker;
    Button scheduleAlarmButton;
    Button stopAlarmButton;
    Button receiveDataButton;
    String alarmToArduino;
    TextView errorMsgField;
    TextView timeDataArduino;
    TextView weatherDataArduino;

    BluetoothAdapter myBluetooth = null;
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
        stopAlarmButton = findViewById(R.id.stopAlarmBtn);
        receiveDataButton = findViewById(R.id.receiveDataBtn);
        errorMsgField = findViewById(R.id.errorMsg);
        timeDataArduino = findViewById(R.id.timeDataArduino);
        weatherDataArduino = findViewById(R.id.weatherDataArduino);


        Intent msgIntent = getIntent();
        deviceAddress = msgIntent.getStringExtra(DeviceListActivity.EXTRA_ADDRESS);

        new ConnectBT().execute(); //Call the class to connect


        stopAlarmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendStopAlarmToArduino();

                Toast alarmToast = Toast.makeText(ClockActivity.this, "Alarm stopped", Toast.LENGTH_SHORT);
                alarmToast.show();
            }
        });


        scheduleAlarmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addAlarm();
                sendSetAlarmToArduino();

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


    private void sendSetAlarmToArduino() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("1".getBytes());
            } catch (IOException e) {
                errorMsgField.setText("Didn't send information but was connected");
            }
        }
    }


    private void sendStopAlarmToArduino() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("0".getBytes());
            } catch (IOException e) {
                errorMsgField.setText("Didn't send information but was connected");
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

//        alarmList.add(alarm);


        TextView alarmTextView = new TextView(ClockActivity.this);
        alarmTextView.setId(alarm.getAlarmId());
        alarmTextView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT));
        alarmTextView.setTextSize(25);
        constraintLayout.addView(alarmTextView);

        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(constraintLayout);
        constraints.connect(alarm.getAlarmId(), ConstraintSet.TOP, R.id.scheduleAlarmBtn, ConstraintSet.BOTTOM, 50);
        constraints.connect(alarm.getAlarmId(), ConstraintSet.START, R.id.clockLayout, ConstraintSet.START, 250);
        constraints.connect(alarm.getAlarmId(), ConstraintSet.END, R.id.clockLayout, ConstraintSet.END, 0);
        constraints.applyTo(constraintLayout);

        String alarmToDisplay;
        if (alarm.getHour() < 10 & alarm.getMinute() < 10) {
            alarmToDisplay = "0" + alarm.getHour() + ":0" + alarm.getMinute();
        } else if (alarm.getHour() > 10 & alarm.getMinute() < 10) {
            alarmToDisplay = "" + alarm.getHour() + ":0" + alarm.getMinute();
        } else if (alarm.getHour() < 10 & alarm.getMinute() > 10) {
            alarmToDisplay = "0" + alarm.getHour() + ":" + alarm.getMinute();
        } else {
            alarmToDisplay = "" + alarm.getHour() + ":" + alarm.getMinute();
        }

        alarmToArduino = alarmToDisplay;

        alarmTextView.setText("Alarm at: " + alarmToDisplay);
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            ;
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(deviceAddress);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                errorMsgField.setText("Failed to connect on clock activity level");
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
                        //int byteCount = inputStream.available();
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
