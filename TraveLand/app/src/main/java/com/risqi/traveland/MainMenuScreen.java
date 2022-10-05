package com.risqi.traveland;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.DataKegiatan;
import com.risqi.traveland.Firebase.MasterDataHotelDetail;
import com.risqi.traveland.Firebase.TransactionHotel;
import com.risqi.traveland.Firebase.TransactionRental;
import com.risqi.traveland.Firebase.TransactionWIisata;
import com.risqi.traveland.HotelScreen.MenuHotelScreen;
import com.risqi.traveland.OrderingScreen.OrderingScreen;
import com.risqi.traveland.RecyclerView.KegiatanRecyclerViewAdapter;
import com.risqi.traveland.RecyclerView.PemberitahuanRecyclerViewAdapter;
import com.risqi.traveland.RentalScreen.MenuRentalScreen;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.SettingScreen.MainProfileScreen;
import com.risqi.traveland.WisataScreen.MenuWisataScreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainMenuScreen extends AppCompatActivity {

    private List<DataKegiatan> dataKegiatanT = new ArrayList<>();
    private List<String> dataInformations = new ArrayList<>();
    private List<String> iddataKegiatanT = new ArrayList<>();


    private DatabaseReference Reff;
    RecyclerView recyclerView;
    RecyclerView recyclerViewPemberitahuan;
    private KegiatanRecyclerViewAdapter kegiatanRecyclerViewAdapter;
    private PemberitahuanRecyclerViewAdapter pemberitahuanRecyclerViewAdapter;

    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private ConstraintLayout constraintLayout;
    private TextView halo, nama, tanggal, judulAwal, subMenu, subKegiatan;
    private ImageView logoUser;
    private SwipeRefreshLayout refresh;

    private DataMode dataMode;
    private DataLoginUser loginUser;
    private DatabaseReference database1, database2, database3, database4, datacase5;

    private Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    private int internet = 0;
    private int AlertcekNIK = 0;

    private String NIKUser;
    private String keyAndroid;
    private int countWisata = 0;
    private String polaCOnvert = "yyyy-MM-dd";

    private String polaCOnvertCreeated = "dd/MM/yyyy HH:mm:ss";
    private TransactionWIisata transWisataa;
    private TransactionHotel transHotel;
    private TransactionRental transRental;
    private MasterDataHotelDetail masterDataHotelDetailUtama;
    private Task updateTrans,updateTransMAster;
    private int closeeAPP=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        keyAndroid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        cekKondisi();
        initialize();
        checkWisata();
        setDataCUstomer();
        userLOGINICON();
        setTanggal();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataInformations.clear();
                setPemberitahuan();
            }
        },4000);
        setData();
        setModeApp();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refresh.setRefreshing(true);
                checkWisata();
                setDataCUstomer();
                setTanggal();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dataInformations.clear();
                        setPemberitahuan();
                    }
                },4000);
                setData();
                setModeApp();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        refresh.setRefreshing(false);
                    }
                }, 5000);

            }
        });
    }

    private void checkRental() {
        Date dateNoww = new Date();
        Cursor mod = loginUser.getDataOne();
        int hasil = mod.getCount();
        mod.moveToFirst();
        String NIK = "";
        if (hasil > 0) {
            while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
                NIK = mod.getString(mod.getColumnIndexOrThrow("nik"));

                mod.moveToNext();
            }
            //CEK DATA
            database1 = FirebaseDatabase.getInstance().getReference("Transaction-Rental");
            String finalNIK = NIK;
            database1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final int[] counts = {0};
                    for (DataSnapshot postData : snapshot.getChildren()) {
                        TransactionRental trans = postData.getValue(TransactionRental.class);
                        Date dateCreated = ConvertStringToDate(trans.getTanggalBuat(), polaCOnvertCreeated, null);
                        Date dateCheckIN = ConvertStringToDate(trans.getCheckIn(), polaCOnvert, null);
                        Date dateCheckOut = ConvertStringToDate(trans.getCheckOut(), polaCOnvert, null);
                        long tempDataDateCreated = (dateCreated.getTime() - dateNoww.getTime());
                        long tempDataDateCreatedIN = (dateCheckIN.getTime() - dateNoww.getTime());
                        long tempDataDateCreatedOUT = (dateCheckOut.getTime() - dateNoww.getTime());
                        long selisihHariCreated = tempDataDateCreated / (24 * 60 * 60 * 1000);
                        long selisihHariIN = tempDataDateCreatedIN / (24 * 60 * 60 * 1000);
                        long selisihHariOut = tempDataDateCreatedOUT / (24 * 60 * 60 * 1000);
                        if (trans.getIdCutomer().equals(finalNIK) && trans.getStatusTransaksi().equals("1") && selisihHariCreated < 0) {

                            //Update
                            HashMap update = transRental.updateBatalkan("2");
                            updateTrans = FirebaseDatabase.getInstance().getReference().child("Transaction-Rental").child(postData.getKey()).updateChildren(update);
                            updateTrans.addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    counts[0] =1;
                                }
                            });

                        }
                       else if (trans.getIdCutomer().equals(finalNIK) && trans.getStatusTransaksi().equals("4") && selisihHariOut < 0) {
                            //Update
                            HashMap update = transRental.updateBatalkan("5");
                            updateTrans = FirebaseDatabase.getInstance().getReference().child("Transaction-Rental").child(postData.getKey()).updateChildren(update);
                            updateTrans.addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    counts[0] =1;
                                }
                            });

                        }
                       else if (trans.getIdCutomer().equals(finalNIK) && trans.getStatusTransaksi().equals("3") && selisihHariIN < 0) {
                            //Update
                            HashMap update = transRental.updateBatalkan("2");
                            updateTrans = FirebaseDatabase.getInstance().getReference().child("Transaction-Rental").child(postData.getKey()).updateChildren(update);
                            updateTrans.addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    counts[0] =1;
                                }
                            });


                        }

                    }
                    if(counts[0] ==0 || counts[0] ==1){
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               dataInformations.clear();
                               setPemberitahuan();
                           }
                       },2000);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        mod.close();
    }

    private void checkHotel() {
        Date dateNoww = new Date();
        Cursor mod = loginUser.getDataOne();
        int hasil = mod.getCount();
        mod.moveToFirst();
        String NIK = "";
        if (hasil > 0) {

             List<String> keyHotel = new ArrayList<>();
             List<String> IDKAMARR = new ArrayList<>();
             List<String> ValueNOW = new ArrayList<>();
             List<String> Jenis = new ArrayList<>();

            while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
                NIK = mod.getString(mod.getColumnIndexOrThrow("nik"));

                mod.moveToNext();
            }
            //CEK DATA
            database1 = FirebaseDatabase.getInstance().getReference("Transaction-Hotel");
            String finalNIK = NIK;
            database1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final int[] counts = {0};
                    final String[] keyIDD = new String[0];
                    final String[] ValueIDD = new String[0];
                    final String[] jumlahh = new String[0] ;
                    int countsss =0;
                    keyHotel.clear();
                    IDKAMARR.clear();
                    ValueNOW.clear();
                    Jenis.clear();
                    for (DataSnapshot postData : snapshot.getChildren()) {
                        TransactionHotel trans = postData.getValue(TransactionHotel.class);
                        Date dateCreated = ConvertStringToDate(trans.getTanggalBuat(), polaCOnvertCreeated, null);
                        Date dateCheckIN = ConvertStringToDate(trans.getCheckIn(), polaCOnvert, null);
                        Date dateCheckOut = ConvertStringToDate(trans.getCheckOut(), polaCOnvert, null);
                        long tempDataDateCreated = (dateCreated.getTime() - dateNoww.getTime());
                        long tempDataDateCreatedIN = (dateCheckIN.getTime() - dateNoww.getTime());
                        long tempDataDateCreatedOUT = (dateCheckOut.getTime() - dateNoww.getTime());
                        long selisihHariCreated = tempDataDateCreated / (24 * 60 * 60 * 1000);
                        long selisihHariIN = tempDataDateCreatedIN / (24 * 60 * 60 * 1000);
                        long selisihHariOut = tempDataDateCreatedOUT / (24 * 60 * 60 * 1000);
                        if (trans.getIdCutomer().equals(finalNIK) && trans.getStatusTransaksi().equals("1") && selisihHariCreated < 0) {

                            //Update
                            HashMap update = transHotel.updateBatalkan("2");
                            updateTrans = FirebaseDatabase.getInstance().getReference().child("Transaction-Hotel").child(postData.getKey()).updateChildren(update);

                            database4 = FirebaseDatabase.getInstance().getReference();
                            database4.child("Master-Data-Hotel-Detail").child(trans.getIdKamar()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Map<String, Object> hotel1 = (Map<String, Object>) task.getResult().getValue();
                                    int TotalData = Integer.parseInt(hotel1.get("JumlahKamar").toString())+Integer.parseInt(trans.getJumlahKamar());
                                    HashMap updateMasss = masterDataHotelDetailUtama.updateJumlahKamar(""+TotalData);
                                    updateTransMAster = FirebaseDatabase.getInstance().getReference().child("Master-Data-Hotel-Detail").child(trans.getIdKamar()).updateChildren(updateMasss);
                                }
                            });


                            countsss++;

                        } else if (trans.getIdCutomer().equals(finalNIK) && trans.getStatusTransaksi().equals("4") && selisihHariOut < 0) {
                            //Update
                            HashMap update = transHotel.updateBatalkan("5");
                            updateTrans = FirebaseDatabase.getInstance().getReference().child("Transaction-Hotel").child(postData.getKey()).updateChildren(update);

                            database4 = FirebaseDatabase.getInstance().getReference();
                            database4.child("Master-Data-Hotel-Detail").child(trans.getIdKamar()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Map<String, Object> hotel1 = (Map<String, Object>) task.getResult().getValue();
                                    int TotalData = Integer.parseInt(hotel1.get("JumlahKamar").toString())+Integer.parseInt(trans.getJumlahKamar());
                                    HashMap updateMasss = masterDataHotelDetailUtama.updateJumlahKamar(""+TotalData);
                                    updateTransMAster = FirebaseDatabase.getInstance().getReference().child("Master-Data-Hotel-Detail").child(trans.getIdKamar()).updateChildren(updateMasss);
                                }
                            });
                            countsss++;
                        } else if (trans.getIdCutomer().equals(finalNIK) && trans.getStatusTransaksi().equals("3") && selisihHariIN < 0) {
                            //Update
                            HashMap update = transHotel.updateBatalkan("2");
                            updateTrans = FirebaseDatabase.getInstance().getReference().child("Transaction-Hotel").child(postData.getKey()).updateChildren(update);

                            database4 = FirebaseDatabase.getInstance().getReference();
                            database4.child("Master-Data-Hotel-Detail").child(trans.getIdKamar()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Map<String, Object> hotel1 = (Map<String, Object>) task.getResult().getValue();
                                    int TotalData = Integer.parseInt(hotel1.get("JumlahKamar").toString())+Integer.parseInt(trans.getJumlahKamar());
                                    HashMap updateMasss = masterDataHotelDetailUtama.updateJumlahKamar(""+TotalData);
                                    updateTransMAster = FirebaseDatabase.getInstance().getReference().child("Master-Data-Hotel-Detail").child(trans.getIdKamar()).updateChildren(updateMasss);
                                }
                            });
                            countsss++;
                        }

                    }


                    if(countsss==0){
                        checkRental();
                    }else{
                        checkRental();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        mod.close();
    }

    private void checkWisata() {
        Date dateNoww = new Date();
        Cursor mod = loginUser.getDataOne();
        int hasil = mod.getCount();
        mod.moveToFirst();
        String NIK = "";
        if (hasil > 0) {
            while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
                NIK = mod.getString(mod.getColumnIndexOrThrow("nik"));

                mod.moveToNext();
            }
            //CEK DATA
            database1 = FirebaseDatabase.getInstance().getReference("Transaction-Wisata");
            String finalNIK = NIK;
            database1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final int[] counts = {0};
                    for (DataSnapshot postData : snapshot.getChildren()) {
                        TransactionWIisata trans = postData.getValue(TransactionWIisata.class);
                        Date dateCreated = ConvertStringToDate(trans.getTanggalBuat(), polaCOnvertCreeated, null);
                        long tempDataDateCreated = (dateCreated.getTime() - dateNoww.getTime());
                        long selisihHari = tempDataDateCreated / (24 * 60 * 60 * 1000);
                        if (trans.getIdCutomer().equals(finalNIK) && trans.getStatusTransaksi().equals("1") && selisihHari < 0) {

                            //Update
                            HashMap update = transWisataa.updateBatalkan("2");
                            updateTrans = FirebaseDatabase.getInstance().getReference().child("Transaction-Wisata").child(postData.getKey()).updateChildren(update);
                            updateTrans.addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    counts[0] =1;
                                }
                            });

                        }

                    }

                    if(counts[0] ==0 || counts[0] ==1){
                        checkHotel();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        mod.close();

    }

    private void cekKondisi() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!adaInternet() && internet == 0) {
                    internet = 1;
                    new SweetAlertDialog(MainMenuScreen.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Internet tidak terhubung")
                            .setContentText("Mohon cek kembali konkesi internet")
                            .setConfirmText("Okey")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(getIntent());
                                    Animatoo.animateFade(MainMenuScreen.this);
                                    onStop();
                                }
                            })
                            .show();
                } else if (adaInternet() && internet == 0) {

                    Cursor resLogin = loginUser.getDataOne();
                    resLogin.moveToFirst();
                    if (resLogin.getCount() > 0) {

                    }


                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private boolean adaInternet() {
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }

    private void setTanggal() {
        tanggal.setText(simpleDateFormat.format(calendar.getTime()).toString());
    }

    private void setModeApp() {
        Cursor mod = dataMode.getDataOne();
        mod.moveToFirst();
        String modeApps = "";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            modeApps = mod.getString(mod.getColumnIndexOrThrow("mode"));
            Log.d("ANEH", "setModeApp: " + modeApps);

            mod.moveToNext();
        }
        mod.close();
        if (modeApps.equals("Malam")) {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.darkMode));
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.darkMode));
            scrollView.setBackgroundColor(getResources().getColor(R.color.darkMode));
            halo.setTextColor(getResources().getColor(R.color.white));
            nama.setTextColor(getResources().getColor(R.color.white));
            subMenu.setTextColor(getResources().getColor(R.color.white));
            subKegiatan.setTextColor(getResources().getColor(R.color.white));
            judulAwal.setTextColor(getResources().getColor(R.color.white));
            tanggal.setTextColor(getResources().getColor(R.color.darkTxt));
        } else {
            linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));
            scrollView.setBackgroundColor(getResources().getColor(R.color.white));
            halo.setTextColor(getResources().getColor(R.color.darkMode));
            nama.setTextColor(getResources().getColor(R.color.darkMode));
            subMenu.setTextColor(getResources().getColor(R.color.darkMode));
            subKegiatan.setTextColor(getResources().getColor(R.color.darkMode));
            judulAwal.setTextColor(getResources().getColor(R.color.darkMode));
            tanggal.setTextColor(getResources().getColor(R.color.accent));
        }
    }

    public void MenuProfile(View view) {
        Intent a = new Intent(MainMenuScreen.this, MainProfileScreen.class);
        startActivity(a);
        Animatoo.animateFade(MainMenuScreen.this);
        onStop();
    }


    public void wisata(View view) {
        Intent a = new Intent(MainMenuScreen.this, MenuWisataScreen.class);
        startActivity(a);
        Animatoo.animateFade(MainMenuScreen.this);
        finish();
        onStop();
    }

    public void hotel(View view) {
        Intent a = new Intent(MainMenuScreen.this, MenuHotelScreen.class);
        startActivity(a);
        Animatoo.animateFade(MainMenuScreen.this);
        finish();
        onStop();
    }

    public void rental(View view) {
        Intent a = new Intent(MainMenuScreen.this, MenuRentalScreen.class);
        startActivity(a);
        Animatoo.animateFade(MainMenuScreen.this);
        finish();
        onStop();
    }

    public void pesanan(View view) {
        Intent a = new Intent(MainMenuScreen.this, OrderingScreen.class);
        startActivity(a);
        Animatoo.animateFade(MainMenuScreen.this);
        finish();
        onStop();
    }


    private void initialize() {

        transWisataa = new TransactionWIisata();
        transHotel = new TransactionHotel();
        transRental = new TransactionRental();
        masterDataHotelDetailUtama = new MasterDataHotelDetail();

        recyclerView = findViewById(R.id.vwKegiatan);
        recyclerViewPemberitahuan = findViewById(R.id.vmPemberitahuan);
        scrollView = findViewById(R.id.scrolLayout);
        linearLayout = findViewById(R.id.linerLayout);
        constraintLayout = findViewById(R.id.constraintLa);
        halo = findViewById(R.id.textView4);
        nama = findViewById(R.id.textView5);
        tanggal = findViewById(R.id.textView6);
        subMenu = findViewById(R.id.textView8);
        judulAwal = findViewById(R.id.textView7);
        subKegiatan = findViewById(R.id.textView11);
        logoUser = findViewById(R.id.imageView3);
        refresh = findViewById(R.id.refresh);

        dataMode = new DataMode(this);
        loginUser = new DataLoginUser(this);
    }

    private void setDataCUstomer() {
        Cursor mod = loginUser.getDataOne();
        mod.moveToFirst();
        String NIK = "";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            NIK = mod.getString(mod.getColumnIndexOrThrow("nik"));

            mod.moveToNext();
        }
        mod.close();
        if (mod.getCount() > 0) {
            database1 = FirebaseDatabase.getInstance().getReference();
            String finalNIK = NIK;
            database1.child("Master-Data-Customer").child(NIK).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Map<String, Object> DataCustomer = (Map<String, Object>) task.getResult().getValue();
                    database2 = FirebaseDatabase.getInstance().getReference();
                    database2.child("Master-Data-Account-Customer").child(finalNIK).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task2) {
                            Map<String, Object> DataCustomerAccount = (Map<String, Object>) task2.getResult().getValue();
                            loginUser.updateDataRealTime(DataCustomer.get("NamaCustomer").toString(), DataCustomer.get("fotoCustomer").toString(), DataCustomer.get("Gender").toString(), DataCustomerAccount.get("KataSandi").toString());
                        }
                    });
                }
            });
            setDataCustomerLocal();
        }


    }

    private void setDataCustomerLocal() {
        Cursor mod2 = loginUser.getDataOne();
        mod2.moveToFirst();
        String nama2 = "";
        while (!mod2.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            nama2 = mod2.getString(mod2.getColumnIndexOrThrow("nama"));

            mod2.moveToNext();
        }
        mod2.close();
        String[] arrayName = nama2.split(" ");
        nama.setText(", " + arrayName[0]);
    }

    private Date ConvertStringToDate(String tanggalDanWaktuStr, String pola, Locale lokal) {
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

    private void userLOGINICON(){
        Cursor modapa = loginUser.getDataOne();
        modapa.moveToFirst();
        int hasil = modapa.getCount();
        modapa.close();

        if(hasil==0){
            nama.setVisibility(View.GONE);
            logoUser.setBackgroundResource(R.drawable.icon_user_warning);
        }else{
            logoUser.setBackgroundResource(R.drawable.icon_no_user);
        }
    }

    private void setPemberitahuan() {

        Cursor modapa = loginUser.getDataOne();
        modapa.moveToFirst();
        int hasil = modapa.getCount();
        modapa.close();

        pemberitahuanRecyclerViewAdapter = new PemberitahuanRecyclerViewAdapter(this, dataInformations);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewPemberitahuan.setLayoutManager(layoutManager);
        recyclerViewPemberitahuan.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPemberitahuan.setAdapter(pemberitahuanRecyclerViewAdapter);
        dataInformations.clear();
        if (hasil == 0) {

            dataInformations.add("Segera Daftar/Masuk Aplikasi");
            pemberitahuanRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            Cursor mod = loginUser.getDataOne();
            mod.moveToFirst();
            String NIK = "";
            while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
                NIK = mod.getString(mod.getColumnIndexOrThrow("nik"));

                mod.moveToNext();
            }
            mod.close();
            database3 = FirebaseDatabase.getInstance().getReference("Transaction-Wisata");
            String finalNIK = NIK;
            database3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = 0;
                    for (DataSnapshot posData : snapshot.getChildren()) {
                        TransactionWIisata transactionWIisata = posData.getValue(TransactionWIisata.class);
                        if (transactionWIisata.getIdCutomer().equals(finalNIK) && transactionWIisata.getStatusTransaksi().equals("1")) {
                            count++;

                        }
                    }
                    if (count > 0) {
                        dataInformations.add(count + " Pemesanan Wisata Belum Terbayar");
                    }

                    database4 = FirebaseDatabase.getInstance().getReference("Transaction-Hotel");
                    database4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotHotel) {
                            int countHotel = 0;
                            int countHotelIN = 0;
                            int countHotelOut = 0;
                            Date dateNow = new Date();
                            for (DataSnapshot postDataHotel : snapshotHotel.getChildren()) {
                                TransactionHotel transactionHotel = postDataHotel.getValue(TransactionHotel.class);

                                Date dateChekIN = ConvertStringToDate(transactionHotel.getCheckIn(), polaCOnvert, null);
                                Date dateChekOut = ConvertStringToDate(transactionHotel.getCheckOut(), polaCOnvert, null);
                                long tempDataDateCheckIN = (dateChekIN.getTime() - dateNow.getTime());
                                long tempDataDateCheckOut = (dateChekOut.getTime() - dateNow.getTime());
                                long selisihHariIN = tempDataDateCheckIN / (24 * 60 * 60 * 1000);
                                long selisihHariOut = tempDataDateCheckOut / (24 * 60 * 60 * 1000);

                                if (transactionHotel.getIdCutomer().equals(finalNIK) && transactionHotel.getStatusTransaksi().equals("1")) {
                                    countHotel++;

                                }
                                if (transactionHotel.getIdCutomer().equals(finalNIK) && transactionHotel.getStatusTransaksi().equals("3") && selisihHariIN < 3) {
                                    countHotelIN++;

                                }
                                if (transactionHotel.getIdCutomer().equals(finalNIK) && transactionHotel.getStatusTransaksi().equals("4") && selisihHariOut < 1) {
                                    countHotelOut++;

                                }
                            }

                            if (countHotel > 0) {
                                dataInformations.add(countHotel + " Pemesanan Hotel Belum Terbayar");
                            }
                            if (countHotelIN > 0) {
                                dataInformations.add(countHotel + " Pemesanan Hotel Mendekati CheckIn");
                            }
                            if (countHotelOut > 0) {
                                dataInformations.add(countHotel + " Pemesanan Hotel Mendekati CheckOut");
                            }

                            datacase5 = FirebaseDatabase.getInstance().getReference("Transaction-Rental");
                            datacase5.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshotRental) {
                                    int countRental = 0;
                                    int countRentalIN = 0;
                                    int countRentalOut = 0;
                                    Date dateNowRental = new Date();
                                    for (DataSnapshot postDataRental : snapshotRental.getChildren()) {
                                        TransactionRental transactionRental = postDataRental.getValue(TransactionRental.class);
                                        Date dateChekINRental = ConvertStringToDate(transactionRental.getCheckIn(), polaCOnvert, null);
                                        Date dateChekOutRental = ConvertStringToDate(transactionRental.getCheckOut(), polaCOnvert, null);
                                        long tempDataDateCheckINRental = (dateChekINRental.getTime() - dateNow.getTime());
                                        long tempDataDateCheckOutRental = (dateChekOutRental.getTime() - dateNow.getTime());
                                        long selisihHariINRental = tempDataDateCheckINRental / (24 * 60 * 60 * 1000);
                                        long selisihHariOutRental = tempDataDateCheckOutRental / (24 * 60 * 60 * 1000);
                                        if (transactionRental.getIdCutomer().equals(finalNIK) && transactionRental.getStatusTransaksi().equals("1")) {
                                            countRental++;

                                        }
                                        if (transactionRental.getIdCutomer().equals(finalNIK) && transactionRental.getStatusTransaksi().equals("3") && selisihHariINRental < 3) {
                                            countRentalIN++;

                                        }
                                        if (transactionRental.getIdCutomer().equals(finalNIK) && transactionRental.getStatusTransaksi().equals("4") && selisihHariOutRental < 1) {
                                            countRentalOut++;
                                        }

                                    }

                                    if (countRental > 0) {
                                        dataInformations.add(countRental + " Pemesanan Rental Belum Terbayar");
                                    }
                                    if (countRentalIN > 0) {
                                        dataInformations.add(countRentalIN + " Pemesanan Rental Mendekati CheckIn");
                                    }
                                    if (countRentalOut > 0) {
                                        dataInformations.add(countRentalOut + " Pemesanan Rental Mendekati CheckOut");
                                    }
                                    pemberitahuanRecyclerViewAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            logoUser.setBackgroundResource(R.drawable.icon_no_user);
        }


    }

    @Override
    public void onBackPressed() {
        if(closeeAPP==0){
            closeeAPP++;
            Toast.makeText(this, "Ketuk satu kali lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
        }else if(closeeAPP==1){
            finish();
            System.exit(0);
        }
    }

    private void setData() {

        kegiatanRecyclerViewAdapter = new KegiatanRecyclerViewAdapter(this, dataKegiatanT, iddataKegiatanT);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(kegiatanRecyclerViewAdapter);

        Reff = FirebaseDatabase.getInstance().getReference("Data-Kegiatan");
        Reff.orderByChild("TanggalMulai").limitToLast(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataKegiatanT.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DataKegiatan dataKegiatan = postSnapshot.getValue(DataKegiatan.class);
                    dataKegiatanT.add(dataKegiatan);
                    iddataKegiatanT.add(postSnapshot.getKey());
                }
                kegiatanRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}