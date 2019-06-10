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

import java.util.ArrayList;
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



    private static final String DB_NAME = "KS_Scale_DB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "BarCore";
    private static final String COL_ID = "ID";
    private static final String COL_b = "Barcode";
    private static final String COL_t = "Time";
    private static final String COL_w = "Weigth";


    private static String CreateSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_b + " TEXT,"
            + COL_w + " TEXT,"
            + COL_t + " TEXT);";

}
