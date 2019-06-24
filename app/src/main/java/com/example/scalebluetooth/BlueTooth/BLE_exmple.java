package com.example.scalebluetooth.BlueTooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.R;


public class BLE_exmple extends Activity implements Excell_BLE.ListenAPI {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button.setOnClickListener(button_listen);
        button.setText("Zero");
        //Must be setting
        BLE = new Excell_BLE(this) ;

        /*
         * Scale Model List :
         *    "CraneScale" (Model : FJ3 , FJ5 , GJW)
         *       State
         *          Support :  SignSymbol , Unit  ,  ZeroSymbol  ,  StableSymbol , NetSymbol  , BeepState , Scale_Off , Point ,  WeightDate , HighLow
         *          None Support :  OLSymbol ,  BatteryState(Only Low Power)
         *   "Scaleplatform" (Model : ExBLE-P)
         *      State
         *         Support : SignSymbol , Unit ,  BatteryState ,  ZeroSymbol  ,  StableSymbol , NetSymbol ,  OLSymbol , BeepState , Scale_Off , Point ,  WeightDate
         *         None Support : HighLow
         *   "General"  (Model : AM Serial ,  ALH Serial , AWH Serial , SA-P Serial , SAP-P Serial , FD Serial , FDP-P Serial , AP Serial , TWH Serial , SBH-P Serial , AZ Serial , HWH Serial , SN Serial , QW , GW etc..)
         *       State
         *          Support : SignSymbol , Unit  ,  StableSymbol , NetSymbol ,  OLSymbol ,  WeightDate
         *          None Support :   ZeroSymbol , BeepState , Scale_Off  , Point , HighLow
         *          Command Mode : BatteryState
         */
        BLE.Setup_Scale("General");
        BLE.Start_Scan();

        mHandlerReadinfo = new Handler();
    }

    /*
     * arg0 : Bluetooth  infromation
     *		(String)Name : arg0.getName()
     * 		(String)Address : arg0.getAddress()
     * 		(int)Bond State : arg0.getBondState()
     * 		(ParcelUUID[])UUID : arg0.getUuids()
     */
    @Override
    public void OnScan_Result(BluetoothDevice arg0) {
        //Scan result can show to listview
        Log.d(TAG , "Scan BLE Device  :  " + arg0.getName() + "  ,   Addr  :  " + arg0.getAddress());
        //if(arg0.getAddress().equals("F4:5E:AB:B8:D3:5B")){General
        //if(arg0.getAddress().equals("78:A5:04:70:58:73")){ Scaleplatform
        //if(arg0.getAddress().equals("78:A5:04:60:FF:DE")){//CraneScale
        if(arg0.getAddress().equals("78:A5:04:60:FF:DE")){
            BLE.Stop_Scan();
            BLE.Connect_Device(arg0);
        }
    }

    /*OnConn_State API ( BLE Connect  State)
     * arg0 :  0 : Disconnect
     *              1 : Connecting
     *              2 : Connected
     */
    @Override
    public void OnConn_State(int arg0) {
        if(arg0 == 0)
            Log.d(TAG, "BLE Scale Disconnect") ;
        else if(arg0 == 2)
            Log.d(TAG, "BLE Scale Connected");

    }

    final Runnable readinfo = new Runnable() {
        public void run() {
            if(pos == true)
                Show_Weight.setText( "+ " +  weight +"  "+  uint);		//arg10 + arg1
            else
                Show_Weight.setText( "- " +  weight +"  "+  uint);		//arg10 + arg1
        }
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
    public void OnWeight_Update(boolean arg0, String arg1, Short arg2, boolean arg3, boolean arg4, boolean arg5,
                                boolean arg6, boolean arg7, boolean arg8, short arg9, String arg10 , short arg11) {
        weight = arg10 ;
        uint  = arg1  ;
        pos = arg0 ;
        mHandlerReadinfo.postDelayed(readinfo,50);

        Log.d(TAG,  "sign : " + arg0 + " , unit : " +  arg1 +" , Battery : " +  arg2 +" , Zero : " + arg3 +" , stable : " + arg4 +" , Net : " + arg5 +" , OL : " + arg6 +" , Buzzer : " + arg7 +"  , PowerOff : " + arg8  +" , point : " + arg9 +" , Weight : " + arg10  +" , HighLow : " + arg11) ;
    }

    /*
     *  Get Tare Value form scale after using  GetTare_API
     *  General and Platform Only
     */
    @Override
    public void OnGetTareValue(String arg0) {
        Log.d(TAG, "Tare Value  : " + arg0 + "\n") ;
    }

    /*
     *  Add by Version V1.2
     *  Get PreTare Value form scale after using  GetPreTare_API
     *  General and Platform Only
     */
    @Override
    public void OnGetPreTareValue(String arg0) {
        Log.d(TAG, "PreTare Value  : " + arg0 + "\n") ;
    }

    @Override
    public void OnGetVersionValue(String arg0) {
        Log.d(TAG, "Version Value  : " + arg0 + "\n") ;

    }

    /*
     *  Add by Version V1.2
     *  Get Battery Value form scale after using  GetBattery_API
     *  General and Platform Only
     */
    @Override
    public void OnGetBatteryValue(String arg0) {
        Log.d(TAG, "Battery Value  : " + arg0 + "\n") ;

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
    public void OnGetScaleFunction(boolean arg0 , boolean arg1 , boolean arg2 , boolean arg3 , boolean arg4 , boolean arg5 , boolean arg6 , boolean arg7 , boolean arg8) {
        //whem using in old organic species  (only support MZ,MT) , Enter this command to reply the error message(E3RC)
        MG_S = arg0;
        MN_S = arg1;
        BT_S = arg2;
        MZ_S = arg3;
        MT_S = arg4;
        RT_S = arg5;
        RP_S = arg6;
        PT_S = arg7;
        RB_S = arg8;
        Log.d(TAG, "OnGetScaleFunction : MG: " + arg0 + ", MN: " + arg1 + ", BT: " + arg2 + ", MZ: " + arg3 + ", MT: " + arg4 + ", RT: " + arg5 + ", RP: " + arg6 + ", PT: " + arg7 + ", RB: " + arg8);
    }

    /*
     * Add by Version V1.2
     * OnGetErrorMsg API(Function Error Message CallBack)
     * State : (NSString*)Value : Error Message
     * trigger form OnGetErrorMsg()
     */
    @Override
    public void OnGetErrorMsg(String Value)
    {
        //whem using in old organic species  (only support MZ,MT) , Enter this command to reply the error message(E3RC)
        Log.d(TAG,"OnGetErrorMsg : " + Value + "\n") ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BLE.Disconnect_Device();
    }

    private Button.OnClickListener button_listen = new Button.OnClickListener() {
        public void onClick(View v) {

            BLE.SetZero();

			/*
			//example
			BLE.Disconnect_Device();
			BLE.Get_Function_API();               //whem using in old organic species  (only support MZ,MT) , Enter this command to reply the error message(E3RC)

			if(MG_S)
				BLE.SetGrossDisplay();
			if(MN_S)
				BLE.SetNetDisplay() ;
			if(MZ_S)
				BLE.SetZero();
			if(MT_S)
				BLE.SetTare();
			if(RT_S)
				BLE.GetTare_API();
			if(BT_S)
				BLE.SetBLEFormat();
			if(PT_S)
				BLE.SetPreTare("1000");          //ex.1.000kg Max.999999
			if(RP_S)
				BLE.GetPreTare_API();
			if(RB_S)
				BLE.GetBattery_API();

			BLE.SetHold();                     //Crane Only
			char[] wbuf = new char[8];
			wbuf[0] = 'E';
			wbuf[1] = 'X';
			wbuf[2] = 'C';
			wbuf[3] = 'E';
			wbuf[4] = 'L';
			wbuf[5] = 'L';
			wbuf[6] = 'G';
			wbuf[7] = 'W';
			BLE.SetBLE_DeviceName(wbuf); ;
			BLE.GetScaleVersion_API();

			//Set High  range value . example 0003.00
			wbuf = new char[6];
			wbuf[0] =0x30 ;
			wbuf[1] = 0x30 ;
			wbuf[2] = 0x30 ;
			wbuf[3] = 0x33 ;
			wbuf[4] = 0x30 ;
			wbuf[5] = 0x30 ;
			//unit : 00 -05 ,  HL : High true Low false , Value
			BLE.SetHighLow(0  , true  , wbuf) ;
			//delay
			SystemClock.sleep(500);
			//Set Low  range value . example 0002.00
			wbuf[0] = 0x30 ;
			wbuf[1] =  0x30 ;
			wbuf[2] =  0x30 ;
			wbuf[3] =  0x32 ;
			wbuf[4] =  0x30 ;
			wbuf[5] =  0x30 ;
			//unit : 00 ,  HL : High true Low false , Value
			BLE.SetHighLow(0  , false  , wbuf) ;
			*/
        }
    };
}






