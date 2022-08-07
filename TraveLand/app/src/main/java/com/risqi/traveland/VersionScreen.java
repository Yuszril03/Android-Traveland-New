package com.risqi.traveland;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.Map;

public class VersionScreen extends AppCompatActivity {

    private DatabaseReference databaseReference, databaseReference2, databaseReference3, databaseReference4;
    private DataFirstApp dataFirstApp;
    private DataLoginUser dataLoginUser;
    private String keyAndroid;

    //Main
    private Button updateNow, btnclose;
    private ConstraintLayout layoutUtama;
    private TextView text, subText;
    //Other
    private String LinkUpdate, BeforeScreen;
    private DataMode dataMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_screen);
        initialize();
        setMode();
        getDataInten();
        toLink();
        toMenu();
    }

    private void toMenu() {
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BeforeScreen.equals("SplasScreen")) {
                    cekUser();
                }else{
                    Intent intent = new Intent(VersionScreen.this, MainProfile.class);
                    startActivity(intent);
                    Animatoo.animateFade(VersionScreen.this);
                    onStop();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (BeforeScreen.equals("SplasScreen")) {
            cekUser();
        }else{
            Intent intent = new Intent(VersionScreen.this, MainProfile.class);
            startActivity(intent);
            Animatoo.animateFade(VersionScreen.this);
            onStop();
        }
    }

    private void setMode() {
        Cursor mod = dataMode.getDataOne();
        mod.moveToFirst();
        String modeApps = "Siang";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            modeApps = mod.getString(mod.getColumnIndexOrThrow("mode"));

            mod.moveToNext();
        }
        mod.close();
        if (modeApps.equals("Malam")) {
            layoutUtama.setBackgroundColor(getResources().getColor(R.color.darkMode));
            btnclose.setBackgroundResource(R.drawable.icon_cancel);
            text.setTextColor(getResources().getColor(R.color.white));
            subText.setTextColor(getResources().getColor(R.color.darkTxt));
        } else {
            layoutUtama.setBackgroundColor(getResources().getColor(R.color.white));
            btnclose.setBackgroundResource(R.drawable.icon_cancel_dark);
            text.setTextColor(getResources().getColor(R.color.darkMode));
            subText.setTextColor(getResources().getColor(R.color.accent));
        }
    }

    private void getDataInten() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            LinkUpdate = bundle.getString("LinkUpdate");
            BeforeScreen = bundle.getString("Before");
        } else {
            LinkUpdate = getIntent().getStringExtra("LinkUpdate");
            BeforeScreen = getIntent().getStringExtra("Before");
        }
    }

    private void toLink() {
        updateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(LinkUpdate); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
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
                                        Intent intent = new Intent(VersionScreen.this, FirstScreen.class);
                                        intent.putExtra("ModeApp", "Siang");
                                        startActivity(intent);
                                        Animatoo.animateFade(VersionScreen.this);
                                        finish();
                                        onStop();
                                    } else {
                                        Intent intent = new Intent(VersionScreen.this, MainMenu.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(VersionScreen.this);
                                        finish();
                                        onStop();
                                    }

                                }
                            }, 100);
                        } else if (resLogin.getCount() == 0 && cekKeyAndroid == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                boolean ok=dataFirstApp.insertData("Masuk");
                                    Cursor res = dataFirstApp.getDataOne();
                                    res.moveToFirst();
                                    databaseReference.onDisconnect();
                                    if (res.getCount() == 0) {
                                        Intent intent = new Intent(VersionScreen.this, FirstScreen.class);
                                        intent.putExtra("ModeApp", "Siang");
                                        startActivity(intent);
                                        Animatoo.animateFade(VersionScreen.this);
                                        finish();
                                        onStop();
                                    } else {
                                        Intent intent = new Intent(VersionScreen.this, MainMenu.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(VersionScreen.this);
                                        finish();
                                        onStop();
                                    }

                                }
                            }, 100);
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
                                        Intent intent = new Intent(VersionScreen.this, FirstScreen.class);
                                        intent.putExtra("ModeApp", "Siang");
                                        startActivity(intent);
                                        Animatoo.animateFade(VersionScreen.this);
                                        finish();
                                        onStop();
                                    } else {
                                        Intent intent = new Intent(VersionScreen.this, MainMenu.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(VersionScreen.this);
                                        finish();
                                        onStop();
                                    }

                                }
                            }, 100);
                        } else if (resLogin.getCount() == 0 && cekKeyAndroid == 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                boolean ok=dataFirstApp.insertData("Masuk");
                                    Cursor res = dataFirstApp.getDataOne();
                                    res.moveToFirst();
                                    databaseReference.onDisconnect();
                                    if (res.getCount() == 0) {
                                        Intent intent = new Intent(VersionScreen.this, FirstScreen.class);
                                        intent.putExtra("ModeApp", "Siang");
                                        startActivity(intent);
                                        Animatoo.animateFade(VersionScreen.this);
                                        finish();
                                        onStop();
                                    } else {
                                        Intent intent = new Intent(VersionScreen.this, MainMenu.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(VersionScreen.this);
                                        finish();
                                        onStop();
                                    }

                                }
                            }, 100);

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
                                        Intent intent = new Intent(VersionScreen.this, FirstScreen.class);
                                        intent.putExtra("ModeApp", "Siang");
                                        startActivity(intent);
                                        Animatoo.animateFade(VersionScreen.this);
                                        finish();
                                        onStop();
                                    } else {
                                        Intent intent = new Intent(VersionScreen.this, MainMenu.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(VersionScreen.this);
                                        finish();
                                        onStop();
                                    }

                                }
                            }, 100);
                        }
                    }

                }
            }
        });

    }

    private void initialize() {

        //Main
        btnclose = findViewById(R.id.btnclose);
        layoutUtama = findViewById(R.id.layoutUtama);
        subText = findViewById(R.id.subText);
        text = findViewById(R.id.text);

        //Other
        updateNow = findViewById(R.id.button);
        dataMode = new DataMode(this);
        dataFirstApp = new DataFirstApp(this);
        dataLoginUser = new DataLoginUser(this);
        keyAndroid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}