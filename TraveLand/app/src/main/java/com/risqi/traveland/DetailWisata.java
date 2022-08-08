package com.risqi.traveland;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.risqi.traveland.Firebase.Grocery;
import com.risqi.traveland.RecyclerView.GroceryRecyclerViewAdapter;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;
import java.util.Map;


public class DetailWisata extends AppCompatActivity {

    //data dummy
    private RecyclerView rvGroceries;
    private GroceryRecyclerViewAdapter groceryRecyclerViewAdapter;



    private MapView map = null;
    private String idWisata;
    private TextView judulwisata;
    private TextView deskripsi;
    private TextView harga;
    private ImageView imagewisata;
    private DatabaseReference getReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);

        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            idWisata = bundle.getString("id");
        }else{
            idWisata = getIntent().getStringExtra("id");
        }
//        Toast.makeText(this, idWisata, Toast.LENGTH_SHORT).show();

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
        getReference.child("Master-Data-Wisata").child(idWisata).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String,Object> wisata = (Map<String, Object>) task.getResult().getValue();


                GeoPoint startPoint = new GeoPoint(Double.parseDouble(""+wisata.get("Latitude")), Double.parseDouble(""+wisata.get("Longlitude")));
                mapController.setCenter(startPoint);
                Marker startMarker = new Marker(map);
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);

                judulwisata.setText(""+wisata.get("NamaWisata"));
                deskripsi.setText(""+wisata.get("DeskripsiWisata"));
                harga.setText("Rp. "+wisata.get("HargaAnak")+" (Anak) Rp."+wisata.get("HargaDewasa")+"(Dewasa)");
                if(wisata.get("fotoWisata").equals(""))
                {

                }else
                {
                    Glide.with(DetailWisata.this).clear(imagewisata);
                    Glide.with(DetailWisata.this)
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

        rvGroceries.setLayoutManager(new LinearLayoutManager(this));
        groceryRecyclerViewAdapter = new GroceryRecyclerViewAdapter();
        rvGroceries.setAdapter(groceryRecyclerViewAdapter);
        setData();



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
        rvGroceries = findViewById(R.id.rvGroceries);
        judulwisata = findViewById(R.id.judulWisata);
        deskripsi = findViewById(R.id.isiDeskripsi);
        harga = findViewById(R.id.hargaWisata);
        imagewisata = findViewById(R.id.imageView13);

    }

    private void setData() {
        List<Grocery> dummyData = DummyGroceryData.groceryList();
        groceryRecyclerViewAdapter.updateData(dummyData);
    }

    public void backtomenuwisata (View view){
        Intent a = new  Intent(DetailWisata.this, MenuWisata.class);
        startActivity(a);
        Animatoo.animateFade(DetailWisata.this);
        finish();
    }



}
