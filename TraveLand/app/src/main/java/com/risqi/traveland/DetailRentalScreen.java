package com.risqi.traveland;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Map;

public class DetailRentalScreen extends AppCompatActivity {

    private MapView map;
    private String idRental, idRentalDetail;
    private TextView judulmobil, namamitra, ukuran, jumlahkursi, deskripsi, harga, deskripsirental;
    private ImageView imagemobil, imagemitra;
    private DatabaseReference getReference, getGetReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rental);

        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            idRental = bundle.getString("idMaster");
            idRentalDetail = bundle.getString("idDetail");
        }else{
            idRental = getIntent().getStringExtra("idMaster");
            idRentalDetail = getIntent().getStringExtra("idDetail");
        }
//        Toast.makeText(this, idRental, Toast.LENGTH_SHORT).show();

        initialize();

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(14);

        getReference = FirebaseDatabase.getInstance().getReference();
        getReference.child("Master-Data-Rental-Detail").child(idRentalDetail).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> rental1 = (Map<String, Object>) task.getResult().getValue();

                judulmobil.setText(""+rental1.get("NamaKendaraan"));
                deskripsi.setText(""+rental1.get("deskripsiKendaraan"));
                harga.setText("Rp. "+rental1.get("HargaSewa")+" /12 jam");
                ukuran.setText(""+rental1.get("UkuranKendaraan"));
                jumlahkursi.setText(""+rental1.get("JumlahKursi"));
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

                GeoPoint startPoint = new GeoPoint(Double.parseDouble(""+rental2.get("Latitude")), Double.parseDouble(""+rental2.get("Longlitude")));
                mapController.setCenter(startPoint);
                Marker startMarker = new Marker(map);
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);

                namamitra.setText(""+rental2.get("NamaRental"));
                deskripsirental.setText(""+rental2.get("DeskripsiRental"));

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

    public void initialize(){
        judulmobil = findViewById(R.id.judulmobil);
        namamitra = findViewById(R.id.namamitrarental);
        ukuran = findViewById(R.id.isiukuran);
        jumlahkursi = findViewById(R.id.isiJumlahKursi);
        deskripsi = findViewById(R.id.isiDeskripsi);
        harga = findViewById(R.id.hargamobil);
        imagemobil = findViewById(R.id.imageviewrentaldetail1);
        imagemitra = findViewById(R.id.imageprofilerental);
        deskripsirental = findViewById(R.id.isideskripsirental);
    }

    public void backtomenurental(View view){
        Intent a = new  Intent(DetailRentalScreen.this, MenuRentalScreen.class);
        startActivity(a);
        Animatoo.animateFade(DetailRentalScreen.this);
        finish();
    }
}