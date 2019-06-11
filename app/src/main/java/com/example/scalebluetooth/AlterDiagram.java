package com.example.scalebluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.Adapter.WiFiAdapter;
import com.example.scalebluetooth.DB.Item;
import com.example.scalebluetooth.R;

import java.io.File;
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

    //https://blog.csdn.net/xiaoyu_93/article/details/76921562
    public void showDialog(String aTitle, final String[] strs){
            builder.setTitle(aTitle);
            builder.setItems(strs, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            Toast.makeText(a, "匯出EXCEL成功", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Intent intent =
                                    new Intent(android.content.Intent.ACTION_SEND);
                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                                    "/" + "英展磅秤條碼資料.xls");
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setType(getMimeType(file.getAbsolutePath()));

                            a.startActivity(Intent.createChooser(intent, "分享至...."));
                            break;
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
            }});
            dialog = builder.create();
            dialog.show();
        }


    private String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }


}
