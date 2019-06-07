package com.example.scalebluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class BlueToothManager {
    private BluetoothAdapter adapter;
    private Activity a;
    private HashMap<String ,BluetoothDevice> map;

    public BlueToothManager(Activity a){
        this.a = a;
        adapter = BluetoothAdapter.getDefaultAdapter();
        map = new HashMap<>();
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


    public void showLIst(){
        Set<BluetoothDevice> list = adapter.getBondedDevices();
        for(BluetoothDevice d:list){
            Log.e("-------",d.getName());
        }
    }















}
