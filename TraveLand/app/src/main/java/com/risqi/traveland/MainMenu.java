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

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private  List<DataKegiatan> dataKegiatanT = new ArrayList<>();
    private DatabaseReference Reff;
    RecyclerView recyclerView;
    private KegiatanRecyclerViewAdapter kegiatanRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        recyclerView = findViewById(R.id.vwKegiatan);
        setData();







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



    private void setData() {
        kegiatanRecyclerViewAdapter = new KegiatanRecyclerViewAdapter(this, dataKegiatanT);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(kegiatanRecyclerViewAdapter);

        Reff = FirebaseDatabase.getInstance().getReference("Data-Kegiatan");
        Reff.addValueEventListener(new ValueEventListener() {
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