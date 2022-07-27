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
import com.risqi.traveland.Firebase.MasterDataRental;
import com.risqi.traveland.RecyclerView.RentalRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuRental extends AppCompatActivity {

    private List<MasterDataRental> masterDataRentall = new ArrayList<>();
    private DatabaseReference Reff;
    RecyclerView recyclerViewRental;
    private RentalRecyclerViewAdapter rentalRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_rental);
        recyclerViewRental = findViewById(R.id.vwRental);
        setRental();
    }

    private void setRental(){
        rentalRecyclerViewAdapter = new RentalRecyclerViewAdapter(this, masterDataRentall);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerViewRental.setLayoutManager(layoutManager);
        recyclerViewRental.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRental.setAdapter(rentalRecyclerViewAdapter);

        Reff = FirebaseDatabase.getInstance().getReference("Master-Data-Rental");
        Reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                masterDataRentall.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    MasterDataRental masterdatarental = postSnapshot.getValue(MasterDataRental.class);
                    masterDataRentall.add(masterdatarental);
                }
                rentalRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void btnbackrental (View view){
        Intent a = new  Intent(MenuRental.this, MainMenu.class);
        startActivity(a);
        Animatoo.animateFade(MenuRental.this);
        finish();
    }
}