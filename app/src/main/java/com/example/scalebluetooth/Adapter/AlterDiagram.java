package com.example.scalebluetooth.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.R;

import java.util.HashMap;
import java.util.logging.Handler;

public class AlterDiagram {
    private AlertDialog alert = null;


    public void showDialog(Context c, String aTitle, final String[] items, final Excell_BLE ble,final HashMap<String, BluetoothDevice> map) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(aTitle);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ble.Stop_Scan();
                ble.Connect_Device(map.get(items[which]));
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
