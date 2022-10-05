package com.risqi.traveland.SettingScreen;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.MasterDataAccountCustomer;
import com.risqi.traveland.Firebase.MasterDataCustomer;
import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataMode;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPasswordScreen extends AppCompatActivity {

    private ConstraintLayout layoutUtama,formEmail, formPassNew, formPasConfrm,layoutData;
    private EditText editEmail,editPassNew,editPasConfrm;
    private ImageView alertEmail,alertPassNew, alertPassConfrm;
    private String textalertPAssNew, textalertPassConfrm, textalertEmail;
    ImageView showPass1, showPass2;
    private Button forgotSubmit,login;
    private int show1 = 0;
    private int show2 = 0;

    private ToolTipsManager toolTipsManager;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date dateNow, dateInput;

    DatabaseReference dataBase1;
    DatabaseReference dataBase2;
    DatabaseReference dataBase3;
    Task dataBase5;
    SweetAlertDialog pDialog;


    private DataMode dataMode;
    private String activityBefore, idDetail;
    private MasterDataAccountCustomer masterDataAccountCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);
        getDatInten();
        initialize();
        setMode();
        setALertForm();
        setDefaultFOrm();
        showHidePass();
        toSubmit();
        toLogin();

    }

    private boolean validasiEmail(String email) {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotPasswordScreen.this, LoginScreen.class);
        intent.putExtra("Before", activityBefore);
        intent.putExtra("idScreen", idDetail);
        startActivity(intent);
        Animatoo.animateSlideLeft(ForgotPasswordScreen.this);
        onStop();
    }

    private void getDatInten() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idDetail = bundle.getString("idScreen");
            activityBefore = bundle.getString("Before");
        } else {
            idDetail = getIntent().getStringExtra("idScreen");
            activityBefore = getIntent().getStringExtra("Before");
        }
    }

    private void toLogin() {
        //Back To Login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordScreen.this, LoginScreen.class);
                intent.putExtra("Before", activityBefore);
                intent.putExtra("idScreen", idDetail);
                startActivity(intent);
                Animatoo.animateSlideLeft(ForgotPasswordScreen.this);
                onStop();
            }
        });
    }

    private void toSubmit() {

        forgotSubmit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    forgotSubmit.setBackgroundResource(R.drawable.button_white_press);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    forgotSubmit.setBackgroundResource(R.drawable.button_white);
                }
                return false;
            }
        });

        forgotSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(ForgotPasswordScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Anda Yakin?")
                        .setContentText("Mereset Kata Sandi Ini!")
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
                                        for(int i=1;i<=3;i++){
                                            if(i==1){
                                                if (TextUtils.isEmpty(editEmail.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertEmail, "Email Harus Diisi!");
                                                    textalertEmail = "Email Harus Diisi!";
                                                    alertEmail.setVisibility(View.VISIBLE);
                                                    formEmail.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if (validasiEmail(editEmail.getText().toString()) == false) {
                                                    jumlah++;
                                                    displayToolTips(alertEmail, "Email tidak valid!");
                                                    textalertEmail = "Email tidak valid!";
                                                    alertEmail.setVisibility(View.VISIBLE);
                                                    formEmail.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertEmail);
                                                    alertEmail.setVisibility(View.INVISIBLE);
                                                    formEmail.setBackgroundResource(R.drawable.form_control);
                                                }
                                            }else if (i == 2) {
                                                if (TextUtils.isEmpty(editPassNew.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertPassNew, "Kata Sandi Harus Diisi!");
                                                    textalertPAssNew = "Kata Sandi Harus Diisi!";
                                                    alertPassNew.setVisibility(View.VISIBLE);
                                                    formPassNew.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if ((editPassNew.getText().toString()).length() < 8) {
                                                    jumlah++;
                                                    displayToolTips(alertPassNew, "Kata Sandi Kurang 8 Karakter!");
                                                    textalertPAssNew = "Kata Sandi Kurang 8 Karakter!";
                                                    alertPassNew.setVisibility(View.VISIBLE);
                                                    formPassNew.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertPassNew);
                                                    alertPassNew.setVisibility(View.INVISIBLE);
                                                    formPassNew.setBackgroundResource(R.drawable.form_control);
                                                }
                                            } else if (i == 3) {
                                                if (TextUtils.isEmpty(editPasConfrm.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertPassConfrm, "Konfirmasi Kata Sandi Harus Diisi!");
                                                    textalertPassConfrm = "Konfirmasi Kata Sandi Harus Diisi!";
                                                    alertPassConfrm.setVisibility(View.VISIBLE);
                                                    formPasConfrm.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if ((editPasConfrm.getText().toString()).length() < 8) {
                                                    jumlah++;
                                                    displayToolTips(alertPassConfrm, "Konfirmasi Kata Sandi Kurang 8 Karakter!");
                                                    textalertPassConfrm = "Konfirmasi Kata Sandi Kurang 8 Karakter!";
                                                    alertPassConfrm.setVisibility(View.VISIBLE);
                                                    formPasConfrm.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if (editPasConfrm.getText().toString().equals(editPassNew.getText().toString()) == false) {
                                                    jumlah++;
                                                    displayToolTips(alertPassConfrm, "Konfirmasi Kata Sandi Tidak Sama!");
                                                    textalertPassConfrm = "Konfirmasi Kata Sandi Tidak Sama!";
                                                    alertPassConfrm.setVisibility(View.VISIBLE);
                                                    formPasConfrm.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertPassConfrm);
                                                    alertPassConfrm.setVisibility(View.INVISIBLE);
                                                    formPasConfrm.setBackgroundResource(R.drawable.form_control);
                                                }
                                            }
                                        }
                                        if (jumlah == 0) {
                                            checkUser();
                                        } else {
                                            pDialog.dismiss();
                                            new SweetAlertDialog(ForgotPasswordScreen.this, SweetAlertDialog.ERROR_TYPE)
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

    private void checkUser() {
        dataBase1 = FirebaseDatabase.getInstance().getReference("Master-Data-Customer");
        dataBase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                String idKey="";
                for(DataSnapshot postData : snapshot.getChildren()){
                    MasterDataCustomer dataCustomer = postData.getValue(MasterDataCustomer.class);
                    if(dataCustomer.getEmailCustomer().equals(editEmail.getText().toString())){
                        count=1;
                        idKey=postData.getKey();
                    }
                }
                if(count==0){
                    pDialog.dismiss();

                    displayToolTips(alertEmail, "Email Tidak Terdaftar!");
                    textalertEmail = "Email Tidak Terdaftar!";
                    alertEmail.setVisibility(View.VISIBLE);
                    formEmail.setBackgroundResource(R.drawable.form_control_danger);

                    new SweetAlertDialog(ForgotPasswordScreen.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Email Tidak Terdaftar!")
                            .setConfirmText("Okey")
                            .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                            .show();
                }else{
                    HashMap updatee= masterDataAccountCustomer.ChangePassword(convertMD5(editPassNew.getText().toString()));
                    dataBase5 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Account-Customer").child(idKey).updateChildren(updatee);
                    dataBase5.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            new SweetAlertDialog(ForgotPasswordScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Berhasil")
                                    .setContentText("Kata Sandi Berhasil Diperbarui!")
                                    .setConfirmText("Iya!")
                                    .showCancelButton(false)
                                    .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            pDialog.dismiss();
                                            Intent intent = new Intent(ForgotPasswordScreen.this, LoginScreen.class);
                                            intent.putExtra("Before", activityBefore);
                                            intent.putExtra("idScreen", idDetail);
                                            startActivity(intent);
                                            Animatoo.animateSlideLeft(ForgotPasswordScreen.this);
                                            onStop();
                                        }
                                    })
                                    .show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showHidePass() {//showPAss
        showPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (show1 == 0) {
                    showPass1.setBackgroundResource(R.drawable.icon_eye_hide);
                    editPassNew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show1 = 1;
                } else {
                    showPass1.setBackgroundResource(R.drawable.icon_eye_show);
                    editPassNew.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    show1 = 0;
                }
            }
        });
        showPass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (show2 == 0) {
                    showPass2.setBackgroundResource(R.drawable.icon_eye_hide);
                    editPasConfrm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    show2 = 1;
                } else {
                    showPass2.setBackgroundResource(R.drawable.icon_eye_show);
                    editPasConfrm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    show2 = 0;
                }
            }
        });

    }

    private void setDefaultFOrm() {
        //Invisable Danger
        alertPassNew.setVisibility(View.INVISIBLE);
        alertPassConfrm.setVisibility(View.INVISIBLE);
        alertEmail.setVisibility(View.INVISIBLE);

        //Form_Control
        formPassNew.setBackgroundResource(R.drawable.form_control);
        formPasConfrm.setBackgroundResource(R.drawable.form_control);
        formEmail.setBackgroundResource(R.drawable.form_control);
    }

    private void setALertForm() {
        alertEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertEmail, textalertEmail);
            }
        });
        alertPassNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertPassNew, textalertPAssNew);
            }
        });
        alertPassConfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertPassConfrm, textalertPassConfrm);
            }
        });
    }
    private void displayToolTips(ImageView component, String textAlert) {
        // create tooltip
        ToolTip.Builder builder = new ToolTip.Builder(this, component, layoutData, textAlert, ToolTip.POSITION_ABOVE);
        // set align
        builder.setAlign(ToolTip.ALIGN_RIGHT);
        // set background color
        builder.setBackgroundColor(Color.RED);
        // show tooltip
        toolTipsManager.show(builder.build());

    }
    private void setMode() {
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
            layoutUtama.setBackgroundResource(R.color.darkMode);
        }
    }

    private void initialize() {
        //Layout
        layoutUtama = findViewById(R.id.layoutUtama);
        layoutData = findViewById(R.id.layoutData);
        formEmail = findViewById(R.id.formEmail);
        formPassNew = findViewById(R.id.formPassword);
        formPasConfrm = findViewById(R.id.formPassword2);

        //EditText
        editEmail = findViewById(R.id.editEmail);
        editPassNew = findViewById(R.id.editPassword);
        editPasConfrm = findViewById(R.id.editPassword2);

//        ALERT
        alertEmail = findViewById(R.id.alertEmail);
        alertPassNew = findViewById(R.id.alertPassword);
        alertPassConfrm = findViewById(R.id.alertPassword2);

        //Eyes
        showPass1 = findViewById(R.id.showPass1);
        showPass2 = findViewById(R.id.showPass2);
        forgotSubmit = findViewById(R.id.button);
        login = findViewById(R.id.button5);

        //Sweet Alert
        pDialog = new SweetAlertDialog(ForgotPasswordScreen.this, SweetAlertDialog.PROGRESS_TYPE);

        // Initialize tooltip manager
        toolTipsManager = new ToolTipsManager();

        dataMode = new DataMode(this);
        masterDataAccountCustomer = new MasterDataAccountCustomer();
    }
}