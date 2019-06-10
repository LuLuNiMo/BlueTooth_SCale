package com.example.scalebluetooth.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.DB.Item;
import com.example.scalebluetooth.DB.SQLite;
import com.example.scalebluetooth.R;

import java.util.ArrayList;
import java.util.HashMap;

public class WiFiAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> list;
    private  Activity a;
    private Excell_BLE ble;
    private TextView tv;
    private Dialog d;

    public WiFiAdapter(Activity a, Excell_BLE ble,TextView tv){
        this.a = a;
        this.tv= tv;
        this.ble = ble;
        list = new ArrayList<>();
    }

    public void setD(Dialog d){
        this.d = d;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getBondState();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) a.getBaseContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);  //LayoutInflater.from(context);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_wift, parent, false);
        }

        final BluetoothDevice device = list.get(position);

        TextView name = (TextView) convertView
                .findViewById(R.id.name);

        TextView addr = (TextView) convertView
                .findViewById(R.id.address);

        LinearLayout bt = (LinearLayout) convertView
                .findViewById(R.id.bt);



        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ble.Stop_Scan();
                ble.Connect_Device(device);
                tv.setText(wifi_name(device.getName()));
                d.dismiss();
            }
        });

        name.setText(wifi_name(device.getName()));
        addr.setText(device.getAddress());

        return convertView;
    }


    public void update(HashMap<String,BluetoothDevice> map){
        list.clear();
        for(String id:map.keySet()){
            list.add(map.get(id));
        }
        this.notifyDataSetChanged();
    }

    public String wifi_name(String name){
       if(name != null){
           if(name.trim().equals("")){
               return "未命名裝置";
           }else{
               return name;
           }
       }
       return "未命名裝置";
    }


}
