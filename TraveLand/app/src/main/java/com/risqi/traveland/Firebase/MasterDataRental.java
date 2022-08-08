package com.risqi.traveland.Firebase;

public class MasterDataRental {
    String AlamatRental, DeskripsiRental, Latitude, Longlitude, NamaRental, TanggalBuat, TanggalUpdate, fotoRental;
    int StatusRental;
    public MasterDataRental(){

    }

    public String getAlamatRental() {
        return AlamatRental;
    }

    public void setAlamatRental(String alamatRental) {
        AlamatRental = alamatRental;
    }

    public String getDeskripsiRental() {
        return DeskripsiRental;
    }

    public void setDeskripsiRental(String deskripsiRental) {
        DeskripsiRental = deskripsiRental;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLonglitude() {
        return Longlitude;
    }

    public void setLonglitude(String longlitude) {
        Longlitude = longlitude;
    }

    public String getNamaRental() {
        return NamaRental;
    }

    public void setNamaRental(String namaRental) {
        NamaRental = namaRental;
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

    public String getFotoRental() {
        return fotoRental;
    }

    public void setFotoRental(String fotoRental) {
        this.fotoRental = fotoRental;
    }

    public int getStatusRental() {
        return StatusRental;
    }

    public void setStatusRental(int statusRental) {
        StatusRental = statusRental;
    }
}
