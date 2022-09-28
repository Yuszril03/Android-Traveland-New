package com.risqi.traveland.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TransactionHotel {
    private String IdCutomer,IdMitra,IdKamar,HargaKamar,JumlahKamar,CheckIn,CheckOut,JenisPembayaran,JumlahHari,TotalSemua,StatusTransaksi,TanggalBuat,TanggalUpdate,BuktiTraksaksi,UlasanCustomer,UlasanMitra,Rating;
    private SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Calendar myCalendar = Calendar.getInstance();

    public TransactionHotel(){

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

    public Map<String, String> insertData(String IdCutomer,String IdMitra,String IdKamar,String HargaKamar,String JumlahKamar,String CheckIn,String CheckOut,String JenisPembayaran,String JumlahHari,String TotalSemua){
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        Map<String, String> insert= new HashMap<>();
        insert.put("IdCutomer", IdCutomer);
        insert.put("IdMitra", IdMitra);
        insert.put("IdKamar", IdKamar);
        insert.put("HargaKamar", HargaKamar);
        insert.put("JumlahKamar", JumlahKamar);
        insert.put("CheckIn", CheckIn);
        insert.put("CheckOut", CheckOut);
        insert.put("JenisPembayaran", JenisPembayaran);
        insert.put("JumlahHari", JumlahHari);
        insert.put("TotalSemua", TotalSemua);
        insert.put("StatusTransaksi", "1");
        insert.put("TanggalBuat", dateTime);
        insert.put("TanggalUpdate", dateTime);
        insert.put("BuktiTraksaksi", "");
        insert.put("UlasanCustomer", "");
        insert.put("UlasanMitra", "");
        insert.put("Rating", "");


        return insert;
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

    public String getIdKamar() {
        return IdKamar;
    }

    public void setIdKamar(String idKamar) {
        IdKamar = idKamar;
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

    public String getCheckIn() {
        return CheckIn;
    }

    public void setCheckIn(String checkIn) {
        CheckIn = checkIn;
    }

    public String getCheckOut() {
        return CheckOut;
    }

    public void setCheckOut(String checkOut) {
        CheckOut = checkOut;
    }

    public String getJenisPembayaran() {
        return JenisPembayaran;
    }

    public void setJenisPembayaran(String jenisPembayaran) {
        JenisPembayaran = jenisPembayaran;
    }

    public String getJumlahHari() {
        return JumlahHari;
    }

    public void setJumlahHari(String jumlahHari) {
        JumlahHari = jumlahHari;
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

    public String getBuktiTraksaksi() {
        return BuktiTraksaksi;
    }

    public void setBuktiTraksaksi(String buktiTraksaksi) {
        BuktiTraksaksi = buktiTraksaksi;
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
}
