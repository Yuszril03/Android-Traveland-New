package com.risqi.traveland.Firebase;

public class DataKegiatan {
    String Judul,Alamat,IsiKegiatan,JenisKegiatan,KegiatanYangBerkaitan,LinkImage,Latitute,Longlitute,TanggalAkhir,TanggalBuat,TanggalMulai,TanggalUpdate;
    int StatusBerita;

    public DataKegiatan(){

    }

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String judul) {
        this.Judul = judul;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        this.Alamat = alamat;
    }

    public String getIsiKegiatan() {
        return IsiKegiatan;
    }

    public void setIsiKegiatan(String isiKegiatan) {
        this.IsiKegiatan = isiKegiatan;
    }

    public String getJenisKegiatan() {
        return JenisKegiatan;
    }

    public void setJenisKegiatan(String jenisKegiatan) {
        this.JenisKegiatan = jenisKegiatan;
    }

    public String getKegiatanYangBerkaitan() {
        return KegiatanYangBerkaitan;
    }

    public void setKegiatanYangBerkaitan(String kegiatanYangBerkaitan) {
        this.KegiatanYangBerkaitan = kegiatanYangBerkaitan;
    }

    public String getLinkImage() {
        return LinkImage;
    }

    public void setLinkImage(String linkImage) {
        this.LinkImage = linkImage;
    }

    public String getLatitute() {
        return Latitute;
    }

    public void setLatitute(String latitute) {
        this.Latitute = latitute;
    }

    public String getLonglitute() {
        return Longlitute;
    }

    public void setLonglitute(String longlitute) {
        this.Longlitute = longlitute;
    }

    public String getTanggalAkhir() {
        return TanggalAkhir;
    }

    public void setTanggalAkhir(String tanggalAkhir) {
        this.TanggalAkhir = tanggalAkhir;
    }

    public String getTanggalBuat() {
        return TanggalBuat;
    }

    public void setTanggalBuat(String tanggalBuat) {
        this.TanggalBuat = tanggalBuat;
    }

    public String getTanggalMulai() {
        return TanggalMulai;
    }

    public void setTanggalMulai(String tanggalMulai) {
        this.TanggalMulai = tanggalMulai;
    }

    public String getTanggalUpdate() {
        return TanggalUpdate;
    }

    public void setTanggalUpdate(String tanggalUpdate) {
        this.TanggalUpdate = tanggalUpdate;
    }

    public int getStatusBerita() {
        return StatusBerita;
    }

    public void setStatusBerita(int statusBerita) {
        this.StatusBerita = statusBerita;
    }
}

