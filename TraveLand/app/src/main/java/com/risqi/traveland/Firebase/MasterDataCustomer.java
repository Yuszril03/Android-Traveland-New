package com.risqi.traveland.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MasterDataCustomer {
    String AlamatCustomer,EmailCustomer,NamaCustomer,TanggalBuat,TanggalLahirCustomer,TanggalUpdate,TelefonCustomer,fotoCustomer;
    int Gender,StatusCustomer;
    private SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Calendar myCalendar = Calendar.getInstance();

    public  MasterDataCustomer(){

    }

    public HashMap updateDataString (String AlamatCustomer,String NamaCustomer,String TanggalLahirCustomer,String TelefonCustomer){
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        HashMap update = new HashMap();
        update.put("AlamatCustomer", AlamatCustomer);
        update.put("NamaCustomer", NamaCustomer);
        update.put("TanggalLahirCustomer", TanggalLahirCustomer);
        update.put("TelefonCustomer", TelefonCustomer);
        update.put("TanggalUpdate", dateTime);

        return  update;
    }
    public HashMap updateDataINT (int Gender){
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        HashMap update = new HashMap();
        update.put("Gender", Gender);
        update.put("TanggalUpdate", dateTime);

        return  update;
    }
    public HashMap updateDataTanggalPembaruan (){
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        HashMap update = new HashMap();
        update.put("TanggalUpdate", dateTime);

        return  update;
    }

    public String getAlamatCustomer() {
        return AlamatCustomer;
    }

    public void setAlamatCustomer(String AlamatCustomer) {
        this.AlamatCustomer = AlamatCustomer;
    }

    public String getEmailCustomer() {
        return EmailCustomer;
    }

    public void setEmailCustomer(String EmailCustomer) {
        this.EmailCustomer = EmailCustomer;
    }

    public String getNamaCustomer() {
        return NamaCustomer;
    }

    public void setNamaCustomer(String NamaCustomer) {
        this.NamaCustomer = NamaCustomer;
    }

    public String getTanggalBuat() {
        return TanggalBuat;
    }

    public void setTanggalBuat(String TanggalBuat) {
        this.TanggalBuat = TanggalBuat;
    }

    public String getTanggalLahirCustomer() {
        return TanggalLahirCustomer;
    }

    public void setTanggalLahirCustomer(String TanggalLahirCustomer) {
        this.TanggalLahirCustomer = TanggalLahirCustomer;
    }

    public String getTanggalUpdate() {
        return TanggalUpdate;
    }

    public void setTanggalUpdate(String TanggalUpdate) {
        this.TanggalUpdate = TanggalUpdate;
    }

    public String getTelefonCustomer() {
        return TelefonCustomer;
    }

    public void setTelefonCustomer(String TelefonCustomer) {
        this.TelefonCustomer = TelefonCustomer;
    }

    public String getFotoCustomer() {
        return fotoCustomer;
    }

    public void setFotoCustomer(String FotoCustomer) {
        this.fotoCustomer = FotoCustomer;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int Gender) {
        this.Gender = Gender;
    }

    public int getStatusCustomer() {
        return StatusCustomer;
    }

    public void setStatusCustomer(int StatusCustomer) {
        this.StatusCustomer = StatusCustomer;
    }
}
