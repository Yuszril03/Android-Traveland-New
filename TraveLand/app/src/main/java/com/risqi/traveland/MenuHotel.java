package com.risqi.traveland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
                if(cari.equals("")){
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        MasterDataHotel masterdatahotel = postSnapshot.getValue(MasterDataHotel.class);
                        masterDataHotell.add(masterdatahotel);
                    }
                }else{
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        MasterDataHotel masterdatahotel = postSnapshot.getValue(MasterDataHotel.class);
                        if(masterdatahotel.getNamaHotel().contains(cari)){
                            masterDataHotell.add(masterdatahotel);
                        }

                    }
                }
                if(masterDataHotell.isEmpty()){
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
        Intent a = new  Intent(MenuHotel.this, MainMenu.class);
        startActivity(a);
        Animatoo.animateFade(MenuHotel.this);
        finish();
    }
}