package com.risqi.traveland;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.risqi.traveland.RecyclerView.TagDataKegiatanRecyclerViewAdapter;
import com.risqi.traveland.SQLite.DataMode;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DetailKegiatanScreen extends AppCompatActivity {

    private MapView map = null;
    private String idKegiatan;
    private List<String> namaTag = new ArrayList<>();
    private TextView judulkegiatan, beritaevent, tanggalmulaiakhir, isikegiatan, judulMap, titleScroller;
    private ConstraintLayout main, utama, backtoscroll;
    private Button btnbackscroller;
    private LinearLayout lineardata;
    private ScrollView scrollable;
    private DataMode dataMode;
    private RecyclerView tagData;
    private TagDataKegiatanRecyclerViewAdapter dataKegiatanRecyclerViewAdapter;
    private ImageView imagekegiatan;
    private DatabaseReference getReference;
    private int scrolY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan);
        getIntenData();
        initialize();
        setMode();
        setData();
        setAnimationScroller();
        toMenu();

    }

    private void toMenu() {
        btnbackscroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(DetailKegiatanScreen.this, MainMenuScreen.class);
                startActivity(a);
                Animatoo.animateFade(DetailKegiatanScreen.this);
                onStop();
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
        Cursor mod = dataMode.getDataOne();
        mod.moveToFirst();
        String modeApps = "";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            modeApps = mod.getString(mod.getColumnIndexOrThrow("mode"));
            Log.d("ANEH", "setModeApp: " + modeApps);

            mod.moveToNext();
        }
        mod.close();
        if (modeApps.equals("Malam")) {
            lineardata.setBackgroundColor(getResources().getColor(R.color.darkMode));
            main.setBackgroundColor(getResources().getColor(R.color.darkMode));
            scrollable.setBackgroundColor(getResources().getColor(R.color.darkMode));
            utama.setBackgroundColor(getResources().getColor(R.color.darkMode));
            judulMap.setTextColor(getResources().getColor(R.color.white));
            isikegiatan.setTextColor(getResources().getColor(R.color.white));
            judulkegiatan.setTextColor(getResources().getColor(R.color.white));
            beritaevent.setTextColor(getResources().getColor(R.color.darkTxt));
            tanggalmulaiakhir.setTextColor(getResources().getColor(R.color.darkTxt));

            titleScroller.setTextColor(getResources().getColor(R.color.white));
            btnbackscroller.setBackgroundResource(R.drawable.icon_left_line_white);
            backtoscroll.setBackgroundResource(R.drawable.bacground_back_scroller_dark);
        } else {
            lineardata.setBackgroundColor(getResources().getColor(R.color.white));
            main.setBackgroundColor(getResources().getColor(R.color.white));
            scrollable.setBackgroundColor(getResources().getColor(R.color.white));
            utama.setBackgroundColor(getResources().getColor(R.color.white));
            judulMap.setTextColor(getResources().getColor(R.color.darkMode));
            isikegiatan.setTextColor(getResources().getColor(R.color.darkMode));
            judulkegiatan.setTextColor(getResources().getColor(R.color.darkMode));
            beritaevent.setTextColor(getResources().getColor(R.color.accent));
            tanggalmulaiakhir.setTextColor(getResources().getColor(R.color.accent));

            titleScroller.setTextColor(getResources().getColor(R.color.darkMode));
            btnbackscroller.setBackgroundResource(R.drawable.icon_left_line_dark);
            backtoscroll.setBackgroundResource(R.drawable.bacground_back_scroller_white);
        }
    }

    private void setData() {

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(14);

        getReference = FirebaseDatabase.getInstance().getReference();
        getReference.child("Data-Kegiatan").child(idKegiatan).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> kegiatan = (Map<String, Object>) task.getResult().getValue();

                GeoPoint startPoint = new GeoPoint(Double.parseDouble("" + kegiatan.get("Latitute")), Double.parseDouble("" + kegiatan.get("Longlitute")));
                mapController.setCenter(startPoint);
                Marker startMarker = new Marker(map);
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);

                judulkegiatan.setText("" + wordCase(kegiatan.get("Judul").toString()));
                beritaevent.setText("" + kegiatan.get("JenisKegiatan"));

                if (kegiatan.get("JenisKegiatan").equals("Berita")) {
                    tanggalmulaiakhir.setText("| " + kegiatan.get("TanggalMulai"));
                } else {
                    tanggalmulaiakhir.setText("| " + kegiatan.get("TanggalMulai") + " - " + kegiatan.get("TanggalAkhir"));
                }

                isikegiatan.setText("" + kegiatan.get("IsiKegiatan"));

                dataKegiatanRecyclerViewAdapter = new TagDataKegiatanRecyclerViewAdapter(DetailKegiatanScreen.this, namaTag);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                tagData.setLayoutManager(layoutManager);
                tagData.setItemAnimator(new DefaultItemAnimator());
                tagData.setAdapter(dataKegiatanRecyclerViewAdapter);
                namaTag.clear();
                String[] array = (kegiatan.get("KegiatanYangBerkaitan")).toString().split(",");
                for (int i = 0; i < array.length; i++) {
                    namaTag.add(array[i]);
                }
                dataKegiatanRecyclerViewAdapter.notifyDataSetChanged();

                if (kegiatan.get("LinkImage").equals("")) {

                } else {
                    Glide.with(DetailKegiatanScreen.this).clear(imagekegiatan);
                    Glide.with(DetailKegiatanScreen.this)
                            .load(kegiatan.get("LinkImage"))
                            .fitCenter()
//                    .transform(new MultiTransformation(new FitCenter()))
                            .apply(new RequestOptions()
//                                    .override(300, 600)
                                    .priority(Priority.HIGH)
                                    .centerCrop())
                            .into(imagekegiatan);
                }


            }
        });
    }

    private void getIntenData() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idKegiatan = bundle.getString("id");
        } else {
            idKegiatan = getIntent().getStringExtra("id");
        }
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

    private void initialize() {
        judulkegiatan = findViewById(R.id.judulKegiatan);
        beritaevent = findViewById(R.id.beritaevent);
        tanggalmulaiakhir = findViewById(R.id.tanggalmulaiakhir);
        isikegiatan = findViewById(R.id.isiDeskripsikegiatan);
        imagekegiatan = findViewById(R.id.imageviewkegiatan);
//        yangberkaitan = findViewById(R.id.yangberkaitan);
        tagData = findViewById(R.id.tagData);
        main = findViewById(R.id.main);
        lineardata = findViewById(R.id.lineardata);
        scrollable = findViewById(R.id.scrollable);
        utama = findViewById(R.id.utama);
        judulMap = findViewById(R.id.judulMap);
        backtoscroll = findViewById(R.id.backtoscroll);
        titleScroller = findViewById(R.id.titleScroller);
        btnbackscroller = findViewById(R.id.btnbackscroller);


        dataMode = new DataMode(this);
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

    public void backtomenuutama(View view) {
        Intent a = new Intent(DetailKegiatanScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(DetailKegiatanScreen.this);
        onStop();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(DetailKegiatanScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(DetailKegiatanScreen.this);
        onStop();
    }
}