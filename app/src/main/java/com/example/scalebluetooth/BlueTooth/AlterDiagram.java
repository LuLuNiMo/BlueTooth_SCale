package com.example.scalebluetooth.BlueTooth;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.scalebluetooth.BuildConfig;
import com.example.scalebluetooth.MainActivity;
import com.example.scalebluetooth.R;

import java.io.File;
import java.sql.Statement;


public class AlterDiagram {
    private  AlertDialog.Builder builder;
    private  AlertDialog dialog;
    private Activity a;

    public AlterDiagram(Activity a){
        this.a = a;
        builder = new AlertDialog.Builder(a);

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
                                    new Intent(Intent.ACTION_SEND);
                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                                    "/" + "Excell_Barcode.xls");

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            if(Build.VERSION.SDK_INT >= 26){ //android 8.0
                                Uri contentUri = FileProvider.getUriForFile(a, BuildConfig.APPLICATION_ID + ".fileProvider", file);
                                intent.setDataAndType(contentUri, "application/octet-stream");
                                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                            }else{
                                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                                intent.setType(getMimeType(file.getAbsolutePath()));
                            }

                            a.startActivity(Intent.createChooser(intent, "分享至..."));

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


    private String getMimeType(String filePath) { //分享APP Type
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


    public void showDialog(String aTitle,String context,String t1,String t2,DialogInterface.OnClickListener blistener){
        builder.setTitle(aTitle);
        builder.setMessage(context);
        builder.setIcon(R.drawable.msg_icon);
        builder.setCancelable(false);

        builder.setPositiveButton(t1,blistener);
        builder.setNegativeButton(t2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(a, MainActivity.class);
                a.startActivity(intent);
                a.finish();
            }});
        dialog = builder.create();
        dialog.show();
    }







}
