package com.risqi.traveland.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MasterDataHotelDetail {
    String FasiltasKamar,HargaKamar,JumlahKamar,NamaKamar,TanggalBuat,TanggalUpdate,fotoKamar,IdHotel;
    int StatusKamar;
    private SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Calendar myCalendar = Calendar.getInstance();
    public MasterDataHotelDetail(){

    }

    public HashMap updateJumlahKamar (String Kamar){
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        HashMap update = new HashMap();
        update.put("JumlahKamar", Kamar);
        update.put("TanggalUpdate", dateTime);

        return  update;
    }

    public String getFasiltasKamar() {
        return FasiltasKamar;
    }

    public void setFasiltasKamar(String fasiltasKamar) {
        FasiltasKamar = fasiltasKamar;
    }

    public String getHargaKamar() {
        return HargaKamar;
    }

    public void setHargaKamar(String hargaKamar) {
        HargaKamar = hargaKamar;
    }

    public String getJumlahKamar() {
        return JumlahKamar;
    }

    public void setJumlahKamar(String jumlahKamar) {
        JumlahKamar = jumlahKamar;
    }

    public String getNamaKamar() {
        return NamaKamar;
    }

    public void setNamaKamar(String namaKamar) {
        NamaKamar = namaKamar;
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

    public String getFotoKamar() {
        return fotoKamar;
    }

    public void setFotoKamar(String fotoKamar) {
        this.fotoKamar = fotoKamar;
    }

    public String getIdHotel() {
        return IdHotel;
    }

    public void setIdHotel(String idHotel) {
        IdHotel = idHotel;
    }

    public int getStatusKamar() {
        return StatusKamar;
    }

    public void setStatusKamar(int statusKamar) {
        StatusKamar = statusKamar;
    }
}
