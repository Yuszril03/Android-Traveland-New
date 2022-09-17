package com.risqi.traveland.WisataScreen;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.risqi.traveland.Firebase.TransactionWIisata;
import com.risqi.traveland.R;
import com.risqi.traveland.RecyclerView.BintangRecyclerViewAdapter;
import com.risqi.traveland.RecyclerView.KomentarRecyclerViewAdapter;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class DetailWisataScreen extends AppCompatActivity {

    //data Komentar
    private RecyclerView recyclerKomenter;
    private KomentarRecyclerViewAdapter komentarRecyclerViewAdapter;
    private List<String> tempKomentar = new ArrayList<>();
    private List<TransactionWIisata> tempKomentarSpill = new ArrayList<>();
    private List<TransactionWIisata> tempKomentarALL = new ArrayList<>();

    //dataBintang
    private BintangRecyclerViewAdapter bintangRecyclerViewAdapter;
    private RecyclerView recyclerBintang;
    private List<String> fillBintang = new ArrayList<>();


    private MapView map = null;
    private int internet = 0;
    private String idWisata;
    private DatabaseReference getReference, database1;

    //Set MODe
    private DataMode dataMode;

    //Main Menu
    private TextView lihatsemuaUlasan;
    private Button buttonPesan;
    private ConstraintLayout backgrooundMain;
    private TextView judulwisata, bintangAwal;
    private TextView deskripsi;
    private TextView harga, hargaDewasa, judulMap, judulDeskripsi, judulkomen, jumlahUlasan,lihatpeta;
    private ImageView imagewisata;

    //Scroable
    private ScrollView scrollable;
    private ConstraintLayout backtoscroll;
    private TextView titleScroller;
    private Button btnbackscroller;
    private int scrolY = 0;

    //Penilaian
    private ConstraintLayout layoutDetailPenilaian, atasPenilaian;
    private Button btnbackpenilian;
    private TextView titlePenilaian, textPenilaian,textnoCOmment;
    private ImageView bgnoComent;
    private View garisPoopUPNilai;
    private ConstraintLayout bgBintang1,bgBintang2,bgBintang3,bgBintang4,bgBintang5;
    private TextView textStart1,textStart2,textStart3,textStart4,textStart5;
    private int kondisiFilterStar=0;
    private int aktifPopupNilai = 0;
    //data Komentar
    private RecyclerView recyclerKomenterAll;
    private KomentarRecyclerViewAdapter komentarRecyclerViewAdapter2;
    private List<String> tempKomentar2 = new ArrayList<>();

    //dataLogin
    private DataLoginUser dataLoginUser;
    //data loginBefore
    private DataBeforeLogin dataBeforeLogin;
    //redirect in maps
    private Uri gmmIntentUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);

        setIntenData();
        initialize();
        setMode();
        setAnimationScroller();
        setDataWisata();
        setAllPenilian(0);
        clickAllPenilian();
        clickCloseAllPenilian();
//        setFilterStar(0);
        setOnlickDataFilterStar();
        toMenu();
        cekKondisi();
        toPemesanan();
        //setTo Map
        setMapsTo();

    }

    private void setMapsTo() {
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

    private void setOnlickDataFilterStar() {
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

    private void setNoComentData(boolean comment){
        if(comment==true){
            textnoCOmment.setVisibility(View.VISIBLE);
            bgnoComent.setVisibility(View.VISIBLE);
        }else{
            textnoCOmment.setVisibility(View.GONE);
            bgnoComent.setVisibility(View.GONE);
        }
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

    private void toPemesanan() {
        buttonPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor resLogin = dataLoginUser.getDataOne();
                resLogin.moveToFirst();
                if (resLogin.getCount() > 0) {
                    Intent a = new Intent(DetailWisataScreen.this, TransactionWisataScreen.class);
                    a.putExtra("Before", "DetailWisata");
                    a.putExtra("idScreen", "" + idWisata);
                    startActivity(a);
                    Animatoo.animateSlideRight(DetailWisataScreen.this);
                    onStop();
                } else {
                    Intent a = new Intent(DetailWisataScreen.this, LoginScreen.class);
                    a.putExtra("Before", "DetailWisata");
                    a.putExtra("idScreen", "" + idWisata);
                    startActivity(a);
                    Animatoo.animateFade(DetailWisataScreen.this);
                    onStop();
                }

            }
        });
    }

    private void toMenu() {
        btnbackscroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(DetailWisataScreen.this, MenuWisataScreen.class);
                startActivity(a);
                Animatoo.animateSlideLeft(DetailWisataScreen.this);
                onStop();
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

                    SweetAlertDialog pDialog = new SweetAlertDialog(DetailWisataScreen.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Internet tidak terhubung")
                            .setContentText("Mohon cek kembali konkesi internet")
                            .setConfirmText("Okey")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(getIntent());
                                    Animatoo.animateFade(DetailWisataScreen.this);
                                    onStop();
                                }
                            });
                    pDialog.setCancelable(false);
                    pDialog.show();

                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private boolean adaInternet() {
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
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
            judulwisata.setTextColor(getResources().getColor(R.color.white));
            hargaDewasa.setTextColor(getResources().getColor(R.color.darkTxt));
            harga.setTextColor(getResources().getColor(R.color.darkTxt));
            judulMap.setTextColor(getResources().getColor(R.color.darkTxt));
            judulDeskripsi.setTextColor(getResources().getColor(R.color.darkTxt));
            deskripsi.setTextColor(getResources().getColor(R.color.white));
            judulkomen.setTextColor(getResources().getColor(R.color.darkTxt));
            jumlahUlasan.setTextColor(getResources().getColor(R.color.darkTxt));

            //SCROLL
            titleScroller.setTextColor(getResources().getColor(R.color.white));
            btnbackscroller.setBackgroundResource(R.drawable.icon_left_line_white);
            backtoscroll.setBackgroundResource(R.drawable.bacground_back_scroller_dark);

            //Penilaian
            layoutDetailPenilaian.setBackgroundResource(R.color.darkMode);
            atasPenilaian.setBackgroundResource(R.color.darkMode2);
            garisPoopUPNilai.setBackgroundResource(R.color.darkMode2);
            btnbackpenilian.setBackgroundResource(R.drawable.icon_left_line_white);
            titlePenilaian.setTextColor(getResources().getColor(R.color.white));
            textnoCOmment.setTextColor(getResources().getColor(R.color.white));
        } else {
            //Main
            backgrooundMain.setBackgroundResource(R.color.white);
            judulwisata.setTextColor(getResources().getColor(R.color.darkMode));
            hargaDewasa.setTextColor(getResources().getColor(R.color.accent));
            harga.setTextColor(getResources().getColor(R.color.accent));
            judulMap.setTextColor(getResources().getColor(R.color.accent));
            judulDeskripsi.setTextColor(getResources().getColor(R.color.accent));
            deskripsi.setTextColor(getResources().getColor(R.color.darkMode));
            judulkomen.setTextColor(getResources().getColor(R.color.accent));
            jumlahUlasan.setTextColor(getResources().getColor(R.color.accent));

            //SCROLL
            titleScroller.setTextColor(getResources().getColor(R.color.darkMode));
            btnbackscroller.setBackgroundResource(R.drawable.icon_left_line_dark);
            backtoscroll.setBackgroundResource(R.drawable.bacground_back_scroller_white);

            //Penilaian
            layoutDetailPenilaian.setBackgroundResource(R.color.white);
            atasPenilaian.setBackgroundResource(R.color.whitemode2);
            garisPoopUPNilai.setBackgroundResource(R.color.whitemode2);
            btnbackpenilian.setBackgroundResource(R.drawable.icon_left_line_dark);
            titlePenilaian.setTextColor(getResources().getColor(R.color.darkMode));
            textnoCOmment.setTextColor(getResources().getColor(R.color.darkMode));
        }
    }

    private void clickCloseAllPenilian() {

        btnbackpenilian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllPenilian(0);
            }
        });
    }

    private void clickAllPenilian() {

        lihatsemuaUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllPenilian(1);
            }
        });
    }

    private void setAllPenilian(int show) {
        if (show == 1) {
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

    private void setDataWisata() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(18);


        getReference = FirebaseDatabase.getInstance().getReference();
        getReference.child("Master-Data-Wisata").child(idWisata).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> wisata = (Map<String, Object>) task.getResult().getValue();

                gmmIntentUri = Uri.parse("google.streetview:cbll="+wisata.get("Latitude")+","+wisata.get("Longlitude"));

                GeoPoint startPoint = new GeoPoint(Double.parseDouble("" + wisata.get("Latitude")), Double.parseDouble("" + wisata.get("Longlitude")));
                mapController.setCenter(startPoint);
                Marker startMarker = new Marker(map);
                startMarker.setIcon(getResources().getDrawable(R.drawable.icon_maps));
                startMarker.setPosition(startPoint);
                startMarker.setTitle(wisata.get("AlamatWisata").toString());
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
                map.getOverlays().add(startMarker);

                judulwisata.setText("" + wordCase(wisata.get("NamaWisata").toString()));
                deskripsi.setText("" + wordCase(wisata.get("DeskripsiWisata").toString()));
                harga.setText(formatRupiah(Double.parseDouble(wisata.get("HargaAnak").toString())));
                hargaDewasa.setText(formatRupiah(Double.parseDouble(wisata.get("HargaDewasa").toString())));

                database1 = FirebaseDatabase.getInstance().getReference("Transaction-Wisata");
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
                            TransactionWIisata wisata = postData.getValue(TransactionWIisata.class);
                            if (wisata.getIdMitra().equals(idWisata)) {

                                if (wisata.getStatusTransaksi().equals("4") && !wisata.getRating().equals("")) {
                                    jumlahUlasanALL++;
                                    if (wisata.getRating().equals("1")) {
                                        valueRating1++;
                                    } else if (wisata.getRating().equals("2")) {
                                        valueRating2++;
                                    } else if (wisata.getRating().equals("3")) {
                                        valueRating3++;
                                    } else if (wisata.getRating().equals("4")) {
                                        valueRating4++;
                                    } else if (wisata.getRating().equals("5")) {
                                        valueRating5++;
                                    }
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


                setDataKomentar();
                if (wisata.get("fotoWisata").equals("")) {

                } else {
                    Glide.with(DetailWisataScreen.this).clear(imagewisata);
                    Glide.with(DetailWisataScreen.this)
                            .load(wisata.get("fotoWisata"))
                            .fitCenter()
//                    .transform(new MultiTransformation(new FitCenter()))
                            .apply(new RequestOptions()
//                                    .override(300, 600)
                                    .priority(Priority.HIGH)
                                    .centerCrop())
                            .into(imagewisata);
                }
            }
        });
    }

    private void setIntenData() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idWisata = bundle.getString("idScreen");
        } else {
            idWisata = getIntent().getStringExtra("idScreen");
        }
//        Toast.makeText(this, idWisata, Toast.LENGTH_SHORT).show();
    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
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

    private void setBintang(int jumlahFill) {
        bintangRecyclerViewAdapter = new BintangRecyclerViewAdapter(DetailWisataScreen.this, fillBintang);
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

    public void initialize() {

        //DataMode
        dataMode = new DataMode(this);

        recyclerKomenter = findViewById(R.id.recyclerKomenter);
        recyclerBintang = findViewById(R.id.recyclerBintang);

        //MainMenu
        judulwisata = findViewById(R.id.judulWisata);
        deskripsi = findViewById(R.id.isiDeskripsi);
        harga = findViewById(R.id.hargaWisata);
        hargaDewasa = findViewById(R.id.hargaDewasa);
        imagewisata = findViewById(R.id.imageView12);
        lihatsemuaUlasan = findViewById(R.id.lihatsemuaUlasan);
        buttonPesan = findViewById(R.id.buttonPesan);
        backgrooundMain = findViewById(R.id.backgrooundMain);
        judulMap = findViewById(R.id.judulMap);
        judulDeskripsi = findViewById(R.id.judulDeskripsi);
        judulkomen = findViewById(R.id.judulkomen);
        jumlahUlasan = findViewById(R.id.jumlahUlasan);
        bintangAwal = findViewById(R.id.bintangAwal);
        lihatpeta = findViewById(R.id.lihatpeta);
        textnoCOmment = findViewById(R.id.textnoCOmment);
        bgnoComent = findViewById(R.id.bgnoComent);


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

        //datalogin
        dataLoginUser = new DataLoginUser(this);
        //data BEforeLogin
        dataBeforeLogin = new DataBeforeLogin(this);


    }

    private String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    private void setDataKomentar() {
        komentarRecyclerViewAdapter = new KomentarRecyclerViewAdapter(DetailWisataScreen.this, tempKomentarSpill);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerKomenter.setLayoutManager(layoutManager);
        recyclerKomenter.setItemAnimator(new DefaultItemAnimator());
        recyclerKomenter.setAdapter(komentarRecyclerViewAdapter);


        database1 = FirebaseDatabase.getInstance().getReference("Transaction-Wisata");
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempKomentarSpill.clear();
                int count = 0;
                for (DataSnapshot postData : snapshot.getChildren()) {
                    TransactionWIisata wisata = postData.getValue(TransactionWIisata.class);
                    if (wisata.getIdMitra().equals(idWisata)) {
                        if (wisata.getStatusTransaksi().equals("4") && !wisata.getRating().equals("")) {
                            if (count < 3) {

                                tempKomentarSpill.add(wisata);
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

    private void setDataKomentarAll( int i) {

        komentarRecyclerViewAdapter2 = new KomentarRecyclerViewAdapter(DetailWisataScreen.this, tempKomentarALL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerKomenterAll.setLayoutManager(layoutManager);
        recyclerKomenterAll.setItemAnimator(new DefaultItemAnimator());
        recyclerKomenterAll.setAdapter(komentarRecyclerViewAdapter2);

        database1 = FirebaseDatabase.getInstance().getReference("Transaction-Wisata");
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempKomentarALL.clear();
                int count = 0;
                for (DataSnapshot postData : snapshot.getChildren()) {
                    TransactionWIisata wisata = postData.getValue(TransactionWIisata.class);
                    if (wisata.getIdMitra().equals(idWisata)) {
                        if(i==0){
                            if (wisata.getStatusTransaksi().equals("4") && !wisata.getRating().equals("")){
                                tempKomentarALL.add(wisata);
                                count++;
                            }
                        }else{
                            if (wisata.getStatusTransaksi().equals("4") && wisata.getRating().equals(""+i)){
                                tempKomentarALL.add(wisata);
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

    public void backtomenuwisata(View view) {
        Intent a = new Intent(DetailWisataScreen.this, MenuWisataScreen.class);
        startActivity(a);
        Animatoo.animateSlideLeft(DetailWisataScreen.this);
        onStop();
    }

    @Override
    public void onBackPressed() {
        if (aktifPopupNilai == 1) {
            setAllPenilian(0);
        } else {
            Intent a = new Intent(DetailWisataScreen.this, MenuWisataScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailWisataScreen.this);
            onStop();
        }
    }
}
