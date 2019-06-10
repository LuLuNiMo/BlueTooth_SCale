package com.example.scalebluetooth.DB;

public class Item {
    private int id;
    private String barcode;
    private String weigth;
    private String time;


    public Item(int id, String barcode, String weigth, String time) {
        this.id = id;
        this.barcode = barcode;
        this.weigth = weigth;
        this.time = time;
    }

    public Item( String barcode, String weigth, String time) {
        this.barcode = barcode;
        this.weigth = weigth;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getWeigth() {
        return weigth;
    }

    public void setWeigth(String weigth) {
        this.weigth = weigth;
    }

    public String getALL(){
        return barcode + String.valueOf(weigth) + time;
    }
}
