package com.risqi.traveland.SettingScreen;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.risqi.traveland.BuildConfig;
import com.risqi.traveland.MainMenuScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataBeforeLogin;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.SplashScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainProfileScreen extends AppCompatActivity {

    //Main
    private LinearLayout linear;
    private ScrollView scrollable;
    private ConstraintLayout constraintLayout5;
    private SwipeRefreshLayout refresh;

    //Atas
    private TextView textEmail, textprofile1;
    private ConstraintLayout backgrooundMain;
    private ImageView back;
    private CircularImageView circularimageview;
    private Button takeImage, btnbackprofile, btnLOgin;

    //Bawah
    private ConstraintLayout backgrooundSecond;
    private TextView TitleSetting, TitleMode;
    //Update APP
    private TextView textprofileUpdate;
    private ImageView arrowupdate;
    private ConstraintLayout constraintLayoutUpdate;
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
    private TextView textVersi1, textVersi2;

    private Switch switchMode;
    private int posisiMode = 0;
    DataMode dataMode;
    private DataLoginUser loginUser;
    private DataBeforeLogin beforeLogin;
    Uri image_uri;
    StorageReference mStorageRef;
    DatabaseReference database1, database2;
    private TextView textImage,textDateTime;
    private String NIKDATA;
    final Calendar calendarCreated = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    SweetAlertDialog pDialog;
    private int internet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);
        cekKondisi();
        initialize();
        //Refresh
        refreshData();
        //Akun
        CheckDataCloud();
        settingAkun();
        //BUtton Keluar
        logoutUser();
        //BUTTON LOGIN
        setButtonLogin();
        //MODE
        SettingMode();
        //switchmode
        ChangeMode();
        //Take Image
        TakeImagee();
        ///To Edit Profile
        toProfile();
        //BackMenu
        backtoMenu();
        //setVersion
        setVersionApp();
        //to Edit Password
        toPassword();
        //toUpdate
        cekUpdate();
        //toRiwayat
        toRiwayat();





    }

    private void toRiwayat() {
        layoutRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainProfileScreen.this, HistoryOrderingScreen.class);
                startActivity(a);
                Animatoo.animateSwipeLeft(MainProfileScreen.this);
                onStop();
            }
        });
    }

    private void cekUpdate() {
        constraintLayoutUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Memuat Data...");
                pDialog.setCancelable(false);
                pDialog.show();
                database1 =  FirebaseDatabase.getInstance().getReference();
                database1.child("Data-Version").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String lastVersi="";
                        String linkVersi="";
                        Map<String, Object> Version = (Map<String, Object>) task.getResult().getValue();
                        for (Map.Entry<String, Object> entry : Version.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            Map<String, Object> Temps = (Map<String, Object>) entry.getValue();
                            lastVersi=Temps.get("nomorVersi").toString();
                            linkVersi=Temps.get("LinkUpdate").toString();

                        }
                        if(lastVersi.equals(BuildConfig.VERSION_NAME)){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(MainProfileScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Aplikasi Mutahir")
                                            .setContentText("Aplikasi telah dimutahir")
                                            .setConfirmText("Okey")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                }
                                            })
                                            .show();

                                }
                            }, 1000);
                        }else{
                            String finalLinkVersi = linkVersi;
                            new Handler().postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   Intent intent = new Intent(MainProfileScreen.this, VersionScreen.class);
                                   intent.putExtra("LinkUpdate", finalLinkVersi);
                                   intent.putExtra("Before", "ProfilMain");
                                   startActivity(intent);
                                   Animatoo.animateFade(MainProfileScreen.this);
                                   onStop();
                               }
                           }, 2000);
                        }
                    }
                });
            }
        });
    }

    private void toPassword() {
        layoutKataSandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainProfileScreen.this, EditPasswordScreen.class);
                startActivity(a);
                Animatoo.animateSwipeLeft(MainProfileScreen.this);
                onStop();
            }
        });
    }

    private void setVersionApp() {
        textVersi1.setText("Versi "+BuildConfig.VERSION_NAME);
        textVersi2.setText("Versi "+BuildConfig.VERSION_NAME);
    }

    private void setButtonLogin() {
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
                Intent intent = new Intent(MainProfileScreen.this, LoginScreen.class);
                intent.putExtra("Before", "Profil");
                intent.putExtra("idScreen", "0");
                startActivity(intent);
                Animatoo.animateSlideUp(MainProfileScreen.this);

                onStop();
            }
        });
    }

    private void backtoMenu() {
        btnbackprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a = new Intent(MainProfileScreen.this, MainMenuScreen.class);
                startActivity(a);
                Animatoo.animateFade(MainProfileScreen.this);
                finish();
                onStop();
            }
        });
    }

    private void refreshData()
    {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CheckDataCloud();
                settingAkun();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }
    private boolean adaInternet() {
        ConnectivityManager koneksi = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return koneksi.getActiveNetworkInfo() != null;
    }
    private void cekKondisi() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!adaInternet() && internet == 0) {
                    internet = 1;
                    new SweetAlertDialog(MainProfileScreen.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Internet tidak terhubung")
                            .setContentText("Mohon cek kembali konkesi internet")
                            .setConfirmText("Okey")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(getIntent());
                                    Animatoo.animateFade(MainProfileScreen.this);
                                    onStop();
                                }
                            })
                            .show();
                } else if (adaInternet() && internet == 0) {

                    Cursor resLogin = loginUser.getDataOne();
                    resLogin.moveToFirst();
                    if(resLogin.getCount()>0){

                    }


                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void toProfile() {
        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainProfileScreen.this, EditProfilScreen.class);
                startActivity(a);
                Animatoo.animateSwipeLeft(MainProfileScreen.this);
                onStop();
            }
        });
    }

    private void CheckDataCloud() {
        Cursor mod = loginUser.getDataOne();
        mod.moveToFirst();
        String NIK = "";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            NIK = mod.getString(mod.getColumnIndexOrThrow("nik"));

            mod.moveToNext();
        }
        mod.close();
        if(mod.getCount()>0){
            database1 = FirebaseDatabase.getInstance().getReference();
            String finalNIK = NIK;
            database1.child("Master-Data-Customer").child(NIK).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Map<String, Object> DataCustomer = (Map<String, Object>) task.getResult().getValue();
                    database2 = FirebaseDatabase.getInstance().getReference();
                    database2.child("Master-Data-Account-Customer").child(finalNIK).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task2) {
                            Map<String, Object> DataCustomerAccount = (Map<String, Object>) task2.getResult().getValue();
                            loginUser.updateDataRealTime(DataCustomer.get("NamaCustomer").toString(),DataCustomer.get("fotoCustomer").toString(),DataCustomer.get("Gender").toString(),DataCustomerAccount.get("KataSandi").toString());
                        }
                    });
                }
            });
        }
    }

    private void logoutUser() {
        layoutKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(MainProfileScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Anda Yakin?")
                        .setContentText("Keluar dari akun ini!")
                        .setConfirmText("Iya!")
                        .setCancelText("Tidak")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Memuat Data...");
                                pDialog.setCancelable(false);
                                pDialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialog.dismiss();
                                        loginUser.deleteDataAll();
                                        Intent intent = new Intent(MainProfileScreen.this, SplashScreen.class);
                                        startActivity(intent);
                                        Animatoo.animateFade(MainProfileScreen.this);
                                        onStop();
                                    }
                                }, 2000);

                            }
                        })
                        .show();
            }
        });
    }

    private void TakeImagee() {
        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
    }

    private void ChangeMode() {
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (posisiMode == 0) {
                    posisiMode = 1;
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

                    //Update
                    textprofileUpdate.setTextColor(getResources().getColor(R.color.white));
                    arrowupdate.setBackgroundResource(R.drawable.icon_right_arrow_dark);
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

                } else {
                    dataMode.updateData("Siang");
                    posisiMode = 0;
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

                    //Update
                    textprofileUpdate.setTextColor(getResources().getColor(R.color.darkMode));
                    arrowupdate.setBackgroundResource(R.drawable.icon_right_arrow_light);
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
    }

    private void SettingMode() {
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
            posisiMode = 1;
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
            //Update
            textprofileUpdate.setTextColor(getResources().getColor(R.color.white));
            arrowupdate.setBackgroundResource(R.drawable.icon_right_arrow_dark);
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
        } else {
            switchMode.setChecked(false);
            posisiMode = 0;
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

            //Update
            textprofileUpdate.setTextColor(getResources().getColor(R.color.darkMode));
            arrowupdate.setBackgroundResource(R.drawable.icon_right_arrow_light);
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

    private void settingAkun() {
        Cursor loginUserDataOne = loginUser.getDataOne();
        loginUserDataOne.moveToFirst();
        int hasil = loginUserDataOne.getCount();
        loginUserDataOne.close();
        if (hasil == 0) {
            textVersi1.setVisibility(View.GONE);
            textVersi2.setVisibility(View.VISIBLE);
            takeImage.setVisibility(View.GONE);
            btnLOgin.setVisibility(View.VISIBLE);
            //layout
            layoutProfile.setVisibility(View.GONE);
            layoutKataSandi.setVisibility(View.GONE);
            layoutRiwayat.setVisibility(View.GONE);
            layoutKeluar.setVisibility(View.GONE);

        } else {
            Cursor mod = loginUser.getDataOne();
            mod.moveToFirst();
            String nama = "";
            String foto = "";
            String nik = "";
            while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
                nama = mod.getString(mod.getColumnIndexOrThrow("nama"));
                foto = mod.getString(mod.getColumnIndexOrThrow("foto"));
                nik = mod.getString(mod.getColumnIndexOrThrow("nik"));
                NIKDATA = mod.getString(mod.getColumnIndexOrThrow("nik"));

                mod.moveToNext();
            }
            mod.close();
            textprofile1.setText(nama);
            textEmail.setText(nik);
            if (foto.equals("")) {

            } else {
                Glide.with(this).clear(circularimageview);
                Glide.with(this)
                        .load(foto)
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()
                                .override(194, 112)
                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(circularimageview);
            }
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            new SweetAlertDialog(MainProfileScreen.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Apakah Anda Yakin?")
                    .setContentText("Mengganti Foto Profil!")
                    .setConfirmText("Iya!")
                    .setCancelText("Tidak")
                    .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Memuat Data...");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            image_uri = data.getData();

                            Fileuploader();
                        }
                    })
                    .show();


            //Toast.makeText(this, "Ini : "+textViewTemp.getText().toString(), Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    private void Fileuploader() {
        String getNama = System.currentTimeMillis() + "." + getExtension(image_uri);
        mStorageRef = FirebaseStorage.getInstance().getReference("images-customer");
        final StorageReference Ref = mStorageRef.child(getNama);
        // Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        //StorageReference riversRef = storageRef.child("images/rivers.jpg");
        final StorageReference demo = mStorageRef.child(getNama);
        final String[] hasil = {""};
        Ref.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        loginUser.updateDataFoto(uri.toString());
                        String dateTime = simpleDateFormat.format(calendarCreated.getTime()).toString();
//                        textImage.setText(uri.toString());
//                        textDateTime.setText(dateTime);

                        Task updateData;

                        HashMap insertCustomerUpdate = new HashMap();
                        insertCustomerUpdate.put("TanggalUpdate", dateTime);
                        insertCustomerUpdate.put("fotoCustomer", uri.toString());

                        updateData = FirebaseDatabase.getInstance().getReference().child("Master-Data-Customer").child(NIKDATA).updateChildren(insertCustomerUpdate);


                        pDialog.dismiss();
                        new SweetAlertDialog(MainProfileScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Berhasil")
                                .setContentText("Berhasil Mengganti Profil!")
                                .setConfirmText("Okey")
                                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                .show();
                        Glide.with(MainProfileScreen.this).clear(circularimageview);
                        Glide.with(MainProfileScreen.this)
                                .load(uri.toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                                .apply(new RequestOptions()
                                        .override(194, 112)
                                        .priority(Priority.HIGH)
                                        .centerCrop())
                                .into(circularimageview);


                        //editTextNama.setText(uri.toString());
//                        textViewTemp.setText(uri.toString());
//                        imageViewProfil.setImageURI(image_uri);
//                        Toast.makeText(MainProfileScreen.this, uri.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(MainProfileScreen.this, MainMenuScreen.class);
        startActivity(a);
        Animatoo.animateFade(MainProfileScreen.this);
        finish();
        onStop();
    }

    private void initialize() {

        dataMode = new DataMode(this);
        loginUser = new DataLoginUser(this);
        beforeLogin = new DataBeforeLogin(this);

        switchMode = findViewById(R.id.switchMode);
        refresh = findViewById(R.id.refresh);

        //Main
        scrollable = findViewById(R.id.scrollable);
        linear = findViewById(R.id.linear);
        constraintLayout5 = findViewById(R.id.constraintLayout5);

        //ATAS
        textprofile1 = findViewById(R.id.textprofile1);
        textEmail = findViewById(R.id.textEmail);
        backgrooundMain = findViewById(R.id.backgrooundMain);
        back = findViewById(R.id.imageprofile);
        takeImage = findViewById(R.id.takeImage);
        btnbackprofile = findViewById(R.id.btnbackprofile);
        btnLOgin = findViewById(R.id.btnLOgin);
        circularimageview = findViewById(R.id.circularImageView);


        //BAwah
        backgrooundSecond = findViewById(R.id.backgrooundSecond);
        TitleSetting = findViewById(R.id.TitleSetting);
        TitleMode = findViewById(R.id.textprofile2);
        //Profile
        textprofileUpdate = findViewById(R.id.textprofileUpdate);
        arrowupdate = findViewById(R.id.arrowupdate);
        constraintLayoutUpdate = findViewById(R.id.constraintLayoutUpdate);
        //Profile
        textProfile = findViewById(R.id.textprofile21);
        arrowProfile = findViewById(R.id.arrow1);
        layoutProfile = findViewById(R.id.constraintLayout7);
        //KataSandi
        textKataSandi = findViewById(R.id.textprofile22);
        arrowKataSandi = findViewById(R.id.arrow2);
        layoutKataSandi = findViewById(R.id.constraintLayout8);
        //Riwayat
        textKataRiwayat = findViewById(R.id.textprofile23);
        arrowRiwayat = findViewById(R.id.arrow3);
        layoutRiwayat = findViewById(R.id.constraintLayout9);
        //Riwayat
        textKeluar = findViewById(R.id.textprofile24);
        arrowKeluar = findViewById(R.id.arrow4);
        layoutKeluar = findViewById(R.id.constraintLayout10);
        //Versi
        textVersi1 = findViewById(R.id.versi1);
        textVersi2 = findViewById(R.id.versi2);

        textDateTime = findViewById(R.id.textDateTime);
        textImage = findViewById(R.id.textImage);

        //Sweet Alert
        pDialog = new SweetAlertDialog(MainProfileScreen.this, SweetAlertDialog.PROGRESS_TYPE);

    }
}