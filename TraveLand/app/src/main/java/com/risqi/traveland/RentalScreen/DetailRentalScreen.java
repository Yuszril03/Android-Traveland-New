package com.risqi.traveland.RentalScreen;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.TransactionRental;
import com.risqi.traveland.R;
import com.risqi.traveland.RecyclerView.BintangRecyclerViewAdapter;
import com.risqi.traveland.RecyclerView.KomentarRentalRecyclerViewAdapter;
import com.risqi.traveland.SQLite.DataBeforeLogin;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.SettingScreen.LoginScreen;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailRentalScreen extends AppCompatActivity {

    //data Komentar
    private RecyclerView recyclerKomenter;
    private KomentarRentalRecyclerViewAdapter komentarRecyclerViewAdapter;
    private List<String> tempKomentar = new ArrayList<>();
    private List<TransactionRental> tempKomentarSpill = new ArrayList<>();
    private List<TransactionRental> tempKomentarALL = new ArrayList<>();

    //dataBintang
    private BintangRecyclerViewAdapter bintangRecyclerViewAdapter;
    private RecyclerView recyclerBintang;
    private List<String> fillBintang = new ArrayList<>();

    //Map
    private MapView map;

    //Main Layout
    private ConstraintLayout backgrooundMain;


    //Main
    private String idRental, idRentalDetail;
    private TextView judulmobil, namamitra, ukuran, jumlahkursi, deskripsi, harga ,isideskripsirental, deskripsirental,lihatsemuaUlasan,jumlahUlasan,judulkomen,judulMap,judulukuran,juduljumlahkursi,judulDeskripsi,lihatpeta;
    private ImageView imagemobil, imagemitra;
    private Button buttonPesan;

    //Database
    private DatabaseReference getReference, getGetReference,database1,database2;

    //Set MODe
    private DataMode dataMode;
    //Penilaian
    private ConstraintLayout layoutDetailPenilaian, atasPenilaian;
    private Button btnbackpenilian;
    private TextView titlePenilaian, textPenilaian,textnoCOmment,bintangAwal;
    private ImageView bgnoComent;
    private View garisPoopUPNilai;
    private ConstraintLayout bgBintang1,bgBintang2,bgBintang3,bgBintang4,bgBintang5;
    private TextView textStart1,textStart2,textStart3,textStart4,textStart5;
    private int kondisiFilterStar=0;
    private int aktifPopupNilai = 0;
    //data Komentar
    private RecyclerView recyclerKomenterAll;
    private KomentarRentalRecyclerViewAdapter komentarRecyclerViewAdapter2;
    private List<String> tempKomentar2 = new ArrayList<>();

    //dataLogin
    private DataLoginUser dataLoginUser;
    //data loginBefore
    private DataBeforeLogin dataBeforeLogin;
    //redirect in maps
    private Uri gmmIntentUri;


    //Scroable
    private ScrollView scrollable;
    private ConstraintLayout backtoscroll;
    private TextView titleScroller;
    private Button btnbackscroller;
    private int scrolY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rental);
        getINten();
        initialize();
        setDataRental();
        setMode();
        setAnimationScroller();

        //toMaps
        toMaps();

        //toMenu
        toMenu();

        //toAll Komentar
        setAllPenilian(0);
        clickAllPenilian();
        clickCloseAllPenilian();
//        setFilterStar(0);
        setOnlickDataFilterStar();

        toTransaksi();


    }

    private void toTransaksi() {

        buttonPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor resLogin = dataLoginUser.getDataOne();
                resLogin.moveToFirst();
                if (resLogin.getCount() > 0) {
                    Intent a = new Intent(DetailRentalScreen.this, TransactionRentalScreen.class);
                    a.putExtra("Before", "DetailRental");
                    a.putExtra("idDetail", "" +idRentalDetail);
                    a.putExtra("idMaster", "" + idRental);
                    startActivity(a);
                    Animatoo.animateSlideRight(DetailRentalScreen.this);
                    onStop();
                } else {
                    Intent a = new Intent(DetailRentalScreen.this, LoginScreen.class);
                    a.putExtra("Before", "DetailRental");
                    a.putExtra("idScreen", "" + idRentalDetail+"-"+idRental);
                    startActivity(a);
                    Animatoo.animateFade(DetailRentalScreen.this);
                    onStop();
                }

            }
        });
    }

    private void setOnlickDataFilterStar(){
        bgBintang1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kondisiFilterStar==1){
                    kondisiFilterStar=0;
                    setFilterStar(0);
                }else{
                    kondisiFilterStar=1;
                    setFilterStar(1);
                }
            }
        });
        bgBintang2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kondisiFilterStar==2){
                    kondisiFilterStar=0;
                    setFilterStar(0);
                }else{
                    kondisiFilterStar=2;
                    setFilterStar(2);
                }
            }
        });
        bgBintang3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kondisiFilterStar==3){
                    kondisiFilterStar=0;
                    setFilterStar(0);
                }else{
                    kondisiFilterStar=3;
                    setFilterStar(3);
                }
            }
        });
        bgBintang4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kondisiFilterStar==4){
                    kondisiFilterStar=0;
                    setFilterStar(0);
                }else{
                    kondisiFilterStar=4;
                    setFilterStar(4);
                }
            }
        });
        bgBintang5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kondisiFilterStar==5){
                    kondisiFilterStar=0;
                    setFilterStar(0);
                }else{
                    kondisiFilterStar=5;
                    setFilterStar(5);
                }
            }
        });
    }
    private void setFilterStar(int i) {
        if(i==0){
            setDataKomentarAll(0);
            bgBintang1.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang2.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang3.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang4.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang5.setBackgroundResource(R.drawable.background_select_bintang_white);
        }else if(i==1){
            setDataKomentarAll(1);
            bgBintang1.setBackgroundResource(R.drawable.background_selected_bintang_white);
            bgBintang2.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang3.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang4.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang5.setBackgroundResource(R.drawable.background_select_bintang_white);
        }else if(i==2){
            setDataKomentarAll(2);
            bgBintang1.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang2.setBackgroundResource(R.drawable.background_selected_bintang_white);
            bgBintang3.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang4.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang5.setBackgroundResource(R.drawable.background_select_bintang_white);
        }else if(i==3){
            setDataKomentarAll(3);
            bgBintang1.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang2.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang3.setBackgroundResource(R.drawable.background_selected_bintang_white);
            bgBintang4.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang5.setBackgroundResource(R.drawable.background_select_bintang_white);
        }else if(i==4){
            setDataKomentarAll(4);
            bgBintang1.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang2.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang3.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang4.setBackgroundResource(R.drawable.background_selected_bintang_white);
            bgBintang5.setBackgroundResource(R.drawable.background_select_bintang_white);
        }else if(i==5){
            setDataKomentarAll(5);
            bgBintang1.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang2.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang3.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang4.setBackgroundResource(R.drawable.background_select_bintang_white);
            bgBintang5.setBackgroundResource(R.drawable.background_selected_bintang_white);
        }
    }

    private void setDataKomentarAll( int i) {

        komentarRecyclerViewAdapter2 = new KomentarRentalRecyclerViewAdapter(DetailRentalScreen.this, tempKomentarALL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerKomenterAll.setLayoutManager(layoutManager);
        recyclerKomenterAll.setItemAnimator(new DefaultItemAnimator());
        recyclerKomenterAll.setAdapter(komentarRecyclerViewAdapter2);

        database1 = FirebaseDatabase.getInstance().getReference("Transaction-Rental");
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempKomentarALL.clear();
                int count = 0;
                for (DataSnapshot postData : snapshot.getChildren()) {
                    TransactionRental transsRENTAL = postData.getValue(TransactionRental.class);
                    if (transsRENTAL.getIdMobil().equals(idRentalDetail)) {
                        if(i==0){
                            if (transsRENTAL.getStatusTransaksi().equals("5") && !transsRENTAL.getRating().equals("")){
                                tempKomentarALL.add(transsRENTAL);
                                count++;
                            }
                        }else{
                            if (transsRENTAL.getStatusTransaksi().equals("5") && transsRENTAL.getRating().equals(""+i)){
                                tempKomentarALL.add(transsRENTAL);
                                count++;
                            }
                        }

                    }
                }
                if(count==0){
                    setNoComentData(true);
                }else {
                    setNoComentData(false);
                }

                komentarRecyclerViewAdapter2.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setNoComentData(boolean comment){
        if(comment==true){
            textnoCOmment.setVisibility(View.VISIBLE);
            bgnoComent.setVisibility(View.VISIBLE);
        }else{
            textnoCOmment.setVisibility(View.GONE);
            bgnoComent.setVisibility(View.GONE);
        }
    }
    private void clickCloseAllPenilian(){
        btnbackpenilian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllPenilian(0);
            }
        });

    }
    private void clickAllPenilian(){
        lihatsemuaUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllPenilian(1);
            }
        });

    }

    private void setAllPenilian(int i) {
        if (i == 1) {
            aktifPopupNilai = 1;
            layoutDetailPenilaian.setAlpha(0.0f);
//            layoutDetailPenilaian.setTranslationY(layoutDetailPenilaian.getHeight());
            layoutDetailPenilaian.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            layoutDetailPenilaian.setVisibility(View.VISIBLE);
                            buttonPesan.setVisibility(View.GONE);
                            setFilterStar(0);
                            kondisiFilterStar=0;
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
            aktifPopupNilai = 0;
            layoutDetailPenilaian.setAlpha(1.0f);
//            layoutDetailPenilaian.setTranslationY(0);
            layoutDetailPenilaian.animate()
                    .translationY(layoutDetailPenilaian.getHeight())
                    .alpha(0.0f)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            buttonPesan.setVisibility(View.VISIBLE);
                            setDataKomentar();

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            layoutDetailPenilaian.setVisibility(View.GONE);

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


    private void toMenu() {
        btnbackscroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(DetailRentalScreen.this, MenuRentalScreen.class);
                startActivity(a);
                Animatoo.animateSlideLeft(DetailRentalScreen.this);
                onStop();
            }
        });
    }

    private void toMaps() {
        lihatpeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

// Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);
            }
        });
    }

    private void setAnimationScroller() {
        backtoscroll.setVisibility(View.GONE);
        scrollable.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                //130
                if (i1 >= 130 && scrolY == 0) {
                    scrolY = 1;
                    backtoscroll.setAlpha(0.0f);
                    backtoscroll.animate()
                            .alpha(1.0f)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    backtoscroll.setVisibility(View.VISIBLE);
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
                } else if (i1 < 130 && scrolY == 1) {
                    scrolY = 0;
                    backtoscroll.setAlpha(1.0f);
                    backtoscroll.animate()
                            .alpha(0.0f)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    backtoscroll.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                }
//                Log.d("scrollX", String.valueOf(i));
//                Log.d("scrollY", String.valueOf(i1));
//                Log.d("oldScrollX", String.valueOf(i2));
//                Log.d("oldScrollY", String.valueOf(i3));
            }
        });
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
            //Main
            backgrooundMain.setBackgroundResource(R.color.darkMode);
            judulmobil.setTextColor(getResources().getColor(R.color.white));
            namamitra.setTextColor(getResources().getColor(R.color.white));
            harga.setTextColor(getResources().getColor(R.color.darkTxt));
            judulMap.setTextColor(getResources().getColor(R.color.darkTxt));
            deskripsirental.setTextColor(getResources().getColor(R.color.darkTxt));
            lihatsemuaUlasan.setTextColor(getResources().getColor(R.color.darkTxt));
            ukuran.setTextColor(getResources().getColor(R.color.white));
            jumlahkursi.setTextColor(getResources().getColor(R.color.white));
            deskripsi.setTextColor(getResources().getColor(R.color.white));
            isideskripsirental.setTextColor(getResources().getColor(R.color.white));
            judulkomen.setTextColor(getResources().getColor(R.color.darkTxt));
            jumlahUlasan.setTextColor(getResources().getColor(R.color.darkTxt));
            judulMap.setTextColor(getResources().getColor(R.color.darkTxt));
            judulukuran.setTextColor(getResources().getColor(R.color.darkTxt));
            juduljumlahkursi.setTextColor(getResources().getColor(R.color.darkTxt));
            judulDeskripsi.setTextColor(getResources().getColor(R.color.darkTxt));

            //SCROLL
            titleScroller.setTextColor(getResources().getColor(R.color.white));
            btnbackscroller.setBackgroundResource(R.drawable.icon_left_line_white);
            backtoscroll.setBackgroundResource(R.drawable.bacground_back_scroller_dark);

//            //Penilaian
            layoutDetailPenilaian.setBackgroundResource(R.color.darkMode);
            atasPenilaian.setBackgroundResource(R.color.darkMode2);
            garisPoopUPNilai.setBackgroundResource(R.color.darkMode2);
            btnbackpenilian.setBackgroundResource(R.drawable.icon_left_line_white);
            titlePenilaian.setTextColor(getResources().getColor(R.color.white));
            textnoCOmment.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void setDataKomentar() {
        komentarRecyclerViewAdapter = new KomentarRentalRecyclerViewAdapter(DetailRentalScreen.this, tempKomentarSpill);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerKomenter.setLayoutManager(layoutManager);
        recyclerKomenter.setItemAnimator(new DefaultItemAnimator());
        recyclerKomenter.setAdapter(komentarRecyclerViewAdapter);


        database1 = FirebaseDatabase.getInstance().getReference("Transaction-Rental");
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempKomentarSpill.clear();
                int count = 0;
                for (DataSnapshot postData : snapshot.getChildren()) {
                    TransactionRental TransactionRentalss = postData.getValue(TransactionRental.class);
                    if (TransactionRentalss.getIdMobil().equals(idRentalDetail)) {
                        if (TransactionRentalss.getStatusTransaksi().equals("5") && !TransactionRentalss.getRating().equals("")) {
                            if (count < 3) {

                                tempKomentarSpill.add(TransactionRentalss);
                                count++;
                            }

                        }
                    }
                }
                if(count==0){
                    lihatsemuaUlasan.setVisibility(View.GONE);
                }else{
                    lihatsemuaUlasan.setVisibility(View.VISIBLE);
                }
                komentarRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setDataRental() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(18);

        database1 = FirebaseDatabase.getInstance().getReference("Transaction-Rental");
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int valueRating1 = 0;
                int valueRating2 = 0;
                int valueRating3 = 0;
                int valueRating4 = 0;
                int valueRating5 = 0;
                int jumlahUlasanALL = 0;
                for (DataSnapshot postData : snapshot.getChildren()) {
                    TransactionRental transRental = postData.getValue(TransactionRental.class);
                    if(transRental.getIdMobil().equals(idRentalDetail) && transRental.getStatusTransaksi().equals("5") && !transRental.getRating().equals("")){
                        jumlahUlasanALL++;
                        if (transRental.getRating().equals("1")) {
                            valueRating1++;
                        } else if (transRental.getRating().equals("2")) {
                            valueRating2++;
                        } else if (transRental.getRating().equals("3")) {
                            valueRating3++;
                        } else if (transRental.getRating().equals("4")) {
                            valueRating4++;
                        } else if (transRental.getRating().equals("5")) {
                            valueRating5++;
                        }
                    }
                }

                textStart1.setText("1 ("+valueRating1+")");
                textStart2.setText("2 ("+valueRating2+")");
                textStart3.setText("3 ("+valueRating3+")");
                textStart4.setText("4 ("+valueRating4+")");
                textStart5.setText("5 ("+valueRating5+")");
                int totalRating = ((1 * valueRating1) + (2 * valueRating2) + (3 * valueRating3) + (4 * valueRating4) + (5 * valueRating5));
                int totalAllRating = (valueRating1 + valueRating2 + valueRating3 + valueRating4 + valueRating5);
                if (totalRating > 0) {
                    textPenilaian.setText((totalRating / totalAllRating) + ".0/5");
                    bintangAwal.setText((totalRating / totalAllRating) + ".0");
                    jumlahUlasan.setText("( " + jumlahUlasanALL + " Ulasan )");
                    setBintang((totalRating / totalAllRating));
                } else {
                    textPenilaian.setText("0.0/5");
                    bintangAwal.setText("0.0");
                    jumlahUlasan.setText("( " + 0 + " Ulasan )");
                    setBintang(0);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getReference = FirebaseDatabase.getInstance().getReference();
        getReference.child("Master-Data-Rental-Detail").child(idRentalDetail).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> rental1 = (Map<String, Object>) task.getResult().getValue();

                judulmobil.setText(""+wordCase(rental1.get("NamaKendaraan").toString()));
                deskripsi.setText(""+wordCase(rental1.get("deskripsiKendaraan").toString()));
                harga.setText("Rp. "+rental1.get("HargaSewa")+" /12 jam");
                ukuran.setText(""+wordCase(rental1.get("UkuranKendaraan").toString()));
                jumlahkursi.setText(""+wordCase(rental1.get("JumlahKursi").toString()));
                if (rental1.get("fotoKendaraan").equals("")){

                }else {
                    Glide.with(DetailRentalScreen.this).clear(imagemobil);
                    Glide.with(DetailRentalScreen.this)
                            .load(rental1.get("fotoKendaraan"))
//                    .transform(new MultiTransformation(new FitCenter()))
                            .apply(new RequestOptions()
//                                    .override(300, 600)
                                    .priority(Priority.HIGH)
                                    .centerCrop())
                            .into(imagemobil);
                }
            }
        });

        getGetReference = FirebaseDatabase.getInstance().getReference();
        getGetReference.child("Master-Data-Rental").child(idRental).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String,Object> rental2 = (Map<String, Object>) task.getResult().getValue();
                gmmIntentUri = Uri.parse("google.streetview:cbll="+rental2.get("Latitude")+","+rental2.get("Longlitude"));
                GeoPoint startPoint = new GeoPoint(Double.parseDouble(""+rental2.get("Latitude")), Double.parseDouble(""+rental2.get("Longlitude")));
                mapController.setCenter(startPoint);
                Marker startMarker = new Marker(map);
                startMarker.setIcon(getResources().getDrawable(R.drawable.icon_maps));
                startMarker.setTitle(rental2.get("AlamatRental").toString());
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
                map.getOverlays().add(startMarker);

                namamitra.setText(""+wordCase(rental2.get("NamaRental").toString()));
                isideskripsirental.setText(""+wordCase(rental2.get("DeskripsiRental").toString()));

                if (rental2.get("fotoRental").equals("")){

                }else {
                    Glide.with(DetailRentalScreen.this).clear(imagemitra);
                    Glide.with(DetailRentalScreen.this)
                            .load(rental2.get("fotoRental"))
//                    .transform(new MultiTransformation(new FitCenter()))
                            .apply(new RequestOptions()
//                                    .override(300, 600)
                                    .circleCrop()
                                    .priority(Priority.HIGH)
                                    .centerCrop())
                            .into(imagemitra);
                }
            }
        });
    }
    private void setBintang(int jumlahFill) {
        bintangRecyclerViewAdapter = new BintangRecyclerViewAdapter(DetailRentalScreen.this, fillBintang);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerBintang.setLayoutManager(layoutManager);
        recyclerBintang.setItemAnimator(new DefaultItemAnimator());
        recyclerBintang.setAdapter(bintangRecyclerViewAdapter);
        fillBintang.clear();

        for (int i = 1; i <= 5; i++) {
            if (i > jumlahFill) {
                fillBintang.add("NonFill");
            } else {
                fillBintang.add("Fill");
            }

        }
        bintangRecyclerViewAdapter.notifyDataSetChanged();
    }


    private void getINten() {
        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            idRental = bundle.getString("idMaster");
            idRentalDetail = bundle.getString("idDetail");
        }else{
            idRental = getIntent().getStringExtra("idMaster");
            idRentalDetail = getIntent().getStringExtra("idDetail");
        }
    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    private String wordCase(String str) {
        String words[] = str.split("\\s");
        String capitalizeWord = "";
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase() + afterfirst + " ";
        }
        return capitalizeWord.trim();
    }

    private void initialize(){

        //Main Layout
        backgrooundMain = findViewById(R.id.backgrooundMain);

        //Main
        judulmobil = findViewById(R.id.judulmobil);
        namamitra = findViewById(R.id.namamitrarental);
        ukuran = findViewById(R.id.isiukuran);
        jumlahkursi = findViewById(R.id.isiJumlahKursi);
        deskripsi = findViewById(R.id.isiDeskripsi);
        harga = findViewById(R.id.hargamobil);
        imagemobil = findViewById(R.id.imageviewrentaldetail1);
        imagemitra = findViewById(R.id.imageprofilerental);
        isideskripsirental = findViewById(R.id.isideskripsirental);
        deskripsirental = findViewById(R.id.deskripsirental);
        lihatsemuaUlasan = findViewById(R.id.lihatsemuaUlasan);
        jumlahUlasan = findViewById(R.id.jumlahUlasan);
        judulkomen = findViewById(R.id.judulkomen);
        judulMap = findViewById(R.id.judulMap);
        judulukuran = findViewById(R.id.judulukuran);
        juduljumlahkursi = findViewById(R.id.juduljumlahkursi);
        judulDeskripsi = findViewById(R.id.judulDeskripsi);
        lihatpeta = findViewById(R.id.lihatpeta);
        buttonPesan = findViewById(R.id.buttonPesan);

        //scrollable
        scrollable = findViewById(R.id.scrollable);
        backtoscroll = findViewById(R.id.backtoscroll);
        titleScroller = findViewById(R.id.titleScroller);
        btnbackscroller = findViewById(R.id.btnbackscroller);

        //Penilaian
        btnbackpenilian = findViewById(R.id.btnbackpenilian);
        textPenilaian = findViewById(R.id.textPenilaian);
        atasPenilaian = findViewById(R.id.atasPenilaian);
        titlePenilaian = findViewById(R.id.titlePenilaian);
        garisPoopUPNilai = findViewById(R.id.garisPoopUPNilai);
        layoutDetailPenilaian = findViewById(R.id.layoutDetailPenilaian);
        recyclerKomenterAll = findViewById(R.id.recyclerKomenterAll);
        textnoCOmment = findViewById(R.id.textnoCOmment);
        bgnoComent = findViewById(R.id.bgnoComent);
        //Filter STart
        bgBintang1 = findViewById(R.id.bgBintang1);
        bgBintang2 = findViewById(R.id.bgBintang2);
        bgBintang3 = findViewById(R.id.bgBintang3);
        bgBintang4 = findViewById(R.id.bgBintang4);
        bgBintang5 = findViewById(R.id.bgBintang5);
        textStart1 = findViewById(R.id.textStart1);
        textStart2 = findViewById(R.id.textStart2);
        textStart3 = findViewById(R.id.textStart3);
        textStart4 = findViewById(R.id.textStart4);
        textStart5 = findViewById(R.id.textStart5);
        bintangAwal = findViewById(R.id.bintangAwal);
        recyclerKomenter = findViewById(R.id.rvGroceries);
        recyclerBintang = findViewById(R.id.recyclerBintang);

        //Mode
        dataMode = new DataMode(this);
        dataLoginUser = new DataLoginUser(this);
    }

    public void backtomenurental(View view){
        Intent a = new  Intent(DetailRentalScreen.this, MenuRentalScreen.class);
        startActivity(a);
        Animatoo.animateSlideLeft(DetailRentalScreen.this);
        onStop();
    }

    @Override
    public void onBackPressed() {
        if (aktifPopupNilai == 1) {
            setAllPenilian(0);
        }else {
            Intent a = new Intent(DetailRentalScreen.this, MenuRentalScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailRentalScreen.this);
            onStop();
        }
    }
}