package com.example.scalebluetooth;

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

import com.example.scalebluetooth.Adapter.RAdapter;
import com.example.scalebluetooth.DB.Item;
import com.example.scalebluetooth.DB.SQLite;
import com.example.scalebluetooth.File.ExcelManager;

import java.util.ArrayList;

public class barcode_record extends AppCompatActivity {
     private EditText text;
     private TextView unit,state;
     private RAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_record);
        setTitle("紀錄");
        initSet();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }




    public void initSet(){
        text = (EditText) findViewById(R.id.text);
        unit = (TextView) findViewById(R.id.unit);
        state = (TextView) findViewById(R.id.state);
        adapter = new RAdapter(this,(ArrayList<Item>) SQLite.getInstance(this).select(null,null));


        state.setText("總共：" +  String.valueOf(adapter.getCount())+" 件");
        ((ListView) findViewById(R.id.listview)).setAdapter(adapter);
        ((Button) findViewById(R.id.btn)).setOnClickListener(btn);

    }


    private Button.OnClickListener btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             ArrayList<Item> list = (ArrayList<Item>) SQLite.getInstance(barcode_record.this).select(null,null);
             ArrayList<Item> result = new ArrayList<>();

             if(text.getText().toString().equals("")){
                 adapter.setList(list);
             }else{
                for(Item item:list){
                    if(item.getALL().contains(text.getText().toString())){
                        result.add(item);
                    }
                }
                 adapter.setList(result);
                if(result.isEmpty()){
                    Toast.makeText(barcode_record.this,"找不到您想找的資料!",Toast.LENGTH_SHORT).show();
                }
             }
            state.setText("總共：" +  String.valueOf(adapter.getCount())+" 件");
        }
    };

    public void setSelectItme(int i){
        unit.setText("已選：" + String.valueOf(i) +" 件");
    }


    //MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case R.id.del:
                adapter.delete();
                if(adapter.getItemscount() != 0){
                    Toast.makeText(this,"刪除成功",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.el:
                new ExcelManager().ExportExcel(this,adapter.getItems());
                break;
            case R.id.all:
                if(adapter.getItemscount() < adapter.getCount()){
                    adapter.SelectALL(true);
                }else{
                    adapter.SelectALL(false);
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }



}


