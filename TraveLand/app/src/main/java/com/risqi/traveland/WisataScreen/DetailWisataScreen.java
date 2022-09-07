package com.risqi.traveland.WisataScreen;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    //dataBintang
    private BintangRecyclerViewAdapter bintangRecyclerViewAdapter;
    private RecyclerView recyclerBintang;
    private List<String> fillBintang = new ArrayList<>();


    private MapView map = null;
    private int internet = 0;
    private String idWisata;
    private DatabaseReference getReference;

    //Set MODe
    private DataMode dataMode;

    //Main Menu
    private TextView lihatsemuaUlasan;
    private Button buttonPesan;
    private ConstraintLayout backgrooundMain;
    private TextView judulwisata;
    private TextView deskripsi;
    private TextView harga, hargaDewasa,judulMap,judulDeskripsi,judulkomen,jumlahUlasan;
    private ImageView imagewisata;

    //Scroable
    private ScrollView scrollable;
    private ConstraintLayout backtoscroll;
    private TextView titleScroller;
    private Button btnbackscroller;
    private int scrolY = 0;

    //Penilaian
    private ConstraintLayout layoutDetailPenilaian,atasPenilaian;
    private Button btnbackpenilian;
    private TextView titlePenilaian;
    private View garisPoopUPNilai;
    private int aktifPopupNilai=0;
    //data Komentar
    private RecyclerView recyclerKomenterAll;
    private KomentarRecyclerViewAdapter komentarRecyclerViewAdapter2;
    private List<String> tempKomentar2 = new ArrayList<>();

    //dataLogin
    private DataLoginUser dataLoginUser;
    //data loginBefore
    private DataBeforeLogin dataBeforeLogin;


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
        toMenu();
        cekKondisi();
        toPemesanan();

    }

    private void toPemesanan() {
        buttonPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor resLogin = dataLoginUser.getDataOne();
                resLogin.moveToFirst();
                if(resLogin.getCount()>0){
                    Intent a = new Intent(DetailWisataScreen.this, TransactionWisataScreen.class);
                    a.putExtra("Before", "DetailWisata");
                    a.putExtra("idScreen", ""+idWisata);
                    startActivity(a);
                    Animatoo.animateSlideRight(DetailWisataScreen.this);
                    onStop();
                }else{
                    Intent a = new Intent(DetailWisataScreen.this, LoginScreen.class);
                    a.putExtra("Before", "DetailWisata");
                    a.putExtra("idScreen", ""+idWisata);
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

                    SweetAlertDialog pDialog=  new SweetAlertDialog(DetailWisataScreen.this, SweetAlertDialog.ERROR_TYPE)
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
        }else{
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
            aktifPopupNilai=1;
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
                            setDataKomentarAll();
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
            aktifPopupNilai=0;
            layoutDetailPenilaian.setAlpha(1.0f);
//            layoutDetailPenilaian.setTranslationY(0);
            layoutDetailPenilaian.animate()
                    .translationY(layoutDetailPenilaian.getHeight())
                    .alpha(0.0f)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            buttonPesan.setVisibility(View.VISIBLE);

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

                setBintang(4);
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


        //scrollable
        scrollable = findViewById(R.id.scrollable);
        backtoscroll = findViewById(R.id.backtoscroll);
        titleScroller = findViewById(R.id.titleScroller);
        btnbackscroller = findViewById(R.id.btnbackscroller);

        //Penilaian
        btnbackpenilian = findViewById(R.id.btnbackpenilian);
        atasPenilaian = findViewById(R.id.atasPenilaian);
        titlePenilaian = findViewById(R.id.titlePenilaian);
        garisPoopUPNilai = findViewById(R.id.garisPoopUPNilai);
        layoutDetailPenilaian = findViewById(R.id.layoutDetailPenilaian);
        recyclerKomenterAll = findViewById(R.id.recyclerKomenterAll);

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
        komentarRecyclerViewAdapter = new KomentarRecyclerViewAdapter(DetailWisataScreen.this, tempKomentar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerKomenter.setLayoutManager(layoutManager);
        recyclerKomenter.setItemAnimator(new DefaultItemAnimator());
        recyclerKomenter.setAdapter(komentarRecyclerViewAdapter);
        tempKomentar.clear();

        for (int i = 1; i <= 3; i++) {
            tempKomentar.add("NonFill");
        }
        komentarRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setDataKomentarAll() {
        komentarRecyclerViewAdapter2 = new KomentarRecyclerViewAdapter(DetailWisataScreen.this, tempKomentar2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerKomenterAll.setLayoutManager(layoutManager);
        recyclerKomenterAll.setItemAnimator(new DefaultItemAnimator());
        recyclerKomenterAll.setAdapter(komentarRecyclerViewAdapter2);
        tempKomentar2.clear();

        for (int i = 1; i <= 10; i++) {
            tempKomentar2.add("NonFill");
        }
        komentarRecyclerViewAdapter2.notifyDataSetChanged();
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
        if(aktifPopupNilai==1){
            setAllPenilian(0);
        }else{
            Intent a = new Intent(DetailWisataScreen.this, MenuWisataScreen.class);
            startActivity(a);
            Animatoo.animateSlideLeft(DetailWisataScreen.this);
            onStop();
        }
    }
}
