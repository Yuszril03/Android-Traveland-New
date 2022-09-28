package com.risqi.traveland.HotelScreen;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.risqi.traveland.Firebase.MasterDataHotel;
import com.risqi.traveland.Firebase.MasterDataHotelDetail;
import com.risqi.traveland.MainMenuScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.RecyclerView.HotelRecyclerViewAdapter;
import com.risqi.traveland.SQLite.DataMode;

import java.util.ArrayList;
import java.util.List;

public class MenuHotelScreen extends AppCompatActivity {

    //Other
    private DataMode dataMode;

    //RecyclerView And Database
    private List<MasterDataHotelDetail> masterDataHotelDetaill = new ArrayList<>();
    private List<String> idmasterDataHotelDetail = new ArrayList<>();
    private DatabaseReference Reff,database1,database2;
    RecyclerView recyclerViewHotel;
    private HotelRecyclerViewAdapter hotelRecyclerViewAdapter;

    //Main
    private ConstraintLayout layoutUtama;
    private TextView textHotel2;
    private EditText textCari;
    private TextWatcher taTextWatcher =null;
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_hotel);
        initialize();
        setMode();
        setHotel("");
        pencarianData();


    }

    private void setMode() {
        //MODE
        Cursor mod = dataMode.getDataOne();
        mod.moveToFirst();
        String modeApps = "";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            modeApps = mod.getString(mod.getColumnIndexOrThrow("mode"));

            mod.moveToNext();
        }
        mod.close();
        if (modeApps.equals("Malam")) {
            layoutUtama.setBackgroundResource(R.color.darkMode2);
            textHotel2.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void pencarianData() {
        taTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(textCari.getText())){
                    setHotel("");
                }else{
                    setHotel(textCari.getText().toString().toLowerCase());
                }
            }
        };
        textCari.addTextChangedListener(taTextWatcher);
    }

    private void initialize() {
        recyclerViewHotel = findViewById(R.id.vwHotel);
        textCari = findViewById(R.id.editSearch);
        textHotel2 = findViewById(R.id.textHotel2);
        layoutUtama = findViewById(R.id.layoutUtama);
        constraintLayout = findViewById(R.id.constrainthotelnodata);

        dataMode = new DataMode(this);
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
                    if(masterDataHotelDetaill.isEmpty()){
                        constraintLayout.setVisibility(View.VISIBLE);
                    }else {
                        constraintLayout.setVisibility(View.INVISIBLE);
                    }
                    hotelRecyclerViewAdapter.notifyDataSetChanged();
                }else{
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        MasterDataHotelDetail masterdatahoteldetail = postSnapshot.getValue(MasterDataHotelDetail.class);

                        database1 = FirebaseDatabase.getInstance().getReference("Master-Data-Hotel").child(masterdatahoteldetail.getIdHotel());
                        database1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotHotel) {
                                MasterDataHotel dataHotel = snapshotHotel.getValue(MasterDataHotel.class);
                                String judul = masterdatahoteldetail.getNamaKamar()+"-"+dataHotel.getNamaHotel();
                                if (judul.toLowerCase().contains(cari)) {
                                    masterDataHotelDetaill.add(masterdatahoteldetail);
                                    idmasterDataHotelDetail.add(postSnapshot.getKey());
                                }

                                if(masterDataHotelDetaill.isEmpty()){
                                    constraintLayout.setVisibility(View.VISIBLE);
                                }else {
                                    constraintLayout.setVisibility(View.INVISIBLE);
                                }
                                hotelRecyclerViewAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError errorHotel) {

                            }
                        });



                    }
                }

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
        onStop();
    }

    @Override
    public void onBackPressed() {
        Intent a = new  Intent(MenuHotelScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(MenuHotelScreen.this);
        onStop();
    }
}