package com.risqi.traveland.RentalScreen;

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
import com.risqi.traveland.Firebase.MasterDataRental;
import com.risqi.traveland.Firebase.MasterDataRentalDetail;
import com.risqi.traveland.MainMenuScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.RecyclerView.RentalRecyclerViewAdapter;
import com.risqi.traveland.SQLite.DataMode;

import java.util.ArrayList;
import java.util.List;

public class MenuRentalScreen extends AppCompatActivity {

    //Other
    private DataMode dataMode;
    //Rcycler and Database
    private List<MasterDataRentalDetail> masterDataRentalDetaill = new ArrayList<>();
    private List<String> idmasterDataRentalDetaill = new ArrayList<>();
    private DatabaseReference Reff,database1;
    RecyclerView recyclerViewRental;
    private RentalRecyclerViewAdapter rentalRecyclerViewAdapter;

    //Main
    private ConstraintLayout layoutUtama;
    private TextView textRental2;
    private EditText textCari;
    private TextWatcher rTextWatcher = null;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_rental);
        initialize();
        setMode();
        setRental("");
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
            textRental2.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void pencarianData(){
        rTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(textCari.getText())){
                    setRental("");
                }else {
                    setRental(textCari.getText().toString().toLowerCase());
                }

            }
        };
        textCari.addTextChangedListener(rTextWatcher);
    }

    private void initialize(){
        recyclerViewRental = findViewById(R.id.vwRental);
        textCari = findViewById(R.id.editSearch);
        constraintLayout = findViewById(R.id.constraintrentalnodata);
        textRental2 = findViewById(R.id.textRental2);
        layoutUtama = findViewById(R.id.layoutUtama);

        dataMode = new DataMode(this);
    }

    private void setRental(String cari){
        rentalRecyclerViewAdapter = new RentalRecyclerViewAdapter(this, masterDataRentalDetaill, idmasterDataRentalDetaill);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerViewRental.setLayoutManager(layoutManager);
        recyclerViewRental.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRental.setAdapter(rentalRecyclerViewAdapter);

        Reff = FirebaseDatabase.getInstance().getReference("Master-Data-Rental-Detail");
        Reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                masterDataRentalDetaill.clear();
                idmasterDataRentalDetaill.clear();
                if (cari.equals("")){
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        MasterDataRentalDetail masterdatarentaldetail = postSnapshot.getValue(MasterDataRentalDetail.class);
                        masterDataRentalDetaill.add(masterdatarentaldetail);
                        idmasterDataRentalDetaill.add(postSnapshot.getKey());
                    }
                    if (masterDataRentalDetaill.isEmpty()){
                        constraintLayout.setVisibility(View.VISIBLE);
                    }else {
                        constraintLayout.setVisibility(View.INVISIBLE);
                    }
                    rentalRecyclerViewAdapter.notifyDataSetChanged();
                }else {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        MasterDataRentalDetail masterdatarentaldetail = postSnapshot.getValue(MasterDataRentalDetail.class);

                        database1 = FirebaseDatabase.getInstance().getReference("Master-Data-Rental").child(masterdatarentaldetail.getIdRental());
                        database1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotRental) {
                                MasterDataRental rental = snapshotRental.getValue(MasterDataRental.class);

                                String judul = masterdatarentaldetail.getNamaKendaraan()+" - "+rental.getNamaRental();
                                if (judul.toLowerCase().contains(cari)){
                                    masterDataRentalDetaill.add(masterdatarentaldetail);
                                    idmasterDataRentalDetaill.add(postSnapshot.getKey());
                                }

                                if (masterDataRentalDetaill.isEmpty()){
                                    constraintLayout.setVisibility(View.VISIBLE);
                                }else {
                                    constraintLayout.setVisibility(View.INVISIBLE);
                                }
                                rentalRecyclerViewAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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

    public void btnbackrental (View view){
        Intent a = new  Intent(MenuRentalScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(MenuRentalScreen.this);
        onStop();
    }

    @Override
    public void onBackPressed() {
        Intent a = new  Intent(MenuRentalScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(MenuRentalScreen.this);
        onStop();
    }
}