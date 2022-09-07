package com.risqi.traveland.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TransactionWIisata {
   private String IdCutomer,IdMitra,JumlahAnak,JumlahDewasa,HargaAnak,HargaDewasa,JenisPembayaran,TotalAnak,TotalDewasa,TotalSemua,StatusTransaksi,TanggalBuat,TanggalUpdate,BuktiTraksaksi,UlasanCustomer,UlasanMitra,Rating;
    private SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Calendar myCalendar = Calendar.getInstance();
    public TransactionWIisata(){

    }

    public Map<String, String> insertData(String IdCutomer, String IdMitra,String JumlahAnak,String JumlahDewasa,String HargaAnak,String HargaDewasa,String JenisPembayaran,String TotalAnak,String TotalDewasa,String TotalSemua)
    {
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        Map<String, String> insert= new HashMap<>();
        insert.put("IdCutomer", IdCutomer);
        insert.put("IdMitra", IdMitra);
        insert.put("JumlahAnak", JumlahAnak);
        insert.put("JumlahDewasa", JumlahDewasa);
        insert.put("HargaAnak", HargaAnak);
        insert.put("HargaDewasa", HargaDewasa);
        insert.put("JenisPembayaran", JenisPembayaran);
        insert.put("TotalAnak", TotalAnak);
        insert.put("TotalDewasa", TotalDewasa);
        insert.put("TotalSemua", TotalSemua);
        insert.put("StatusTransaksi", "1");
        insert.put("TanggalBuat", dateTime);
        insert.put("TanggalUpdate", dateTime);
        insert.put("BuktiTraksaksi", "");
        insert.put("UlasanMitra", "");
        insert.put("UlasanCustomer", "");
        insert.put("Rating", "");

        return insert;
    }

    public HashMap updateBuktiPembayaran (String status,String foto){
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        HashMap update = new HashMap();
        update.put("StatusTransaksi", status);
        update.put("BuktiTraksaksi", foto);
        update.put("TanggalUpdate", dateTime);

        return  update;
    }

    public HashMap updateBatalkan (String status){
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        HashMap update = new HashMap();
        update.put("StatusTransaksi", status);
        update.put("TanggalUpdate", dateTime);

        return  update;
    }
    public HashMap updatePenilaian (String bintang, String ulasan){
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        HashMap update = new HashMap();
        update.put("Rating", bintang);
        update.put("UlasanCustomer", ulasan);
        update.put("TanggalUpdate", dateTime);

        return  update;
    }


    public String getUlasanCustomer() {
        return UlasanCustomer;
    }

    public void setUlasanCustomer(String ulasanCustomer) {
        UlasanCustomer = ulasanCustomer;
    }

    public String getUlasanMitra() {
        return UlasanMitra;
    }

    public void setUlasanMitra(String ulasanMitra) {
        UlasanMitra = ulasanMitra;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getBuktiTraksaksi() {
        return BuktiTraksaksi;
    }

    public void setBuktiTraksaksi(String buktiTraksaksi) {
        BuktiTraksaksi = buktiTraksaksi;
    }

    public String getIdCutomer() {
        return IdCutomer;
    }

    public void setIdCutomer(String idCutomer) {
        IdCutomer = idCutomer;
    }

    public String getIdMitra() {
        return IdMitra;
    }

    public void setIdMitra(String idMitra) {
        IdMitra = idMitra;
    }

    public String getJumlahAnak() {
        return JumlahAnak;
    }

    public void setJumlahAnak(String jumlahAnak) {
        JumlahAnak = jumlahAnak;
    }

    public String getJumlahDewasa() {
        return JumlahDewasa;
    }

    public void setJumlahDewasa(String jumlahDewasa) {
        JumlahDewasa = jumlahDewasa;
    }

    public String getHargaAnak() {
        return HargaAnak;
    }

    public void setHargaAnak(String hargaAnak) {
        HargaAnak = hargaAnak;
    }

    public String getHargaDewasa() {
        return HargaDewasa;
    }

    public void setHargaDewasa(String hargaDewasa) {
        HargaDewasa = hargaDewasa;
    }

    public String getJenisPembayaran() {
        return JenisPembayaran;
    }

    public void setJenisPembayaran(String jenisPembayaran) {
        JenisPembayaran = jenisPembayaran;
    }

    public String getTotalAnak() {
        return TotalAnak;
    }

    public void setTotalAnak(String totalAnak) {
        TotalAnak = totalAnak;
    }

    public String getTotalDewasa() {
        return TotalDewasa;
    }

    public void setTotalDewasa(String totalDewasa) {
        TotalDewasa = totalDewasa;
    }

    public String getTotalSemua() {
        return TotalSemua;
    }

    public void setTotalSemua(String totalSemua) {
        TotalSemua = totalSemua;
    }

    public String getStatusTransaksi() {
        return StatusTransaksi;
    }

    public void setStatusTransaksi(String statusTransaksi) {
        StatusTransaksi = statusTransaksi;
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
}
