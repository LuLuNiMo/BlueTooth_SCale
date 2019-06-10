package com.example.scalebluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.Adapter.WiFiAdapter;
import com.example.scalebluetooth.DB.Item;
import com.example.scalebluetooth.R;

import java.util.HashMap;
import java.util.logging.Handler;

public class AlterDiagram {
    private  AlertDialog.Builder builder;
    private  AlertDialog dialog;
    private WiFiAdapter adapter;
    private Activity a;

    public AlterDiagram(Activity a){
        this.a = a;
        builder = new AlertDialog.Builder(a);

    }

    public void showDialog(String aTitle,
                           final Excell_BLE ble
            ,TextView tv, HashMap<String,BluetoothDevice> map){

        if (dialog == null) {
            builder.setTitle(aTitle);
            View v =  LayoutInflater.from(a).inflate(R.layout.listview2, null);
            adapter = new WiFiAdapter(a,ble,tv);
            ((ListView) v.findViewById(R.id.listview)).setAdapter(adapter);
            builder.setView(v);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ble.Stop_Scan();
                }
            });
            dialog = builder.create();
            dialog.show();
            adapter.setD(dialog);

        }else{
            adapter.update(map);
        }
    }



}
