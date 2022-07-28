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
import com.risqi.traveland.Firebase.MasterDataRental;
import com.risqi.traveland.RecyclerView.RentalRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuRental extends AppCompatActivity {

    private List<MasterDataRental> masterDataRentall = new ArrayList<>();
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
                if (cari.equals("")){
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        MasterDataRental masterdatarental = postSnapshot.getValue(MasterDataRental.class);
                        masterDataRentall.add(masterdatarental);
                    }
                }else {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()){
                        MasterDataRental masterdatarental = postSnapshot.getValue(MasterDataRental.class);
                        if (masterdatarental.getNamaRental().contains(cari)){
                            masterDataRentall.add(masterdatarental);
                        }
                    }
                }
                if (masterDataRentall.isEmpty()){
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
        Intent a = new  Intent(MenuRental.this, MainMenu.class);
        startActivity(a);
        Animatoo.animateFade(MenuRental.this);
        finish();
    }
}