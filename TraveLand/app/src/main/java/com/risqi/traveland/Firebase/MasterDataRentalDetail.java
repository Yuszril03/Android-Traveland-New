package com.risqi.traveland.Firebase;

public class MasterDataRentalDetail {
    String HargaSewa, IdRental, JumlahKursi, NamaKendaraan, TanggalBuat, TanggalUpdate, UkuranKendaraan, deskripsiKendaraan, fotoKendaraan;
    int StatusKendaraan;
    public MasterDataRentalDetail(){

    }

    public String getHargaSewa() {
        return HargaSewa;
    }

    public void setHargaSewa(String hargaSewa) {
        HargaSewa = hargaSewa;
    }

    public String getIdRental() {
        return IdRental;
    }

    public void setIdRental(String idRental) {
        IdRental = idRental;
    }

    public String getJumlahKursi() {
        return JumlahKursi;
    }

    public void setJumlahKursi(String jumlahKursi) {
        JumlahKursi = jumlahKursi;
    }

    public String getNamaKendaraan() {
        return NamaKendaraan;
    }

    public void setNamaKendaraan(String namaKendaraan) {
        NamaKendaraan = namaKendaraan;
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

    public String getUkuranKendaraan() {
        return UkuranKendaraan;
    }

    public void setUkuranKendaraan(String ukuranKendaraan) {
        UkuranKendaraan = ukuranKendaraan;
    }

    public String getDeskripsiKendaraan() {
        return deskripsiKendaraan;
    }

    public void setDeskripsiKendaraan(String deskripsiKendaraan) {
        this.deskripsiKendaraan = deskripsiKendaraan;
    }

    public String getFotoKendaraan() {
        return fotoKendaraan;
    }

    public void setFotoKendaraan(String fotoKendaraan) {
        this.fotoKendaraan = fotoKendaraan;
    }

    public int getStatusKendaraan() {
        return StatusKendaraan;
    }

    public void setStatusKendaraan(int statusKendaraan) {
        StatusKendaraan = statusKendaraan;
    }
}
