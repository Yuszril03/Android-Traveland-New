package com.risqi.traveland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


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
import com.risqi.traveland.Firebase.MasterDataWisata;
import com.risqi.traveland.RecyclerView.WisataRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuWisata extends AppCompatActivity {
    private List<MasterDataWisata> masterDataWisata1 = new ArrayList<>();
    private DatabaseReference Reff;
    RecyclerView recyclerViewWisata;
    private WisataRecyclerViewAdapter wisataRecyclerViewAdapter;
    private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_wisata);
        recyclerViewWisata = findViewById(R.id.vwWisata);
        editSearch = (EditText) findViewById(R.id.editText);

        setWisata();
//        searchWisata();



//        editSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                // TODO Auto-generated method stub
//                Toast.makeText(MenuWisata.this,"before text change", Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                // TODO Auto-generated method stub
//            }
//        });
        
    }

    private void setWisata(){
        wisataRecyclerViewAdapter = new WisataRecyclerViewAdapter(this, masterDataWisata1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerViewWisata.setLayoutManager(layoutManager);
        recyclerViewWisata.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWisata.setAdapter(wisataRecyclerViewAdapter);

        Reff = FirebaseDatabase.getInstance().getReference("Master-Data-Wisata");
        Reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                masterDataWisata1.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    MasterDataWisata masterdatawisata = postSnapshot.getValue(MasterDataWisata.class);
                    masterDataWisata1.add(masterdatawisata);
                }
                wisataRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void btnbackwisata (View view){
        Intent a = new  Intent(MenuWisata.this, MainMenu.class);
        startActivity(a);
        Animatoo.animateFade(MenuWisata.this);
        finish();
    }

    private void searchWisata(){
        

    }
}