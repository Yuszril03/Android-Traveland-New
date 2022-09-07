package com.risqi.traveland.SettingScreen;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.TransactionWIisata;
import com.risqi.traveland.R;
import com.risqi.traveland.RecyclerView.OrderingHotelRecyclerViewAdapter;
import com.risqi.traveland.RecyclerView.OrderingRentalRecyclerViewAdapter;
import com.risqi.traveland.RecyclerView.OrderingWisataRecyclerViewAdapter;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;

import java.util.ArrayList;
import java.util.List;

public class HistoryOrderingScreen extends AppCompatActivity {


    //Layout Utama
    private ConstraintLayout layoutUtama;

    //Top
    private Button btnbackscroller;

    //Data RecycleView Transaksi
    private RecyclerView dataTransaksi;

    //ALL KEY DATA
    List<String> KeyData = new ArrayList<>();

    //Tranaksi WIsata
    private OrderingWisataRecyclerViewAdapter orderingWisataRecyclerViewAdapter;
    private List<TransactionWIisata> dataWisata = new ArrayList<>();

    //Transaksi Hotel
    private OrderingHotelRecyclerViewAdapter orderingHotelRecyclerViewAdapter;
    private List<String> dataHotel = new ArrayList<>();

    //Transaksi rental
    private OrderingRentalRecyclerViewAdapter orderingRentalRecyclerViewAdapter;
    private List<String> dataRental = new ArrayList<>();

    //Button Pilihan
    //Wisata
    private TextView textWisata;
    private ConstraintLayout bgWisata;
    private View garis1;

    //Hotel
    private TextView textHotel;
    private ConstraintLayout bgHotel;
    private View garis2;

    //Rental
    private TextView textRental;
    private ConstraintLayout bgRental;
    private View garis3;

    //NO data
    private ConstraintLayout constraintwisatanodata;
    private TextView textOrderFailed;

    //DarkMOde
    private DataMode dataMode;
    private DataLoginUser dataLoginUser;
    private String NIK ="";

    //Firebase
    DatabaseReference database1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_ordering_screen);
        initialize();
        setMode();
        setDataLoginUSer();

        setChoiceTRansaction(0);
        setOnclickPilihan();

        toMenu();
    }

    private void toMenu() {
        btnbackscroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(HistoryOrderingScreen.this, MainProfileScreen.class);
                startActivity(a);
                Animatoo.animateSlideRight(HistoryOrderingScreen.this);
                onStop();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(HistoryOrderingScreen.this, MainProfileScreen.class);
        startActivity(a);
        Animatoo.animateSlideRight(HistoryOrderingScreen.this);
        onStop();
    }

    private void setOnclickPilihan() {
        bgWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChoiceTRansaction(0);
            }
        });
        bgHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChoiceTRansaction(1);
            }
        });

        bgRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChoiceTRansaction(2);
            }
        });
    }

    private void setChoiceTRansaction(int i) {
        if(i==0){
            textWisata.setTypeface(ResourcesCompat.getFont(this,R.font.roboto_medium));
            garis1.setVisibility(View.VISIBLE);

            textHotel.setTypeface(ResourcesCompat.getFont(this,R.font.roboto));
            garis2.setVisibility(View.GONE);

            textRental.setTypeface(ResourcesCompat.getFont(this,R.font.roboto));
            garis3.setVisibility(View.GONE);
            setDataTransaksiWisata();
        }else if(i==1){
            textWisata.setTypeface(ResourcesCompat.getFont(this,R.font.roboto));
            garis1.setVisibility(View.GONE);

            textHotel.setTypeface(ResourcesCompat.getFont(this,R.font.roboto_medium));
            garis2.setVisibility(View.VISIBLE);

            textRental.setTypeface(ResourcesCompat.getFont(this,R.font.roboto));
            garis3.setVisibility(View.GONE);
            setDataTransaksiHotel();
        }else{
            textWisata.setTypeface(ResourcesCompat.getFont(this,R.font.roboto));
            garis1.setVisibility(View.GONE);

            textHotel.setTypeface(ResourcesCompat.getFont(this,R.font.roboto));
            garis2.setVisibility(View.GONE);

            textRental.setTypeface(ResourcesCompat.getFont(this,R.font.roboto_medium));
            garis3.setVisibility(View.VISIBLE);
            setDataTransaksiRental();
        }
    }

    private void setDataTransaksiWisata() {
        orderingWisataRecyclerViewAdapter = new OrderingWisataRecyclerViewAdapter(HistoryOrderingScreen.this, dataWisata,KeyData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        dataTransaksi.setLayoutManager(layoutManager);
        dataTransaksi.setItemAnimator(new DefaultItemAnimator());
        dataTransaksi.setAdapter(orderingWisataRecyclerViewAdapter);

        database1 = FirebaseDatabase.getInstance().getReference("Transaction-Wisata");
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataWisata.clear();
                KeyData.clear();
                for (DataSnapshot postData : snapshot.getChildren()){
                    TransactionWIisata transactionWIisata = postData.getValue(TransactionWIisata.class);
                    if(transactionWIisata.getStatusTransaksi().equals("4")){
                        if(transactionWIisata.getIdCutomer().equals(NIK)){
                            dataWisata.add(transactionWIisata);
                            KeyData.add(postData.getKey());
                        }

                    }else if(transactionWIisata.getStatusTransaksi().equals("2")){
                        if(transactionWIisata.getIdCutomer().equals(NIK)){
                            dataWisata.add(transactionWIisata);
                            KeyData.add(postData.getKey());
                        }

                    }else{


                    }

                }
                if(dataWisata.size()==0){
                    constraintwisatanodata.setVisibility(View.VISIBLE);
                    textOrderFailed.setText("Belum ada data transaksi");
                }
                else{
                    constraintwisatanodata.setVisibility(View.GONE);
                }
                orderingWisataRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        for (int i = 1; i <= 5; i++) {
//            dataWisata.add("Fill");
//
//        }

    }

    private void setDataTransaksiHotel() {
        orderingHotelRecyclerViewAdapter = new OrderingHotelRecyclerViewAdapter(HistoryOrderingScreen.this, dataHotel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        dataTransaksi.setLayoutManager(layoutManager);
        dataTransaksi.setItemAnimator(new DefaultItemAnimator());
        dataTransaksi.setAdapter(orderingHotelRecyclerViewAdapter);
        dataHotel.clear();

        for (int i = 1; i <= 2; i++) {
            dataHotel.add("Fill");

        }
        if(dataHotel.size()==0){
            constraintwisatanodata.setVisibility(View.VISIBLE);
            textOrderFailed.setText("Belum ada data transaksi");
        }
        else{
            constraintwisatanodata.setVisibility(View.GONE);
        }

        orderingHotelRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setDataTransaksiRental() {
        orderingRentalRecyclerViewAdapter = new OrderingRentalRecyclerViewAdapter(HistoryOrderingScreen.this, dataRental);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        dataTransaksi.setLayoutManager(layoutManager);
        dataTransaksi.setItemAnimator(new DefaultItemAnimator());
        dataTransaksi.setAdapter(orderingRentalRecyclerViewAdapter);
        dataRental.clear();

        for (int i = 1; i <= 10; i++) {
            dataRental.add("Fill");

        }
        if(dataRental.size()==0){
            constraintwisatanodata.setVisibility(View.VISIBLE);
            textOrderFailed.setText("Belum ada data transaksi");
        }else{
            constraintwisatanodata.setVisibility(View.GONE);
        }
        orderingRentalRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setDataLoginUSer() {
        //MODE
        Cursor mod = dataLoginUser.getDataOne();
        mod.moveToFirst();
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            NIK = mod.getString(mod.getColumnIndexOrThrow("nik"));

            mod.moveToNext();
        }
        mod.close();
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
            textOrderFailed.setTextColor(getResources().getColor(R.color.white));
            layoutUtama.setBackgroundResource(R.color.darkMode2);
            garis1.setBackgroundResource(R.color.darkMode);
            garis2.setBackgroundResource(R.color.darkMode);
            garis3.setBackgroundResource(R.color.darkMode);
        }
    }


    private void initialize() {

        //Layout Utama
        layoutUtama = findViewById(R.id.layoutUtama);

        //top
        btnbackscroller = findViewById(R.id.btnbackscroller);

        //DataTransaksi
        dataTransaksi= findViewById(R.id.dataTransaksi);

        //pilihan Data
        bgWisata = findViewById(R.id.bgWisata);
        textWisata = findViewById(R.id.textWisata);
        garis1 = findViewById(R.id.garis1);

        //pilihan Data
        bgHotel = findViewById(R.id.bgHotel);
        textHotel = findViewById(R.id.textHotel);
        garis2 = findViewById(R.id.garis2);

        //pilihan Data
        bgRental = findViewById(R.id.bgRental);
        textRental = findViewById(R.id.textRental);
        garis3 = findViewById(R.id.garis3);

        //No Data
        constraintwisatanodata= findViewById(R.id.constraintwisatanodata);
        textOrderFailed= findViewById(R.id.textOrderFailed);

        //DataMOde
        dataMode = new DataMode(this);
        dataLoginUser = new DataLoginUser(this);
    }
}