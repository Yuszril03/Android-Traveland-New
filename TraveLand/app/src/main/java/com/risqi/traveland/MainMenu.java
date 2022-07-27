package com.risqi.traveland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
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

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private  List<DataKegiatan> dataKegiatanT = new ArrayList<>();
    private  List<TempDataInformation> dataInformations = new ArrayList<>();
    private DatabaseReference Reff;
    RecyclerView recyclerView;
    RecyclerView recyclerViewPemberitahuan;
    private KegiatanRecyclerViewAdapter kegiatanRecyclerViewAdapter;
    private PemberitahuanRecyclerViewAdapter pemberitahuanRecyclerViewAdapter;

    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private ConstraintLayout constraintLayout;
    private TextView halo,nama,tanggal,judulAwal,subMenu,subKegiatan;

    private DataMode dataMode;
    private DataLoginUser loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initialize();
        setData();
        setPemberitahuan();
        setModeApp();

//        rvGroceries = findViewById(R.id.vwKegiatan);
//        rvGroceries.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
//
//        groceryRecyclerViewAdapter = new GroceryRecyclerViewAdapter();
//        rvGroceries.setAdapter(groceryRecyclerViewAdapter);
//
//        setData();
    }

    private void setModeApp() {
        Cursor mod = dataMode.getDataOne();
        mod.moveToFirst();
        String modeApps = "";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            modeApps = mod.getString(mod.getColumnIndexOrThrow("mode"));
            Log.d("ANEH", "setModeApp: "+modeApps);

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
        }else{
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

    public void MenuProfile(View view){
        Intent a = new Intent(MainMenu.this, MainProfile.class);
        startActivity(a);
        Animatoo.animateFade(MainMenu.this);
        finish();
    }

    public void wisata (View view){
        Intent a = new  Intent(MainMenu.this, MenuWisata.class);
        startActivity(a);
        Animatoo.animateFade(MainMenu.this);
        finish();
    }
    public void hotel (View view){
        Intent a = new  Intent(MainMenu.this, MenuHotel.class);
        startActivity(a);
        Animatoo.animateFade(MainMenu.this);
        finish();
    }
    public void rental (View view){
        Intent a = new  Intent(MainMenu.this, MenuRental.class);
        startActivity(a);
        Animatoo.animateFade(MainMenu.this);
        finish();
    }


    private void initialize(){
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

        dataMode= new DataMode(this);
        loginUser = new DataLoginUser(this);
    }


    private  void  setPemberitahuan(){

        Cursor modapa = loginUser.getDataOne();
        modapa.moveToFirst();
        int hasil = modapa.getCount();
        modapa.close();

        pemberitahuanRecyclerViewAdapter = new PemberitahuanRecyclerViewAdapter(this,dataInformations);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewPemberitahuan.setLayoutManager(layoutManager);
        recyclerViewPemberitahuan.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPemberitahuan.setAdapter(pemberitahuanRecyclerViewAdapter);

        if(hasil==0){
            TempDataInformation information = new TempDataInformation();
            information.setNamaInformasi("Segera Daftar/Masuk Aplikasi");
            dataInformations.add(information);
            pemberitahuanRecyclerViewAdapter.notifyDataSetChanged();
        }else{

        }


    }


    private void setData() {
        kegiatanRecyclerViewAdapter = new KegiatanRecyclerViewAdapter(this, dataKegiatanT);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(kegiatanRecyclerViewAdapter);

        Reff = FirebaseDatabase.getInstance().getReference("Data-Kegiatan");
        Reff.orderByChild("TanggalMulai").limitToLast(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataKegiatanT.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
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