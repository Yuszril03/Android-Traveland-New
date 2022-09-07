package com.risqi.traveland.ETicket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.risqi.traveland.R;
import com.risqi.traveland.WisataScreen.DetailTransactionWisataScreen;

public class ETicketScreen extends AppCompatActivity {

    private ImageView kodeQR;
    private TextView kodeBooking;
    private String idWisata="",jenisScreen="";
    private Button backtoscroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket_screen);
        getDataInten();
        initialize();
        setCOdeQR();
        toMenu();


    }



    private void toMenu() {
        backtoscroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(jenisScreen.equals("Wisata")){
                    Intent intent = new Intent(ETicketScreen.this, DetailTransactionWisataScreen.class);
                    intent.putExtra("idScreen",idWisata);
                    startActivity(intent);
                    Animatoo.animateSlideDown(ETicketScreen.this);
                    onStop();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(jenisScreen.equals("Wisata")){
            Intent intent = new Intent(this, DetailTransactionWisataScreen.class);
            intent.putExtra("idScreen",idWisata);
            startActivity(intent);
            Animatoo.animateSlideDown(this);
            onStop();
        }
    }

    private void setCOdeQR() {
        kodeBooking.setText(idWisata);
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            BitMatrix mMatrix = mWriter.encode(idWisata+"-"+jenisScreen, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);
            kodeQR.setImageBitmap(mBitmap);
        }catch (WriterException e) {
            e.printStackTrace();
        }
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

    private void initialize() {
        kodeQR = findViewById(R.id.kodeQR);
        kodeBooking = findViewById(R.id.kodeBooking);
        backtoscroll = findViewById(R.id.backtoscroll);
    }
}