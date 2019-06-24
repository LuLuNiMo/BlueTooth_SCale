package com.example.scalebluetooth.Adapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.excell_ble_api.Excell_BLE;
import com.example.scalebluetooth.DB.Device;
import com.example.scalebluetooth.R;

import java.util.ArrayList;

public abstract class BTbaseAdapter extends BaseAdapter implements Excell_BLE.ListenAPI{
    protected ArrayList<Device> list;
    protected Activity activity;
    protected Excell_BLE BLE;
    protected Device devices;
    protected boolean show;


    public BTbaseAdapter(Activity activity, Excell_BLE BLE){
        this.activity = activity;
        this.BLE = BLE;
        list = new ArrayList<>();
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);  //LayoutInflater.from(context);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_wift, parent, false);
        }

        final Device device = list.get(position);

        TextView name = (TextView) convertView
                .findViewById(R.id.name);

        TextView addr = (TextView) convertView
                .findViewById(R.id.address);

        LinearLayout bt = (LinearLayout) convertView
                .findViewById(R.id.bt);


        Button b = (Button) convertView
                .findViewById(R.id.button);

        Button s = (Button) convertView
                .findViewById(R.id.state);

        final Button del = (Button) convertView
                .findViewById(R.id.del);

        if(show){
            del.setLayoutParams(getLayoutParams(90,90));
            del.setVisibility(View.VISIBLE);
        }else{
            del.setLayoutParams(getLayoutParams(0,0));
        }

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });

        if(device.isState() && show){
            s.setVisibility(View.VISIBLE);
            s.setLayoutParams(getLayoutParams(90,90));
        }else{
            s.setVisibility(View.INVISIBLE);
            s.setLayoutParams(getLayoutParams(0,0));
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMethod(position);
            }
        });

        name.setText(wifi_name(device.getName()));
        addr.setText(device.getAddr());

        return convertView;
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

    public Device getDevices(){
        return devices;
    }

    public void add(BluetoothDevice bd){
        boolean check = true;
        for (Device device:list){
            if(bd.getAddress().equals(device.getAddr())){
                check = false;
                break;
            }
        }

        if(check){
            Device device = new Device(bd.getName(),bd.getAddress(),bd,false);
            list.add(device);
            this.notifyDataSetChanged();
        }
    }


    abstract void btnMethod(int position);

    abstract void delete(int position);







    @Override
    public void OnGetTareValue(String s) {
        Log.d("GetTareValue--", "TareValue Value  : " + s + "\n");
    }

    @Override
    public void OnGetPreTareValue(String s) {
        Log.d("PreTareValue--", "PreTare Value  : " + s + "\n");
    }

    @Override
    public void OnGetVersionValue(String s) {
        Log.d("VersionValue--", "Version Value  : " + s + "\n");
    }

    @Override
    public void OnGetBatteryValue(String s) {
        Log.d("tBatteryValue--", "tBattery Value  : " + s + "\n");
    }



    @Override
    public void OnGetScaleFunction(boolean arg0, boolean arg1, boolean arg2, boolean arg3, boolean arg4, boolean arg5, boolean arg6,
                                   boolean arg7, boolean arg8) {}
    @Override
    public void OnGetErrorMsg(String s) {
        Log.e("ErrorMsg--", "OnGetErrorMsg : " + s + "\n");
    }

    @Override
    public void OnWeight_Update(boolean b, String s, Short aShort, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, short i, String s1, short i1) {
    }

    public LinearLayout.LayoutParams getLayoutParams(int a,int b){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                (a,b,0); // , 1是可選寫的
        lp.setMargins(0, 0, 0, 0);
        return lp;
    }


}
