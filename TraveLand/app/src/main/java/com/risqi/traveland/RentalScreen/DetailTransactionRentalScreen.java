package com.risqi.traveland.RentalScreen;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.risqi.traveland.ETicket.ETicketScreen;
import com.risqi.traveland.Firebase.MasterDataRentalDetail;
import com.risqi.traveland.Firebase.TransactionRental;
import com.risqi.traveland.OrderingScreen.OrderingScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.RatingScreen.RatingScreen;
import com.risqi.traveland.RatingScreen.ReviewRatingScreen;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.SettingScreen.HistoryOrderingScreen;
import com.risqi.traveland.TakePhotoTransaction.TakePhotoTransactionScreen;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailTransactionRentalScreen extends AppCompatActivity {

    //Main Menu
    private ConstraintLayout layoutUtama;
    //Top Layout
    private Button btnbackscroller;
    //Layout Detail User
    private TextView textPersonal, judulNama, Nama, judulTelefon, Telefon, judulEmail, Email;
    private ConstraintLayout bgPersonal;
    //Layout Detail Hotel
    private ConstraintLayout bgHotel;
    private TextView textHotel,KodeHotel,judulKOde,jRental,tRental,jAlamatRental,tAlamatRental,jMobil,tMobil,jJenis,tJenis,jHarga,tHarga,jCheckin,tCheckin,jCheckOut,tCheckOut,StatusText,judulStatus;
    //LayoutMain payment
    private ImageView logoBank;
    private ConstraintLayout bgPembayaran;
    private TextView namaPembayaran, textPembayaran;
    //Layout Total
    private ConstraintLayout bgRincian;
    private TextView textRincian, totalTextAnak, totalAnak, totalTextDewasa, totalDewasa, Totaltext, totalPembayaran;

    //Button Submit
    private Button buttonKondisiSubmit,Batalkan;

    //Other
    private String idHotel,iDetail;
    private int jumlahKamarAsli, jumlahKamarOrder;
    private DatabaseReference database1, database2;
    private DataMode dataMode;
    private SweetAlertDialog pDialog;
    private TransactionRental transactionRental,transactionRentalExt;
    private MasterDataRentalDetail masterDataRentalDetail;
    private Task update,update2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction_rental_screen);
        getDataInten();
        initialize();

        SetMode();
        //SetDAta
        setDataTransaksi();

        toMenuOrder();
        toCancel();
        //submit
        toSubmit();

    }

    private void toSubmit() {
        buttonKondisiSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonKondisiSubmit.getText().equals("Upload Bukti Transaksi")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, TakePhotoTransactionScreen.class);
                    a.putExtra("idScreen",idHotel);
                    a.putExtra("jenisScreen","Rental");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionRentalScreen.this);
                    onStop();

                }else if(buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket Pengambilan")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, ETicketScreen.class);
                    a.putExtra("idScreen",idHotel);
                    a.putExtra("jenisScreen","Rental");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionRentalScreen.this);
                    onStop();
                }else if(buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket Pengembalian")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, ETicketScreen.class);
                    a.putExtra("idScreen",idHotel);
                    a.putExtra("jenisScreen","Rental");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionRentalScreen.this);
                    onStop();
                }
                else if(buttonKondisiSubmit.getText().equals("Kembali")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
                    onStop();
                }else if(buttonKondisiSubmit.getText().equals("Lihat Penilaian")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, ReviewRatingScreen.class);
                    a.putExtra("idScreen",idHotel);
                    a.putExtra("jenisScreen","Rental");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionRentalScreen.this);
                    onStop();
                }
                else if(buttonKondisiSubmit.getText().equals("Penilaian")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, RatingScreen.class);
                    a.putExtra("idScreen",idHotel);
                    a.putExtra("jenisScreen","Rental");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionRentalScreen.this);
                    onStop();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(buttonKondisiSubmit.getText().equals("Upload Bukti Transaksi")){
            Intent a = new Intent(DetailTransactionRentalScreen.this, OrderingScreen.class);

            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
            onStop();

        }else if(buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket Pengambilan") || buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket Pengembalian")){
            Intent a = new Intent(DetailTransactionRentalScreen.this, OrderingScreen.class);

            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
            onStop();
        }
        else if(buttonKondisiSubmit.getText().equals("Kembali")){
            Intent a = new Intent(DetailTransactionRentalScreen.this, HistoryOrderingScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
            onStop();
        }else if(buttonKondisiSubmit.getText().equals("Lihat Penilaian")){
            Intent a = new Intent(DetailTransactionRentalScreen.this, HistoryOrderingScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
            onStop();
        }
        else if(buttonKondisiSubmit.getText().equals("Penilaian")){
            Intent a = new Intent(DetailTransactionRentalScreen.this, HistoryOrderingScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
            onStop();
        }
    }

    private void toCancel() {
        Batalkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(DetailTransactionRentalScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Anda Yakin")
                        .setContentText("Membatalkan transaksi ini!")
                        .setConfirmText("Iya!")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Memuat Data...");
                                pDialog.setCancelable(false);
                                pDialog.show();

                                HashMap updateData = transactionRentalExt.updateBatalkan("2");
                                update = FirebaseDatabase.getInstance().getReference().child("Transaction-Rental").child(idHotel).updateChildren(updateData);
                                update.addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task2) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                pDialog.dismiss();
                                                setDataTransaksi();
                                                new SweetAlertDialog(DetailTransactionRentalScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Berhasil")
                                                        .setContentText("berhasil membatalkan transaksi!")
                                                        .setConfirmText("Iya!")
                                                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismissWithAnimation();
                                                            }
                                                        }).show();
                                            }
                                        }, 2000);
                                    }
                                });

                            }
                        }).show();
            }
        });
    }

    private void toMenuOrder() {
        btnbackscroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonKondisiSubmit.getText().equals("Upload Bukti Transaksi")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, OrderingScreen.class);

                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
                    onStop();

                }else if(buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket Pengambilan") || buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket Pengembalian")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, OrderingScreen.class);

                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
                    onStop();
                }
                else if(buttonKondisiSubmit.getText().equals("Kembali")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
                    onStop();
                }else if(buttonKondisiSubmit.getText().equals("Lihat Penilaian")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
                    onStop();
                }
                else if(buttonKondisiSubmit.getText().equals("Penilaian")){
                    Intent a = new Intent(DetailTransactionRentalScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionRentalScreen.this);
                    onStop();
                }
            }
        });
    }

    private void setDataTransaksi() {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Transaction-Rental").child(idHotel).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> transaksiRental = (Map<String, Object>) task.getResult().getValue();
                setDataUser(transaksiRental.get("IdCutomer").toString());
                setDataRental(transaksiRental.get("IdMitra").toString());
                setDataMobil(transaksiRental.get("IdMobil").toString());
                setDataBank(transaksiRental.get("JenisPembayaran").toString());
                iDetail= transaksiRental.get("IdMobil").toString();

                KodeHotel.setText(idHotel);

                tHarga.setText("Rp."+transaksiRental.get("HargaMobil"));
                tJenis.setText(transaksiRental.get("UkuranMobil").toString());
                jumlahKamarOrder = 1;
                tCheckin.setText(transaksiRental.get("CheckIn").toString());
                tCheckOut.setText(transaksiRental.get("CheckOut").toString());

                totalTextDewasa.setText("Total Sewa ("+transaksiRental.get("JumlahHari")+" Hari)");
                totalDewasa.setText("Rp."+transaksiRental.get("TotalSemua"));
                totalPembayaran.setText("Rp."+transaksiRental.get("TotalSemua"));


                if(transaksiRental.get("StatusTransaksi").toString().equals("1")){
                    Batalkan.setVisibility(View.VISIBLE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Belum Terbayar");
                    buttonKondisiSubmit.setText("Upload Bukti Transaksi");
                }else  if(transaksiRental.get("StatusTransaksi").toString().equals("2")){
                    Batalkan.setVisibility(View.GONE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Dibatalkan");
                    buttonKondisiSubmit.setText("Kembali");
                }else  if(transaksiRental.get("StatusTransaksi").toString().equals("3")){
                    Batalkan.setVisibility(View.GONE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Sudah Terbayar");
                    buttonKondisiSubmit.setText("Tampilkan E-Tiket Pengambilan");
                }else  if(transaksiRental.get("StatusTransaksi").toString().equals("4")){
                    Batalkan.setVisibility(View.GONE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Sudah Diambil");
                    buttonKondisiSubmit.setText("Tampilkan E-Tiket Pengembalian");
                }else  if(transaksiRental.get("StatusTransaksi").toString().equals("5")){
                    Batalkan.setVisibility(View.GONE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Sudah Kembalikan");
                    if(transaksiRental.get("UlasanCustomer").toString().equals("")){
                        buttonKondisiSubmit.setText("Penilaian");
                    }else{
                        buttonKondisiSubmit.setText("Lihat Penilaian");
                    }
                }
            }
        });
    }

    private void setDataRental(String idMitra) {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Rental").child(idMitra).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> dataRental= (Map<String, Object>) task.getResult().getValue();
                tRental.setText(dataRental.get("NamaRental").toString());
                tAlamatRental.setText(dataRental.get("AlamatRental").toString());
            }
        });
    }
    private void setDataUser(String IdCutomer) {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Customer").child(IdCutomer).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> dataCustomer = (Map<String, Object>) task.getResult().getValue();
                Nama.setText(dataCustomer.get("NamaCustomer").toString());
                Telefon.setText(dataCustomer.get("TelefonCustomer").toString());
                Email.setText(dataCustomer.get("EmailCustomer").toString());
            }
        });
    }
    private void setDataMobil(String IdKamar) {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Rental-Detail").child(IdKamar).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> dataHotelDetail = (Map<String, Object>) task.getResult().getValue();
                tMobil.setText(dataHotelDetail.get("NamaKendaraan").toString());
            }
        });
    }
    private void setDataBank(String jenisPembayaran) {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Bank").child(jenisPembayaran).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> dataBank = (Map<String, Object>) task.getResult().getValue();
                namaPembayaran.setText(dataBank.get("NamaBank").toString());
                Glide.with(DetailTransactionRentalScreen.this).clear(logoBank);
                Glide.with(DetailTransactionRentalScreen.this)
                        .load(dataBank.get("GambarBank").toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()

                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(logoBank);
            }
        });
    }

    private void getDataInten() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idHotel = bundle.getString("idScreen");
        } else {
            idHotel = getIntent().getStringExtra("idScreen");
        }
    }

    private void SetMode() {
        //MODE
        Cursor mod = dataMode.getDataOne();
        mod.moveToFirst();
        String modeApps = "";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            modeApps = mod.getString(mod.getColumnIndexOrThrow("mode"));

            mod.moveToNext();
        }
        mod.close();
        if (modeApps.equals("Malam")) {
            //Main
            layoutUtama.setBackgroundResource(R.color.darkMode2);

            //Layout User
            textPersonal.setTextColor(getResources().getColor(R.color.white));
            bgPersonal.setBackgroundResource(R.drawable.background_payment_dark);
            judulNama.setTextColor(getResources().getColor(R.color.white));
            Nama.setTextColor(getResources().getColor(R.color.white));
            judulTelefon.setTextColor(getResources().getColor(R.color.white));
            Telefon.setTextColor(getResources().getColor(R.color.white));
            judulEmail.setTextColor(getResources().getColor(R.color.white));
            Email.setTextColor(getResources().getColor(R.color.white));

            //Layout Wisata
            textHotel.setTextColor(getResources().getColor(R.color.white));
            bgHotel.setBackgroundResource(R.drawable.background_payment_dark);
            KodeHotel.setTextColor(getResources().getColor(R.color.white));
            judulKOde.setTextColor(getResources().getColor(R.color.white));
            jRental.setTextColor(getResources().getColor(R.color.white));
            tRental.setTextColor(getResources().getColor(R.color.white));
            jAlamatRental.setTextColor(getResources().getColor(R.color.white));
            tAlamatRental.setTextColor(getResources().getColor(R.color.white));
            jMobil.setTextColor(getResources().getColor(R.color.white));
            tMobil.setTextColor(getResources().getColor(R.color.white));
            jHarga.setTextColor(getResources().getColor(R.color.white));
            tHarga.setTextColor(getResources().getColor(R.color.white));
            jJenis.setTextColor(getResources().getColor(R.color.white));
            tJenis.setTextColor(getResources().getColor(R.color.white));
            jCheckin.setTextColor(getResources().getColor(R.color.white));
            tCheckin.setTextColor(getResources().getColor(R.color.white));
            jCheckOut.setTextColor(getResources().getColor(R.color.white));
            tCheckOut.setTextColor(getResources().getColor(R.color.white));
            judulStatus.setTextColor(getResources().getColor(R.color.white));


            //Layout Payment
            textPembayaran.setTextColor(getResources().getColor(R.color.white));
            bgPembayaran.setBackgroundResource(R.drawable.background_payment_dark);
            namaPembayaran.setTextColor(getResources().getColor(R.color.white));

            //Total
            textRincian.setTextColor(getResources().getColor(R.color.white));
            bgRincian.setBackgroundResource(R.drawable.background_payment_dark);
            totalTextAnak.setTextColor(getResources().getColor(R.color.darkTxt));
            totalAnak.setTextColor(getResources().getColor(R.color.darkTxt));
            totalTextDewasa.setTextColor(getResources().getColor(R.color.darkTxt));
            totalDewasa.setTextColor(getResources().getColor(R.color.darkTxt));
            Totaltext.setTextColor(getResources().getColor(R.color.white));
            totalPembayaran.setTextColor(getResources().getColor(R.color.white));




        }
    }

    private void initialize() {
        //Layout Main
        layoutUtama = findViewById(R.id.layoutUtama);

        //Transaksi
        transactionRental = new TransactionRental();
        masterDataRentalDetail = new MasterDataRentalDetail();

        //Sweet Alert
        pDialog = new SweetAlertDialog(DetailTransactionRentalScreen.this, SweetAlertDialog.PROGRESS_TYPE);

        //Layout Top
        btnbackscroller = findViewById(R.id.btnbackscroller);

        //Layout User
        textPersonal = findViewById(R.id.textPersonal);
        bgPersonal = findViewById(R.id.bgPersonal);
        judulNama = findViewById(R.id.judulNama);
        Nama = findViewById(R.id.Nama);
        judulTelefon = findViewById(R.id.judulTelefon);
        Telefon = findViewById(R.id.Telefon);
        judulEmail = findViewById(R.id.judulEmail);
        Email = findViewById(R.id.Email);

        //Layout Hotel
        textHotel = findViewById(R.id.textWisata);
        bgHotel = findViewById(R.id.bgWisata);
        KodeHotel = findViewById(R.id.KodeWisata);
        judulKOde = findViewById(R.id.judulKOde);
        jRental = findViewById(R.id.judulWisata);
        tRental = findViewById(R.id.NamaWisata);
        jAlamatRental = findViewById(R.id.JudulAlamatWisata);
        tAlamatRental = findViewById(R.id.AlamatWisata);
        jMobil = findViewById(R.id.judulAnakKecil);
        tMobil = findViewById(R.id.hargaAnakText);
        jHarga = findViewById(R.id.judulHargaRental);
        tHarga = findViewById(R.id.jumlahDewasa);
        jJenis = findViewById(R.id.judulDewasa);
        tJenis = findViewById(R.id.hargaDewasaText);
        jCheckin = findViewById(R.id.judulCheckIn);
        tCheckin = findViewById(R.id.checkIN);
        jCheckOut = findViewById(R.id.judulCheckOut);
        tCheckOut = findViewById(R.id.checkOut);
        StatusText = findViewById(R.id.StatusText);
        judulStatus = findViewById(R.id.judulStatus);

        //LayoutMain Payment
        bgPembayaran = findViewById(R.id.bgPembayaran);
        namaPembayaran = findViewById(R.id.namaPembayaran);
        textPembayaran = findViewById(R.id.textPembayaran);
        logoBank = findViewById(R.id.arrowpayment);

        //Layout Total
        textRincian = findViewById(R.id.textRincian);
        bgRincian = findViewById(R.id.bgRincian);
        totalTextAnak = findViewById(R.id.totalTextAnak);
        totalAnak = findViewById(R.id.totalAnak);
        totalTextDewasa = findViewById(R.id.totalTextDewasa);
        totalDewasa = findViewById(R.id.totalDewasa);
        Totaltext = findViewById(R.id.Totaltext);
        totalPembayaran = findViewById(R.id.totalPembayaran);

        //Button
        buttonKondisiSubmit= findViewById(R.id.PesanSekarang);
        Batalkan= findViewById(R.id.Batalkan);


//        //List Payment popup
//        textpopup = findViewById(R.id.textpopup);
//        jenisPayment = findViewById(R.id.jenisPayment);
//        closePembayaran = findViewById(R.id.closePembayaran);
//        layoutPaymentt = findViewById(R.id.layoutPaymentt);
//        bgpaymentmain = findViewById(R.id.bgpaymentmain);
//        buttonPayment = findViewById(R.id.buttonPayment);

//        //other
        dataMode = new DataMode(this);
        transactionRentalExt= new TransactionRental();

    }
}