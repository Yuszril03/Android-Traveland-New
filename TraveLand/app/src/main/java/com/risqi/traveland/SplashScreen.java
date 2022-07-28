package com.risqi.traveland;

//import android.support.v7.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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

import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    DatabaseReference databaseReference,databaseReference2;
    DataFirstApp dataFirstApp;
    DataMode dataMode;
    DataLoginUser dataLoginUser;
    ConstraintLayout layoutUtama;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initialize();
        String keyAndroid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
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
                    Log.d("ANEHE", "onComplete: " + Isi);
                    String cekNIK = "";
                    int cekKeyAndroid = 0;
                    if (Isi.equals("null")) {
                        cekKeyAndroid = 0;
                    } else {
                        for (Map.Entry<String, Object> entry : Customer.entrySet()) {
                            String key = entry.getKey();
                            cekNIK = entry.getKey();
                            Object value = entry.getValue();
                            Map<String, Object> Temps = (Map<String, Object>) entry.getValue();

                            if (Temps.get("KeyAndroid").equals(keyAndroid)) {
                                cekKeyAndroid = 1;
                            }
                        }
                    }
                    Cursor resLogin = dataLoginUser.getDataOne();
                    resLogin.moveToFirst();

                    if (cekKeyAndroid == 0) {
                        if (resLogin.getCount() == 1 && cekKeyAndroid == 0) {

                            new SweetAlertDialog(SplashScreen.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Opps...")
                                    .setContentText("Akun sedang tertaut di perangkat lain!")
                                    .setConfirmText("Iya!")
                                    .showCancelButton(false)
                                    .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            databaseReference.onDisconnect();
                                            sDialog.dismissWithAnimation();
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
                                                    } else {
                                                        Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                                                        startActivity(intent);
                                                        Animatoo.animateFade(SplashScreen.this);
                                                        finish();
                                                    }

                                                }
                                            }, 1000);
                                        }
                                    })
                                    .show();
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
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
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
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                    }

                                }
                            }, 1000);
                        }else  if(resLogin.getCount()==0 && cekKeyAndroid==1){
                            databaseReference2 = FirebaseDatabase.getInstance().getReference("Data-Login-Customer").child(cekNIK);
                            databaseReference2.getRef().onDisconnect().removeValue();
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
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
                                    }

                                }
                            }, 1000);
                        }
                        else  if(resLogin.getCount()==1 && cekKeyAndroid==1){

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
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(SplashScreen.this);
                                        finish();
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
        dataLoginUser = new DataLoginUser(this);
        layoutUtama = findViewById(R.id.layoutUtama);
        logo = findViewById(R.id.imageView);
    }
}