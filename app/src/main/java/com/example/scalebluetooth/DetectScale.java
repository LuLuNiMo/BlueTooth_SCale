package com.example.scalebluetooth;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.excell_ble_api.Excell_BLE;

public class DetectScale implements Excell_BLE.ListenAPI {
    private Activity activity;


    public DetectScale(Activity a){
        this.activity = a;
        BLE = new Excell_BLE(a) ;
        BLE.setAPI(this);

        BLE.Setup_Scale("General");
        BLE.Start_Scan();

        mHandlerReadinfo = new Handler();
    }





    Excell_BLE BLE ;
    String TAG = "Excell_Example";
    private TextView Show_Weight ;
    private Button button ;
    String weight ;
    String uint ;
    boolean pos ;
    Handler mHandlerReadinfo=null;
    boolean MG_S=false,MN_S=false,BT_S=false,MZ_S=false,MT_S=false,RT_S=false,RP_S=false,PT_S=false,RB_S=false ;


    @Override
    public void OnScan_Result(BluetoothDevice bluetoothDevice) {

    }

    @Override
    public void OnWeight_Update(boolean b, String s, Short aShort, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, short i, String s1, short i1) {

    }

    @Override
    public void OnConn_State(int i) {

    }

    @Override
    public void OnGetTareValue(String s) {

    }

    @Override
    public void OnGetPreTareValue(String s) {

    }

    @Override
    public void OnGetVersionValue(String s) {

    }

    @Override
    public void OnGetBatteryValue(String s) {

    }

    @Override
    public void OnGetScaleFunction(boolean b, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, boolean b7, boolean b8) {

    }

    @Override
    public void OnGetErrorMsg(String s) {

    }
}
