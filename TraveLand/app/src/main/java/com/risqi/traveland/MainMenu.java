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
import com.risqi.traveland.RecyclerView.KegiatanRecyclerViewAdapter;
import com.risqi.traveland.RecyclerView.PemberitahuanRecyclerViewAdapter;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.TempData.TempDataInformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainMenu extends AppCompatActivity {

    private List<DataKegiatan> dataKegiatanT = new ArrayList<>();
    private List<TempDataInformation> dataInformations = new ArrayList<>();
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
    private DatabaseReference database1,database2;

    private Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    private int internet = 0;
    private int AlertcekNIK = 0;

    private String NIKUser;
    private String keyAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        keyAndroid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        cekKondisi();
        initialize();
        setDataCUstomer();
        setTanggal();
        setPemberitahuan();
        setData();
        setModeApp();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refresh.setRefreshing(true);
                setDataCUstomer();
                setTanggal();
                setPemberitahuan();
                setData();
                setModeApp();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        refresh.setRefreshing(false);
                    }
                },5000);

            }
        });
    }

    private void cekKondisi() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!adaInternet() && internet == 0) {
                    internet = 1;
                    new SweetAlertDialog(MainMenu.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Internet tidak terhubung")
                            .setContentText("Mohon cek kembali konkesi internet")
                            .setConfirmText("Okey")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(getIntent());
                                    Animatoo.animateFade(MainMenu.this);
                                    onStop();
                                }
                            })
                            .show();
                } else if (adaInternet() && internet == 0) {

                    Cursor resLogin = loginUser.getDataOne();
                    resLogin.moveToFirst();
                    if(resLogin.getCount()>0){

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
        Intent a = new Intent(MainMenu.this, MainProfile.class);
        startActivity(a);
        Animatoo.animateFade(MainMenu.this);
        onStop();
    }

    private void initialize() {
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
    private void setDataCUstomer(){
        Cursor mod = loginUser.getDataOne();
        mod.moveToFirst();
        String NIK = "";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            NIK = mod.getString(mod.getColumnIndexOrThrow("nik"));

            mod.moveToNext();
        }
        mod.close();
        if(mod.getCount()>0){
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
                            loginUser.updateDataRealTime(DataCustomer.get("NamaCustomer").toString(),DataCustomer.get("fotoCustomer").toString(),DataCustomer.get("Gender").toString(),DataCustomerAccount.get("KataSandi").toString());
                        }
                    });
                }
            });
            setDataCustomerLocal();
        }


    }
    private void setDataCustomerLocal(){
        Cursor mod2 = loginUser.getDataOne();
        mod2.moveToFirst();
        String nama2 = "";
        while (!mod2.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            nama2 = mod2.getString(mod2.getColumnIndexOrThrow("nama"));

            mod2.moveToNext();
        }
        mod2.close();
        String [] arrayName=nama2.split(" ");
        nama.setText(", " + arrayName[0]);
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
            nama.setVisibility(View.GONE);
            logoUser.setBackgroundResource(R.drawable.icon_user_warning);
            TempDataInformation information = new TempDataInformation();
            information.setNamaInformasi("Segera Daftar/Masuk Aplikasi");
            dataInformations.add(information);
            pemberitahuanRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            logoUser.setBackgroundResource(R.drawable.icon_no_user);
        }


    }


    private void setData() {
        kegiatanRecyclerViewAdapter = new KegiatanRecyclerViewAdapter(this, dataKegiatanT);
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
                }
                kegiatanRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}