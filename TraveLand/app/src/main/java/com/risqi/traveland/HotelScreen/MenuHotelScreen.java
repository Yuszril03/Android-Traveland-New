package com.risqi.traveland.HotelScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.risqi.traveland.Firebase.MasterDataHotelDetail;
import com.risqi.traveland.MainMenuScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.RecyclerView.HotelRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuHotelScreen extends AppCompatActivity {

    private List<MasterDataHotelDetail> masterDataHotelDetaill = new ArrayList<>();
    private List<String> idmasterDataHotelDetail = new ArrayList<>();
    private DatabaseReference Reff;
    RecyclerView recyclerViewHotel;
    private HotelRecyclerViewAdapter hotelRecyclerViewAdapter;
    private EditText textCari;
    private TextWatcher taTextWatcher =null;
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_hotel);
        initialize();
        setHotel("");
        taTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TextUtils.isEmpty(textCari.getText())){
                    setHotel("");
                }else{
                    setHotel(textCari.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        textCari.addTextChangedListener(taTextWatcher);

    }

    private void initialize() {
        recyclerViewHotel = findViewById(R.id.vwHotel);
        textCari = findViewById(R.id.editSearchhotel);
        constraintLayout = findViewById(R.id.constrainthotelnodata);
    }

    private void setHotel(String cari){
        hotelRecyclerViewAdapter = new HotelRecyclerViewAdapter(this, masterDataHotelDetaill,idmasterDataHotelDetail);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerViewHotel.setLayoutManager(layoutManager);
        recyclerViewHotel.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHotel.setAdapter(hotelRecyclerViewAdapter);

        Reff = FirebaseDatabase.getInstance().getReference("Master-Data-Hotel-Detail");
        Reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                masterDataHotelDetaill.clear();
                idmasterDataHotelDetail.clear();
                if(cari.equals("")){
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        MasterDataHotelDetail masterdatahoteldetail = postSnapshot.getValue(MasterDataHotelDetail.class);
                        masterDataHotelDetaill.add(masterdatahoteldetail);
                        idmasterDataHotelDetail.add(postSnapshot.getKey());
                    }
                }else{
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        MasterDataHotelDetail masterdatahoteldetail = postSnapshot.getValue(MasterDataHotelDetail.class);
                        if(masterdatahoteldetail.getNamaKamar().contains(cari)){
                            masterDataHotelDetaill.add(masterdatahoteldetail);
                            idmasterDataHotelDetail.add(postSnapshot.getKey());
                        }

                    }
                }
                if(masterDataHotelDetaill.isEmpty()){
                    constraintLayout.setVisibility(View.VISIBLE);
                }else {
                    constraintLayout.setVisibility(View.INVISIBLE);
                }
                hotelRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void btnbackhotel (View view){
        Intent a = new  Intent(MenuHotelScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(MenuHotelScreen.this);
        finish();
    }
}