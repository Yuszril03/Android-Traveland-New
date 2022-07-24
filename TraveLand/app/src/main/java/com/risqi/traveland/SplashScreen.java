package com.risqi.traveland;

//import android.support.v7.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DatabaseReference;
import com.risqi.traveland.SQLite.DataFirstApp;
import com.risqi.traveland.SQLite.DataMode;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    DatabaseReference databaseReference;
    DataFirstApp dataFirstApp;
    DataMode dataMode;
    ConstraintLayout layoutUtama;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initialize();
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
        }else{
            layoutUtama.setBackgroundColor(getResources().getColor(R.color.white));
            logo.setBackgroundResource(R.drawable.penuhlogo);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                boolean ok=dataFirstApp.insertData("Masuk");
                Cursor res = dataFirstApp.getDataOne();
                res.moveToFirst();
                if(res.getCount()==0)
                {
                    Intent intent = new Intent(SplashScreen.this, FirstScreen.class);
                    intent.putExtra("ModeApp","Siang");
                    startActivity(intent);
                    Animatoo.animateFade(SplashScreen.this);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                    startActivity(intent);
                    Animatoo.animateFade(SplashScreen.this);
                    finish();
                }

            }
        }, 1000);
    }

    private void initialize() {
        dataFirstApp = new DataFirstApp(this);
        dataMode = new DataMode(this);
        layoutUtama = findViewById(R.id.layoutUtama);
        logo = findViewById(R.id.imageView);
    }
}