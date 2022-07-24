package com.risqi.traveland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.DataKegiatan;
import com.risqi.traveland.RecyclerView.KegiatanRecyclerViewAdapter;
import com.risqi.traveland.RecyclerView.PemberitahuanRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private  List<DataKegiatan> dataKegiatanT = new ArrayList<>();
    private DatabaseReference Reff;
    RecyclerView recyclerView;
    RecyclerView recyclerViewPemberitahuan;
    private KegiatanRecyclerViewAdapter kegiatanRecyclerViewAdapter;
    private PemberitahuanRecyclerViewAdapter pemberitahuanRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initialize();
        setData();
        setPemberitahuan();

//        rvGroceries = findViewById(R.id.vwKegiatan);
//        rvGroceries.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
//
//        groceryRecyclerViewAdapter = new GroceryRecyclerViewAdapter();
//        rvGroceries.setAdapter(groceryRecyclerViewAdapter);
//
//        setData();
    }
    public void MenuProfile(View view){
        Intent a = new Intent(MainMenu.this, MainProfile.class);
        startActivity(a);
    }

    private void initialize(){
        recyclerView = findViewById(R.id.vwKegiatan);
        recyclerViewPemberitahuan = findViewById(R.id.vmPemberitahuan);
    }

    private  void  setPemberitahuan(){
        pemberitahuanRecyclerViewAdapter = new PemberitahuanRecyclerViewAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewPemberitahuan.setLayoutManager(layoutManager);
        recyclerViewPemberitahuan.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPemberitahuan.setAdapter(pemberitahuanRecyclerViewAdapter);
    }


    private void setData() {
        kegiatanRecyclerViewAdapter = new KegiatanRecyclerViewAdapter(this, dataKegiatanT);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(kegiatanRecyclerViewAdapter);

        Reff = FirebaseDatabase.getInstance().getReference("Data-Kegiatan");
        Reff.orderByChild("TanggalMulai").limitToFirst(5).addValueEventListener(new ValueEventListener() {
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