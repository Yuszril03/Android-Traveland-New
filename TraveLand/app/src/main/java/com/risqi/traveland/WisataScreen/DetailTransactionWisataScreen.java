package com.risqi.traveland.WisataScreen;

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
import com.risqi.traveland.Firebase.TransactionWIisata;
import com.risqi.traveland.OrderingScreen.OrderingScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.RatingScreen.RatingScreen;
import com.risqi.traveland.RatingScreen.ReviewRatingScreen;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.SettingScreen.HistoryOrderingScreen;
import com.risqi.traveland.TakePhotoTransaction.TakePhotoTransactionScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailTransactionWisataScreen extends AppCompatActivity {

    //Main Menu
    private ConstraintLayout layoutUtama;
    //Top Layout
    private Button btnbackscroller;
    //Layout Detail User
    private TextView textPersonal, judulNama, Nama, judulTelefon, Telefon, judulEmail, Email;
    private ConstraintLayout bgPersonal;
    //Layout Detail Wisata
    private ConstraintLayout bgWisata;
    private TextView maxPembayaran, KodeWisata, judulKOde, jumlahDewasa, jumlahAnak, textWisata, judulWisata, NamaWisata, JudulAlamatWisata, AlamatWisata, judulAnakKecil, hargaAnakText, judulDewasa, hargaDewasaText, StatusText, judulStatus;
    //LayoutMain payment
    private ImageView logoBank;
    private ConstraintLayout bgPembayaran;
    private TextView namaPembayaran, textPembayaran;
    //Layout Total
    private ConstraintLayout bgRincian;
    private TextView textRincian, totalTextAnak, totalAnak, totalTextDewasa, totalDewasa, Totaltext, totalPembayaran;

    //Button Submit
    private Button buttonKondisiSubmit, Batalkan;

    //Other
    private String idWisata = "";
    private DatabaseReference database1, database2;
    private DataMode dataMode;
    private SweetAlertDialog pDialog;
    private TransactionWIisata transactionWIisata;
    private Task update;
    private TransactionWIisata transactionWisataExt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction_wisata_screen);
        getDataInten();
        initialize();
        //SetMode
        setMode();

        //SetDAta
        setDataTransaksi();

        toMenuOrder();

        //submit
        toSubmit();

        //batalkan
        toCancel();

    }

    private void toCancel() {
        Batalkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(DetailTransactionWisataScreen.this, SweetAlertDialog.WARNING_TYPE)
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

                                HashMap updateData = transactionWisataExt.updateBatalkan("2");
                                update = FirebaseDatabase.getInstance().getReference().child("Transaction-Wisata").child(idWisata).updateChildren(updateData);
                                update.addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                pDialog.dismiss();
                                                setDataTransaksi();
                                                new SweetAlertDialog(DetailTransactionWisataScreen.this, SweetAlertDialog.SUCCESS_TYPE)
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

    private void toSubmit() {
        buttonKondisiSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonKondisiSubmit.getText().equals("Upload Bukti Transaksi")) {
                    Intent a = new Intent(DetailTransactionWisataScreen.this, TakePhotoTransactionScreen.class);
                    a.putExtra("idScreen", idWisata);
                    a.putExtra("jenisScreen", "Wisata");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionWisataScreen.this);
                    onStop();

                } else if (buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket")) {
                    Intent a = new Intent(DetailTransactionWisataScreen.this, ETicketScreen.class);
                    a.putExtra("idScreen", idWisata);
                    a.putExtra("jenisScreen", "Wisata");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionWisataScreen.this);
                    onStop();
                } else if (buttonKondisiSubmit.getText().equals("Kembali")) {
                    Intent a = new Intent(DetailTransactionWisataScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
                    onStop();
                } else if (buttonKondisiSubmit.getText().equals("Lihat Penilaian")) {
                    Intent a = new Intent(DetailTransactionWisataScreen.this, ReviewRatingScreen.class);
                    a.putExtra("idScreen", idWisata);
                    a.putExtra("jenisScreen", "Wisata");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionWisataScreen.this);
                    onStop();
                } else if (buttonKondisiSubmit.getText().equals("Penilaian")) {
                    Intent a = new Intent(DetailTransactionWisataScreen.this, RatingScreen.class);
                    a.putExtra("idScreen", idWisata);
                    a.putExtra("jenisScreen", "Wisata");
                    startActivity(a);
                    Animatoo.animateSlideUp(DetailTransactionWisataScreen.this);
                    onStop();
                }
            }
        });
    }

    private void setDataTransaksi() {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Transaction-Wisata").child(idWisata).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> transaksiWisata = (Map<String, Object>) task.getResult().getValue();
                setDataUser(transaksiWisata.get("IdCutomer").toString());
                setDataWisata(transaksiWisata.get("IdMitra").toString());
                setDataBank(transaksiWisata.get("JenisPembayaran").toString());

                KodeWisata.setText(idWisata);

                jumlahDewasa.setText(transaksiWisata.get("JumlahDewasa").toString() + "x");
                jumlahAnak.setText(transaksiWisata.get("JumlahAnak").toString() + "x");
                hargaAnakText.setText("Rp." + transaksiWisata.get("HargaAnak").toString());
                hargaDewasaText.setText("Rp." + transaksiWisata.get("HargaDewasa").toString());
                totalAnak.setText("Rp." + transaksiWisata.get("TotalAnak").toString());
                totalDewasa.setText("Rp." + transaksiWisata.get("TotalDewasa").toString());
                totalPembayaran.setText("Rp." + transaksiWisata.get("TotalSemua").toString());

                if (transaksiWisata.get("StatusTransaksi").toString().equals("1")) {
                    maxPembayaran.setVisibility(View.VISIBLE);
                    String [] split1 = transaksiWisata.get("TanggalBuat").toString().split(" ");
                    String [] split2 = split1[0].split("/");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
                    Calendar myCalendar = Calendar.getInstance();
                    myCalendar.set(Integer.parseInt(split2[2]),Integer.parseInt(split2[1])-1,Integer.parseInt(split2[0]));
                    myCalendar.add(Calendar.DATE,1);
                    maxPembayaran.setText("(Max Pembayaran : "+simpleDateFormat.format(myCalendar.getTime())+")");

                    Batalkan.setVisibility(View.VISIBLE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Belum Terbayar");
                    buttonKondisiSubmit.setText("Upload Bukti Transaksi");
                } else if (transaksiWisata.get("StatusTransaksi").toString().equals("2")) {
                    maxPembayaran.setVisibility(View.GONE);
                    Batalkan.setVisibility(View.GONE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Dibatalkan");
                    buttonKondisiSubmit.setText("Kembali");
                } else if (transaksiWisata.get("StatusTransaksi").toString().equals("3")) {
                    maxPembayaran.setVisibility(View.GONE);
                    Batalkan.setVisibility(View.GONE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Sudah Terbayar");
                    buttonKondisiSubmit.setText("Tampilkan E-Tiket");
                } else if (transaksiWisata.get("StatusTransaksi").toString().equals("4")) {
                    maxPembayaran.setVisibility(View.GONE);
                    Batalkan.setVisibility(View.GONE);
                    StatusText.setTextColor(getResources().getColor(R.color.secondary));
                    StatusText.setText("Tiket Sudah Digunakan");
                    if (transaksiWisata.get("UlasanCustomer").toString().equals("")) {
                        buttonKondisiSubmit.setText("Penilaian");
                    } else {
                        buttonKondisiSubmit.setText("Lihat Penilaian");
                    }
                }

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
                Glide.with(DetailTransactionWisataScreen.this).clear(logoBank);
                Glide.with(DetailTransactionWisataScreen.this)
                        .load(dataBank.get("GambarBank").toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()

                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(logoBank);
            }
        });
    }

    private void setDataWisata(String idMitra) {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Wisata").child(idMitra).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> dataWisata = (Map<String, Object>) task.getResult().getValue();
                NamaWisata.setText(dataWisata.get("NamaWisata").toString());
                AlamatWisata.setText(dataWisata.get("AlamatWisata").toString());
            }
        });
    }

    private void setDataUser(String idCutomer) {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Customer").child(idCutomer).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> dataCustomer = (Map<String, Object>) task.getResult().getValue();
                Nama.setText(dataCustomer.get("NamaCustomer").toString());
                Telefon.setText(dataCustomer.get("TelefonCustomer").toString());
                Email.setText(dataCustomer.get("EmailCustomer").toString());
            }
        });
    }

    private void toMenuOrder() {
        btnbackscroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonKondisiSubmit.getText().equals("Upload Bukti Transaksi")) {
                    Intent a = new Intent(DetailTransactionWisataScreen.this, OrderingScreen.class);

                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
                    onStop();

                } else if (buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket")) {
                    Intent a = new Intent(DetailTransactionWisataScreen.this, OrderingScreen.class);

                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
                    onStop();
                } else if (buttonKondisiSubmit.getText().equals("Kembali")) {
                    Intent a = new Intent(DetailTransactionWisataScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
                    onStop();
                } else if (buttonKondisiSubmit.getText().equals("Lihat Penilaian")) {
                    Intent a = new Intent(DetailTransactionWisataScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
                    onStop();
                } else if (buttonKondisiSubmit.getText().equals("Penilaian")) {
                    Intent a = new Intent(DetailTransactionWisataScreen.this, HistoryOrderingScreen.class);
                    startActivity(a);
                    Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
                    onStop();
                }
            }
        });
    }

    private void initialize() {

        //Layout Main
        layoutUtama = findViewById(R.id.layoutUtama);

        //Transaksi
        transactionWIisata = new TransactionWIisata();

        //Sweet Alert
        pDialog = new SweetAlertDialog(DetailTransactionWisataScreen.this, SweetAlertDialog.PROGRESS_TYPE);

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

//        Layout Detail Wisata
        KodeWisata = findViewById(R.id.KodeWisata);
        judulKOde = findViewById(R.id.judulKOde);
        jumlahAnak = findViewById(R.id.jumlahAnak);
        jumlahDewasa = findViewById(R.id.jumlahDewasa);
        textWisata = findViewById(R.id.textWisata);
        bgWisata = findViewById(R.id.bgWisata);
        judulWisata = findViewById(R.id.judulWisata);
        NamaWisata = findViewById(R.id.NamaWisata);
        JudulAlamatWisata = findViewById(R.id.JudulAlamatWisata);
        AlamatWisata = findViewById(R.id.AlamatWisata);
        judulAnakKecil = findViewById(R.id.judulAnakKecil);
        judulDewasa = findViewById(R.id.judulDewasa);
        hargaAnakText = findViewById(R.id.hargaAnakText);
        hargaDewasaText = findViewById(R.id.hargaDewasaText);
        StatusText = findViewById(R.id.StatusText);
        judulStatus = findViewById(R.id.judulStatus);
        maxPembayaran = findViewById(R.id.maxPembayaran);

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
        buttonKondisiSubmit = findViewById(R.id.PesanSekarang);
        Batalkan = findViewById(R.id.Batalkan);

        pDialog = new SweetAlertDialog(DetailTransactionWisataScreen.this, SweetAlertDialog.PROGRESS_TYPE);

//        //List Payment popup
//        textpopup = findViewById(R.id.textpopup);
//        jenisPayment = findViewById(R.id.jenisPayment);
//        closePembayaran = findViewById(R.id.closePembayaran);
//        layoutPaymentt = findViewById(R.id.layoutPaymentt);
//        bgpaymentmain = findViewById(R.id.bgpaymentmain);
//        buttonPayment = findViewById(R.id.buttonPayment);

//        //other
        dataMode = new DataMode(this);
        transactionWisataExt = new TransactionWIisata();
    }

    private void getDataInten() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idWisata = bundle.getString("idScreen");
        } else {
            idWisata = getIntent().getStringExtra("idScreen");
        }
    }

    private void setMode() {
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
            textWisata.setTextColor(getResources().getColor(R.color.white));
            bgWisata.setBackgroundResource(R.drawable.background_payment_dark);
            judulKOde.setTextColor(getResources().getColor(R.color.white));
            KodeWisata.setTextColor(getResources().getColor(R.color.white));
            judulWisata.setTextColor(getResources().getColor(R.color.white));
            NamaWisata.setTextColor(getResources().getColor(R.color.white));
            JudulAlamatWisata.setTextColor(getResources().getColor(R.color.white));
            AlamatWisata.setTextColor(getResources().getColor(R.color.white));
            judulAnakKecil.setTextColor(getResources().getColor(R.color.white));
            hargaAnakText.setTextColor(getResources().getColor(R.color.white));
            jumlahAnak.setTextColor(getResources().getColor(R.color.white));
            judulDewasa.setTextColor(getResources().getColor(R.color.white));
            hargaDewasaText.setTextColor(getResources().getColor(R.color.white));
            jumlahDewasa.setTextColor(getResources().getColor(R.color.white));
            judulStatus.setTextColor(getResources().getColor(R.color.white));
            maxPembayaran.setTextColor(getResources().getColor(R.color.white));


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

    @Override
    public void onBackPressed() {
        if (buttonKondisiSubmit.getText().equals("Upload Bukti Transaksi")) {
            Intent a = new Intent(DetailTransactionWisataScreen.this, OrderingScreen.class);

            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
            onStop();

        } else if (buttonKondisiSubmit.getText().equals("Tampilkan E-Tiket")) {
            Intent a = new Intent(DetailTransactionWisataScreen.this, OrderingScreen.class);

            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
            onStop();
        } else if (buttonKondisiSubmit.getText().equals("Kembali")) {
            Intent a = new Intent(DetailTransactionWisataScreen.this, HistoryOrderingScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
            onStop();
        } else if (buttonKondisiSubmit.getText().equals("Lihat Penilaian")) {
            Intent a = new Intent(DetailTransactionWisataScreen.this, HistoryOrderingScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
            onStop();
        } else if (buttonKondisiSubmit.getText().equals("Penilaian")) {
            Intent a = new Intent(DetailTransactionWisataScreen.this, HistoryOrderingScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailTransactionWisataScreen.this);
            onStop();
        }
    }
}