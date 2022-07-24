package com.risqi.traveland;

//import android.support.v7.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DatabaseReference;
import com.risqi.traveland.SQLite.DataFirstApp;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    DatabaseReference databaseReference;
    DataFirstApp dataFirstApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initialize();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Cursor res = dataFirstApp.getDataOne();
                res.moveToFirst();
                boolean ok=false;
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
    }
}