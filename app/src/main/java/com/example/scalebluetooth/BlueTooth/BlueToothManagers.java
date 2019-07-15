package com.example.scalebluetooth.BlueTooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.DB.Device;
import com.example.scalebluetooth.DB.SQLite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class BlueToothManagers implements Excell_BLE.ListenAPI {
    private BluetoothAdapter adapter;
    private Activity a;
    private TextView name, state, w;
    private Excell_BLE BLE;
    private Device device;
    private ArrayList<Device> list;


    public BlueToothManagers(Activity a) {
        this.a = a;
        adapter = BluetoothAdapter.getDefaultAdapter();
        BLE = new Excell_BLE(a);
        BLE.setAPI(this);

    }


    public boolean getBTs() {
        return adapter.isEnabled();
    }

    public boolean getLocalR() { //定位權限 (Android 6.0 以上使用藍芽功能(搜尋裝置)時需有此權限)
        if (Build.VERSION.SDK_INT >= 23) { //API
            return ((ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    && (ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            );
        } else {
            return true;
        }
    }

    public static final void openGPS(Activity a) {
       final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
       a.startActivity(intent);
    }


    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通過GPS衛星定位，定位級別可以精確到街（通過24顆衛星定位，在室外和空曠的地方定位准確、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通過WLAN或移動網絡(3G/2G)確定的位置（也稱作AGPS，輔助GPS定位。主要用於在室內或遮蓋物（建築群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }


    public void Request() { //要求權限(定位+藍芽存取)
        if (!adapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            a.startActivityForResult(enableIntent, 0);
        } else if (!getLocalR()) {
            ActivityCompat.requestPermissions(
                    a,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    //顯示先前的裝置
    public ArrayList<String> paireDevice() {
        ArrayList<String> list = new ArrayList<>();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice d : devices) {
            list.add(d.getName());
        }
        return list;
    }


    public void ConnectDevices(TextView name, TextView state, TextView w, Device d) {
        this.name = name;
        this.state = state;
        this.w = w;
        this.device = d;


        list = (ArrayList<Device>) SQLite.getInstance(a).selectR(null, null);
        BLE.Setup_Scale("General");
        BLE.Start_Scan();
    }


    @Override
    public void OnScan_Result(BluetoothDevice arg0) {
        state.setText("連接中");
        state.setTextColor(Color.DKGRAY);
        boolean check =true;


        try{
           if(device != null){
               if (!device.getAddr().equals("")) { //手動優先選擇
                   if (device.getAddr().equals(arg0.getAddress())) {
                       BLE.Stop_Scan();
                       BLE.Connect_Device(arg0);
                       device.setDevice(arg0);
                       check = false;
                   }
           }

            } //自動選擇
              if(check){
                  for (Device d : list) {
                      if (d.getAddr().equals(arg0.getAddress())) {
                          device = new Device(arg0.getName(), arg0.getAddress(), arg0, true);
                          BLE.Stop_Scan();
                          BLE.Connect_Device(arg0);
                      }
                  }
              }


        }catch (Exception ex){
            ex.printStackTrace();
            Log.e("OnScan_Result_ERROR",ex.getMessage());
        }


    }

    @Override
    public void OnWeight_Update(boolean arg0, String arg1, Short arg2, boolean arg3, boolean arg4, boolean arg5,
                                boolean arg6, boolean arg7, boolean arg8, short arg9, String arg10, short arg11) {
        Message m = new Message();
        m.obj = new String[]{String.valueOf(arg0), arg10, arg1};
        handler2.sendMessage(m);
    }

    @Override
    public void OnConn_State(int i) {
        handler.sendEmptyMessage(i);
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                    switch (msg.what) {
                        case 1:
                        case 2:
                            if (device.getName().equals("")) {
                                name.setText("未命名裝置");
                            } else {
                                name.setText(device.getName());
                            }
                            state.setText("已連接");
                            state.setTextColor(Color.BLUE);
                            Log.e("Scale State--", "BLE Scale is Connected");
                            break;
                        case 0:
                        default:
                            Log.e("Scale State--", "BLE Scale Disconnect");
                            state.setText("連接失敗");
                            w.setText("0.00 kg");
                            state.setTextColor(Color.RED);
                            break;
                    }
            } catch (Exception e) {
                Log.e("Handler1--Exception", e.getMessage());
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            try {
                String[] strs = (String[]) msg.obj;
                if (Boolean.valueOf(strs[0])) {
                    w.setText(strs[1] + " " + strs[2]);
                } else {
                    w.setText("-" + strs[1] + " " + strs[2]);
                }
            } catch (Exception e) {
                Log.e("Handler2----Exception", e.getMessage());
            }
        }
    };


    public void close() {
        try {
            BLE.Disconnect_Device();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void OnGetTareValue(String s) {
        Log.d("Tare Value  : ", s + "\n");
    }

    @Override
    public void OnGetPreTareValue(String s) {
        Log.d("Tare PreValue  : ", s + "\n");
    }

    @Override
    public void OnGetVersionValue(String s) {
        Log.d("Version Value  : ", s + "\n");
    }

    @Override
    public void OnGetBatteryValue(String arg0) {
        Log.d("Battery Value  : ", arg0 + "\n");
    }

    @Override
    public void OnGetScaleFunction(boolean b, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, boolean b7, boolean b8) {
    }

    @Override
    public void OnGetErrorMsg(String Value) {
        Log.d("OnGetErrorMsg : ", Value + "\n");
    }
}
