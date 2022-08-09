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

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.MasterDataRentalDetail;
import com.risqi.traveland.RecyclerView.RentalRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuRentalScreen extends AppCompatActivity {

    private List<MasterDataRentalDetail> masterDataRentalDetaill = new ArrayList<>();
    private List<String> idmasterDataRentalDetaill = new ArrayList<>();
    private DatabaseReference Reff;
    RecyclerView recyclerViewRental;
    private RentalRecyclerViewAdapter rentalRecyclerViewAdapter;
    private EditText textCari;
    private TextWatcher rTextWatcher = null;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_rental);
        initialize();
        setRental("");
        rTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(textCari.getText())){
                    setRental("");
                }else {
                    setRental(textCari.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        textCari.addTextChangedListener(rTextWatcher);
    }

    private void initialize(){
        recyclerViewRental = findViewById(R.id.vwRental);
        textCari = findViewById(R.id.editSearchrental);
        constraintLayout = findViewById(R.id.constraintrentalnodata);
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
                }else {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        MasterDataRentalDetail masterdatarentaldetail = postSnapshot.getValue(MasterDataRentalDetail.class);
                        if (masterdatarentaldetail.getNamaKendaraan().contains(cari)){
                            masterDataRentalDetaill.add(masterdatarentaldetail);
                            idmasterDataRentalDetaill.add(postSnapshot.getKey());
                        }
                    }
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

    public void btnbackrental (View view){
        Intent a = new  Intent(MenuRentalScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(MenuRentalScreen.this);
        finish();
    }
}