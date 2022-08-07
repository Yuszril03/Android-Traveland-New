package com.risqi.traveland.Firebase;

import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DataLoginCustomer {
    private String NIK,KeyAndroid,TanggalBuat,TanggalUpdate,ModelDevice,Produk;
    private SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Calendar myCalendar = Calendar.getInstance();

    public DataLoginCustomer(){

    }


    public  Map<String, String> insertData( String NIK,String ModelDevice,String Produk){

        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        Map<String, String> insertLogin = new HashMap<>();
        insertLogin.put("NIK", NIK);
        insertLogin.put("Produk", Produk);
        insertLogin.put("TanggalBuat", dateTime);
        insertLogin.put("TanggalUpdate", dateTime);
        insertLogin.put("ModelDevice", Build.MODEL);
        return  insertLogin;
    }

    public HashMap updateLogin (String NIK){
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        HashMap update = new HashMap();
        update.put("NIK", NIK);
        update.put("TanggalUpdate", dateTime);

        return  update;
    }

    public String getModelDevice() {
        return ModelDevice;
    }

    public void setModelDevice(String modelDevice) {
        ModelDevice = modelDevice;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }

    public String getKeyAndroid() {
        return KeyAndroid;
    }

    public void setKeyAndroid(String keyAndroid) {
        KeyAndroid = keyAndroid;
    }

    public String getTanggalBuat() {
        return TanggalBuat;
    }

    public void setTanggalBuat(String tanggalBuat) {
        TanggalBuat = tanggalBuat;
    }

    public String getTanggalUpdate() {
        return TanggalUpdate;
    }

    public void setTanggalUpdate(String tanggalUpdate) {
        TanggalUpdate = tanggalUpdate;
    }

    public String getProduk() {
        return Produk;
    }

    public void setProduk(String produk) {
        Produk = produk;
    }
}
