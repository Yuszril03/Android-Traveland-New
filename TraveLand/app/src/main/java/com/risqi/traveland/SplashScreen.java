package com.risqi.traveland;

//import android.support.v7.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.risqi.traveland.SQLite.DataFirstApp;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.SQLite.DataVersion;
import com.risqi.traveland.SettingScreen.VersionScreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    private DatabaseReference databaseReference, databaseReference2, databaseReference3, databaseReference4;
    private DataFirstApp dataFirstApp;
    private DataMode dataMode;
    private DataVersion dataVersion;
    private DataLoginUser dataLoginUser;
    private ConstraintLayout layoutUtama;
    private ImageView logo;
    private String keyAndroid;
    private int internet = 0;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date currentDate = new Date();
    Date versionDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initialize();
        setMode();
        cekKondisi();


    }

    private void setMode() {
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
            layoutUtama.setBackgroundColor(getResources().getColor(R.color.darkMode));
            logo.setBackgroundResource(R.drawable.penuhlogowhite);
        } else {
            layoutUtama.setBackgroundColor(getResources().getColor(R.color.white));
            logo.setBackgroundResource(R.drawable.penuhlogo);
        }
    }

    private boolean adaInternet() {
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }

    private void cekKondisi() {
        if (!adaInternet() && internet == 0) {
            internet = 1;
            new SweetAlertDialog(SplashScreen.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Internet tidak terhubung")
                    .setContentText("Mohon cek kembali konkesi internet")
                    .setConfirmText("Okey")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(getIntent());
                            Animatoo.animateFade(SplashScreen.this);
                            onStop();
                        }
                    })
                    .show();
        } else if (adaInternet() && internet == 0) {

            cekVersion();
        }
    }

    private void cekVersion() {
        databaseReference4 = FirebaseDatabase.getInstance().getReference();
        databaseReference4.child("Data-Version").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String lastVersi="";
                String linkVersi="";
                Map<String, Object> Version = (Map<String, Object>) task.getResult().getValue();
                for (Map.Entry<String, Object> entry : Version.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    Map<String, Object> Temps = (Map<String, Object>) entry.getValue();
                    lastVersi=Temps.get("nomorVersi").toString();
                    linkVersi=Temps.get("LinkUpdate").toString();

                }

                if(lastVersi.equals(BuildConfig.VERSION_NAME)){
                    cekUser();
                }else{
                    String finalLinkVersi = linkVersi;
                    Cursor mod = dataVersion.getAllData();
                    mod.moveToFirst();
                    String tanggalVersi = "";
                    while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
                        tanggalVersi = mod.getString(mod.getColumnIndexOrThrow("waktu"));

                        mod.moveToNext();
                    }
                    mod.close();
                    if(tanggalVersi.equals("")){
                        dataVersion.insertData(finalLinkVersi,format.format(currentDate));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashScreen.this, VersionScreen.class);
                                intent.putExtra("LinkUpdate", finalLinkVersi);
                                intent.putExtra("Before", "SplasScreen");
                                startActivity(intent);
                                Animatoo.animateFade(SplashScreen.this);
                                onStop();
                            }
                        }, 1000);
                    }else{
                        try {
                            versionDate = format.parse(tanggalVersi);
                            //time difference in milliseconds
                            long diff = currentDate.getTime() - versionDate.getTime();

                            long diffHours = diff / (60 * 60 * 1000);
                            if(diffHours >= 4){
                                dataVersion.updateData(finalLinkVersi,format.format(currentDate));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(SplashScreen.this, VersionScreen.class);
                                        intent.putExtra("LinkUpdate", finalLinkVersi);
                                        intent.putExtra("Before", "SplasScreen");
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        onStop();
                                    }
                                }, 1000);
                            }else{
                                cekUser();
                            }



//                            Toast.makeText(SplashScreen.this, ""+diffHours, Toast.LENGTH_SHORT).show();


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                }

            }
        });
    }

    private void cekUser() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Data-Login-Customer").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    Map<String, Object> Customer = (Map<String, Object>) task.getResult().getValue();
                    String Isi = String.valueOf(task.getResult().getValue());
//                    Log.d("ANEHE", "onComplete: " + Isi);
                    String cekNIK = "";
                    int cekKeyAndroid = 0;
                    if (Isi.equals("null")) {
                        cekKeyAndroid = 0;
                    } else {
                        for (Map.Entry<String, Object> entry : Customer.entrySet()) {
                            String key = entry.getKey();

                            Object value = entry.getValue();
                            Map<String, Object> Temps = (Map<String, Object>) entry.getValue();

                            if (key.equals(keyAndroid)) {
                                cekKeyAndroid = 1;
                                cekNIK = Temps.get("NIK").toString();
                            }
                        }
                    }
                    Cursor resLogin = dataLoginUser.getDataOne();
                    resLogin.moveToFirst();
//
                    if (cekKeyAndroid == 0) {
                        if (resLogin.getCount() == 1 && cekKeyAndroid == 0) {

                            databaseReference.onDisconnect();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dataLoginUser.deleteDataAll();
//                boolean ok=dataFirstApp.insertData("Masuk");
                                    Cursor res = dataFirstApp.getDataOne();
                                    res.moveToFirst();
                                    if (res.getCount() == 0) {
                                        Intent intent = new Intent(SplashScreen.this, FirstScreen.class);
                                        intent.putExtra("ModeApp", "Siang");
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                        onStop();
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, MainMenuScreen.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                        onStop();
                                    }

                                }
                            }, 1000);
                        } else if (resLogin.getCount() == 0 && cekKeyAndroid == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                boolean ok=dataFirstApp.insertData("Masuk");
                                    Cursor res = dataFirstApp.getDataOne();
                                    res.moveToFirst();
                                    databaseReference.onDisconnect();
                                    if (res.getCount() == 0) {
                                        Intent intent = new Intent(SplashScreen.this, FirstScreen.class);
                                        intent.putExtra("ModeApp", "Siang");
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                        onStop();
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, MainMenuScreen.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                        onStop();
                                    }

                                }
                            }, 1000);
                        }

                    } else {

                        if (resLogin.getCount() == 0 && cekKeyAndroid == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                boolean ok=dataFirstApp.insertData("Masuk");
                                    Cursor res = dataFirstApp.getDataOne();
                                    res.moveToFirst();
                                    databaseReference.onDisconnect();
                                    if (res.getCount() == 0) {
                                        Intent intent = new Intent(SplashScreen.this, FirstScreen.class);
                                        intent.putExtra("ModeApp", "Siang");
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                        onStop();
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, MainMenuScreen.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                        onStop();
                                    }

                                }
                            }, 1000);
                        } else if (resLogin.getCount() == 0 && cekKeyAndroid == 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                boolean ok=dataFirstApp.insertData("Masuk");
                                    Cursor res = dataFirstApp.getDataOne();
                                    res.moveToFirst();
                                    databaseReference.onDisconnect();
                                    if (res.getCount() == 0) {
                                        Intent intent = new Intent(SplashScreen.this, FirstScreen.class);
                                        intent.putExtra("ModeApp", "Siang");
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                        onStop();
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, MainMenuScreen.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                        onStop();
                                    }

                                }
                            }, 1000);

                        } else if (resLogin.getCount() == 1 && cekKeyAndroid == 1) {

                            databaseReference2 = FirebaseDatabase.getInstance().getReference();
                            String finalCekNIK = cekNIK;
                            databaseReference2.child("Master-Data-Customer").child(cekNIK).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task2) {
//                                    Log.d("firebase2", String.valueOf(task2.getResult().getValue()));
                                    Map<String, Object> DataCustomer = (Map<String, Object>) task2.getResult().getValue();
                                    databaseReference3 = FirebaseDatabase.getInstance().getReference();
                                    databaseReference3.child("Master-Data-Account-Customer").child(finalCekNIK).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task3) {
                                            Map<String, Object> DataCustomerAccount = (Map<String, Object>) task3.getResult().getValue();
                                            dataLoginUser.updateDataRealTime(DataCustomer.get("NamaCustomer").toString(), DataCustomer.get("fotoCustomer").toString(), DataCustomer.get("Gender").toString(), DataCustomerAccount.get("KataSandi").toString());
                                        }
                                    });


                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                boolean ok=dataFirstApp.insertData("Masuk");
                                    Cursor res = dataFirstApp.getDataOne();
                                    res.moveToFirst();
                                    databaseReference.onDisconnect();
                                    if (res.getCount() == 0) {
                                        Intent intent = new Intent(SplashScreen.this, FirstScreen.class);
                                        intent.putExtra("ModeApp", "Siang");
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                        onStop();
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, MainMenuScreen.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                        onStop();
                                    }

                                }
                            }, 1000);
                        }
                    }

                }
            }
        });

    }

    private void initialize() {
        dataFirstApp = new DataFirstApp(this);
        dataMode = new DataMode(this);
        dataVersion = new DataVersion(this);
        dataLoginUser = new DataLoginUser(this);
        layoutUtama = findViewById(R.id.layoutUtama);
        logo = findViewById(R.id.imageView);
        keyAndroid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}