package com.example.scalebluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name, weigth, state;
    private EditText code;
    private ListView listView;
    private BlueToothManager manager;
    private Handler handler;
    private MenuItem m;


    // Initial setting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSet();

    }

    public void initSet() {
        name = (TextView) findViewById(R.id.name);
        weigth = (TextView) findViewById(R.id.weigth);
        state = (TextView) findViewById(R.id.dev_s);
        code = (EditText) findViewById(R.id.code);
        listView = (ListView) findViewById(R.id.listview);


        ((Button) findViewById(R.id.c_dev)).setOnClickListener(this);
        ((Button) findViewById(R.id.ok)).setOnClickListener(this);
        ((Button) findViewById(R.id.wbtn)).setOnClickListener(this);
        ((Button) findViewById(R.id.output)).setOnClickListener(this);
        ((Button) findViewById(R.id.del)).setOnClickListener(this);


        manager = new BlueToothManager(this);
        handler = new Handler();

    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(BT_state, 1000);
    }


    // main Method


    public void connect_device(){
        manager.showLIst();
    }













    //BT Permission response
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (manager.getBTs()) {
            connect_device();
        }else{
            Toast.makeText(this, "藍芽尚未開啟!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.c_dev:
                if (manager.getBTs()) {
                    connect_device();
                } else {
                    manager.Request();
                }
                break;
            case R.id.ok:
                break;

            case R.id.wbtn:

                break;
            case R.id.output:

                break;
            case R.id.del:

                break;
        }
    }


    //Dectet BLUETOOTH state
    Runnable BT_state = new Runnable() {
        public void run() {
            handler.postDelayed(this, 1000);
            if (m != null) {
                if (manager.getBTs()) {
                    m.setIcon(R.drawable.bt_icon_enable);
                    m.setTitle("已開啟");
                } else {
                    m.setIcon(R.drawable.bt_icon_disable);
                    m.setTitle("未開");
                }
            }

        }
    };


    //MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        m = menu.findItem(R.id.bt);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        handler.postDelayed(BT_state, 500);

        switch (id) {
            case R.id.bt:
                break;
            case R.id.bc:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

}


