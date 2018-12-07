package com.example.saurabhomer.cityprobe;

import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Datasets extends AppCompatActivity {
    BluetoothSocket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datasets);
        socket=Bluetooth_set.socket;

    }
}
