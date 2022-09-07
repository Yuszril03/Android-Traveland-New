package com.risqi.traveland.WisataScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.risqi.traveland.MainMenuScreen;
import com.risqi.traveland.R;

public class ProofPaymentWisataScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof_payment_wisata_screen);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(ProofPaymentWisataScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(ProofPaymentWisataScreen.this);
        onStop();
    }
}