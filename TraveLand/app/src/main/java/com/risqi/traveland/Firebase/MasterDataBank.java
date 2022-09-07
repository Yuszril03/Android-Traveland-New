package com.risqi.traveland.Firebase;

public class MasterDataBank {
    String CaraPembayaran,NamaBank,TanggalBuat,TanggalUpdate,GambarBank,RekeningBank;
    int StatusBank;

    public MasterDataBank(){

    }

    public String getGambarBank() {
        return GambarBank;
    }

    public void setGambarBank(String gambarBank) {
        GambarBank = gambarBank;
    }

    public String getCaraPembayaran() {
        return CaraPembayaran;
    }

    public void setCaraPembayaran(String caraPembayaran) {
        CaraPembayaran = caraPembayaran;
    }

    public String getNamaBank() {
        return NamaBank;
    }

    public void setNamaBank(String namaBank) {
        NamaBank = namaBank;
    }

    public String getRekeningBank() {
        return RekeningBank;
    }

    public void setRekeningBank(String rekeningBank) {
        RekeningBank = rekeningBank;
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

    public int getStatusBank() {
        return StatusBank;
    }

    public void setStatusBank(int statusBank) {
        StatusBank = statusBank;
    }
}
