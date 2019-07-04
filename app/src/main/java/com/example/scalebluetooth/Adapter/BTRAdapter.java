package com.example.scalebluetooth.Adapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.DB.Device;
import com.example.scalebluetooth.DB.SQLite;
import java.util.ArrayList;


public class BTRAdapter extends BTbaseAdapter {

    public BTRAdapter(Activity activity, Excell_BLE BLE) {
        super(activity, BLE);
        list = (ArrayList<Device>) SQLite.getInstance(activity).selectR(null, null);

        show = true;

    }

    public void add(Device d) {
        boolean check = true;

        for (Device device : list) {
            if (d.getAddr().equals(device.getAddr())) {
                check = false;
            }
        }

        if(check){list.add(d);
            SQLite.getInstance(activity).insert(d);
        }

        this.notifyDataSetChanged();
    }

    @Override
    void btnMethod(int position) {
        BLE.Stop_Scan();

        BLE.setAPI(this);
        BLE.Start_Scan();

        devices = list.get(position);

    }

    @Override
    void delete(int position) {
        SQLite.getInstance(activity).deleteR(list.get(position).getAddr(),"Address");
        list.remove(position);

        this.notifyDataSetChanged();
    }


    @Override
    public void OnScan_Result(BluetoothDevice arg0) {
        if (devices.getAddr().equals(arg0.getAddress())) {
            ChangeState(arg0.getAddress());

            BLE.Stop_Scan();
            BLE.Connect_Device(arg0);
        }
    }

    public void ChangeState(String address) {
        for (Device d : list) {
            if (!d.getAddr().equals(address)) {
                d.setState(false);
            } else {
                d.setState(true);
            }
        }
        this.notifyDataSetChanged();
    }


    @Override
    public void OnConn_State(int i) {
        handler.sendEmptyMessage(i);
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        Log.d("Scale State--", devices.getAddr() + "BLE Scale Disconnect");
                        ChangeState("");
                        break;
                    case 1:
                    case 2:
                        Log.d("Scale State--", devices.getAddr() + "BLE Scale is Connected");
                        break;
                }
            } catch (Exception e) {
                Log.e("HandlerBTR--Exception", e.getMessage());
            }
        }
    };


    public Device getDev() {
        for (Device d : list) {
            if (d.isState()) {
                return d;
            }
        }
        return new Device();
    }


}
