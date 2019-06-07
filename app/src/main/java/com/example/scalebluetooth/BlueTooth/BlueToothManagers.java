package com.example.scalebluetooth.BlueTooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlueToothManagers {
    private BluetoothAdapter adapter;
    private Activity a;
    private DetectScale scale;
    private ArrayList<BluetoothDevice> devices;
    private BluetoothLeScanner scanner;

    public BlueToothManagers(Activity a){
        this.a = a;
        adapter = BluetoothAdapter.getDefaultAdapter();
        scale = new DetectScale(a);
        devices = new ArrayList<>();
    }


    public boolean getBTs(){
        return adapter.isEnabled();
    }

    public void Request(){
        if (!adapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            a.startActivityForResult(enableIntent, 0);
        }
    }

    public ArrayList<String> paireDevice(){
        ArrayList<String> list = new ArrayList<>();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for(BluetoothDevice d:devices){
            list.add(d.getName());
        }
        return list;
    }

    public void showLIst(TextView tv) {
        scale.dectet_Device(tv);
    }



}
