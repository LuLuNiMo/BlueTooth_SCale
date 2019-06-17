package com.example.scalebluetooth;

import android.Manifest;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scalebluetooth.Adapter.BCAdapter;
import com.example.scalebluetooth.BlueTooth.BlueToothManagers;
import com.example.scalebluetooth.BlueTooth.DetectScale;
import com.example.scalebluetooth.File.ExcelManager;

import org.apache.poi.hssf.record.formula.functions.T;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name, weigth, state;
    private EditText code;
    private BCAdapter adapter;
    private BlueToothManagers manager;
    private ExcelManager EM;
    private Handler handler;
    private MenuItem m;
    private boolean type = false; //true = 單筆 false = 連續


    // Initial setting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSet();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void initSet() {
        name = (TextView) findViewById(R.id.name);
        weigth = (TextView) findViewById(R.id.weigth);
        state = (TextView) findViewById(R.id.dev_s);
        code = (EditText) findViewById(R.id.code);


        ((Button) findViewById(R.id.c_dev)).setOnClickListener(this);
        ((Button) findViewById(R.id.ok)).setOnClickListener(this);
        ((Button) findViewById(R.id.wbtn)).setOnClickListener(this);
        ((Button) findViewById(R.id.output)).setOnClickListener(this);
        ((Button) findViewById(R.id.del)).setOnClickListener(this);


        manager = new BlueToothManagers(this);
        handler = new Handler();
        EM = new ExcelManager();
        adapter = new BCAdapter(this);
        ((ListView) findViewById(R.id.listview)).setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(BT_state, 1000);
    }


    // main Method

    public void connect_device() {
        manager.ConnectDevices(name, weigth, state);
    }

    public void dectet_weigth() {
        if (code.getText().length() == 13) {
            adapter.add(code.getText().toString(), weigth.getText().toString());
            Toast.makeText(this, "新增成功", Toast.LENGTH_SHORT).show();
            if (!type) {
                code.setText("");
            }
        } else {
            Toast.makeText(this, "長度不符", Toast.LENGTH_SHORT).show();
            code.setText("");
        }
    }


    //BT Permission response
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (manager.getBTs() && manager.getLocalR()) {
            connect_device();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
    }




    //File Write and location Perrmission
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode){
            case 0:
                if (manager.getBTs() && manager.getLocalR()) {
                    connect_device();
                } else {
                    Toast.makeText(this,"尚未開放藍芽權限",Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if (EM.permission(this)) {
                    Toast.makeText(this, "尚未開放權限!!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    EM.ExportExcel(MainActivity.this, adapter.getList());
                }
                break;
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.c_dev:
                if (manager.getBTs() && manager.getLocalR()) {
                    connect_device();
                } else {
                    manager.Request();
                }
                break;
            case R.id.ok:
                dectet_weigth();
                break;
            case R.id.wbtn:
                if (type) {
                    ((Button) findViewById(R.id.wbtn)).setText("單筆測重");
                    type = false;
                } else {
                    ((Button) findViewById(R.id.wbtn)).setText("連續測重");
                    type = true;
                }

                break;
            case R.id.output:

                if (EM.permission(MainActivity.this)) {
                    EM.Requset_permission(MainActivity.this);
                } else {
                    if(adapter.getList().isEmpty() || adapter.getList().size() < 1){
                        Toast.makeText(this,"無資料", Toast.LENGTH_SHORT).show();
                    }else{
                        EM.ExportExcel(MainActivity.this, adapter.getList());
                        new AlterDiagram(this).showDialog("匯出至......",new String[]{"手機","其他"});
                    }
                }

                break;
            case R.id.del:
              adapter.deleteAll();
                break;
        }
    }

    //根據副檔名取得相對應mmr TYPE
    private static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";

        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return mime;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return mime;
            } catch (RuntimeException e) {
                e.printStackTrace();
                return mime;
            }
        }
        return mime;
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
                Intent intent = new Intent();
                intent.setClass(this, barcode_record.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}


