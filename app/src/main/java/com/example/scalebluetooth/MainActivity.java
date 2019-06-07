package com.example.scalebluetooth;

import android.content.Intent;
import android.os.Handler;
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

    public void connect_device(){
         manager.showLIst(name);
    }

    public void dectet_weigth(){
        if(code.getText().length()== 13){
            adapter.add(code.getText().toString(),Float.valueOf(weigth.getText().toString().substring(0,
                    weigth.getText().toString().indexOf('K'))));
            Toast.makeText(this,"新增成功",Toast.LENGTH_SHORT).show();
            if(!type){
                code.setText("");
            }
        }else{
            Toast.makeText(this,"長度不符",Toast.LENGTH_SHORT).show();
            code.setText("");
        }
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

    //File Write Perrmission
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
       if(EM.permission(this)){
           Toast.makeText(this, "尚未開放權限!!", Toast.LENGTH_SHORT).show();
           finish();
       }else{
           EM.ExportExcel(MainActivity.this,adapter.getList());
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
                dectet_weigth();
                break;
            case R.id.wbtn:
                if(type){
                    ((Button) findViewById(R.id.wbtn)).setText("單筆測重");
                     type = false;
                }else{
                    ((Button) findViewById(R.id.wbtn)).setText("連續測重");
                     type = true;
                }

                break;
            case R.id.output:
                if(EM.permission(MainActivity.this)){EM.Requset_permission(MainActivity.this);}
                else{ EM.ExportExcel(MainActivity.this,adapter.getList());}
                break;
            case R.id.del:
                adapter.deleteAll();
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
      //  handler.postDelayed(BT_state, 500);

        switch (id) {
            case R.id.bt:
                break;
            case R.id.bc:
                Intent intent = new Intent();
                intent.setClass(this,barcode_record.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}


