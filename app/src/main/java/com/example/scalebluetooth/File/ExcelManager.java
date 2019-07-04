package com.example.scalebluetooth.File;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.example.scalebluetooth.DB.Item;

import java.io.File;
import java.util.ArrayList;

public class ExcelManager {
    private static String[] title = {"編號", "條碼", "重量", "時間"};
    private String fileName = "Excell_Barcode.xls";
    private ExcelFormat excelFormat;


    public ExcelManager()
    {
        excelFormat = new ExcelFormat();
    }

    public void ExportExcel(Activity a,ArrayList<Item> data){
        File file =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        excelFormat.initExcel(file.toString() + "/" + fileName, title);
        excelFormat.WriteEcel(getRecordData(data), file.toString() + "/" + fileName, a);
    }

    private ArrayList<ArrayList<String>> getRecordData(ArrayList<Item> data) {
         ArrayList<ArrayList<String>> sheets = new ArrayList<>();

         for(Item item:data){
             ArrayList<String> record = new ArrayList<>();
             record.add(String.valueOf(item.getId()));
             record.add(item.getBarcode());
             record.add(String.valueOf(item.getWeigth()));
             record.add(item.getTime());

             sheets.add(record);
         }
        return sheets;
    }

    public  boolean permission(Activity a){
        return ActivityCompat.checkSelfPermission(a, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    public  void  Requset_permission(Activity a){
        ActivityCompat.requestPermissions(a,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }






}
