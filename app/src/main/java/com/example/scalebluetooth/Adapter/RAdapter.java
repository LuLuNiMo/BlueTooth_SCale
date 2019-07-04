package com.example.scalebluetooth.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scalebluetooth.DB.Item;
import com.example.scalebluetooth.DB.SQLite;
import com.example.scalebluetooth.R;
import com.example.scalebluetooth.barcode_record;

import java.util.ArrayList;
import java.util.HashMap;


public class RAdapter extends BCAdapter {
    private HashMap<Integer, Boolean> map;
    private HashMap<Integer, Item> items;

    public RAdapter(Activity activity, ArrayList<Item> list) {
        super(activity);
        this.list = list;
        map = new HashMap<>();
        items = new HashMap<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);  //LayoutInflater.from(context);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview, parent, false);
        }

        final Item item = list.get(position);

        final EditText code = (EditText) convertView
                .findViewById(R.id.code);
        code.setFocusableInTouchMode(false);
        code.setFocusable(false);

        final LinearLayout all = (LinearLayout) convertView
                .findViewById(R.id.all);

        final LinearLayout l = (LinearLayout) convertView
                .findViewById(R.id.l);

        TextView time = (TextView) convertView
                .findViewById(R.id.date);

        TextView id = (TextView) convertView
                .findViewById(R.id.id);

        TextView w = (TextView) convertView
                .findViewById(R.id.weigth);

        final Button btn = (Button) convertView
                .findViewById(R.id.btn);
        btn.setBackground(activity.getDrawable(R.drawable.edit));


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelcet(item.getId(), item, code.getText().toString());
            }
        });

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect2(item.getId(), item);
            }
        });


        //單筆選擇修改狀態改變--修改狀態
        if (map.get(item.getId()) == null) {
            code.setFocusableInTouchMode(false);
            code.setFocusable(false);
            code.setBackground(activity.getDrawable(R.drawable.empty));
            btn.setBackground(activity.getDrawable(R.drawable.edit));

        } else if (map.get(item.getId())) {
            code.setFocusableInTouchMode(true);
            code.setFocusable(true);
            code.setBackground(activity.getDrawable(R.drawable.tvstyle));
            btn.setBackground(activity.getDrawable(R.drawable.edit2));
        }

         //單筆選擇狀態改變--按一下
        if (items.get(item.getId()) == null) {
            code.setTextColor(Color.BLACK);
            all.setBackgroundColor(activity.getResources().getColor(R.color.linearBG));
        } else {
            code.setTextColor(Color.parseColor("#055E88"));
            all.setBackgroundColor(Color.parseColor("#87CDEC"));
        }

        code.setText(item.getBarcode());
        time.setText(item.getTime());
        id.setText(String.valueOf(item.getId()));
        w.setText(String.valueOf(item.getWeigth()));
        return convertView;
    }


    public void onSelcet(int id, Item item, String code) {
        if (map.get(id) == null) {
            map.put(id, true);
        } else {
            if (map.get(id)) {
                item.setBarcode(code);
                SQLite.getInstance(activity).update(item);
            }
            map.remove(id);
        }
        this.notifyDataSetChanged();
    }


    public void onSelect2(int id, Item item) {
        if (items.get(id) == null) {
            items.put(id, item);
        } else {
            items.remove(id);
        }
        ((barcode_record) activity).setSelectItme(items.size());
        this.notifyDataSetChanged();
    }


    public void setList(ArrayList<Item> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public int getItemscount(){
        return items.size();
    }

    public ArrayList<Item> getItems(){
        ArrayList<Item> list = new ArrayList<>();
        for (int id : items.keySet()) {
           list.add(items.get(id));
        }
        return list;
    }


    public void delete() {
        for (int id : items.keySet()) {
            list.remove(items.get(id));
            SQLite.getInstance(activity).delete(String.valueOf(id),"ID");
        }
        items.clear();
        this.notifyDataSetChanged();
        ((barcode_record) activity).setSelectItme(items.size());
        ((barcode_record) activity).setAllItme();

    }


    public void SelectALL(boolean check){
        if(check){
            for(Item item:list){
                items.put(item.getId(),item);
            }
        }else{
            items.clear();
        }
        ((barcode_record) activity).setSelectItme(items.size());
        this.notifyDataSetChanged();
    }

}
