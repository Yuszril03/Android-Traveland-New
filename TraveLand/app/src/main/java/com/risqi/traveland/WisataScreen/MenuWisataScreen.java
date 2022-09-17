package com.risqi.traveland.WisataScreen;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.MasterDataWisata;
import com.risqi.traveland.MainMenuScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.RecyclerView.WisataRecyclerViewAdapter;
import com.risqi.traveland.SQLite.DataMode;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MenuWisataScreen extends AppCompatActivity {

    private List<MasterDataWisata> masterDataWisata1 = new ArrayList<>();
    private List<String> idmasterDataWisata1 = new ArrayList<>();
    private DatabaseReference Reff, database1;
    RecyclerView recyclerViewWisata;
    private WisataRecyclerViewAdapter wisataRecyclerViewAdapter;
    private ConstraintLayout backgrooundMain;
    private TextView alertText;
    private EditText textCari;
    private TextWatcher textWatcher = null;
    private ConstraintLayout constraintLayout;
    private DataMode dataMode;
    private Button filterData;
    private SwipeRefreshLayout swipeRefresh;

    //modal filter
    private RadioButton bintang5, bintang4, bintang3, bintang2, bintang1;
    private RadioGroup radioButton;
    private TextView judulFilter;
    private Button btnclosefilter, reset, submit;
    private EditText minHarga, maxHarga;
    private ConstraintLayout modalMainBackground, modalBackground;
    private int modalFilter = 0;
    private int internet = 0;
    private int kondisiFilter = 0;
    private String dataBintang = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_wisata);
        initialize();
        setMode();
        setWisata("");
        setDataFilter();
        pencarianData();
        setFilterMode(0);
        showFilter();
        hideFilter();
        refreshData();
        cekKondisi();


    }

    private void setDataFilter() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(minHarga.getText()) && !TextUtils.isEmpty(maxHarga.getText())) {
                    if (Integer.parseInt(minHarga.getText().toString()) > Integer.parseInt(maxHarga.getText().toString())) {
                        new SweetAlertDialog(MenuWisataScreen.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Opps...")
                                .setContentText("Harga minimum tidak boleh lebih tinggi dari harga maksimum")
                                .setConfirmText("Okey")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else {
                        if (bintang1.isChecked()) {
                            dataBintang = "1";
                        } else if (bintang2.isChecked()) {
                            dataBintang = "2";
                        } else if (bintang3.isChecked()) {
                            dataBintang = "3";
                        } else if (bintang4.isChecked()) {
                            dataBintang = "4";
                        } else if (bintang5.isChecked()) {
                            dataBintang = "5";
                        } else {
                            dataBintang = "0";
                        }
                        kondisiFilter = 1;
                        textCari.setText("");
                        setWisata("");
                        filterData.setBackgroundResource(R.drawable.icon_filter_fill);
                        setFilterMode(0);
                    }
                } else {
                    if (bintang1.isChecked()) {
                        dataBintang = "1";
                    } else if (bintang2.isChecked()) {
                        dataBintang = "2";
                    } else if (bintang3.isChecked()) {
                        dataBintang = "3";
                    } else if (bintang4.isChecked()) {
                        dataBintang = "4";
                    } else if (bintang5.isChecked()) {
                        dataBintang = "5";
                    } else {
                        dataBintang = "0";
                    }

                    if (dataBintang.equals("0")) {
                        bintang1.setChecked(false);
                        bintang2.setChecked(false);
                        bintang3.setChecked(false);
                        bintang4.setChecked(false);
                        bintang5.setChecked(false);
                        dataBintang = "0";
                        kondisiFilter = 0;
                        minHarga.setText("");
                        maxHarga.setText("");
                        textCari.setText("");
                        setWisata("");
                        filterData.setBackgroundResource(R.drawable.icon_filter_no);
                        setFilterMode(0);
                    } else {
                        kondisiFilter = 1;
                        textCari.setText("");
                        setWisata("");
                        filterData.setBackgroundResource(R.drawable.icon_filter_fill);
                        setFilterMode(0);
                    }

                }


            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bintang1.setChecked(false);
                bintang2.setChecked(false);
                bintang3.setChecked(false);
                bintang4.setChecked(false);
                bintang5.setChecked(false);
                dataBintang = "0";
                kondisiFilter = 0;
                minHarga.setText("");
                maxHarga.setText("");
                textCari.setText("");
                setWisata("");
                filterData.setBackgroundResource(R.drawable.icon_filter_no);
                setFilterMode(0);
            }
        });
    }


    private void cekKondisi() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!adaInternet() && internet == 0) {
                    internet = 1;
                    recyclerViewWisata.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.VISIBLE);
                    new SweetAlertDialog(MenuWisataScreen.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Internet tidak terhubung")
                            .setContentText("Mohon cek kembali konkesi internet")
                            .setConfirmText("Okey")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(getIntent());
                                    Animatoo.animateFade(MenuWisataScreen.this);
                                    onStop();
                                }
                            })
                            .show();
                } else if (adaInternet() && internet == 0) {

                    recyclerViewWisata.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.GONE);


                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private boolean adaInternet() {
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }

    private void refreshData() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    private void hideFilter() {
        btnclosefilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFilterMode(0);
            }
        });
    }

    private void showFilter() {
        filterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFilterMode(1);
            }
        });
    }

    private void setFilterMode(int show) {
        if (show == 1) {
            modalFilter = 1;
            modalBackground.setAlpha(0.0f);
            modalBackground.animate()
                    .alpha(1.0f)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            modalBackground.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

            modalMainBackground.setAlpha(0.0f);
            modalMainBackground.setTranslationX(modalMainBackground.getWidth());
            modalMainBackground.animate()
                    .translationX(0)
                    .alpha(1.0f)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            modalMainBackground.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
        } else {
            modalFilter = 0;
            modalBackground.setAlpha(1.0f);
            modalBackground.animate()
                    .alpha(0.0f)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            modalBackground.setVisibility(View.GONE);

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
            modalMainBackground.setTranslationX(0);
            modalMainBackground.animate()
                    .translationX(modalMainBackground.getWidth())
                    .alpha(1.0f)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            modalMainBackground.setVisibility(View.GONE);

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
        }
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
            backgrooundMain.setBackgroundResource(R.color.darkMode2);
            alertText.setTextColor(getResources().getColor(R.color.white));

            //modal filter
            ColorStateList colorStateList = this.getResources().getColorStateList(R.color.white);
            bintang5.setTextColor(getResources().getColor(R.color.white));
            bintang5.setButtonTintList(colorStateList);
            bintang4.setTextColor(getResources().getColor(R.color.white));
            bintang4.setButtonTintList(colorStateList);
            bintang3.setTextColor(getResources().getColor(R.color.white));
            bintang3.setButtonTintList(colorStateList);
            bintang2.setTextColor(getResources().getColor(R.color.white));
            bintang2.setButtonTintList(colorStateList);
            bintang1.setTextColor(getResources().getColor(R.color.white));
            bintang1.setButtonTintList(colorStateList);
            judulFilter.setTextColor(getResources().getColor(R.color.white));
            btnclosefilter.setBackgroundResource(R.drawable.icon_cancel);
            modalMainBackground.setBackgroundResource(R.drawable.background_filter_dark);


        } else {
            ColorStateList colorStateList = this.getResources().getColorStateList(R.color.darkMode);
            backgrooundMain.setBackgroundResource(R.color.whitemode2);
            alertText.setTextColor(getResources().getColor(R.color.darkMode));
            //modal filter
            bintang5.setTextColor(getResources().getColor(R.color.darkMode));
            bintang5.setButtonTintList(colorStateList);
            bintang4.setTextColor(getResources().getColor(R.color.darkMode));
            bintang4.setButtonTintList(colorStateList);
            bintang3.setTextColor(getResources().getColor(R.color.darkMode));
            bintang3.setButtonTintList(colorStateList);
            bintang2.setTextColor(getResources().getColor(R.color.darkMode));
            bintang2.setButtonTintList(colorStateList);
            bintang1.setTextColor(getResources().getColor(R.color.darkMode));
            bintang1.setButtonTintList(colorStateList);
            judulFilter.setTextColor(getResources().getColor(R.color.darkMode));
            btnclosefilter.setBackgroundResource(R.drawable.icon_cancel_dark);
            modalMainBackground.setBackgroundResource(R.drawable.background_filter_white);
        }
    }

    private void pencarianData() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(textCari.getText())) {
                    setWisata("");
                } else {
                    setWisata(textCari.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        textCari.addTextChangedListener(textWatcher);
    }

    private void initialize() {
        recyclerViewWisata = findViewById(R.id.vwWisata);
        textCari = findViewById(R.id.editSearch);
        constraintLayout = findViewById(R.id.constraintwisatanodata);
        backgrooundMain = findViewById(R.id.backgrooundMain);
        alertText = findViewById(R.id.textWisata2);
        filterData = findViewById(R.id.filterData);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        dataMode = new DataMode(this);

        //modal filter
        bintang5 = findViewById(R.id.bintang5);
        bintang4 = findViewById(R.id.bintang4);
        bintang3 = findViewById(R.id.bintang3);
        bintang2 = findViewById(R.id.bintang2);
        bintang1 = findViewById(R.id.bintang1);

        radioButton = findViewById(R.id.radioButton);

        judulFilter = findViewById(R.id.judulFilter);
        reset = findViewById(R.id.reset);
        submit = findViewById(R.id.submit);
        maxHarga = findViewById(R.id.maxHarga);
        minHarga = findViewById(R.id.minHarga);
        btnclosefilter = findViewById(R.id.btnclosefilter);
        modalMainBackground = findViewById(R.id.modalMainBackground);
        modalBackground = findViewById(R.id.modalBackground);

    }

    private void setWisata(String cari) {
        wisataRecyclerViewAdapter = new WisataRecyclerViewAdapter(this, masterDataWisata1, idmasterDataWisata1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewWisata.setLayoutManager(layoutManager);
        recyclerViewWisata.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWisata.setAdapter(wisataRecyclerViewAdapter);

        Toast.makeText(this, "" + kondisiFilter, Toast.LENGTH_SHORT).show();
        if (kondisiFilter == 1) {
            if (TextUtils.isEmpty(minHarga.getText()) && TextUtils.isEmpty(maxHarga.getText())) {
                Reff = FirebaseDatabase.getInstance().getReference("Master-Data-Wisata");
                if (dataBintang.equals("0")) {
                    Reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            masterDataWisata1.clear();
                            idmasterDataWisata1.clear();
                            if (cari.equals("")) {
                                for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
                                    MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
                                    masterDataWisata1.add(masterdatawisataa);
                                    idmasterDataWisata1.add(postSnapshot1.getKey());
                                }
                            } else {
                                for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
                                    MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
                                    if (masterdatawisataa.getNamaWisata().contains(cari)) {
                                        masterDataWisata1.add(masterdatawisataa);
                                        idmasterDataWisata1.add(postSnapshot1.getKey());
                                    }
                                }
                            }

                            if (masterDataWisata1.isEmpty()) {
                                constraintLayout.setVisibility(View.VISIBLE);
                            } else {
                                constraintLayout.setVisibility(View.INVISIBLE);
                            }
                            wisataRecyclerViewAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            masterDataWisata1.clear();
                            idmasterDataWisata1.clear();
                            if (cari.equals("")) {
                                for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
                                    MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
                                    masterDataWisata1.add(masterdatawisataa);
                                    idmasterDataWisata1.add(postSnapshot1.getKey());
                                }
                            } else {
                                for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
                                    MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
                                    if (masterdatawisataa.getNamaWisata().contains(cari)) {
                                        masterDataWisata1.add(masterdatawisataa);
                                        idmasterDataWisata1.add(postSnapshot1.getKey());
                                    }
                                }
                            }

                            if (masterDataWisata1.isEmpty()) {
                                constraintLayout.setVisibility(View.VISIBLE);
                            } else {
                                constraintLayout.setVisibility(View.INVISIBLE);
                            }
                            wisataRecyclerViewAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

        } else {
            Reff = FirebaseDatabase.getInstance().getReference("Master-Data-Wisata");
            Reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    masterDataWisata1.clear();
                    idmasterDataWisata1.clear();

                    if (cari.equals("")) {
                        for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
                            MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
                            masterDataWisata1.add(masterdatawisataa);
                            idmasterDataWisata1.add(postSnapshot1.getKey());


                        }
                    } else {
                        for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
                            MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
                            if (masterdatawisataa.getNamaWisata().contains(cari)) {
                                masterDataWisata1.add(masterdatawisataa);
                                idmasterDataWisata1.add(postSnapshot1.getKey());
                            }
                        }
                    }


                    if (masterDataWisata1.isEmpty()) {
                        constraintLayout.setVisibility(View.VISIBLE);
                    } else {
                        constraintLayout.setVisibility(View.INVISIBLE);
                    }
                    wisataRecyclerViewAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        // Reff = FirebaseDatabase.getInstance().getReference("Master-Data-Wisata");
//        Reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                masterDataWisata1.clear();
//                idmasterDataWisata1.clear();
//                if(kondisiFilter==1){
//                    Toast.makeText(MenuWisataScreen.this, "Ini", Toast.LENGTH_SHORT).show();
////                    if(TextUtils.isEmpty(minHarga.getText()) && TextUtils.isEmpty(maxHarga.getText())){
////                        if(dataBintang.equals("0")){
////                            if (cari.equals("")) {
////                                for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
////                                    MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
////                                    masterDataWisata1.add(masterdatawisataa);
////                                    idmasterDataWisata1.add(postSnapshot1.getKey());
////                                }
////                            } else {
////                                for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
////                                    MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
////                                    if (masterdatawisataa.getNamaWisata().contains(cari)) {
////                                        masterDataWisata1.add(masterdatawisataa);
////                                        idmasterDataWisata1.add(postSnapshot1.getKey());
////                                    }
////                                }
////                            }
////                        }else{
////                            if (cari.equals("")) {
//////                                for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
//////                                    MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
//////
//////                                    database1 = FirebaseDatabase.getInstance().getReference("Transaction-Wisata");
//////                                    database1.addValueEventListener(new ValueEventListener() {
//////                                        @Override
//////                                        public void onDataChange(@NonNull DataSnapshot snapshotTransaksi) {
//////                                            int valueRating1 = 0;
//////                                            int valueRating2 = 0;
//////                                            int valueRating3 = 0;
//////                                            int valueRating4 = 0;
//////                                            int valueRating5 = 0;
////////                                            for (DataSnapshot postTransaksi : snapshotTransaksi.getChildren()){
////////                                                TransactionWIisata transactionWIisataData = postTransaksi.getValue(TransactionWIisata.class);
////////                                                if (transactionWIisataData.getIdMitra().equals(""+postTransaksi.getKey())) {
////////
////////                                                    if (transactionWIisataData.getStatusTransaksi().equals("4") && !transactionWIisataData.getRating().equals("")) {
//////////                                                        jumlahUlasanALL++;
////////                                                        if (transactionWIisataData.getRating().equals("1")) {
////////                                                            valueRating1++;
////////                                                        } else if (transactionWIisataData.getRating().equals("2")) {
////////                                                            valueRating2++;
////////                                                        } else if (transactionWIisataData.getRating().equals("3")) {
////////                                                            valueRating3++;
////////                                                        } else if (transactionWIisataData.getRating().equals("4")) {
////////                                                            valueRating4++;
////////                                                        } else if (transactionWIisataData.getRating().equals("5")) {
////////                                                            valueRating5++;
////////                                                        }
////////                                                    }
////////
////////                                                }
////////                                            }
////////
////////                                            int totalRating = ((1 * valueRating1) + (2 * valueRating2) + (3 * valueRating3) + (4 * valueRating4) + (5 * valueRating5));
////////                                            int totalAllRating = (valueRating1 + valueRating2 + valueRating3 + valueRating4 + valueRating5);
//////////                                            if (totalRating > 0) {
//////////                                                if((totalRating / totalAllRating) == Integer.parseInt(dataBintang)){
//////////                                                    masterDataWisata1.add(masterdatawisataa);
//////////                                                    idmasterDataWisata1.add(postSnapshot1.getKey());
//////////                                                }
//////////                                            }
//////
//////
//////                                        }
//////
//////                                        @Override
//////                                        public void onCancelled(@NonNull DatabaseError error) {
//////
//////                                        }
//////                                    });
//////
//////
//////                                }
////                            } else {
////                                for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
////                                    MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
////
////                                    database1 = FirebaseDatabase.getInstance().getReference("Transaction-Wisata");
////                                    database1.addValueEventListener(new ValueEventListener() {
////                                        @Override
////                                        public void onDataChange(@NonNull DataSnapshot snapshotTransaksi) {
////                                            int valueRating1 = 0;
////                                            int valueRating2 = 0;
////                                            int valueRating3 = 0;
////                                            int valueRating4 = 0;
////                                            int valueRating5 = 0;
////                                            for (DataSnapshot postTransaksi : snapshotTransaksi.getChildren()){
////                                                TransactionWIisata transactionWIisataData = postTransaksi.getValue(TransactionWIisata.class);
////                                                if (transactionWIisataData.getIdMitra().equals(postTransaksi.getKey())) {
////
////                                                    if (transactionWIisataData.getStatusTransaksi().equals("4") && !transactionWIisataData.getRating().equals("")) {
//////                                                        jumlahUlasanALL++;
////                                                        if (transactionWIisataData.getRating().equals("1")) {
////                                                            valueRating1++;
////                                                        } else if (transactionWIisataData.getRating().equals("2")) {
////                                                            valueRating2++;
////                                                        } else if (transactionWIisataData.getRating().equals("3")) {
////                                                            valueRating3++;
////                                                        } else if (transactionWIisataData.getRating().equals("4")) {
////                                                            valueRating4++;
////                                                        } else if (transactionWIisataData.getRating().equals("5")) {
////                                                            valueRating5++;
////                                                        }
////                                                    }
////
////                                                }
////                                            }
////
////                                            int totalRating = ((1 * valueRating1) + (2 * valueRating2) + (3 * valueRating3) + (4 * valueRating4) + (5 * valueRating5));
////                                            int totalAllRating = (valueRating1 + valueRating2 + valueRating3 + valueRating4 + valueRating5);
////
////                                            if(totalRating == Integer.parseInt(dataBintang) && masterdatawisataa.getNamaWisata().contains(cari)){
////                                                masterDataWisata1.add(masterdatawisataa);
////                                                idmasterDataWisata1.add(postSnapshot1.getKey());
////                                            }
////
////                                        }
////
////                                        @Override
////                                        public void onCancelled(@NonNull DatabaseError error) {
////
////                                        }
////                                    });
////
//////                                    if (masterdatawisataa.getNamaWisata().contains(cari)) {
//////                                        masterDataWisata1.add(masterdatawisataa);
//////                                        idmasterDataWisata1.add(postSnapshot1.getKey());
//////                                    }
////                                }
////                            }
////                        }
////
////                    }
//                }else{
//                    if (cari.equals("")) {
//                        for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
//                            MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
//                            masterDataWisata1.add(masterdatawisataa);
//                            idmasterDataWisata1.add(postSnapshot1.getKey());
//                        }
//                    } else {
//                        for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
//                            MasterDataWisata masterdatawisataa = postSnapshot1.getValue(MasterDataWisata.class);
//                            if (masterdatawisataa.getNamaWisata().contains(cari)) {
//                                masterDataWisata1.add(masterdatawisataa);
//                                idmasterDataWisata1.add(postSnapshot1.getKey());
//                            }
//                        }
//                    }
//                }
//
//                if (masterDataWisata1.isEmpty()) {
//                    constraintLayout.setVisibility(View.VISIBLE);
//                } else {
//                    constraintLayout.setVisibility(View.INVISIBLE);
//                }
//                wisataRecyclerViewAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void btnbackwisata(View view) {
        Intent a = new Intent(MenuWisataScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(MenuWisataScreen.this);
        onStop();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(MenuWisataScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(MenuWisataScreen.this);
        onStop();
    }
}