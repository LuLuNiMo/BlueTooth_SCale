package com.example.scalebluetooth.DB;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class SQLite extends SQLiteOpenHelper {
    private static SQLite instance = null;

    private SQLite(Context c) {  //指定連結資料庫DB_NAME
        super(c, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CreateSQL);
            db.execSQL(CreateSQL2);
        } catch (SQLiteConstraintException ex) {
            ex.printStackTrace();
            Log.v("DB create fail.", Log.getStackTraceString(ex));
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.v("DB create fail.", Log.getStackTraceString(ex));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS  " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS  " + TABLE_NAME2);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.v("DB Upgrade fail.", Log.getStackTraceString(ex));
        }
        onCreate(db);
    }

    synchronized static public SQLite getInstance(Context c) {
        if (instance == null) {
            instance = new SQLite(c);
        }
        return instance;
    }

    public static int getNo(Activity a){
        return a.getSharedPreferences("preferences", MODE_PRIVATE).getInt("DBW_No", 0);
    }


    //條碼保存期限：3天
    public void AutoDelData(){
        ArrayList<Item> list = (ArrayList<Item>) select(null,null);
        java.util.Date date = new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        long today_day = Long.valueOf(sdFormat.format(date).substring(8, 10)) +
                Long.valueOf(sdFormat.format(date).substring(5, 7))*30;

        if(list.size() > 0 && list != null){
            for (Item item : list) {
                long this_day = Long.valueOf(item.getTime().substring(8, 10)) +
                        Long.valueOf(item.getTime().substring(5, 7))*30;
                if(today_day - this_day > 3){
                    delete(String.valueOf(item.getId()),COL_ID);
                    Log.i("Delete-AutoDATA","---SUCCESS");
                }
            }
        }
    }


    public long insert(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_b, item.getBarcode());
        values.put(COL_w, item.getWeigth());
        values.put(COL_t, item.getTime());

        return db.insert(TABLE_NAME, null, values);
    }


    public int delete(String str, String col) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = col + " = ?;";
        String[] whereArgs = {str};
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public int update(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ID, item.getId());
        values.put(COL_b, item.getBarcode());
        values.put(COL_w, item.getWeigth());
        values.put(COL_t, item.getTime());

        String whereClause = COL_ID + " = ?;";
        String[] whereArgs = {String.valueOf(item.getId())};
        Log.i("UPDATEDB","----------SUCCESS");
        return db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public List<Item> select(String select, String values){
        List<Item> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COL_ID, COL_b, COL_w, COL_t
        };
        Cursor cursor;


        if(select != null){
            select += " = ?;";
            String[] selectionArgs = new String[]{values};
            cursor = db.query(TABLE_NAME, columns, select, selectionArgs, null, null,
                    null);
        }else{
            cursor = db.query(TABLE_NAME, columns, null, null, null, null,
                    null);
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String b = cursor.getString(1);
            String w = cursor.getString(2);
            String t = cursor.getString(3);
            Item data = new Item(id, b, w, t );
            list.add(data);
        }
        cursor.close();
        return list;
    }

//----------------------------------------------------------------------------------------------


    public long insert(Device item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        if(item.getName().equals("")){
            item.setName("未命名裝置");
        }

        values.put(COL_n, item.getName());
        values.put(COL_addr, item.getAddr());

        return db.insert(TABLE_NAME2, null, values);
    }

    public void deleteR(String str, String col) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = col + " = ?;";
        String[] whereArgs = {str};
        db.delete(TABLE_NAME2, whereClause, whereArgs);
    }


    public List<Device> selectR(String select, String values){
        List<Device> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COL_ID, COL_n, COL_addr
        };
        Cursor cursor;


        if(select != null){
            select += " = ?;";
            String[] selectionArgs = new String[]{values};
            cursor = db.query(TABLE_NAME2, columns, select, selectionArgs, null, null,
                    null);
        }else{
            cursor = db.query(TABLE_NAME2, columns, null, null, null, null,
                    null);
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String n = cursor.getString(1);
            String addr = cursor.getString(2);

            list.add(new Device(n,addr,null,false));
        }
        cursor.close();
        return list;
    }


//----------------------------------------------------------------------------------------------


    private static final String DB_NAME = "KS_Scale_DB";


    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "BarCode";
    private static final String COL_ID = "ID";
    private static final String COL_b = "Barcode";
    private static final String COL_t = "Time";
    private static final String COL_w = "Weigth";

    private static final String TABLE_NAME2 = "BTRecord";
    private static final String COL_n = "Name";
    private static final String COL_addr = "Address";


    private static String CreateSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_b + " TEXT,"
            + COL_w + " TEXT,"
            + COL_t + " TEXT);";

    private static String CreateSQL2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + " ( "
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_n + " TEXT,"
            + COL_addr + " TEXT);";


    /*   2 tables

    BarCode table : Recording user input information
           ID : AUTOINCREMENT primary key
           Barcode : barcode 13
           Weight :  Protect weight
           time : Recode time( = input time)

    BTRecord table : Recording connect BlueDevice record
           ID : AUTOINCREMENT primary key
           Name : BlueDevice Name
           Address : BlueDevice MAC address


     */

}
