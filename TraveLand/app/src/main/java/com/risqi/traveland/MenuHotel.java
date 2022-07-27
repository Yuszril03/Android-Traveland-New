package com.risqi.traveland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.MasterDataHotel;
import com.risqi.traveland.RecyclerView.HotelRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuHotel extends AppCompatActivity {

    private List<MasterDataHotel> masterDataHotell = new ArrayList<>();
    private DatabaseReference Reff;
    RecyclerView recyclerViewHotel;
    private HotelRecyclerViewAdapter hotelRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_hotel);
        recyclerViewHotel = findViewById(R.id.vwHotel);
        setHotel();
    }

    private void setHotel(){
        hotelRecyclerViewAdapter = new HotelRecyclerViewAdapter(this, masterDataHotell);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerViewHotel.setLayoutManager(layoutManager);
        recyclerViewHotel.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHotel.setAdapter(hotelRecyclerViewAdapter);

        Reff = FirebaseDatabase.getInstance().getReference("Master-Data-Hotel");
        Reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                masterDataHotell.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    MasterDataHotel masterdatahotel = postSnapshot.getValue(MasterDataHotel.class);
                    masterDataHotell.add(masterdatahotel);
                }
                hotelRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void btnbackhotel (View view){
        Intent a = new  Intent(MenuHotel.this, MainMenu.class);
        startActivity(a);
        Animatoo.animateFade(MenuHotel.this);
        finish();
    }
}