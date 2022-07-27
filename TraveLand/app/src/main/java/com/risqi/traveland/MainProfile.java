package com.risqi.traveland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;

public class MainProfile extends AppCompatActivity {

    //Main
    private LinearLayout linear;
    private ScrollView scrollable;
    private ConstraintLayout constraintLayout5;

    //Atas
    private TextView textEmail,textprofile1;
    private ConstraintLayout backgrooundMain;
    private ImageView back;
    private Button takeImage,btnbackprofile,btnLOgin;

    //Bawah
    private ConstraintLayout backgrooundSecond;
    private TextView TitleSetting,TitleMode;

    //Profile
    private TextView textProfile;
    private ImageView arrowProfile;
    private ConstraintLayout layoutProfile;
    //KataSandi
    private TextView textKataSandi;
    private ImageView arrowKataSandi;
    private ConstraintLayout layoutKataSandi;
    //RIwayat
    private TextView textKataRiwayat;
    private ImageView arrowRiwayat;
    private ConstraintLayout layoutRiwayat;
    //KEluar
    private TextView textKeluar;
    private ImageView arrowKeluar;
    private ConstraintLayout layoutKeluar;
    //Versi
    private TextView textVersi1,textVersi2;

    private Switch switchMode;
    private int posisiMode=0;
    DataMode dataMode;
    private DataLoginUser loginUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);
        initialize();

        //Akun
        Cursor loginUserDataOne = loginUser.getDataOne();
        loginUserDataOne.moveToFirst();
        int hasil = loginUserDataOne.getCount();
        loginUserDataOne.close();
        if(hasil==0){
            textVersi1.setVisibility(View.GONE);
            textVersi2.setVisibility(View.VISIBLE);
            takeImage.setVisibility(View.GONE);
            btnLOgin.setVisibility(View.VISIBLE);
            //layout
            layoutProfile.setVisibility(View.GONE);
            layoutKataSandi.setVisibility(View.GONE);
            layoutRiwayat.setVisibility(View.GONE);
            layoutKeluar.setVisibility(View.GONE);

        }else{
            textVersi1.setVisibility(View.VISIBLE);
            textVersi2.setVisibility(View.INVISIBLE);
            takeImage.setVisibility(View.VISIBLE);
            btnLOgin.setVisibility(View.INVISIBLE);
            //layout
            layoutProfile.setVisibility(View.VISIBLE);
            layoutKataSandi.setVisibility(View.VISIBLE);
            layoutRiwayat.setVisibility(View.VISIBLE);
            layoutKeluar.setVisibility(View.VISIBLE);
        }

        //BUTTON LOGIN
        btnLOgin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    btnLOgin.setBackgroundResource(R.drawable.button_blue_press);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    btnLOgin.setBackgroundResource(R.drawable.button_blue);
                }
                return false;
            }
        });
        btnLOgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainProfile.this, LoginScreen.class);
                intent.putExtra("BeforeActivty", "Profil");
                startActivity(intent);
                Animatoo.animateSlideUp(MainProfile.this);
                finish();
            }
        });

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
            posisiMode=1;
            switchMode.setChecked(true);
            switchMode.setTrackResource(R.drawable.track_switch_mode_on);
            //Main
            linear.setBackgroundResource(R.color.darkMode);
            scrollable.setBackgroundResource(R.color.darkMode);
            constraintLayout5.setBackgroundResource(R.color.darkMode);
            //ATAS
            backgrooundMain.setBackgroundResource(R.color.darkMode2);
            textprofile1.setTextColor(getResources().getColor(R.color.white));
            textEmail.setTextColor(getResources().getColor(R.color.darkTxt));
            takeImage.setBackgroundResource(R.drawable.icon_add_image_dark_mode);
            back.setBackgroundResource(R.drawable.icon_left_arrow_dark_mode);

            //Bawah
            backgrooundSecond.setBackgroundResource(R.drawable.borders_menu_profile_dark_mode);
            TitleSetting.setTextColor(getResources().getColor(R.color.white));
            TitleMode.setTextColor(getResources().getColor(R.color.white));
            //Profile
            textProfile.setTextColor(getResources().getColor(R.color.white));
            arrowProfile.setBackgroundResource(R.drawable.icon_right_arrow_dark);
            //KataSandi
            textKataSandi.setTextColor(getResources().getColor(R.color.white));
            arrowKataSandi.setBackgroundResource(R.drawable.icon_right_arrow_dark);
            //Profile
            textKataRiwayat.setTextColor(getResources().getColor(R.color.white));
            arrowRiwayat.setBackgroundResource(R.drawable.icon_right_arrow_dark);
            //Profile
            textKeluar.setTextColor(getResources().getColor(R.color.white));
            arrowKeluar.setBackgroundResource(R.drawable.icon_right_arrow_dark);
            //versi
            textVersi1.setTextColor(getResources().getColor(R.color.white));
            textVersi2.setTextColor(getResources().getColor(R.color.white));
        }else{
            switchMode.setChecked(false);
            posisiMode=0;
            switchMode.setTrackResource(R.drawable.track_switch_mode_off);
            //main
            linear.setBackgroundResource(R.color.white);
            scrollable.setBackgroundResource(R.color.white);
            constraintLayout5.setBackgroundResource(R.color.white);
            //ATAS
            backgrooundMain.setBackgroundResource(R.color.whitemode2);
            textprofile1.setTextColor(getResources().getColor(R.color.darkMode));
            textEmail.setTextColor(getResources().getColor(R.color.accent));
            takeImage.setBackgroundResource(R.drawable.icon_add_image);
            back.setBackgroundResource(R.drawable.icon_left_arrow);
            //Bawah
            backgrooundSecond.setBackgroundResource(R.drawable.borders_menu_profile);
            TitleSetting.setTextColor(getResources().getColor(R.color.darkMode));
            TitleMode.setTextColor(getResources().getColor(R.color.darkMode));

            //Profile
            textProfile.setTextColor(getResources().getColor(R.color.darkMode));
            arrowProfile.setBackgroundResource(R.drawable.icon_right_arrow_light);
            //KataSandi
            textKataSandi.setTextColor(getResources().getColor(R.color.darkMode));
            arrowKataSandi.setBackgroundResource(R.drawable.icon_right_arrow_light);
            //Profile
            textKataRiwayat.setTextColor(getResources().getColor(R.color.darkMode));
            arrowRiwayat.setBackgroundResource(R.drawable.icon_right_arrow_light);
            //Profile
            textKeluar.setTextColor(getResources().getColor(R.color.darkMode));
            arrowKeluar.setBackgroundResource(R.drawable.icon_right_arrow_light);
            //versi
            textVersi1.setTextColor(getResources().getColor(R.color.darkMode));
            textVersi2.setTextColor(getResources().getColor(R.color.darkMode));
        }

        //switchmode
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(posisiMode==0){
                    posisiMode=1;
                    dataMode.updateData("Malam");
                    switchMode.setTrackResource(R.drawable.track_switch_mode_on);
                    //Main
                    linear.setBackgroundResource(R.color.darkMode);
                    scrollable.setBackgroundResource(R.color.darkMode);
                    constraintLayout5.setBackgroundResource(R.color.darkMode);
                    //ATAS
                    backgrooundMain.setBackgroundResource(R.color.darkMode2);
                    textprofile1.setTextColor(getResources().getColor(R.color.white));
                    textEmail.setTextColor(getResources().getColor(R.color.darkTxt));
                    takeImage.setBackgroundResource(R.drawable.icon_add_image_dark_mode);
                    back.setBackgroundResource(R.drawable.icon_left_arrow_dark_mode);

                    //Bawah
                    backgrooundSecond.setBackgroundResource(R.drawable.borders_menu_profile_dark_mode);
                    TitleSetting.setTextColor(getResources().getColor(R.color.white));
                    TitleMode.setTextColor(getResources().getColor(R.color.white));

                    //Profile
                    textProfile.setTextColor(getResources().getColor(R.color.white));
                    arrowProfile.setBackgroundResource(R.drawable.icon_right_arrow_dark);
                    //KataSandi
                    textKataSandi.setTextColor(getResources().getColor(R.color.white));
                    arrowKataSandi.setBackgroundResource(R.drawable.icon_right_arrow_dark);
                    //Profile
                    textKataRiwayat.setTextColor(getResources().getColor(R.color.white));
                    arrowRiwayat.setBackgroundResource(R.drawable.icon_right_arrow_dark);
                    //Profile
                    textKeluar.setTextColor(getResources().getColor(R.color.white));
                    arrowKeluar.setBackgroundResource(R.drawable.icon_right_arrow_dark);
                    //versi
                    textVersi1.setTextColor(getResources().getColor(R.color.white));
                    textVersi2.setTextColor(getResources().getColor(R.color.white));

                }else{
                    dataMode.updateData("Siang");
                    posisiMode=0;
                    switchMode.setTrackResource(R.drawable.track_switch_mode_off);
                    //main
                    linear.setBackgroundResource(R.color.white);
                    scrollable.setBackgroundResource(R.color.white);
                    constraintLayout5.setBackgroundResource(R.color.white);
                    //ATAS
                    backgrooundMain.setBackgroundResource(R.color.whitemode2);
                    textprofile1.setTextColor(getResources().getColor(R.color.darkMode));
                    textEmail.setTextColor(getResources().getColor(R.color.accent));
                    takeImage.setBackgroundResource(R.drawable.icon_add_image);
                    back.setBackgroundResource(R.drawable.icon_left_arrow);
                    //Bawah
                    backgrooundSecond.setBackgroundResource(R.drawable.borders_menu_profile);
                    TitleSetting.setTextColor(getResources().getColor(R.color.darkMode));
                    TitleMode.setTextColor(getResources().getColor(R.color.darkMode));

                    //Profile
                    textProfile.setTextColor(getResources().getColor(R.color.darkMode));
                    arrowProfile.setBackgroundResource(R.drawable.icon_right_arrow_light);
                    //KataSandi
                    textKataSandi.setTextColor(getResources().getColor(R.color.darkMode));
                    arrowKataSandi.setBackgroundResource(R.drawable.icon_right_arrow_light);
                    //Profile
                    textKataRiwayat.setTextColor(getResources().getColor(R.color.darkMode));
                    arrowRiwayat.setBackgroundResource(R.drawable.icon_right_arrow_light);
                    //Profile
                    textKeluar.setTextColor(getResources().getColor(R.color.darkMode));
                    arrowKeluar.setBackgroundResource(R.drawable.icon_right_arrow_light);
                    //versi
                    textVersi1.setTextColor(getResources().getColor(R.color.darkMode));
                    textVersi2.setTextColor(getResources().getColor(R.color.darkMode));
                }
            }
        });

        btnbackprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainProfile.this, MainMenu.class);
                startActivity(a);
                Animatoo.animateFade(MainProfile.this);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(MainProfile.this, MainMenu.class);
        startActivity(a);
        Animatoo.animateFade(MainProfile.this);
        finish();
    }

    private void initialize() {

        dataMode = new DataMode(this);
        loginUser = new DataLoginUser(this);

        switchMode = findViewById(R.id.switchMode);

        //Main
        scrollable= findViewById(R.id.scrollable);
        linear= findViewById(R.id.linear);
        constraintLayout5= findViewById(R.id.constraintLayout5);

        //ATAS
        textprofile1 = findViewById(R.id.textprofile1);
        textEmail = findViewById(R.id.textEmail);
        backgrooundMain = findViewById(R.id.backgrooundMain);
        back = findViewById(R.id.imageprofile);
        takeImage = findViewById(R.id.takeImage);
        btnbackprofile = findViewById(R.id.btnbackprofile);
        btnLOgin = findViewById(R.id.btnLOgin);


        //BAwah
        backgrooundSecond = findViewById(R.id.backgrooundSecond);
        TitleSetting = findViewById(R.id.TitleSetting);
        TitleMode = findViewById(R.id.textprofile2);

        //Profile
        textProfile = findViewById(R.id.textprofile21);
        arrowProfile = findViewById(R.id.arrow1);
        layoutProfile = findViewById(R.id.constraintLayout7);
        //KataSandi
        textKataSandi= findViewById(R.id.textprofile22);
        arrowKataSandi = findViewById(R.id.arrow2);
        layoutKataSandi = findViewById(R.id.constraintLayout8);
        //Riwayat
        textKataRiwayat= findViewById(R.id.textprofile23);
        arrowRiwayat = findViewById(R.id.arrow3);
        layoutRiwayat= findViewById(R.id.constraintLayout9);
        //Riwayat
        textKeluar= findViewById(R.id.textprofile24);
        arrowKeluar = findViewById(R.id.arrow4);
        layoutKeluar = findViewById(R.id.constraintLayout10);
        //Versi
        textVersi1= findViewById(R.id.versi1);
        textVersi2= findViewById(R.id.versi2);
    }
}