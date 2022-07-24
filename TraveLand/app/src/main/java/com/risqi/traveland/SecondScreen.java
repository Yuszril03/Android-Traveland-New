package com.risqi.traveland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class SecondScreen extends AppCompatActivity {

    Button prev,next;
    RadioGroup radioGroup;
    RadioButton siang,gelap;
    ConstraintLayout layoutUtama;
    TextView title;
    String ModeApp="Siang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);
        initialize();
        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            ModeApp = bundle.getString("ModeApp");
        }else{
            ModeApp = getIntent().getStringExtra("ModeApp");
        }
        if(ModeApp.equals("Siang")){
            siang.setChecked(true);
            layoutUtama.setBackgroundColor(getResources().getColor(R.color.white));
            siang.setTextColor(getResources().getColor(R.color.darkMode));
            siang.setButtonTintList(getResources().getColorStateList(R.color.darkMode));
            gelap.setButtonTintList(getResources().getColorStateList(R.color.darkMode));
            gelap.setTextColor(getResources().getColor(R.color.darkMode));
            title.setTextColor(getResources().getColor(R.color.darkMode));
        }else{
            gelap.setChecked(true);
            layoutUtama.setBackgroundColor(getResources().getColor(R.color.darkMode));
            siang.setTextColor(getResources().getColor(R.color.white));
            gelap.setTextColor(getResources().getColor(R.color.white));
            siang.setButtonTintList(getResources().getColorStateList(R.color.white));
            gelap.setButtonTintList(getResources().getColorStateList(R.color.white));
            title.setTextColor(getResources().getColor(R.color.white));
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
                Intent intent = new Intent(SecondScreen.this, ThridScreen.class);
                intent.putExtra("ModeApp",ModeApp);
                startActivity(intent);
                Animatoo.animateSwipeRight(SecondScreen.this);
                finish();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondScreen.this, FirstScreen.class);
                intent.putExtra("ModeApp",ModeApp);
                startActivity(intent);
                Animatoo.animateSwipeLeft(SecondScreen.this);
                finish();
            }
        });

        siang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModeApp="Siang";
                layoutUtama.setBackgroundColor(getResources().getColor(R.color.white));
                siang.setTextColor(getResources().getColor(R.color.darkMode));
                siang.setButtonTintList(getResources().getColorStateList(R.color.darkMode));
                gelap.setButtonTintList(getResources().getColorStateList(R.color.darkMode));
                gelap.setTextColor(getResources().getColor(R.color.darkMode));
                title.setTextColor(getResources().getColor(R.color.darkMode));
            }
        });
        gelap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModeApp="Malam";
                layoutUtama.setBackgroundColor(getResources().getColor(R.color.darkMode));
                siang.setTextColor(getResources().getColor(R.color.white));
                gelap.setTextColor(getResources().getColor(R.color.white));
                siang.setButtonTintList(getResources().getColorStateList(R.color.white));
                gelap.setButtonTintList(getResources().getColorStateList(R.color.white));
                title.setTextColor(getResources().getColor(R.color.white));
            }
        });

    }

    private void initialize(){
        next = findViewById(R.id.button6);
        prev = findViewById(R.id.button7);
        radioGroup = findViewById(R.id.radioGroup);
        siang = findViewById(R.id.radioButton);
        gelap = findViewById(R.id.radioButton2);
        layoutUtama= findViewById(R.id.layoutUtama);
        title= findViewById(R.id.textView5);
    }
}