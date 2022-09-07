package com.risqi.traveland.HotelScreen;

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
import com.risqi.traveland.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Map;

public class DetailHotelScreen extends AppCompatActivity {


    private MapView map = null;
    private String idHotel,idDetail;
    private TextView judulkamar;
    private TextView judulhotel;
    private TextView fasilitas;
    private TextView deskripsihotel;
    private TextView harga;
    private ImageView imagekamar;
    private ImageView imagehotel;
    private DatabaseReference getReference;
    private DatabaseReference getGetReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hotel);

        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            idHotel = bundle.getString("idMaster");
            idDetail = bundle.getString("idDetail");
        }else{
            idHotel = getIntent().getStringExtra("idMaster");
            idDetail = getIntent().getStringExtra("idDetail");
        }
//        Toast.makeText(this, idHotel, Toast.LENGTH_SHORT).show();

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
        getReference.child("Master-Data-Hotel-Detail").child(idDetail).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String,Object> hotel1 = (Map<String, Object>) task.getResult().getValue();

                judulkamar.setText(""+hotel1.get("NamaKamar"));
                fasilitas.setText(""+hotel1.get("FasilitasKamar"));
                harga.setText("Rp. "+hotel1.get("HargaKamar")+" /Hari");
                if(hotel1.get("fotoKamar").equals(""))
                {

                }else
                {
                    Glide.with(DetailHotelScreen.this).clear(imagekamar);
                    Glide.with(DetailHotelScreen.this)
                            .load(hotel1.get("fotoKamar"))
                            .fitCenter()
//                    .transform(new MultiTransformation(new FitCenter()))
                            .apply(new RequestOptions()
//                                    .override(300, 600)
                                    .priority(Priority.HIGH)
                                    .centerCrop())
                            .into(imagekamar);
                }
            }
        });
        getGetReference = FirebaseDatabase.getInstance().getReference();
        getGetReference.child("Master-Data-Hotel").child(idHotel).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String,Object> hotel2= (Map<String, Object>) task.getResult().getValue();
//                Log.d("aneh", "onComplete: "+String.valueOf(task.getResult().getValue()));
                GeoPoint startPoint = new GeoPoint(Double.parseDouble(""+hotel2.get("Latitude")), Double.parseDouble(""+hotel2.get("Longlitude")));
                mapController.setCenter(startPoint);
                Marker startMarker = new Marker(map);
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);

                judulhotel.setText(""+hotel2.get("NamaHotel"));
                deskripsihotel.setText(""+hotel2.get("DeskripsiHotel"));

                if(hotel2.get("fotoHotel").equals(""))
                {

                }else
                {
                    Glide.with(DetailHotelScreen.this).clear(imagehotel);
                    Glide.with(DetailHotelScreen.this)
                            .load(hotel2.get("fotoHotel"))

//                    .transform(new MultiTransformation(new FitCenter()))
                            .apply(new RequestOptions()
//                                    .override(300, 600)
                                    .priority(Priority.HIGH)
                                    .circleCrop()
                                    .centerCrop())
                            .into(imagehotel);
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
        judulkamar = findViewById(R.id.judulHotel);
        judulhotel = findViewById(R.id.namahotelmitra);
        fasilitas = findViewById(R.id.isiDeskripsi);
        harga = findViewById(R.id.hargaKamar);
        imagekamar = findViewById(R.id.imageviewhoteldetail1);
        imagehotel = findViewById(R.id.imageprofilehotel);
        deskripsihotel = findViewById(R.id.isiDeskripsihotel);
    }

    public void backtomenuhotel (View view){
        Intent a = new  Intent(DetailHotelScreen.this, MenuHotelScreen.class);
        startActivity(a);
        Animatoo.animateFade(DetailHotelScreen.this);
        finish();
    }
}