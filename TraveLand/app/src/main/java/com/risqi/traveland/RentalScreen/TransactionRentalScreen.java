package com.risqi.traveland.RentalScreen;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.MasterDataBank;
import com.risqi.traveland.Firebase.MasterDataRentalDetail;
import com.risqi.traveland.Firebase.TransactionRental;
import com.risqi.traveland.NotifSucess.NotifTransactionSuccessScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TransactionRentalScreen extends AppCompatActivity {

    //Firebase or Other
    private DatabaseReference database1, database2,database3;
    private Task updateData1;
    private String idHotel,idDetail,NIKUSER;
    private DataMode dataMode;
    private DataLoginUser dataLoginUser;
    private SweetAlertDialog pDialog;

    private TransactionRental transactionRental ;
    private MasterDataRentalDetail masterDataRentalDetail ;

    //Layout Main
    private ConstraintLayout layoutUtama;

    //Layout Top
    private Button btnbackscroller;

    //Layout Detail User
    private TextView textPersonal, judulNama, Nama, judulTelefon, Telefon, judulEmail, Email;
    private ConstraintLayout bgPersonal;

    //Layout Detail Wisata
    private ConstraintLayout bgHotel,bgCheckin,bgCheckout;
    private ImageView calendarCheckIn,calendarCheckOut;
    private EditText tCheckin,tCheckOut;
    private Button PlusKamar, minusKamar;
    private TextView tetxtPemesanan, jRental,tRental,jAlamatRental,tAlamatRental,jMobil,tMobil,jJenis,tJenis,jHarga,tHarga,jCheckin,jCheckout,tempDate;

    //LayoutMain payment
    private ImageView arrowpayment;
    private ConstraintLayout bgPembayaran;
    private TextView namaPembayaran, textPembayaran;

    //Layout Total
    private ConstraintLayout bgRincian;
    private TextView textRincian, totalTextAnak, totalAnak, totalTextDewasa, totalDewasa, Totaltext, totalPembayaran;

    //Button Submit
    private Button PesanSekarang;

    //Payment List popup
    private Button closePembayaran, buttonPayment;
    private TextView textpopup;
    private ConstraintLayout layoutPaymentt, bgpaymentmain;
    private List<MasterDataBank> masterDataBanks = new ArrayList<>();
    private RecyclerView jenisPayment;
    private List<String> listPayment = new ArrayList<>();
    private TransactionRentalScreen.PaymentRecyclerViewAdapter paymentRecyclerViewAdapter;
    private String idPayment = "";
    private String namePaymentTemps = "";
    private String namePayment = "";
    private String keyPayments = "";
    private int HargaAnak = 0, tempAnak = 0,totalAnakFinal=0;
    private int HargaDewasa = 0, tempDewasa = 0,totalDewasaFinal=0;
    private int TotalHarga = 0;
    private int showPayment=0;

    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar2 = Calendar.getInstance();
    final Calendar calendarCreated = Calendar.getInstance();
    final Calendar calendarTemp = Calendar.getInstance();
    final Calendar calendarTemp2 = Calendar.getInstance();
    final Calendar nowDate = Calendar.getInstance();
    final Calendar nowDate2 = Calendar.getInstance();
    private int jumlahKamarr=0;
    private int hargakamarr=0;

    private String polaCOnvert = "yyyy-MM-dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_rental_screen);

        getIntents();

        initiliaze();

        setMode();

        SetData();

        toMenu();

        defaultTanggal();
        SetTanggalIN();
        SetTanggalout();


        //Payment
        setDataPayment();
        swipeUPlistPayment(0);
        setNamePayment("");
        setShowPayment();
        setHidePayment();
        setpaymentChoice();

        pesanNow();

    }

    private void pesanNow() {
        PesanSekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(TransactionRentalScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Anda Yakin")
                        .setContentText("Untuk melanjutkan transaksi ini!")
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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(keyPayments.equals("")){
                                            pDialog.dismiss();
                                            new SweetAlertDialog(TransactionRentalScreen.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Oops...")
                                                    .setContentText("Jenis Pembayaran Harap Dipilih!")
                                                    .setConfirmText("Okey")
                                                    .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                                    .show();
                                        }else{
                                            sendTranksaksi();
                                        }
                                    }
                                }, 2000);

                            }
                        }).show();

            }
        });
    }

    private void sendTranksaksi() {
        int value = 1;
        Date date1 = ConvertStringToDate(tCheckin.getText().toString(), polaCOnvert, null);
        Date date2 = ConvertStringToDate(tCheckOut.getText().toString(), polaCOnvert, null);
        long tempDataDate = Math.abs(date2.getTime() - date1.getTime());
        long selisihHari = tempDataDate / (24 * 60 * 60 * 1000);
        long totalJUmlah = (hargakamarr*((value)*selisihHari));

        String key=RandGeneratedStr(12);
        Map<String, String> insertData = transactionRental.insertData(NIKUSER,idHotel,idDetail,hargakamarr+"",tJenis.getText().toString(),tCheckin.getText().toString(),tCheckOut.getText().toString(),keyPayments,""+selisihHari,""+totalJUmlah);
        database1 = FirebaseDatabase.getInstance().getReference().child("Transaction-Rental").child(key);
        database1.setValue(insertData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent a = new Intent(TransactionRentalScreen.this, NotifTransactionSuccessScreen.class);
                a.putExtra("Next", "UploadFotoRental");
                a.putExtra("idScreen", ""+key);
                startActivity(a);
                Animatoo.animateSlideLeft(TransactionRentalScreen.this);
                onStop();
            }
        });

    }
    private String RandGeneratedStr(int l)

    {

        // a list of characters to choose from in form of a string

        String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

        // creating a StringBuffer size of AlphaNumericStr

        StringBuilder s = new StringBuilder(l);

        int i;

        for ( i=0; i<l; i++) {

            //generating a random number using math.random()

            int ch = (int)(AlphaNumericStr.length() * Math.random());

            //adding Random character one by one at the end of s

            s.append(AlphaNumericStr.charAt(ch));

        }

        return s.toString();

    }

    private void SetTanggalout() {
        //set Datepicker
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, month);
                myCalendar2.set(Calendar.DAY_OF_MONTH, day);

                updateLabelCheckIout();
            }
        };

        tCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialogs = new DatePickerDialog(TransactionRentalScreen.this, date, calendarTemp.get(Calendar.YEAR), calendarTemp.get(Calendar.MONTH), calendarTemp.get(Calendar.DAY_OF_MONTH));
                calendarTemp2.setTimeInMillis(myCalendar.getTimeInMillis());
                nowDate2.setTimeInMillis(myCalendar.getTimeInMillis());
                calendarTemp2.add(Calendar.DATE,1);
                nowDate2.add(Calendar.DATE,Integer.parseInt(tempDate.getText().toString()));
                datePickerDialogs.getDatePicker().setMinDate(calendarTemp2.getTimeInMillis());
                datePickerDialogs.getDatePicker().setMaxDate(nowDate2.getTimeInMillis());
//                datePickerDialogs.getDatePicker().set(myCalendar.getTimeInMillis()+10);
                datePickerDialogs.show();

            }
        });
    }

    private void updateLabelCheckIout() {
        //        String myFormat = "dd/MM/yyyy";
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        calendarTemp.setTimeInMillis(myCalendar2.getTimeInMillis());
        tCheckOut.setText(dateFormat.format(myCalendar2.getTime()));

        int value = 1;
        Date date1 = ConvertStringToDate(tCheckin.getText().toString(), polaCOnvert, null);
        Date date2 = ConvertStringToDate(tCheckOut.getText().toString(), polaCOnvert, null);
        long tempDataDate = Math.abs(date2.getTime() - date1.getTime());
        long selisihHari = tempDataDate / (24 * 60 * 60 * 1000);
        totalTextDewasa.setText("Total Sewa ("+(selisihHari)+" Hari)");
        totalDewasa.setText("Rp."+(hargakamarr*((value)*selisihHari)));
        totalPembayaran.setText("Rp."+(hargakamarr*((value)*selisihHari)));

    }

    private void defaultTanggal() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        tCheckin.setText(dateFormat.format(nowDate.getTime()));
        nowDate.add(Calendar.DATE,1);
        tCheckOut.setText(dateFormat.format(nowDate.getTime()));

    }

    private void SetTanggalIN() {
        //set Datepicker
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabelCheckIN();
            }
        };

        tCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialogs = new DatePickerDialog(TransactionRentalScreen.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogs.getDatePicker().setMinDate(calendarCreated.getTimeInMillis()+1);
                datePickerDialogs.show();

            }
        });

    }

    private void updateLabelCheckIN() {
//        String myFormat = "dd/MM/yyyy";
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        tCheckin.setText(dateFormat.format(myCalendar.getTime()));
        calendarTemp.setTimeInMillis(myCalendar.getTimeInMillis()+1);
        calendarTemp.add(Calendar.DATE,1);
        SetTanggalout();
        tCheckOut.setText(dateFormat.format(calendarTemp.getTimeInMillis()));

        int value = 1;
        Date date1 = ConvertStringToDate(tCheckin.getText().toString(), polaCOnvert, null);
        Date date2 = ConvertStringToDate(tCheckOut.getText().toString(), polaCOnvert, null);
        long tempDataDate = Math.abs(date2.getTime() - date1.getTime());
        long selisihHari = tempDataDate / (24 * 60 * 60 * 1000);
        totalTextDewasa.setText("Total Sewa ("+selisihHari+" Hari)");
        totalDewasa.setText("Rp."+(hargakamarr*((value)*selisihHari)));
        totalPembayaran.setText("Rp."+(hargakamarr*((value)*selisihHari)));

    }

    private Date ConvertStringToDate(String tanggalDanWaktuStr, String pola, Locale lokal){
        Date tanggalDate = null;
        SimpleDateFormat formatter;
        if (lokal == null) {
            formatter = new SimpleDateFormat(pola);
        } else {
            formatter = new SimpleDateFormat(pola, lokal);
        }
        try {
            tanggalDate = formatter.parse(tanggalDanWaktuStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return tanggalDate;
    }

    private void SetData() {//User
        Cursor mod = dataLoginUser.getDataOne();
        mod.moveToFirst();
        String NIK = "";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            NIK = mod.getString(mod.getColumnIndexOrThrow("nik"));
            NIKUSER = mod.getString(mod.getColumnIndexOrThrow("nik"));

            mod.moveToNext();
        }
        mod.close();

        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Customer").child(NIK).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> user = (Map<String, Object>) task.getResult().getValue();
                Nama.setText(wordCase(user.get("NamaCustomer").toString()));
                Telefon.setText(user.get("TelefonCustomer").toString());
                Email.setText(user.get("EmailCustomer").toString());

            }
        });

        database2 = FirebaseDatabase.getInstance().getReference();
        database2.child("Master-Data-Rental").child(idHotel).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> hotel = (Map<String, Object>) task.getResult().getValue();
                tRental.setText(wordCase(hotel.get("NamaRental").toString()));
                tAlamatRental.setText(wordCase(hotel.get("AlamatRental").toString()));
            }
        });


        database3= FirebaseDatabase.getInstance().getReference();
        database3.child("Master-Data-Rental-Detail").child(idDetail).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> Detailhotel = (Map<String, Object>) task.getResult().getValue();
                tMobil.setText(wordCase(Detailhotel.get("NamaKendaraan").toString()));
                tempDate.setText(wordCase(Detailhotel.get("MaksimalRentalMobil").toString()));
                tJenis.setText(wordCase(Detailhotel.get("UkuranKendaraan").toString()));
                tHarga.setText("Rp."+wordCase(Detailhotel.get("HargaSewa").toString()));
                hargakamarr = Integer.parseInt(Detailhotel.get("HargaSewa").toString());
                totalTextDewasa.setText("Total Sewa ("+1+" Hari)");
                totalDewasa.setText("Rp."+wordCase(Detailhotel.get("HargaSewa").toString()));
                totalPembayaran.setText("Rp."+wordCase(Detailhotel.get("HargaSewa").toString()));

            }
        });


    }

    private String wordCase(String str) {
        String words[] = str.split("\\s");
        String capitalizeWord = "";
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase() + afterfirst + " ";
        }
        return capitalizeWord.trim();
    }

    private void setpaymentChoice() {
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyPayments = idPayment;
                namePayment = namePaymentTemps;
                setNamePayment(namePayment);
                swipeUPlistPayment(0);
            }
        });
    }
    private void setHidePayment() {
        closePembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idPayment = "";
                namePaymentTemps = "";
                showPayment=0;
                swipeUPlistPayment(0);
            }
        });
    }
    private void setShowPayment() {
        bgPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPayment=1;
                swipeUPlistPayment(1);
            }
        });
    }
    private void setNamePayment(String s) {
        if (s.equals("")) {
            namaPembayaran.setText("Pilih Pembayaran");
        } else {
            namaPembayaran.setText(s);
        }
    }
    private void swipeUPlistPayment(int i) {
        if (i == 1) {
            idPayment = keyPayments;
            namePaymentTemps = namePayment;
            setDataPayment();
            bgpaymentmain.setAlpha(0.0f);
            bgpaymentmain.setTranslationY(bgpaymentmain.getHeight());
            bgpaymentmain.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            bgpaymentmain.setVisibility(View.VISIBLE);
                            layoutPaymentt.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
        } else {
            bgpaymentmain.setAlpha(1.0f);
            bgpaymentmain.setTranslationY(0);
            bgpaymentmain.animate()
                    .translationY(bgpaymentmain.getHeight())
                    .alpha(0.0f)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            bgpaymentmain.setVisibility(View.GONE);
                            layoutPaymentt.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
        }
    }

    private void setDataPayment() {
        paymentRecyclerViewAdapter = new TransactionRentalScreen.PaymentRecyclerViewAdapter(TransactionRentalScreen.this, masterDataBanks, listPayment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        jenisPayment.setLayoutManager(layoutManager);
        jenisPayment.setItemAnimator(new DefaultItemAnimator());
        jenisPayment.setAdapter(paymentRecyclerViewAdapter);

        database1 = FirebaseDatabase.getInstance().getReference("Master-Data-Bank");
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                masterDataBanks.clear();
                listPayment.clear();
                for (DataSnapshot posdata : snapshot.getChildren()) {
                    MasterDataBank banks = posdata.getValue(MasterDataBank.class);
                    masterDataBanks.add(banks);
                    listPayment.add(posdata.getKey());
                }
                paymentRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getIntents() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idHotel = bundle.getString("idMaster");
            idDetail = bundle.getString("idDetail");
        } else {
            idHotel = getIntent().getStringExtra("idMaster");
            idDetail = getIntent().getStringExtra("idDetail");
        }
    }

    private void toMenu() {
        btnbackscroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(TransactionRentalScreen.this, DetailRentalScreen.class);
                a.putExtra("idDetail", "" +idDetail);
                a.putExtra("idMaster", "" + idHotel);
                startActivity(a);
                Animatoo.animateSlideLeft(TransactionRentalScreen.this);
                onStop();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(showPayment==0){
            Intent a = new Intent(TransactionRentalScreen.this, DetailRentalScreen.class);
            a.putExtra("idDetail", "" +idDetail);
            a.putExtra("idMaster", "" + idHotel);
            startActivity(a);
            Animatoo.animateSlideLeft(TransactionRentalScreen.this);
            onStop();
        }else{
            idPayment = "";
            namePaymentTemps = "";
            showPayment=0;
            swipeUPlistPayment(0);
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

            //LayoutHotel
            tetxtPemesanan.setTextColor(getResources().getColor(R.color.white));
            bgHotel.setBackgroundResource(R.drawable.background_payment_dark);
            bgCheckin.setBackgroundResource(R.drawable.form_control_10dp_dark);
            bgCheckout.setBackgroundResource(R.drawable.form_control_10dp_dark);
            calendarCheckIn.setBackgroundResource(R.drawable.icon_date);
            calendarCheckOut.setBackgroundResource(R.drawable.icon_date);
            jRental.setTextColor(getResources().getColor(R.color.white));
            tRental.setTextColor(getResources().getColor(R.color.white));
            jAlamatRental.setTextColor(getResources().getColor(R.color.white));
            tAlamatRental.setTextColor(getResources().getColor(R.color.white));
            jMobil.setTextColor(getResources().getColor(R.color.white));
            tMobil.setTextColor(getResources().getColor(R.color.white));
            jJenis.setTextColor(getResources().getColor(R.color.white));
            tJenis.setTextColor(getResources().getColor(R.color.white));
            jHarga.setTextColor(getResources().getColor(R.color.white));
            tHarga.setTextColor(getResources().getColor(R.color.white));
            jCheckin.setTextColor(getResources().getColor(R.color.white));
            jCheckout.setTextColor(getResources().getColor(R.color.white));
            tCheckin.setTextColor(getResources().getColor(R.color.white));
            tCheckOut.setTextColor(getResources().getColor(R.color.white));
            tCheckOut.setHintTextColor(getResources().getColor(R.color.white));
            tCheckin.setHintTextColor(getResources().getColor(R.color.white));

            //Layout Payment
            textPembayaran.setTextColor(getResources().getColor(R.color.white));
            bgPembayaran.setBackgroundResource(R.drawable.background_payment_dark);
            namaPembayaran.setTextColor(getResources().getColor(R.color.white));
            arrowpayment.setBackgroundResource(R.drawable.icon_right_arrow_dark);

            //Total
            textRincian.setTextColor(getResources().getColor(R.color.white));
            bgRincian.setBackgroundResource(R.drawable.background_payment_dark);
            totalTextAnak.setTextColor(getResources().getColor(R.color.darkTxt));
            totalAnak.setTextColor(getResources().getColor(R.color.darkTxt));
            totalTextDewasa.setTextColor(getResources().getColor(R.color.darkTxt));
            totalDewasa.setTextColor(getResources().getColor(R.color.darkTxt));
            Totaltext.setTextColor(getResources().getColor(R.color.white));
            totalPembayaran.setTextColor(getResources().getColor(R.color.white));

            //PopUp
            textpopup.setTextColor(getResources().getColor(R.color.white));
            bgpaymentmain.setBackgroundResource(R.drawable.background_popup_payment_dark);
            closePembayaran.setBackgroundResource(R.drawable.icon_cancel);

        }
    }

    private void initiliaze() {

        //Layout Main
        layoutUtama = findViewById(R.id.layoutUtama);

        //Sweet Alert
        pDialog = new SweetAlertDialog(TransactionRentalScreen.this, SweetAlertDialog.PROGRESS_TYPE);

        //Layout Top
        btnbackscroller = findViewById(R.id.btnbackscroller);

        transactionRental = new TransactionRental();
        masterDataRentalDetail = new MasterDataRentalDetail();

        //Layout User
        textPersonal = findViewById(R.id.textPersonal);
        bgPersonal = findViewById(R.id.bgPersonal);
        judulNama = findViewById(R.id.judulNama);
        Nama = findViewById(R.id.Nama);
        judulTelefon = findViewById(R.id.judulTelefon);
        Telefon = findViewById(R.id.Telefon);
        judulEmail = findViewById(R.id.judulEmail);
        Email = findViewById(R.id.Email);

//        Layout Detail Hotel
        tetxtPemesanan = findViewById(R.id.textWisata);
        bgHotel = findViewById(R.id.bgWisata);
        bgCheckin = findViewById(R.id.bgChechin);
        bgCheckout = findViewById(R.id.bgChechout);
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
        jCheckout = findViewById(R.id.judulCheckOut);
        calendarCheckIn = findViewById(R.id.imageView85);
        calendarCheckOut = findViewById(R.id.imageView86);
        tCheckin = findViewById(R.id.editTanggalChechIn);
        tCheckOut = findViewById(R.id.editTanggalChechOut);
        tempDate = findViewById(R.id.tempDate);

        //LayoutMain Payment
        bgPembayaran = findViewById(R.id.bgPembayaran);
        namaPembayaran = findViewById(R.id.namaPembayaran);
        textPembayaran = findViewById(R.id.textPembayaran);
        arrowpayment = findViewById(R.id.arrowpayment);

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
        PesanSekarang= findViewById(R.id.PesanSekarang);

        //List Payment popup
        textpopup = findViewById(R.id.textpopup);
        jenisPayment = findViewById(R.id.jenisPayment);
        closePembayaran = findViewById(R.id.closePembayaran);
        layoutPaymentt = findViewById(R.id.layoutPaymentt);
        bgpaymentmain = findViewById(R.id.bgpaymentmain);
        buttonPayment = findViewById(R.id.buttonPayment);

        //other
        dataMode = new DataMode(this);
        dataLoginUser = new DataLoginUser(this);
    }


    private class PaymentRecyclerViewAdapter extends RecyclerView.Adapter<TransactionRentalScreen.PaymentRecyclerViewAdapter.NameViewHolder> {
        public class NameViewHolder extends RecyclerView.ViewHolder {
            TextView namaBank;
            ImageView checkList,imageBank;
            ConstraintLayout layoutPayment;
            View line;

            public NameViewHolder(@NonNull View itemView) {
                super(itemView);
                namaBank = (TextView) itemView.findViewById(R.id.namaBank);
                namaBank = (TextView) itemView.findViewById(R.id.namaBank);
                checkList = (ImageView) itemView.findViewById(R.id.checkList);
                imageBank = (ImageView) itemView.findViewById(R.id.imageBank);
                layoutPayment = (ConstraintLayout) itemView.findViewById(R.id.layoutPayment);
                line = (View) itemView.findViewById(R.id.line);

            }
        }

        @NonNull
        @Override
        public TransactionRentalScreen.PaymentRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.item_list_payment, parent, false);
            return new TransactionRentalScreen.PaymentRecyclerViewAdapter.NameViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionRentalScreen.PaymentRecyclerViewAdapter.NameViewHolder holder, @SuppressLint("RecyclerView") int position) {
            MasterDataBank databbanks = databankmaster.get(position);
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
                holder.namaBank.setTextColor(getResources().getColor(R.color.white));
                holder.line.setBackgroundResource(R.color.colorGarisDark);
            }
            holder.namaBank.setText(databbanks.getNamaBank());
            if(databbanks.getGambarBank().equals("")){

            }else{
                Glide.with(TransactionRentalScreen.this).clear(holder.imageBank);
                Glide.with(TransactionRentalScreen.this)
                        .load(databbanks.getGambarBank())
                        .fitCenter()
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()
//                                    .override(300, 600)
                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(holder.imageBank);
            }
            if (idPayment.equals("")) {
                holder.checkList.setVisibility(View.GONE);
            } else {
                if (idPayment.equals(keyPayment.get(position))) {
                    holder.checkList.setVisibility(View.VISIBLE);
                } else {
                    holder.checkList.setVisibility(View.GONE);
                }
            }
            holder.layoutPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    idPayment = keyPayment.get(position);
                    namePaymentTemps = databbanks.getNamaBank();
                    setDataPayment();
                }
            });
        }

        @Override
        public int getItemCount() {
            return databankmaster.size();
        }

        private Context context;
        private List<String> keyPayment;
        private List<MasterDataBank> databankmaster;

        public PaymentRecyclerViewAdapter(Context context, List<MasterDataBank> databankmaster, List<String> keyPayment) {
            this.context = context;
            this.databankmaster = databankmaster;
            this.keyPayment = keyPayment;
        }
    }
}