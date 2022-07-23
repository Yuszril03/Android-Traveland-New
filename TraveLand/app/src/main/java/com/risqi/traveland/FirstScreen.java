package com.risqi.traveland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class FirstScreen extends AppCompatActivity {
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        initialize();
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstScreen.this, SecondScreen.class);
                startActivity(intent);
                Animatoo.animateSwipeRight(FirstScreen.this);
                finish();
            }
        });

    }
    private void initialize(){
        next = findViewById(R.id.button6);
    }
}