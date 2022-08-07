package com.risqi.traveland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;


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
    private List<String> idmasterDataWisata1 = new ArrayList<>();
    private DatabaseReference Reff;
    RecyclerView recyclerViewWisata;
    private WisataRecyclerViewAdapter wisataRecyclerViewAdapter;
    private EditText textCari;
    private TextWatcher textWatcher =null;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_wisata);
        initialize();
        setWisata("");
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TextUtils.isEmpty(textCari.getText())){
                    setWisata("");
                }else{
                    setWisata(textCari.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        textCari.addTextChangedListener(textWatcher);
        
    }
    private void initialize(){
        recyclerViewWisata = findViewById(R.id.vwWisata);
        textCari = findViewById(R.id.editSearch);
        constraintLayout = findViewById(R.id.constraintwisatanodata);
    }

    private void setWisata(String cari){
        wisataRecyclerViewAdapter = new WisataRecyclerViewAdapter(this, masterDataWisata1,idmasterDataWisata1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerViewWisata.setLayoutManager(layoutManager);
        recyclerViewWisata.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWisata.setAdapter(wisataRecyclerViewAdapter);

        Reff = FirebaseDatabase.getInstance().getReference("Master-Data-Wisata");
        Reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                masterDataWisata1.clear();
                idmasterDataWisata1.clear();
                if (cari.equals("")){
                    for (DataSnapshot postSnapshot1 : snapshot.getChildren()){
                        MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
                        masterDataWisata1.add(masterdatawisataa);
                        idmasterDataWisata1.add(postSnapshot1.getKey());
                    }
                }else {
                    for (DataSnapshot postSnapshot1 : snapshot.getChildren()){
                        MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
                        if (masterdatawisataa.getNamaWisata().contains(cari)){
                            masterDataWisata1.add(masterdatawisataa);
                            idmasterDataWisata1.add(postSnapshot1.getKey());
                        }
                    }
                }
                if (masterDataWisata1.isEmpty()){
                    constraintLayout.setVisibility(View.VISIBLE);
                }else {
                    constraintLayout.setVisibility(View.INVISIBLE);
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

}