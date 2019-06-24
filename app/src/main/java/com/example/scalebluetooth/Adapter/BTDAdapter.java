package com.example.scalebluetooth.Adapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.icu.text.UnicodeSetSpanner;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.DB.Device;

import java.util.ArrayList;

public class BTDAdapter extends BTbaseAdapter {
    protected BTRAdapter adapter;

    public BTDAdapter(Activity activity, Excell_BLE BLE) {
        super(activity, BLE);
        BLE.setAPI(this);

        show = false;
    }

    public void startSCane( BTRAdapter adapter) {
        this.adapter = adapter;
        BLE.Setup_Scale("General");
        BLE.Start_Scan();
    }

    @Override
    void btnMethod(int position) {
        BLE.Stop_Scan();
        BLE.Connect_Device(list.get(position).getDevice());

        devices = list.get(position);

        this.notifyDataSetChanged();

    }

    @Override
    void delete(int position) {
    }

    public ArrayList<Device> getLIst() {
        return list;
    }

    @Override
    public void OnScan_Result(BluetoothDevice bluetoothDevice) {
        add(bluetoothDevice);
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
                        Toast.makeText(activity, "連接失敗", Toast.LENGTH_SHORT).show();
                        Log.d("Scale State--", devices.getAddr() + "BLE Scale Disconnect");
                        break;
                    case 1:
                    case 2:
                        adapter.add(getDevices());
                        adapter.ChangeState(getDevices().getAddr());
                        Toast.makeText(activity, "連接成功", Toast.LENGTH_SHORT).show();
                        Log.d("Scale State--", devices.getAddr() + "BLE Scale is Connected");
                        break;
                }
            } catch (Exception e) {
                Log.e("Handler--Exception", e.getMessage());
            }
        }
    };

}
