package com.risqi.traveland.HotelScreen;

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
import com.risqi.traveland.Firebase.MasterDataHotelDetail;
import com.risqi.traveland.Firebase.TransactionHotel;
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

public class DetailTransactionHotelScreen extends AppCompatActivity {
    //Main Menu
    private ConstraintLayout layoutUtama;
    //Top Layout
    private Button btnbackscroller;
    //Layout Detail User
    private TextView textPersonal, judulNama, Nama, judulTelefon, Telefon, judulEmail, Email;
    private ConstraintLayout bgPersonal;
    //Layout Detail Hotel
    private ConstraintLayout bgHotel;
    private TextView textHotel,KodeHotel,judulKOde,jHotel,tHotel,jAlamat,tAlamat,jKamar,tKamar,jHarga,tHarga,tJumlahKamar,jCheckin,tCheckin,jCheckOut,tCheckOut,StatusText,judulStatus;
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
    private TransactionHotel transactionHotel,transactionHotelEXt;
    private MasterDataHotelDetail masterDataHotelDetailExt;
    private Task update,update2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction_hotel_screen);
        getDataInten();
        initialize();

        SetMode();

        //SetDAta
        setDataTransaksi();
        toMenuOrder();

        //submit
        toSubmit();

        //batalkan
        toCancel();

    }

    @Override
    public void onBackPressed() {
        if(buttonKondisiSubmit.getText().equals("Upload Bukti Transaksi")){
            Intent a = new Intent(DetailTransactionHotelScreen.this, OrderingScreen.class);

            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
            onStop();

        }else if(buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket")){
            Intent a = new Intent(DetailTransactionHotelScreen.this, OrderingScreen.class);

            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
            onStop();
        }
        else if(buttonKondisiSubmit.getText().equals("Kembali")){
            Intent a = new Intent(DetailTransactionHotelScreen.this, HistoryOrderingScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
            onStop();
        }else if(buttonKondisiSubmit.getText().equals("Lihat Penilaian")){
            Intent a = new Intent(DetailTransactionHotelScreen.this, HistoryOrderingScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
            onStop();
        }
        else if(buttonKondisiSubmit.getText().equals("Penilaian")){
            Intent a = new Intent(DetailTransactionHotelScreen.this, HistoryOrderingScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
            onStop();
        }
    }

    private void toCancel() {
        Batalkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(DetailTransactionHotelScreen.this, SweetAlertDialog.WARNING_TYPE)
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

                                int jumlahTotal = jumlahKamarAsli+jumlahKamarOrder;

                                HashMap updateDataKamar = masterDataHotelDetailExt.updateJumlahKamar(""+jumlahTotal);
                                update2 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Hotel-Detail").child(iDetail).updateChildren(updateDataKamar);
                                update2.addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        HashMap updateData = transactionHotelEXt.updateBatalkan("2");
                                        update = FirebaseDatabase.getInstance().getReference().child("Transaction-Hotel").child(idHotel).updateChildren(updateData);
                                        update.addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task2) {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        pDialog.dismiss();
                                                        setDataTransaksi();
                                                        new SweetAlertDialog(DetailTransactionHotelScreen.this, SweetAlertDialog.SUCCESS_TYPE)
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
                                });

                            }
                        }).show();
            }
        });
    }

    private void toSubmit() {
        buttonKondisiSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonKondisiSubmit.getText().equals("Upload Bukti Transaksi")){
                    Intent a = new Intent(DetailTransactionHotelScreen.this, TakePhotoTransactionScreen.class);
                    a.putExtra("idScreen",idHotel);
                    a.putExtra("jenisScreen","Hotel");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionHotelScreen.this);
                    onStop();

                }else if(buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket")){
                    Intent a = new Intent(DetailTransactionHotelScreen.this, ETicketScreen.class);
                    a.putExtra("idScreen",idHotel);
                    a.putExtra("jenisScreen","Hotel");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionHotelScreen.this);
                    onStop();
                }
                else if(buttonKondisiSubmit.getText().equals("Kembali")){
                    Intent a = new Intent(DetailTransactionHotelScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
                    onStop();
                }else if(buttonKondisiSubmit.getText().equals("Lihat Penilaian")){
                    Intent a = new Intent(DetailTransactionHotelScreen.this, ReviewRatingScreen.class);
                    a.putExtra("idScreen",idHotel);
                    a.putExtra("jenisScreen","Hotel");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionHotelScreen.this);
                    onStop();
                }
                else if(buttonKondisiSubmit.getText().equals("Penilaian")){
                    Intent a = new Intent(DetailTransactionHotelScreen.this, RatingScreen.class);
                    a.putExtra("idScreen",idHotel);
                    a.putExtra("jenisScreen","Hotel");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionHotelScreen.this);
                    onStop();
                }
            }
        });
    }

    private void toMenuOrder() {
        btnbackscroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonKondisiSubmit.getText().equals("Upload Bukti Transaksi")){
                    Intent a = new Intent(DetailTransactionHotelScreen.this, OrderingScreen.class);

                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
                    onStop();

                }else if(buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket")){
                    Intent a = new Intent(DetailTransactionHotelScreen.this, OrderingScreen.class);

                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
                    onStop();
                }
                else if(buttonKondisiSubmit.getText().equals("Kembali")){
                    Intent a = new Intent(DetailTransactionHotelScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
                    onStop();
                }else if(buttonKondisiSubmit.getText().equals("Lihat Penilaian")){
                    Intent a = new Intent(DetailTransactionHotelScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
                    onStop();
                }
                else if(buttonKondisiSubmit.getText().equals("Penilaian")){
                    Intent a = new Intent(DetailTransactionHotelScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionHotelScreen.this);
                    onStop();
                }
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

    private void setDataTransaksi() {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Transaction-Hotel").child(idHotel).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> transaksiHotel = (Map<String, Object>) task.getResult().getValue();
                setDataUser(transaksiHotel.get("IdCutomer").toString());
                setDataHotel(transaksiHotel.get("IdMitra").toString());
                setDataKamar(transaksiHotel.get("IdKamar").toString());
                setDataBank(transaksiHotel.get("JenisPembayaran").toString());
                iDetail= transaksiHotel.get("IdKamar").toString();

                KodeHotel.setText(idHotel);

                tHarga.setText("Rp."+transaksiHotel.get("HargaKamar"));
                tJumlahKamar.setText("x"+transaksiHotel.get("JumlahKamar")+" Kamar");
                jumlahKamarOrder = Integer.parseInt(transaksiHotel.get("JumlahKamar").toString());
                tCheckin.setText(transaksiHotel.get("CheckIn").toString());
                tCheckOut.setText(transaksiHotel.get("CheckOut").toString());

                totalTextDewasa.setText("Total Sewa ("+transaksiHotel.get("JumlahHari")+" Hari)");
                totalDewasa.setText("Rp."+transaksiHotel.get("TotalSemua"));
                totalPembayaran.setText("Rp."+transaksiHotel.get("TotalSemua"));


                if(transaksiHotel.get("StatusTransaksi").toString().equals("1")){
                    Batalkan.setVisibility(View.VISIBLE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Belum Terbayar");
                    buttonKondisiSubmit.setText("Upload Bukti Transaksi");
                }else  if(transaksiHotel.get("StatusTransaksi").toString().equals("2")){
                    Batalkan.setVisibility(View.GONE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Dibatalkan");
                    buttonKondisiSubmit.setText("Kembali");
                }else  if(transaksiHotel.get("StatusTransaksi").toString().equals("3")){
                    Batalkan.setVisibility(View.GONE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Sudah Terbayar");
                    buttonKondisiSubmit.setText("Tampilkan E-Tiket");
                }else  if(transaksiHotel.get("StatusTransaksi").toString().equals("4")){
                    Batalkan.setVisibility(View.GONE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Tiket Sudah Digunakan");
                    if(transaksiHotel.get("UlasanCustomer").toString().equals("")){
                        buttonKondisiSubmit.setText("Penilaian");
                    }else{
                        buttonKondisiSubmit.setText("Lihat Penilaian");
                    }
                }
            }
        });
    }

    private void setDataHotel(String idMitra) {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Hotel").child(idMitra).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> dataHotel = (Map<String, Object>) task.getResult().getValue();
                tHotel.setText(dataHotel.get("NamaHotel").toString());
                tAlamat.setText(dataHotel.get("AlamatHotel").toString());
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
    private void setDataKamar(String IdKamar) {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Hotel-Detail").child(IdKamar).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> dataHotelDetail = (Map<String, Object>) task.getResult().getValue();
                tKamar.setText(dataHotelDetail.get("NamaKamar").toString());
                jumlahKamarAsli = Integer.parseInt(dataHotelDetail.get("JumlahKamar").toString());
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
                Glide.with(DetailTransactionHotelScreen.this).clear(logoBank);
                Glide.with(DetailTransactionHotelScreen.this)
                        .load(dataBank.get("GambarBank").toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()

                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(logoBank);
            }
        });
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
            jHotel.setTextColor(getResources().getColor(R.color.white));
            tHotel.setTextColor(getResources().getColor(R.color.white));
            jAlamat.setTextColor(getResources().getColor(R.color.white));
            tAlamat.setTextColor(getResources().getColor(R.color.white));
            jKamar.setTextColor(getResources().getColor(R.color.white));
            tKamar.setTextColor(getResources().getColor(R.color.white));
            jHarga.setTextColor(getResources().getColor(R.color.white));
            tHarga.setTextColor(getResources().getColor(R.color.white));
            tJumlahKamar.setTextColor(getResources().getColor(R.color.white));
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
        transactionHotel = new TransactionHotel();
        masterDataHotelDetailExt = new MasterDataHotelDetail();

        //Sweet Alert
        pDialog = new SweetAlertDialog(DetailTransactionHotelScreen.this, SweetAlertDialog.PROGRESS_TYPE);

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
        jHotel = findViewById(R.id.judulWisata);
        tHotel = findViewById(R.id.NamaWisata);
        jAlamat = findViewById(R.id.JudulAlamatWisata);
        tAlamat = findViewById(R.id.AlamatWisata);
        jKamar = findViewById(R.id.judulAnakKecil);
        tKamar = findViewById(R.id.hargaAnakText);
        jHarga = findViewById(R.id.judulDewasa);
        tHarga = findViewById(R.id.hargaDewasaText);
        tJumlahKamar = findViewById(R.id.jumlahDewasa);
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
        transactionHotelEXt= new TransactionHotel();

    }
}