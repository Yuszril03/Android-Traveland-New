package com.risqi.traveland.RatingScreen;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.risqi.traveland.Firebase.TransactionHotel;
import com.risqi.traveland.Firebase.TransactionWIisata;
import com.risqi.traveland.HotelScreen.DetailTransactionHotelScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.WisataScreen.DetailTransactionWisataScreen;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RatingScreen extends AppCompatActivity {

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
    private TextView judulKomentar;
    private ConstraintLayout bgKolomKomentar;
    private EditText komentar;

    //Submit
    private  Button buttonSUbmit;

    //Other
    private DataMode dataMode;
    private String idWisata = "";
    private String jenisScreen = "";
    private TransactionWIisata transactionWIisata2;
    private TransactionHotel transHotel;
    private DatabaseReference database1, database2;
    private Task databaseupdate;
    private SweetAlertDialog pDialog;
    private int star=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_screen);
        getDataInten();
        initialize();
        //SetMode;
        setMode();
        //setData
        setData();
        //setBintang
        setBintang();
        //Menu
        toBack();
        //submit
        submitData();
    }

    private void submitData() {
        buttonSUbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(RatingScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah anda yakin")
                        .setContentText("Menyimpan penilain transaksi!")
                        .setConfirmText("Iya!")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Memuat Data...");
                                pDialog.setCancelable(false);
                                pDialog.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(star ==0){
                                            pDialog.dismiss();
                                            new SweetAlertDialog(RatingScreen.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Opps...")
                                                    .setContentText("Bintang penilaian transaksi belum dipilih!")
                                                    .setConfirmText("Iya!")
                                                    .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismissWithAnimation();
                                                        }
                                                    }).show();
                                        }else{
                                            if (jenisScreen.equals("Wisata")){
                                                HashMap updateDAta = transactionWIisata2.updatePenilaian(String.valueOf(star),komentar.getText().toString());
                                                databaseupdate = FirebaseDatabase.getInstance().getReference().child("Transaction-Wisata").child(idWisata).updateChildren(updateDAta);
                                                databaseupdate.addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        pDialog.dismiss();
                                                        new SweetAlertDialog(RatingScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Berhasil")
                                                                .setContentText("Data berhasil tersimpan!")
                                                                .setConfirmText("Okey")
                                                                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                        sweetAlertDialog.dismissWithAnimation();
                                                                        if (jenisScreen.equals("Wisata")) {
                                                                            Intent a = new Intent(RatingScreen.this, DetailTransactionWisataScreen.class);
                                                                            a.putExtra("idScreen", idWisata);
                                                                            startActivity(a);
                                                                            Animatoo.animateSlideDown(RatingScreen.this);
                                                                            onStop();
                                                                        }
                                                                    }
                                                                }).show();
                                                    }
                                                });
                                            }else  if (jenisScreen.equals("Hotel")){

                                                HashMap updateDAta = transHotel.updatePenilaian(String.valueOf(star),komentar.getText().toString());
                                                databaseupdate = FirebaseDatabase.getInstance().getReference().child("Transaction-Hotel").child(idWisata).updateChildren(updateDAta);
                                                databaseupdate.addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        pDialog.dismiss();
                                                        new SweetAlertDialog(RatingScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Berhasil")
                                                                .setContentText("Data berhasil tersimpan!")
                                                                .setConfirmText("Okey")
                                                                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                        sweetAlertDialog.dismissWithAnimation();
                                                                        if (jenisScreen.equals("Hotel")) {
                                                                            Intent a = new Intent(RatingScreen.this, DetailTransactionHotelScreen.class);
                                                                            a.putExtra("idScreen", idWisata);
                                                                            startActivity(a);
                                                                            Animatoo.animateSlideDown(RatingScreen.this);
                                                                            onStop();
                                                                        }
                                                                    }
                                                                }).show();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }, 2000);

                            }
                        }).show();


            }
        });
    }

    private void setBintang() {
        bintang1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star=1;
                bintang1.setBackgroundResource(R.drawable.icon_star_primary);
                bintang2.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
                bintang3.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
                bintang4.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
                bintang5.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
            }
        });
        bintang2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star=2;
                bintang1.setBackgroundResource(R.drawable.icon_star_primary);
                bintang2.setBackgroundResource(R.drawable.icon_star_primary);
                bintang3.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
                bintang4.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
                bintang5.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
            }
        });
        bintang3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star=3;
                bintang1.setBackgroundResource(R.drawable.icon_star_primary);
                bintang2.setBackgroundResource(R.drawable.icon_star_primary);
                bintang3.setBackgroundResource(R.drawable.icon_star_primary);
                bintang4.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
                bintang5.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
            }
        });
        bintang4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star=4;
                bintang1.setBackgroundResource(R.drawable.icon_star_primary);
                bintang2.setBackgroundResource(R.drawable.icon_star_primary);
                bintang3.setBackgroundResource(R.drawable.icon_star_primary);
                bintang4.setBackgroundResource(R.drawable.icon_star_primary);
                bintang5.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
            }
        });
        bintang5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star=5;
                bintang1.setBackgroundResource(R.drawable.icon_star_primary);
                bintang2.setBackgroundResource(R.drawable.icon_star_primary);
                bintang3.setBackgroundResource(R.drawable.icon_star_primary);
                bintang4.setBackgroundResource(R.drawable.icon_star_primary);
                bintang5.setBackgroundResource(R.drawable.icon_star_primary);
            }
        });
    }

    private void setData() {
        if (jenisScreen.equals("Wisata")) {
            database1 = FirebaseDatabase.getInstance().getReference();
            database1.child("Transaction-Wisata").child(idWisata).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Map<String, Object> transaksiWisata = (Map<String, Object>) task.getResult().getValue();
                    setDataWisata(transaksiWisata.get("IdMitra").toString());
                }
            });
        }else  if (jenisScreen.equals("Hotel")) {
            database1 = FirebaseDatabase.getInstance().getReference();
            database1.child("Transaction-Hotel").child(idWisata).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Map<String, Object> transaksiHotel = (Map<String, Object>) task.getResult().getValue();
                    setDataHotel(transaksiHotel.get("IdMitra").toString(),transaksiHotel.get("IdKamar").toString());
                }
            });
        }
    }

    private void setDataHotel(String idMitra, String idKamar) {
        database2 = FirebaseDatabase.getInstance().getReference();
        database2.child("Master-Data-Hotel-Detail").child(idKamar).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> taskKamar) {
                Map<String, Object> detailKamar = (Map<String, Object>) taskKamar.getResult().getValue();

                hargaAnak.setText("Rp."+detailKamar.get("HargaKamar").toString());
                Glide.with(RatingScreen.this).clear(imageViewWisata);
                Glide.with(RatingScreen.this)
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
                Glide.with(RatingScreen.this).clear(imageViewWisata);
                Glide.with(RatingScreen.this)
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

    private void toBack() {
        backtoscroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(RatingScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Anda Yakin")
                        .setContentText("Tidak menyimpan penilaian transaksi!")
                        .setConfirmText("Iya!")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if (jenisScreen.equals("Wisata")) {
                                    Intent a = new Intent(RatingScreen.this, DetailTransactionWisataScreen.class);
                                    a.putExtra("idScreen", idWisata);
                                    startActivity(a);
                                    Animatoo.animateSlideDown(RatingScreen.this);
                                    onStop();
                                }else if (jenisScreen.equals("Hotel")) {
                                    Intent a = new Intent(RatingScreen.this, DetailTransactionHotelScreen.class);
                                    a.putExtra("idScreen", idWisata);
                                    startActivity(a);
                                    Animatoo.animateSlideDown(RatingScreen.this);
                                    onStop();
                                }
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(RatingScreen.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah Anda Yakin")
                .setContentText("Tidak menyimpan penilaian transaksi!")
                .setConfirmText("Iya!")
                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                .setCancelText("Tidak")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (jenisScreen.equals("Wisata")) {
                            Intent a = new Intent(RatingScreen.this, DetailTransactionWisataScreen.class);
                            a.putExtra("idScreen", idWisata);
                            startActivity(a);
                            Animatoo.animateSlideDown(RatingScreen.this);
                            onStop();
                        }else if (jenisScreen.equals("Hotel")) {
                            Intent a = new Intent(RatingScreen.this, DetailTransactionHotelScreen.class);
                            a.putExtra("idScreen", idWisata);
                            startActivity(a);
                            Animatoo.animateSlideDown(RatingScreen.this);
                            onStop();
                        }
                    }
                }).show();
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
            bgKolomKomentar.setBackgroundResource(R.drawable.background_list_dark_v2);
            komentar.setTextColor(getResources().getColor(R.color.white));
            komentar.setHintTextColor(getResources().getColor(R.color.darkTxt));

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
        bgKolomKomentar = findViewById(R.id.bgKolomKomentar);
        komentar = findViewById(R.id.komentar);
        //Submimt
        buttonSUbmit=findViewById(R.id.buttonSUbmit);
        //Other
        dataMode = new DataMode(this);
        transactionWIisata2 = new TransactionWIisata();
        transHotel= new TransactionHotel();
        pDialog = new SweetAlertDialog(RatingScreen.this, SweetAlertDialog.PROGRESS_TYPE);

    }
}