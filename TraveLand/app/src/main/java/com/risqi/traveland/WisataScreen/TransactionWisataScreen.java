package com.risqi.traveland.WisataScreen;

import android.animation.Animator;
import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.MasterDataBank;
import com.risqi.traveland.Firebase.TransactionWIisata;
import com.risqi.traveland.NotifSucess.NotifTransactionSuccessScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TransactionWisataScreen extends AppCompatActivity {

    //Firebase or Other
    private DatabaseReference database1, database2;
    private String idWisata,NIKUSER;
    private DataMode dataMode;
    private DataLoginUser dataLoginUser;
    private SweetAlertDialog pDialog;
    private TransactionWIisata transactionWIisata;

    //Layout Main
    private ConstraintLayout layoutUtama;

    //Layout Top
    private Button btnbackscroller;

    //Layout Detail User
    private TextView textPersonal, judulNama, Nama, judulTelefon, Telefon, judulEmail, Email;
    private ConstraintLayout bgPersonal;

    //Layout Detail Wisata
    private ConstraintLayout bgWisata;
    private Button PlusAnak, minusAnak, minusDewasa, PlusDewasa;
    private TextView jumlahDewasa, jumlahAnak, textWisata, judulWisata, NamaWisata, JudulAlamatWisata, AlamatWisata, judulAnakKecil, hargaAnakText, judulDewasa, hargaDewasaText;

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
    private PaymentRecyclerViewAdapter paymentRecyclerViewAdapter;
    private String idPayment = "";
    private String namePaymentTemps = "";
    private String namePayment = "";
    private String keyPayments = "";
    private int HargaAnak = 0, tempAnak = 0,totalAnakFinal=0;
    private int HargaDewasa = 0, tempDewasa = 0,totalDewasaFinal=0;
    private int TotalHarga = 0;
    private int showPayment=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_wisata_screen);
        getDataInten();
        initialize();
//
//        //setMode
        setMode();
//
        setData();
//
//        //layout Top
        toMenu();
//
//        //DetailWisata
        setdefaultWisata();
        setPlusMinusDewasa();
        setPlusMinusAnak();
//        //Payment
        setDataPayment();
        swipeUPlistPayment(0);
        setNamePayment("");
        setShowPayment();
        setHidePayment();
        setpaymentChoice();
//        //Submit
        pesanNow();


    }

    private void pesanNow() {
        PesanSekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(TransactionWisataScreen.this, SweetAlertDialog.WARNING_TYPE)
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
                                            new SweetAlertDialog(TransactionWisataScreen.this, SweetAlertDialog.ERROR_TYPE)
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

    private void sendTranksaksi(){
        String key=RandGeneratedStr(12);
        Map<String, String> insertData = transactionWIisata.insertData(NIKUSER,idWisata,jumlahAnak.getText().toString(),jumlahDewasa.getText().toString(),""+HargaAnak,""+HargaDewasa,keyPayments,""+totalAnakFinal,""+totalDewasaFinal,""+TotalHarga);
        database1 = FirebaseDatabase.getInstance().getReference().child("Transaction-Wisata").child(key);
        database1.setValue(insertData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent a = new Intent(TransactionWisataScreen.this, NotifTransactionSuccessScreen.class);
                a.putExtra("Next", "UploadFotoWisata");
                a.putExtra("idScreen", ""+key);
                startActivity(a);
                Animatoo.animateSlideLeft(TransactionWisataScreen.this);
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

    private void setData() {
        //User
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
                Nama.setText(user.get("NamaCustomer").toString());
                Telefon.setText(user.get("TelefonCustomer").toString());
                Email.setText(user.get("EmailCustomer").toString());

            }
        });

        database2 = FirebaseDatabase.getInstance().getReference();
        database2.child("Master-Data-Wisata").child(idWisata).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> wisata = (Map<String, Object>) task.getResult().getValue();
                NamaWisata.setText(wisata.get("NamaWisata").toString());
                AlamatWisata.setText(wisata.get("AlamatWisata").toString());
                hargaAnakText.setText("Rp." + wisata.get("HargaAnak").toString());
                hargaDewasaText.setText("Rp." + wisata.get("HargaDewasa").toString());
                HargaAnak = Integer.parseInt(wisata.get("HargaAnak").toString());
                HargaDewasa = Integer.parseInt(wisata.get("HargaDewasa").toString());
                TotalHarga = 0 + HargaDewasa;
                totalAnakFinal=0;
                totalDewasaFinal=HargaDewasa;
                //Total
                totalAnak.setText("Rp.0");
                totalDewasa.setText("Rp." + HargaDewasa);
                totalPembayaran.setText("Rp." + TotalHarga);
            }
        });


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

    private void getDataInten() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idWisata = bundle.getString("idScreen");
        } else {
            idWisata = getIntent().getStringExtra("idScreen");
        }
    }

    private void toMenu() {
        btnbackscroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(TransactionWisataScreen.this, DetailWisataScreen.class);
                a.putExtra("idScreen", idWisata);
                startActivity(a);
                Animatoo.animateSlideLeft(TransactionWisataScreen.this);
                onStop();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(showPayment==0){
            Intent a = new Intent(TransactionWisataScreen.this, DetailWisataScreen.class);
            a.putExtra("idScreen", idWisata);
            startActivity(a);
            Animatoo.animateSlideLeft(TransactionWisataScreen.this);
            onStop();
        }else{
            idPayment = "";
            namePaymentTemps = "";
            showPayment=0;
            swipeUPlistPayment(0);
        }

    }

    private void setPlusMinusAnak() {
        minusAnak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int valueANak = Integer.parseInt(jumlahAnak.getText().toString());
                int valueADewasa = Integer.parseInt(jumlahDewasa.getText().toString());
                if (valueANak > 0) {
                    jumlahAnak.setText("" + (valueANak - 1));
                    tempAnak = HargaAnak * (valueANak-1);
                    tempDewasa = HargaDewasa * valueADewasa;
                    totalAnakFinal=tempAnak;
                    totalDewasaFinal=tempDewasa;
                    TotalHarga = tempAnak + tempDewasa;
                    //Total
                    totalAnak.setText("Rp." + tempAnak);
                    totalDewasa.setText("Rp." + tempDewasa);
                    totalPembayaran.setText("Rp." + TotalHarga);
                }else{
                    tempAnak = HargaAnak * valueANak;
                    tempDewasa = HargaDewasa * valueADewasa;
                    totalAnakFinal=tempAnak;
                    totalDewasaFinal=tempDewasa;
                    TotalHarga = tempAnak + tempDewasa;
                    //Total
                    totalAnak.setText("Rp." + tempAnak);
                    totalDewasa.setText("Rp." + tempDewasa);
                    totalPembayaran.setText("Rp." + TotalHarga);
                }
            }
        });
        PlusAnak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int valueANak = Integer.parseInt(jumlahAnak.getText().toString());
                int valueADewasa = Integer.parseInt(jumlahDewasa.getText().toString());
                jumlahAnak.setText("" + (valueANak + 1));
                tempAnak = HargaAnak * (valueANak+1);
                tempDewasa = HargaDewasa * valueADewasa;
                totalAnakFinal=tempAnak;
                totalDewasaFinal=tempDewasa;
                TotalHarga = tempAnak + tempDewasa;
                //Total
                totalAnak.setText("Rp." + tempAnak);
                totalDewasa.setText("Rp." + tempDewasa);
                totalPembayaran.setText("Rp." + TotalHarga);
            }
        });
    }

    private void setPlusMinusDewasa() {
        minusDewasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int valueANak = Integer.parseInt(jumlahAnak.getText().toString());
                int valueADewasa = Integer.parseInt(jumlahDewasa.getText().toString());
                if (valueADewasa > 1) {
                    jumlahDewasa.setText("" + (valueADewasa - 1));
                    tempAnak = HargaAnak * valueANak;
                    tempDewasa = HargaDewasa * (valueADewasa - 1);
                    totalAnakFinal=tempAnak;
                    totalDewasaFinal=tempDewasa;
                    TotalHarga = tempAnak + tempDewasa;
                    //Total
                    totalAnak.setText("Rp." + tempAnak);
                    totalDewasa.setText("Rp." + tempDewasa);
                    totalPembayaran.setText("Rp." + TotalHarga);
                }else{
                    tempAnak = HargaAnak * valueANak;
                    tempDewasa = HargaDewasa * valueADewasa;
                    totalAnakFinal=tempAnak;
                    totalDewasaFinal=tempDewasa;
                    TotalHarga = tempAnak + tempDewasa;
                    //Total
                    totalAnak.setText("Rp." + tempAnak);
                    totalDewasa.setText("Rp." + tempDewasa);
                    totalPembayaran.setText("Rp." + TotalHarga);
                }
            }
        });
        PlusDewasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int valueANak = Integer.parseInt(jumlahAnak.getText().toString());
                int valueADewasa = Integer.parseInt(jumlahDewasa.getText().toString());
                jumlahDewasa.setText("" + (valueADewasa + 1));
                tempAnak = HargaAnak * valueANak;
                tempDewasa = HargaDewasa * (valueADewasa + 1);
                totalAnakFinal=tempAnak;
                totalDewasaFinal=tempDewasa;
                TotalHarga = tempAnak + tempDewasa;
                //Total
                totalAnak.setText("Rp." + tempAnak);
                totalDewasa.setText("Rp." + tempDewasa);
                totalPembayaran.setText("Rp." + TotalHarga);
            }
        });
    }

    private void setdefaultWisata() {
        jumlahAnak.setText("0");
        jumlahDewasa.setText("1");
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

    private void setNamePayment(String s) {
        if (s.equals("")) {
            namaPembayaran.setText("Pilih Pembayaran");
        } else {
            namaPembayaran.setText(s);
        }
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
        paymentRecyclerViewAdapter = new PaymentRecyclerViewAdapter(TransactionWisataScreen.this, masterDataBanks, listPayment);
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

    private void initialize() {

        //Layout Main
        layoutUtama = findViewById(R.id.layoutUtama);

        //Transaksi
        transactionWIisata = new TransactionWIisata();

        //Sweet Alert
        pDialog = new SweetAlertDialog(TransactionWisataScreen.this, SweetAlertDialog.PROGRESS_TYPE);

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
        PlusAnak = findViewById(R.id.PlusAnak);
        jumlahAnak = findViewById(R.id.jumlahAnak);
        minusAnak = findViewById(R.id.minusAnak);
        minusDewasa = findViewById(R.id.minusDewasa);
        jumlahDewasa = findViewById(R.id.jumlahDewasa);
        PlusDewasa = findViewById(R.id.PlusDewasa);
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

    private class PaymentRecyclerViewAdapter extends RecyclerView.Adapter<PaymentRecyclerViewAdapter.NameViewHolder> {
        public class NameViewHolder extends RecyclerView.ViewHolder {
            TextView namaBank;
            ImageView checkList;
            ConstraintLayout layoutPayment;
            View line;

            public NameViewHolder(@NonNull View itemView) {
                super(itemView);
                namaBank = (TextView) itemView.findViewById(R.id.namaBank);
                namaBank = (TextView) itemView.findViewById(R.id.namaBank);
                checkList = (ImageView) itemView.findViewById(R.id.checkList);
                layoutPayment = (ConstraintLayout) itemView.findViewById(R.id.layoutPayment);
                line = (View) itemView.findViewById(R.id.line);

            }
        }

        @NonNull
        @Override
        public PaymentRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.item_list_payment, parent, false);
            return new PaymentRecyclerViewAdapter.NameViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PaymentRecyclerViewAdapter.NameViewHolder holder, @SuppressLint("RecyclerView") int position) {
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