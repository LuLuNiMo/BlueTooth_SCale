package com.example.scalebluetooth.BlueTooth;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.AlterDiagram;

import java.util.HashMap;

import static java.lang.Thread.sleep;

public class DetectScale implements Excell_BLE.ListenAPI {
    private Activity activity;
    private Excell_BLE BLE;
    private AlterDiagram diagram;

    private TextView n, w,s;
    private HashMap<String, BluetoothDevice> devices;
    boolean MG_S = false, MN_S = false, BT_S = false, MZ_S = false, MT_S = false, RT_S = false, RP_S = false, PT_S = false, RB_S = false;

    public DetectScale(Activity a) {
        this.activity = a;
        BLE = new Excell_BLE(a);
        BLE.setAPI(this);
        devices = new HashMap();
    }


    public void dectet_Device(TextView n, TextView w,TextView s) {
        diagram = new AlterDiagram(activity);
        BLE.Setup_Scale("General");
        BLE.Start_Scan();
        this.n = n;
        this.w = w;
        this.s = s;

        handler.postDelayed(showDargramThread,1000);
    }

    //連線該裝置
    @Override
    public void OnScan_Result(BluetoothDevice arg0) {
        devices.put(arg0.getAddress(), arg0);
    }


    //取得相關資訊
    @Override
    public void OnWeight_Update(boolean arg0, String arg1, Short arg2, boolean arg3,
                                boolean arg4, boolean arg5, boolean arg6, boolean arg7, boolean arg8, short arg9, String arg10, short arg11) {
        if(arg0){
            w.setText(arg10 + " " +arg1);
        }else{
            w.setText("-" +arg10 + " " +arg1);
        }
    }

    Runnable showDargramThread = new Runnable() {
        public void run() {
            handler.postDelayed(this, 1000);
            try {
                String str = "";
                for (String id : devices.keySet()) {
                    str += id + ";";
                }

                if (!str.equals("")) {
                    String[] strs = str.split(";");
                    diagram.showDialog("藍芽裝置",BLE, n,devices);
                }
            } catch (Exception e) {
                Log.e("Handler error", "Handler error  " + e.getMessage());
            }
        }
    };


    //裝置連線狀態
    @Override
    public void OnConn_State(int i) {
        handler.sendEmptyMessage(i);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what){
                    case 0:
                        s.setText("已離線");
                        s.setTextColor(Color.RED);
                        n.setText("連線裝置");
                        Log.i("Scale State--", "BLE Scale Disconnect");
                        break;
                    case 1:
                        s.setText("連線中");
                        s.setTextColor(Color.GREEN);
                        Log.i("Scale State--", "BLE Scale is Connecting");
                        break;
                    case 2:
                        s.setText("已連線");
                        s.setTextColor(Color.BLUE);
                        Log.i("Scale State--", "BLE Scale is Connected");
                        break;
                }
            } catch (Exception e) {
                Log.e("Handler--Exception", e.getMessage());
            }}
    };


    /*
     * 	(boolean) arg0 : Weight sign symbol , + are false , - are  true .
     *     (String)     arg1 : Weight Unit .
     *     (Short)      arg2 : Battery State form 0 -100 .
     *     (boolean) arg3 : Zero Flag symbol , Zero are true , None Zero are false .
     *     (boolean) arg4 : Weight Stable Flag symbol , Stable are  true , None Stable are  false .
     *     (boolean) arg5 : Weight NET Flag symbol , NET are  true , Gross  are  false .
     *     (boolean) arg6 : Weight Over Load Flag symbol ,  Over Load  are  true , None  Over Load are  false .
     *     (boolean) arg7 : Buzzer ON symbol , Buzzer ON  are  true .
     *     (boolean) arg8 : Scale Power OFF symbol , If scale power OFF  are  true .
     *     (Short)      arg9 : Weight Point number .
     *     (String)     arg10 : Scale weight display.
     *     (short)     arg11 : High Low OK instruct. 0: none , 1: High state , 2: OK state  , 3: Low state
     */


    @Override
    public void OnGetTareValue(String s) {
        Log.d("GetTareValue--", "TareValue Value  : " + s + "\n");
    }

    @Override
    public void OnGetPreTareValue(String s) {
        Log.d("PreTareValue--", "PreTare Value  : " + s + "\n");
    }

    @Override
    public void OnGetVersionValue(String s) {
        Log.d("VersionValue--", "Version Value  : " + s + "\n");
    }

    @Override
    public void OnGetBatteryValue(String s) {
        Log.d("tBatteryValue--", "tBattery Value  : " + s + "\n");
    }

    public void set() {
        BLE.SetZero();

        BLE.Disconnect_Device();
        BLE.Get_Function_API();

        if (MG_S)
            BLE.SetGrossDisplay();
        if (MN_S)
            BLE.SetNetDisplay();
        if (MZ_S)
            BLE.SetZero();
        if (MT_S)
            BLE.SetTare();
        if (RT_S)
            BLE.GetTare_API();
        if (BT_S)
            BLE.SetBLEFormat();
        if (PT_S)
            BLE.SetPreTare("1000");
        if (RP_S)
            BLE.GetPreTare_API();
        if (RB_S)
            BLE.GetBattery_API();

        BLE.SetHold();
        char[] wbuf = new char[8];
        wbuf[0] = 'E';
        wbuf[1] = 'X';
        wbuf[2] = 'C';
        wbuf[3] = 'E';
        wbuf[4] = 'L';
        wbuf[5] = 'L';
        wbuf[6] = 'G';
        wbuf[7] = 'W';
        BLE.SetBLE_DeviceName(wbuf);
        ;
        BLE.GetScaleVersion_API();

        //Set High  range value . example 0003.00
        wbuf = new char[6];
        wbuf[0] = 0x30;
        wbuf[1] = 0x30;
        wbuf[2] = 0x30;
        wbuf[3] = 0x33;
        wbuf[4] = 0x30;
        wbuf[5] = 0x30;
        //unit : 00 -05 ,  HL : High true Low false , Value
        BLE.SetHighLow(0, true, wbuf);
        //delay
        SystemClock.sleep(500);
        //Set Low  range value . example 0002.00
        wbuf[0] = 0x30;
        wbuf[1] = 0x30;
        wbuf[2] = 0x30;
        wbuf[3] = 0x32;
        wbuf[4] = 0x30;
        wbuf[5] = 0x30;
        //unit : 00 ,  HL : High true Low false , Value
        BLE.SetHighLow(0, false, wbuf);
    }



    /*
	 *  Add by Version V1.2
	 *  OnGetScaleFunction API(Scale function CallBack)
	 *  State : (Boolean)MG : True = Support MG(switch to display Gross) Command , false = not support
	 *  (Boolean)MN : True = Support MN(switch to display Net) Command , false = not support
	 *  (Boolean)BT : True = Support BT(set BLE Format on Scale) Command , false = not support
	 *  (Boolean)MZ : True = Support MZ(Zero) Command , false = not support
	 *  (Boolean)MT : True = Support MT(Tare) Command , false = not support
	 *  (Boolean)RT : True = Support RT(Read Tare Value) Command , false = not support
	 *  (Boolean)RP : True = Support RP(Read PreTare Value) Command , false = not support
	 *  (Boolean)PT : True = Support PT(Set Pretare Value) Command , false = not support
	 *  (Boolean)RB : True = Support RB(Read Battery Value) Command , false = not support
 	   trigger form Get_Function_API() That Command not support old Protocol
	 */

    @Override
    public void OnGetScaleFunction(boolean arg0, boolean arg1, boolean arg2, boolean arg3, boolean arg4, boolean arg5, boolean arg6,
                                   boolean arg7, boolean arg8) {
        MG_S = arg0;
        MN_S = arg1;
        BT_S = arg2;
        MZ_S = arg3;
        MT_S = arg4;
        RT_S = arg5;
        RP_S = arg6;
        PT_S = arg7;
        RB_S = arg8;
    }

    @Override
    public void OnGetErrorMsg(String s) {
        Log.e("ErrorMsg--", "OnGetErrorMsg : " + s + "\n");
    }
}
