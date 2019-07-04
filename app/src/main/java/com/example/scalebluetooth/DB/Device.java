package com.example.scalebluetooth.DB;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

public class Device  implements Serializable {
    private String name;
    private String addr;
    private BluetoothDevice device;
    private boolean State;

    public Device() {
        addr = "";
    }

    public Device(String name, String addr, BluetoothDevice device,boolean State) {
        this.name = name;
        this.addr = addr;
        this.device = device;
        this.State = State;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public boolean isState() {
        return State;
    }

    public void setState(boolean state) {
        State = state;
    }

}
