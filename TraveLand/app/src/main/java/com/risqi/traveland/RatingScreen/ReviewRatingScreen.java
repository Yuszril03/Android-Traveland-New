package com.risqi.traveland.RatingScreen;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.risqi.traveland.HotelScreen.DetailTransactionHotelScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.RentalScreen.DetailTransactionRentalScreen;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.WisataScreen.DetailTransactionWisataScreen;

import java.util.Map;

public class ReviewRatingScreen extends AppCompatActivity {

    //Layout Utama
    private ConstraintLayout layoutUtamaFirst;

    //Top
    private ConstraintLayout bgTop;
    private Button backtoscroll;
    private TextView judulScreen;

    //MItra
    private ConstraintLayout bgMitra;
    private ImageView imageViewWisata;
    private TextView judulWisata, alamatwisata, hargaAnak;

    //Bintang
    private TextView judulRating;
    private Button bintang1, bintang2, bintang3, bintang4, bintang5;

    //Komentar
    private TextView judulKomentar,komentarUser,KomentarMitra;
    private ConstraintLayout bgkomenUser,bgkomeMitra;

    //Thank You
    private TextView textSuccess;

    //Other
    private DataMode dataMode;
    private String idWisata = "";
    private String jenisScreen = "";
    private DatabaseReference database1, database2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rating_screen);
        getDataInten();
        initialize();
        //setData
        setData();
        //setMode
        setMode();
        //back
        toBack();
    }

    private void setData() {
        if (jenisScreen.equals("Wisata")) {
            database1 = FirebaseDatabase.getInstance().getReference();
            database1.child("Transaction-Wisata").child(idWisata).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Map<String, Object> transaksiWisata = (Map<String, Object>) task.getResult().getValue();
                    setDataWisata(transaksiWisata.get("IdMitra").toString());
                    setBIntang(Integer.parseInt(transaksiWisata.get("Rating").toString()));
                    if(transaksiWisata.get("UlasanCustomer").toString().equals("")){
                        komentarUser.setText("Tidak ada komentar yang diberikan.");
                    }else{
                        komentarUser.setText(wordCase(transaksiWisata.get("UlasanCustomer").toString()));
                        if(transaksiWisata.get("UlasanMitra").toString().equals("")){
                            bgkomeMitra.setVisibility(View.GONE);
                        }else{
                            bgkomeMitra.setVisibility(View.VISIBLE);
                            KomentarMitra.setText(wordCase(transaksiWisata.get("UlasanMitra").toString()));
                        }
                    }
                }
            });
        }else if (jenisScreen.equals("Hotel")) {
            database1 = FirebaseDatabase.getInstance().getReference();
            database1.child("Transaction-Hotel").child(idWisata).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Map<String, Object> transaksiHOtels = (Map<String, Object>) task.getResult().getValue();
                    setDataHotel(transaksiHOtels.get("IdMitra").toString(),transaksiHOtels.get("IdKamar").toString());
                    setBIntang(Integer.parseInt(transaksiHOtels.get("Rating").toString()));
                    if(transaksiHOtels.get("UlasanCustomer").toString().equals("")){
                        komentarUser.setText("Tidak ada komentar yang diberikan.");
                    }else{
                        komentarUser.setText(wordCase(transaksiHOtels.get("UlasanCustomer").toString()));
                        if(transaksiHOtels.get("UlasanMitra").toString().equals("")){
                            bgkomeMitra.setVisibility(View.GONE);
                        }else{
                            bgkomeMitra.setVisibility(View.VISIBLE);
                            KomentarMitra.setText(wordCase(transaksiHOtels.get("UlasanMitra").toString()));
                        }
                    }
                }
            });
        }else if (jenisScreen.equals("Rental")) {
            database1 = FirebaseDatabase.getInstance().getReference();
            database1.child("Transaction-Rental").child(idWisata).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Map<String, Object> transaksiRentals = (Map<String, Object>) task.getResult().getValue();
                    setDataRental(transaksiRentals.get("IdMitra").toString(),transaksiRentals.get("IdMobil").toString());
                    setBIntang(Integer.parseInt(transaksiRentals.get("Rating").toString()));
                    if(transaksiRentals.get("UlasanCustomer").toString().equals("")){
                        komentarUser.setText("Tidak ada komentar yang diberikan.");
                    }else{
                        komentarUser.setText(wordCase(transaksiRentals.get("UlasanCustomer").toString()));
                        if(transaksiRentals.get("UlasanMitra").toString().equals("")){
                            bgkomeMitra.setVisibility(View.GONE);
                        }else{
                            bgkomeMitra.setVisibility(View.VISIBLE);
                            KomentarMitra.setText(wordCase(transaksiRentals.get("UlasanMitra").toString()));
                        }
                    }
                }
            });
        }
    }

    private void setDataRental(String idMitra, String idMobil) {
        database2 = FirebaseDatabase.getInstance().getReference();
        database2.child("Master-Data-Rental-Detail").child(idMobil).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> taskMobil) {
                Map<String, Object> detailMobil = (Map<String, Object>) taskMobil.getResult().getValue();

                hargaAnak.setText("Rp."+detailMobil.get("HargaSewa").toString());
                Glide.with(ReviewRatingScreen.this).clear(imageViewWisata);
                Glide.with(ReviewRatingScreen.this)
                        .load(detailMobil.get("fotoKendaraan").toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()

                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(imageViewWisata);

                database1 = FirebaseDatabase.getInstance().getReference();
                database1.child("Master-Data-Rental").child(idMitra).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> taskRental) {
                        Map<String, Object> detailRental= (Map<String, Object>) taskRental.getResult().getValue();

                        String judul = wordCase(detailMobil.get("NamaKendaraan").toString()+" - "+detailRental.get("NamaRental").toString());
                        judulWisata.setText(judul);
                        String textTemp = wordCase(detailRental.get("AlamatRental").toString());
                        if(textTemp.length() <=55){
                            alamatwisata.setText(textTemp);
                        }else{
                            String resultAlamat="";
                            String [] arrayAlamat = textTemp.split("");
                            for(int i =0; i<52;i++){
                                resultAlamat=resultAlamat+""+arrayAlamat[i];
                            }
                            alamatwisata.setText(wordCase(resultAlamat));
                        }
                    }
                });
            }
        });

    }

    private void setBIntang(int rating) {
        if(rating==1){
            bintang1.setBackgroundResource(R.drawable.icon_star_primary);
            bintang2.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
            bintang3.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
            bintang4.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
            bintang5.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
        }else if(rating==2){
            bintang1.setBackgroundResource(R.drawable.icon_star_primary);
            bintang2.setBackgroundResource(R.drawable.icon_star_primary);
            bintang3.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
            bintang4.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
            bintang5.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
        }else if(rating==3){
            bintang1.setBackgroundResource(R.drawable.icon_star_primary);
            bintang2.setBackgroundResource(R.drawable.icon_star_primary);
            bintang3.setBackgroundResource(R.drawable.icon_star_primary);
            bintang4.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
            bintang5.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
        }else if(rating==4){
            bintang1.setBackgroundResource(R.drawable.icon_star_primary);
            bintang2.setBackgroundResource(R.drawable.icon_star_primary);
            bintang3.setBackgroundResource(R.drawable.icon_star_primary);
            bintang4.setBackgroundResource(R.drawable.icon_star_primary);
            bintang5.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
        }else if(rating==5){
            bintang1.setBackgroundResource(R.drawable.icon_star_primary);
            bintang2.setBackgroundResource(R.drawable.icon_star_primary);
            bintang3.setBackgroundResource(R.drawable.icon_star_primary);
            bintang4.setBackgroundResource(R.drawable.icon_star_primary);
            bintang5.setBackgroundResource(R.drawable.icon_star_primary);
        }
    }

    private void setDataHotel(String idMitra, String idKamar) {
        database2 = FirebaseDatabase.getInstance().getReference();
        database2.child("Master-Data-Hotel-Detail").child(idKamar).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> taskKamar) {
                Map<String, Object> detailKamar = (Map<String, Object>) taskKamar.getResult().getValue();

                hargaAnak.setText("Rp."+detailKamar.get("HargaKamar").toString());
                Glide.with(ReviewRatingScreen.this).clear(imageViewWisata);
                Glide.with(ReviewRatingScreen.this)
                        .load(detailKamar.get("fotoKamar").toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()

                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(imageViewWisata);

                database1 = FirebaseDatabase.getInstance().getReference();
                database1.child("Master-Data-Hotel").child(idMitra).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> taskHotel) {
                        Map<String, Object> detailHotel= (Map<String, Object>) taskHotel.getResult().getValue();

                        String judul = wordCase(detailKamar.get("NamaKamar").toString()+" - "+detailHotel.get("NamaHotel").toString());
                        judulWisata.setText(judul);
                        String textTemp = wordCase(detailHotel.get("AlamatHotel").toString());
                        if(textTemp.length() <=55){
                            alamatwisata.setText(textTemp);
                        }else{
                            String resultAlamat="";
                            String [] arrayAlamat = textTemp.split("");
                            for(int i =0; i<52;i++){
                                resultAlamat=resultAlamat+""+arrayAlamat[i];
                            }
                            alamatwisata.setText(wordCase(resultAlamat));
                        }
                    }
                });
            }
        });
    }

    private void setDataWisata(String idMitra) {
        database2 = FirebaseDatabase.getInstance().getReference();
        database2.child("Master-Data-Wisata").child(idMitra).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> wisata = (Map<String, Object>) task.getResult().getValue();
                judulWisata.setText(wordCase(wisata.get("NamaWisata").toString()));
                alamatwisata.setText(wordCase(wisata.get("AlamatWisata").toString()));
                String textTemp = wisata.get("AlamatWisata").toString();
                if(textTemp.length() <=55){
                    alamatwisata.setText(wordCase(wisata.get("AlamatWisata").toString()));
                }else{
                    String resultAlamat="";
                    String [] arrayAlamat = textTemp.split("");
                    for(int i =0; i<52;i++){
                        resultAlamat=resultAlamat+""+arrayAlamat[i];
                    }
                    alamatwisata.setText(wordCase(resultAlamat));
                }
                hargaAnak.setText("Rp."+wisata.get("HargaDewasa").toString());
                Glide.with(ReviewRatingScreen.this).clear(imageViewWisata);
                Glide.with(ReviewRatingScreen.this)
                        .load(wisata.get("fotoWisata").toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()

                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(imageViewWisata);
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

    private void getDataInten() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idWisata = bundle.getString("idScreen");
            jenisScreen = bundle.getString("jenisScreen");
        } else {
            idWisata = getIntent().getStringExtra("idScreen");
            jenisScreen = getIntent().getStringExtra("jenisScreen");
        }
    }

    private void toBack() {
        backtoscroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jenisScreen.equals("Wisata")) {
                    Intent a = new Intent(ReviewRatingScreen.this, DetailTransactionWisataScreen.class);
                    a.putExtra("idScreen", idWisata);
                    startActivity(a);
                    Animatoo.animateSlideDown(ReviewRatingScreen.this);
                    onStop();
                }else  if (jenisScreen.equals("Hotel")) {
                    Intent a = new Intent(ReviewRatingScreen.this, DetailTransactionHotelScreen.class);
                    a.putExtra("idScreen", idWisata);
                    startActivity(a);
                    Animatoo.animateSlideDown(ReviewRatingScreen.this);
                    onStop();
                }else  if (jenisScreen.equals("Rental")) {
                    Intent a = new Intent(ReviewRatingScreen.this, DetailTransactionRentalScreen.class);
                    a.putExtra("idScreen", idWisata);
                    startActivity(a);
                    Animatoo.animateSlideDown(ReviewRatingScreen.this);
                    onStop();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (jenisScreen.equals("Wisata")) {
            Intent a = new Intent(ReviewRatingScreen.this, DetailTransactionWisataScreen.class);
            a.putExtra("idScreen", idWisata);
            startActivity(a);
            Animatoo.animateSlideDown(ReviewRatingScreen.this);
            onStop();
        }else  if (jenisScreen.equals("Hotel")) {
            Intent a = new Intent(ReviewRatingScreen.this, DetailTransactionHotelScreen.class);
            a.putExtra("idScreen", idWisata);
            startActivity(a);
            Animatoo.animateSlideDown(ReviewRatingScreen.this);
            onStop();
        }else  if (jenisScreen.equals("Rental")) {
            Intent a = new Intent(ReviewRatingScreen.this, DetailTransactionRentalScreen.class);
            a.putExtra("idScreen", idWisata);
            startActivity(a);
            Animatoo.animateSlideDown(ReviewRatingScreen.this);
            onStop();
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
            //Layout Utama
            layoutUtamaFirst.setBackgroundResource(R.color.darkMode);
            //Top
            bgTop.setBackgroundResource(R.drawable.border_header_dark);
            backtoscroll.setBackgroundResource(R.drawable.icon_cancel);
            judulScreen.setTextColor(getResources().getColor(R.color.white));
            //Mitra
            bgMitra.setBackgroundResource(R.drawable.background_list_dark_v2);
            judulWisata.setTextColor(getResources().getColor(R.color.white));
            alamatwisata.setTextColor(getResources().getColor(R.color.darkTxt));
            //Rating
            judulRating.setTextColor(getResources().getColor(R.color.white));
            //KOmentar
            judulKomentar.setTextColor(getResources().getColor(R.color.white));
            //thank you
            textSuccess.setTextColor(getResources().getColor(R.color.white));


        }
    }

    private void initialize() {
        //Layout Utama
        layoutUtamaFirst = findViewById(R.id.layoutUtamaFirst);
        //Top
        bgTop = findViewById(R.id.bgTop);
        backtoscroll = findViewById(R.id.backtoscroll);
        judulScreen = findViewById(R.id.judulScreen);
        //MIttra
        bgMitra = findViewById(R.id.bgMitra);
        imageViewWisata = findViewById(R.id.imageViewWisata);
        judulWisata = findViewById(R.id.judulWisata);
        alamatwisata = findViewById(R.id.alamatwisata);
        hargaAnak = findViewById(R.id.hargaAnak);
        //Rating
        judulRating = findViewById(R.id.judulRating);
        bintang1 = findViewById(R.id.bintang1);
        bintang2 = findViewById(R.id.bintang2);
        bintang3 = findViewById(R.id.bintang3);
        bintang4 = findViewById(R.id.bintang4);
        bintang5 = findViewById(R.id.bintang5);
        //Komentar
        judulKomentar = findViewById(R.id.judulKomentar);
        bgkomenUser = findViewById(R.id.bgkomenUser);
        komentarUser = findViewById(R.id.komentarUser);
        bgkomeMitra = findViewById(R.id.bgkomeMitra);
        KomentarMitra = findViewById(R.id.KomentarMitra);
        //Thank you
        textSuccess=findViewById(R.id.textSuccess);
        //Other
        dataMode = new DataMode(this);

    }
}