package com.example.alarmclockapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter myBluetooth;
    BluetoothSocket btSocket;
    private boolean isBtConnected = false;
    String address;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    char mainText = ' ';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (btSocket == null || !isBtConnected) {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView output = findViewById(R.id.tvMain);

        try {
            mainText = (char) btSocket.getInputStream().read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        output.setText(mainText);

        Button sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write('1');
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        Button receiveBtn = findViewById(R.id.recieveBtn);
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket!=null)
                {
                    try
                    {
                        mainText = (char) btSocket.getInputStream().read();
                        output.setText(mainText);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}