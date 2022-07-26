package com.risqi.traveland.NotifSucess;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.risqi.traveland.HotelScreen.DetailTransactionHotelScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.RentalScreen.DetailTransactionRentalScreen;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.WisataScreen.DetailTransactionWisataScreen;

public class NotifTransactionSuccessScreen extends AppCompatActivity {

    private DataMode dataMode;
    private TextView textData, textSub;
    private ConstraintLayout layoutUtama;
    private ImageView imageSuccess;
    private String idWisata = "", NextScreen = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_transaction_success_screen);
        initialize();
        setDataMode();
        getDataInten();
        setDataNext();

    }

    private void getDataInten() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idWisata = bundle.getString("idScreen");
            NextScreen = bundle.getString("Next");
            setDataNext();
        } else {
            idWisata = getIntent().getStringExtra("idScreen");
            NextScreen = getIntent().getStringExtra("Next");
            setDataNext();

        }
    }



    private void setDataNext() {
        if (NextScreen.equals("UploadFotoWisata")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent a = new Intent(NotifTransactionSuccessScreen.this, DetailTransactionWisataScreen.class);
                    a.putExtra("idScreen", "" + idWisata);
                    startActivity(a);
                    Animatoo.animateSlideRight(NotifTransactionSuccessScreen.this);
                    onStop();
                }
            }, 2000);
        }else if(NextScreen.equals("UploadFotoHotel")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent a = new Intent(NotifTransactionSuccessScreen.this, DetailTransactionHotelScreen.class);
                    a.putExtra("idScreen", "" + idWisata);
                    startActivity(a);
                    Animatoo.animateSlideRight(NotifTransactionSuccessScreen.this);
                    onStop();
                }
            }, 2000);
        }else if(NextScreen.equals("UploadFotoRental")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent a = new Intent(NotifTransactionSuccessScreen.this, DetailTransactionRentalScreen.class);
                    a.putExtra("idScreen", "" + idWisata);
                    startActivity(a);
                    Animatoo.animateSlideRight(NotifTransactionSuccessScreen.this);
                    onStop();
                }
            }, 2000);
        }

    }

    @Override
    public void onBackPressed() {

    }

    private void setDataMode() {
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
            textData.setTextColor(getResources().getColor(R.color.white));
            textSub.setTextColor(getResources().getColor(R.color.white));
            layoutUtama.setBackgroundResource(R.color.darkMode);
            imageSuccess.setBackgroundResource(R.drawable.icon_suceess_white);
        }

    }

    private void initialize() {
        dataMode = new DataMode(this);
        textData = findViewById(R.id.textData);
        textSub = findViewById(R.id.textSub);
        layoutUtama = findViewById(R.id.layoutUtama);
        imageSuccess = findViewById(R.id.imageSuccess);
    }
}