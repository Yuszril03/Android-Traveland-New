package com.risqi.traveland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.risqi.traveland.SQLite.DataFirstApp;
import com.risqi.traveland.SQLite.DataMode;

public class ThridScreen extends AppCompatActivity {

    Button prev,next;
    TextView subTitle,title;
    String ModeApp;
    ConstraintLayout layoutUtama;

    private DataFirstApp dataFirstApp;
    private DataMode dataMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrid_screen);
        initialize();
        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            ModeApp = bundle.getString("ModeApp");
        }else{
            ModeApp = getIntent().getStringExtra("ModeApp");
        }
        if(ModeApp.equals("Siang")){
            layoutUtama.setBackgroundColor(getResources().getColor(R.color.white));
            title.setTextColor(getResources().getColor(R.color.darkMode));
            subTitle.setTextColor(getResources().getColor(R.color.darkMode));
        }else{
            layoutUtama.setBackgroundColor(getResources().getColor(R.color.darkMode));
            title.setTextColor(getResources().getColor(R.color.white));
            subTitle.setTextColor(getResources().getColor(R.color.white));
        }
        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    next.setBackgroundResource(R.drawable.button_primary_pressed);
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    next.setBackgroundResource(R.drawable.button_primary);
                }
                return false;
            }
        });

        prev.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    prev.setBackgroundResource(R.drawable.button_primary_pressed);
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    prev.setBackgroundResource(R.drawable.button_primary);
                }
                return false;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataFirstApp.insertData("Masuk");
                dataMode.insertData(ModeApp);
                Intent intent = new Intent(ThridScreen.this, MainMenuScreen.class);
                intent.putExtra("ModeApp",ModeApp);
                startActivity(intent);
                Animatoo.animateFade(ThridScreen.this);
                finish();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThridScreen.this, SecondScreen.class);
                intent.putExtra("ModeApp",ModeApp);
                startActivity(intent);
                Animatoo.animateSlideLeft(ThridScreen.this);
                finish();
            }
        });
    }
    private void initialize(){
        next = findViewById(R.id.button6);
        prev = findViewById(R.id.button7);
        title = findViewById(R.id.textView5);
        subTitle = findViewById(R.id.textView7);
        layoutUtama = findViewById(R.id.layoutUtama);

        dataFirstApp = new DataFirstApp(this);
        dataMode = new DataMode(this);
    }
}