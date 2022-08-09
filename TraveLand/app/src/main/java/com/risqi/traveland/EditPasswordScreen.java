package com.risqi.traveland;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.risqi.traveland.Firebase.MasterDataAccountCustomer;
import com.risqi.traveland.Firebase.MasterDataCustomer;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditPasswordScreen extends AppCompatActivity {
    ///Main
    private LinearLayout linear;
    private ScrollView scrollable;
    private ConstraintLayout constraintLayout5;

    //Atas
    private TextView textProfil, text2;
    private ConstraintLayout backgrooundMain;
    private ImageView back;
    private Button btnback;

    //Bawah
    private ConstraintLayout backgrooundSecond;
    //FORM
    private EditText editPasswordOld, editPassword, editPassword2;
    private ImageView alertPasswordOld, alertPassword, alertPassword2;
    private String textalertPasswordOld, textalertPassword, textalertPassword2;
    private ConstraintLayout formOldPassword, formPassword, formPassword2;
    private ImageView showPassOld,showPass1,showPass2;
    private ToolTipsManager toolTipsManager;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Button submit;

    ///Other
    private int show2=0,show1=0,showold=0;
    private int internet = 0;
    private String oldPasswordData="";
    private String NIK="";
    private MasterDataCustomer masterDataCustomer;
    private MasterDataAccountCustomer masterDataAccountCustomer;
    private DataMode dataMode;
    private DataLoginUser dataLoginUser;
    DatabaseReference dataBase1;
    Task updates1,updates2;
    SweetAlertDialog pDialog;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password_screen);
        cekKondisi();
        initialize();
        //to Menu Profil
        toMenuProfil();
        //setMode
        setMode();
        //SetDefault Form
        setDefaultForm();
        //onclick View Password
        setOnlickViewPassword();
        //getOldSandi
        getOldSandi();
        //getNIK
        getNIK();
        //setOnlickAlert
        setOnclickAlert();
        //SetUpButton + SetOnlick
        setButtonSubmit();

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
                    new SweetAlertDialog(EditPasswordScreen.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Internet tidak terhubung")
                            .setContentText("Mohon cek kembali konkesi internet")
                            .setConfirmText("Okey")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(getIntent());
                                    Animatoo.animateFade(EditPasswordScreen.this);
                                    onStop();
                                }
                            })
                            .show();
                } else if (adaInternet() && internet == 0) {

//                    Cursor resLogin = loginUser.getDataOne();
//                    resLogin.moveToFirst();
//                    if(resLogin.getCount()>0){
//
//                    }


                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void getNIK() {
        Cursor mod = dataLoginUser.getDataOne();
        mod.moveToFirst();
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            NIK = mod.getString(mod.getColumnIndexOrThrow("nik"));

            mod.moveToNext();
        }
        mod.close();
    }

    private void setButtonSubmit() {
        submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    submit.setBackgroundResource(R.drawable.button_primary_pressed);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    submit.setBackgroundResource(R.drawable.button_primary);
                }
                return false;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(EditPasswordScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Anda Yakin")
                        .setContentText("Untuk Menyimpan Data Ini")
                        .setConfirmText("Iya!")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Memuat Data...");
                                pDialog.setCancelable(false);
                                pDialog.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        int jumlah = 0;
                                        toolTipsManager.dismissAll();
                                        for (int i = 1; i <= 3; i++) {
                                            if (i == 1) {
                                                if (TextUtils.isEmpty(editPasswordOld.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertPasswordOld, "Kata Sandi Lama Harus Diisi!");
                                                    textalertPasswordOld = "Kata Sandi Lama Harus Diisi!";
                                                    alertPasswordOld.setVisibility(View.VISIBLE);
                                                    formOldPassword.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if ((editPasswordOld.getText().toString()).length() < 8) {
                                                    jumlah++;
                                                    displayToolTips(alertPasswordOld, "Kata Sandi Kurang 8 Karakter!");
                                                    textalertPasswordOld = "Kata Sandi Kurang 8 Karakter!";
                                                    alertPasswordOld.setVisibility(View.VISIBLE);
                                                    formOldPassword.setBackgroundResource(R.drawable.form_control_danger);
                                                }else if (!oldPasswordData.equals(convertMD5(editPasswordOld.getText().toString()))) {
                                                    jumlah++;
                                                    displayToolTips(alertPasswordOld, "Kata Sandi Lama Tidak Sama!");
                                                    textalertPasswordOld = "Kata Sandi Lama Tidak Sama!";
                                                    alertPasswordOld.setVisibility(View.VISIBLE);
                                                    formOldPassword.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertPasswordOld);
                                                    alertPasswordOld.setVisibility(View.INVISIBLE);
                                                    formOldPassword.setBackgroundResource(R.drawable.form_control);
                                                }
                                            } else if (i == 2) {
                                                if (TextUtils.isEmpty(editPassword.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertPassword, "Kata Sandi Baru Harus Diisi!");
                                                    textalertPassword = "Kata Sandi Baru Harus Diisi!";
                                                    alertPassword.setVisibility(View.VISIBLE);
                                                    formPassword.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if ((editPassword.getText().toString()).length() < 8) {
                                                    jumlah++;
                                                    displayToolTips(alertPassword, "Kata Sandi Kurang 8 Karakter!");
                                                    textalertPassword = "Kata Sandi Kurang 8 Karakter!";
                                                    alertPassword.setVisibility(View.VISIBLE);
                                                    formPassword.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertPassword);
                                                    alertPassword.setVisibility(View.INVISIBLE);
                                                    formPassword.setBackgroundResource(R.drawable.form_control);
                                                }
                                            } else if (i == 3) {
                                                if (TextUtils.isEmpty(editPassword2.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertPassword2, "Konfirmasi Kata Sandi Harus Diisi!");
                                                    textalertPassword2 = "Konfirmasi Kata Sandi Harus Diisi!";
                                                    alertPassword2.setVisibility(View.VISIBLE);
                                                    formPassword2.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if ((editPassword2.getText().toString()).length() < 8) {
                                                    jumlah++;
                                                    displayToolTips(alertPassword2, "Konfirmasi Kata Sandi Kurang 8 Karakter!");
                                                    textalertPassword2 = "Konfirmasi Kata Sandi Kurang 8 Karakter!";
                                                    alertPassword2.setVisibility(View.VISIBLE);
                                                    formPassword2.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if (editPassword2.getText().toString().equals(editPassword.getText().toString()) == false) {
                                                    jumlah++;
                                                    displayToolTips(alertPassword2, "Konfirmasi Kata Sandi Tidak Sama!");
                                                    textalertPassword2 = "Konfirmasi Kata Sandi Tidak Sama!";
                                                    alertPassword2.setVisibility(View.VISIBLE);
                                                    formPassword2.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertPassword2);
                                                    alertPassword2.setVisibility(View.INVISIBLE);
                                                    formPassword2.setBackgroundResource(R.drawable.form_control);
                                                }
                                            }
                                        }
                                        if (jumlah == 0) {
                                            saveData();
                                        } else {
                                            pDialog.dismiss();
                                            new SweetAlertDialog(EditPasswordScreen.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Oops...")
                                                    .setContentText("Kolom Tidak Valid!")
                                                    .setConfirmText("Okey")
                                                    .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                                    .show();
                                        }

                                    }
                                }, 2000);
                            }
                        })
                        .show();
            }
        });
    }

    private void saveData() {
        HashMap updateKataSandi = masterDataAccountCustomer.ChangePassword(convertMD5(editPassword.getText().toString()));
        HashMap updateTime = masterDataCustomer.updateDataTanggalPembaruan();

        updates1 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Customer").child(NIK).updateChildren(updateTime);
        updates1.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                updates2 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Account-Customer").child(NIK).updateChildren(updateKataSandi);
                updates2.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        pDialog.dismiss();
                        dataLoginUser.updateDataKataSandi(convertMD5(editPassword.getText().toString()));
                        new SweetAlertDialog(EditPasswordScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Berhasil")
                                .setContentText("Data berhasil tersimpan")
                                .setConfirmText("Okey")
                                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent a = new Intent(EditPasswordScreen.this, MainProfileScreen.class);
                                        startActivity(a);
                                        Animatoo.animateSwipeRight(EditPasswordScreen.this);
                                        onStop();
                                    }
                                })
                                .show();
                    }
                });
            }
        });

    }

    private void setOnclickAlert() {
        //SetOnlick Alert Active
        alertPasswordOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertPasswordOld, textalertPasswordOld);
            }
        });
        alertPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertPassword, textalertPassword);
            }
        });
        alertPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertPassword2, textalertPassword2);
            }
        });

    }


    private void getOldSandi() {
        Cursor mod = dataLoginUser.getDataOne();
        mod.moveToFirst();
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            oldPasswordData = mod.getString(mod.getColumnIndexOrThrow("katasandi"));

            mod.moveToNext();
        }
        mod.close();
    }

    private void displayToolTips(ImageView component, String textAlert) {
        // create tooltip
        ToolTip.Builder builder = new ToolTip.Builder(this, component, constraintLayout5, textAlert, ToolTip.POSITION_ABOVE);
        // set align
        builder.setAlign(ToolTip.ALIGN_RIGHT);
        // set background color
        builder.setBackgroundColor(Color.RED);
        // show tooltip
        toolTipsManager.show(builder.build());

    }

    private void setOnlickViewPassword() {

        //showPAssold
        showPassOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showold == 0) {
                    showPassOld.setBackgroundResource(R.drawable.icon_eye_hide);
                    editPasswordOld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showold = 1;
                } else {
                    showPassOld.setBackgroundResource(R.drawable.icon_eye_show);
                    editPasswordOld.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showold = 0;
                }
            }
        });

        //showPAss
        showPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (show1 == 0) {
                    showPass1.setBackgroundResource(R.drawable.icon_eye_hide);
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show1 = 1;
                } else {
                    showPass1.setBackgroundResource(R.drawable.icon_eye_show);
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    show1 = 0;
                }
            }
        });
        showPass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (show2 == 0) {
                    showPass2.setBackgroundResource(R.drawable.icon_eye_hide);
                    editPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show2 = 1;
                } else {
                    showPass2.setBackgroundResource(R.drawable.icon_eye_show);
                    editPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    show2 = 0;
                }
            }
        });


    }

    private void toMenuProfil() {
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(EditPasswordScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Anda Yakin")
                        .setContentText("Tidak Menyimpan Data Ini")
                        .setConfirmText("Iya!")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent a = new Intent(EditPasswordScreen.this, MainProfileScreen.class);
                                startActivity(a);
                                Animatoo.animateSwipeRight(EditPasswordScreen.this);
                                onStop();
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(EditPasswordScreen.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah Anda Yakin")
                .setContentText("Tidak Menyimpan Data Ini")
                .setConfirmText("Iya!")
                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                .setCancelText("Tidak")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent a = new Intent(EditPasswordScreen.this, MainProfileScreen.class);
                        startActivity(a);
                        Animatoo.animateSwipeRight(EditPasswordScreen.this);
                        onStop();
                    }
                })
                .show();
    }

    private void setDefaultForm() {
        //Invisable Danger
        alertPasswordOld.setVisibility(View.INVISIBLE);
        alertPassword.setVisibility(View.INVISIBLE);
        alertPassword2.setVisibility(View.INVISIBLE);

        //Form_Control
        formOldPassword.setBackgroundResource(R.drawable.form_control);
        formPassword.setBackgroundResource(R.drawable.form_control);
        formPassword2.setBackgroundResource(R.drawable.form_control);


    }

    private String convertMD5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void setMode() {
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

            //Main
            linear.setBackgroundResource(R.color.darkMode);
            scrollable.setBackgroundResource(R.color.darkMode);
            constraintLayout5.setBackgroundResource(R.color.darkMode);
            //ATAS
            backgrooundMain.setBackgroundResource(R.color.darkMode2);
            textProfil.setTextColor(getResources().getColor(R.color.white));
            text2.setTextColor(getResources().getColor(R.color.darkTxt));
            back.setBackgroundResource(R.drawable.icon_left_arrow_dark_mode);

            //Bawah
            backgrooundSecond.setBackgroundResource(R.drawable.borders_menu_profile_dark_mode);
//
        } else {
            //main
            linear.setBackgroundResource(R.color.white);
            scrollable.setBackgroundResource(R.color.white);
            constraintLayout5.setBackgroundResource(R.color.white);
            //ATAS
            backgrooundMain.setBackgroundResource(R.color.whitemode2);
            textProfil.setTextColor(getResources().getColor(R.color.darkMode));
            text2.setTextColor(getResources().getColor(R.color.accent));
            back.setBackgroundResource(R.drawable.icon_left_arrow);
            //Bawah
            backgrooundSecond.setBackgroundResource(R.drawable.borders_menu_profile);
        }
    }

    private void  initialize(){
        //Main
        linear = findViewById(R.id.linear);
        scrollable = findViewById(R.id.scrollable);
        constraintLayout5 = findViewById(R.id.constraintLayout5);

        //Bagian Atas
        btnback = findViewById(R.id.btnback);
        textProfil = findViewById(R.id.textProfil);
        text2 = findViewById(R.id.text2);
        backgrooundMain = findViewById(R.id.backgrooundMain);
        back = findViewById(R.id.imageprofile);

        //Bagian Bawah
        backgrooundSecond = findViewById(R.id.backgrooundSecond);
        //FORM EDIT
        editPasswordOld = findViewById(R.id.editPasswordOld);
        editPassword = findViewById(R.id.editPassword);
        editPassword2 = findViewById(R.id.editPassword2);
        //ALERT
        alertPasswordOld = findViewById(R.id.alertPasswordOld);
        alertPassword = findViewById(R.id.alertPassword);
        alertPassword2 = findViewById(R.id.alertPassword2);
        //LAYOUT
        formOldPassword = findViewById(R.id.formOldPassword);
        formPassword = findViewById(R.id.formPassword);
        formPassword2 = findViewById(R.id.formPassword2);
        submit = findViewById(R.id.button);
        //ShowPass
        showPassOld = findViewById(R.id.showPassOld);
        showPass1 = findViewById(R.id.showPass1);
        showPass2 = findViewById(R.id.showPass2);

        //Other
        masterDataCustomer = new MasterDataCustomer();
        masterDataAccountCustomer = new MasterDataAccountCustomer();
        dataMode = new DataMode(this);
        dataLoginUser = new DataLoginUser(this);

        //Sweet Alert
        pDialog = new SweetAlertDialog(EditPasswordScreen.this, SweetAlertDialog.PROGRESS_TYPE);

        // Initialize tooltip manager
        toolTipsManager = new ToolTipsManager();
    }
}