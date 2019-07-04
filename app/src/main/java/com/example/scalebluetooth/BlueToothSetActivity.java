package com.example.scalebluetooth;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.Adapter.BTDAdapter;
import com.example.scalebluetooth.Adapter.BTRAdapter;
import com.example.scalebluetooth.BlueTooth.AlterDiagram;
import com.example.scalebluetooth.BlueTooth.BlueToothManagers;

public class BlueToothSetActivity extends AppCompatActivity {
    private Excell_BLE BLE;
    private BTDAdapter adapterD;
    private BTRAdapter adapterR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_set);
        setTitle("藍芽設定");

        BLE = new Excell_BLE(this);

       adapterD = new BTDAdapter(this,BLE);
        adapterR = new BTRAdapter(this,BLE);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    protected void onStart() {
        super.onStart();

       ((ListView)findViewById(R.id.finbt)).setAdapter(adapterD);
        ((ListView)findViewById(R.id.storebt)).setAdapter(adapterR);

        if(!BlueToothManagers.isOPen(this)){
            new AlterDiagram(this).showDialog("GPS 開啟通知","需開啟定位才能設定藍芽裝置","馬上設定開啟","現在不要",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BlueToothManagers.openGPS(BlueToothSetActivity.this);
                }
            });
        }

        adapterD.startSCane(adapterR);

    }


    @Override
    public void onBackPressed() {
        Log.d("back to Main", "onBackPressed back to MainActivity");
        backToMain();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d("back to Main", "Actionbar back to MainActivity");
        if(item.getItemId() == android.R.id.home){
            backToMain();
        }
        return super.onOptionsItemSelected(item);
    }


    public void backToMain(){
        Intent intent;
        intent = new Intent();

        try{
            BLE.Disconnect_Device();
            intent.putExtra("device",  adapterR.getDev());
            startActivity(intent);

        }catch (Exception ex){
            ex.printStackTrace();
            intent = new Intent();
        }finally {
            intent.setClass(this,MainActivity.class);
            startActivity(intent);

            finish();
        }
    }


}
