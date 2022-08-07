package com.risqi.traveland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class DetailKegiatan extends AppCompatActivity {

    private MapView map = null;
    private String idKegiatan;
    private TextView judulkegiatan,beritaevent,tanggalmulaiakhir,isikegiatan,yangberkaitan;
    private ImageView imagekegiatan;
    private DatabaseReference getReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan);

        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            idKegiatan = bundle.getString("id");
        }else{
            idKegiatan = getIntent().getStringExtra("id");
        }
//        Toast.makeText(this, idKegiatan, Toast.LENGTH_SHORT).show();

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
        getReference.child("Data-Kegiatan").child(idKegiatan).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String,Object> kegiatan = (Map<String, Object>) task.getResult().getValue();

                GeoPoint startPoint = new GeoPoint(Double.parseDouble(""+kegiatan.get("Latitute")), Double.parseDouble(""+kegiatan.get("Longlitute")));
                mapController.setCenter(startPoint);
                Marker startMarker = new Marker(map);
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);

                judulkegiatan.setText(""+kegiatan.get("Judul"));
                beritaevent.setText(""+kegiatan.get("JenisKegiatan"));

                if (kegiatan.get("JenisKegiatan").equals("Berita")){
                    tanggalmulaiakhir.setText(""+kegiatan.get("TanggalMulai"));
                }else {
                    tanggalmulaiakhir.setText(""+kegiatan.get("TanggalMulai")+" - "+kegiatan.get("TanggalAkhir"));
                }

                isikegiatan.setText(""+kegiatan.get("IsiKegiatan"));

                if(kegiatan.get("LinkImage").equals(""))
                {

                }else
                {
                    Glide.with(DetailKegiatan.this).clear(imagekegiatan);
                    Glide.with(DetailKegiatan.this)
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
        judulkegiatan = findViewById(R.id.judulKegiatan);
        beritaevent = findViewById(R.id.beritaevent);
        tanggalmulaiakhir = findViewById(R.id.tanggalmulaiakhir);
        isikegiatan = findViewById(R.id.isiDeskripsikegiatan);
        imagekegiatan = findViewById(R.id.imageviewkegiatan1);
        yangberkaitan = findViewById(R.id.yangberkaitan);
    }

    public void backtomenuutama(View view){
        Intent a = new  Intent(DetailKegiatan.this, MainMenu.class);
        startActivity(a);
        Animatoo.animateFade(DetailKegiatan.this);
        finish();
    }
}