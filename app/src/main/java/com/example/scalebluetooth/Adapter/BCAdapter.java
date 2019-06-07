package com.example.scalebluetooth.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scalebluetooth.DB.Item;
import com.example.scalebluetooth.DB.SQLite;
import com.example.scalebluetooth.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class BCAdapter extends BaseAdapter {
    protected ArrayList<Item> list;
    protected Activity activity;

    public BCAdapter(Activity activity) {
        this.activity = activity;
        list = new ArrayList<>();
    }


    public ArrayList<Item> getList(){
        return list;
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
        return list.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);  //LayoutInflater.from(context);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview, parent, false);
        }

        final Item item = list.get(position);

        EditText code = (EditText) convertView
                .findViewById(R.id.code);
        code.setFocusableInTouchMode(false);
        code.setFocusable(false);

        TextView time = (TextView) convertView
                .findViewById(R.id.date);

        TextView w = (TextView) convertView
                .findViewById(R.id.weigth);

        Button btn = (Button) convertView
                .findViewById(R.id.btn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                SQLite.getInstance(activity).delete(String.valueOf(item.getId()),"ID");
                BCAdapter.this.notifyDataSetChanged();
            }
        });

        code.setText(item.getBarcode());
        time.setText(item.getTime());
        w.setText(String.valueOf(item.getWeigth()));
        return convertView;
    }

    public void add(String code, float w) {
        Item item = new Item(code, w, getTime());

        item.setId(activity.getSharedPreferences("preferences", MODE_PRIVATE).getInt("DBW_No", 0));
        activity.getSharedPreferences("preferences", MODE_PRIVATE).edit().putInt("DBW_No",
                activity.getSharedPreferences("preferences", MODE_PRIVATE).getInt("DBW_No", 0)+1).apply();

        Log.e("2222---",String.valueOf(item.getId()));


        list.add(item);
        SQLite.getInstance(activity).insert(item);
        this.notifyDataSetChanged();
    }


    public String getTime() {
        Date date = new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        return sdFormat.format(date);
    }


    public void deleteAll() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("刪除確認");
        dialog.setIcon(R.drawable.delete);
        dialog.setMessage("確定要刪除全部的資料嗎？");
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
               if(!list.isEmpty()){
                   list.clear();
                   for(Item item:list){
                       SQLite.getInstance(activity).delete(String.valueOf(item.getId()),"ID");
                   }
                   BCAdapter.this.notifyDataSetChanged();
                   Toast.makeText(activity,"刪除成功",Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(activity,"無資料",Toast.LENGTH_SHORT).show();
               }
            }
        });
        dialog.show();
    }

}
