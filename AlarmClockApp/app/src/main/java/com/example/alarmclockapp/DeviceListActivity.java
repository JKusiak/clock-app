package com.example.alarmclockapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {
    Button showPairedDevicesButton;
    ListView deviceList;
    BluetoothAdapter bluetoothAdapter = null;
    Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        showPairedDevicesButton = findViewById(R.id.showPairedDevicesBtn);
        deviceList = (ListView) findViewById(R.id.devicesListView);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null)
        {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            finish();
        }
        else if(!bluetoothAdapter.isEnabled())
        {
            // if bluetooth is off, ask user to switch it on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }


        showPairedDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevicesList();
            }
        });
    }

    private void pairedDevicesList() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList devicesList = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                // add to list device name and its MAC address
                devicesList.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, devicesList);
        deviceList.setAdapter(adapter);
        // call this when device from list is clicked
        deviceList.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // get device MAC address - last 17 characters
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent i = new Intent(DeviceListActivity.this, ClockActivity.class);

            // send address to clock activity and move to it
            i.putExtra(EXTRA_ADDRESS, address);
            startActivity(i);
        }
    };
}
