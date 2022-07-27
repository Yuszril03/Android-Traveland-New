package com.risqi.traveland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.MasterDataAccountCustomer;
import com.risqi.traveland.Firebase.MasterDataCustomer;
import com.risqi.traveland.TempData.TempDataCustomer;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginScreen extends AppCompatActivity {

    //Layout
    private Button buttonLogin,btnDaftar,back;
    private EditText emailText,KataSandiText;
    private ImageView dangerEmail,dangerKataSandi,formEmail, formKataSandi;
    private  ImageButton showPass;

    //Database
    private DatabaseReference databaseReference,databaseReference2;

    private ConstraintLayout layoutData;
    private ToolTipsManager toolTipsManager;

    private String textDangerEmail,textDangerKataSandi;

    private  int ShowHide=0;
    private String activityBefore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        initialize();

        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            activityBefore = bundle.getString("BeforeActivty");

        }else{
            activityBefore = getIntent().getStringExtra("BeforeActivty");

        }

        // Initialize tooltip manager
        toolTipsManager=new ToolTipsManager();
        //Invisable Danger
        dangerEmail.setVisibility(View.INVISIBLE);
        dangerKataSandi.setVisibility(View.INVISIBLE);
        //Form_Control
        formEmail.setBackgroundResource(R.drawable.form_control);
        formKataSandi.setBackgroundResource(R.drawable.form_control);

        buttonLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    buttonLogin.setBackgroundResource(R.drawable.button_white_press);
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    buttonLogin.setBackgroundResource(R.drawable.button_white);
                }
                return false;
            }
        });

        //dafter
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
                startActivity(intent);
                Animatoo.animateSlideRight(LoginScreen.this);
                finish();

            }
        });

        //showPAss
        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(ShowHide==0){
                   showPass.setBackgroundResource(R.drawable.icon_eye_hide);
                   KataSandiText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                   ShowHide=1;
               }else{
                   showPass.setBackgroundResource(R.drawable.icon_eye_show);
                   KataSandiText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                   ShowHide=0;
               }
            }
        });

        //submit
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailData= emailText.getText().toString();
                String katasandiData= (KataSandiText.getText().toString());
                if(TextUtils.isEmpty(emailData) || TextUtils.isEmpty(katasandiData)){

                    if(TextUtils.isEmpty(emailData)){
                        displayToolTips(dangerEmail,"Email Harus Diisi!");
                        textDangerEmail="Email Harus Diisi!";
                        dangerEmail.setVisibility(View.VISIBLE);
                        formEmail.setBackgroundResource(R.drawable.form_control_danger);
                    }else{
                        toolTipsManager.findAndDismiss(dangerEmail);
                        dangerEmail.setVisibility(View.INVISIBLE);
                        formEmail.setBackgroundResource(R.drawable.form_control);
                    }
                    if(TextUtils.isEmpty(katasandiData)){
                        displayToolTips(dangerKataSandi,"Kata Sandi Harus Diisi!");
                        textDangerKataSandi="Kata Sandi Harus Diisi!";
                        dangerKataSandi.setVisibility(View.VISIBLE);
                        formKataSandi.setBackgroundResource(R.drawable.form_control_danger);
                    }else{
                        toolTipsManager.findAndDismiss(dangerKataSandi);
                        dangerKataSandi.setVisibility(View.INVISIBLE);
                        formKataSandi.setBackgroundResource(R.drawable.form_control);
                    }

//                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("Oops...")
//                            .setContentText("Harap kolom harus diisi!")
//                            .setConfirmText("Okey")
//                            .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
//                            .show();
                }else if(validasiEmail(emailData)==false){
                    toolTipsManager.dismissAll();
                    displayToolTips(dangerEmail,"Email Tidak Valid!");
                    textDangerEmail="Email Tidak Valid!";
                    dangerEmail.setVisibility(View.VISIBLE);
                    dangerKataSandi.setVisibility(View.INVISIBLE);

                    formEmail.setBackgroundResource(R.drawable.form_control_danger);
                    formKataSandi.setBackgroundResource(R.drawable.form_control);

//                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("Oops...")
//                            .setContentText("Email Tidak Valid!")
//                            .setConfirmText("Okey")
//                            .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
//                            .show();
                }
                else{
                    toolTipsManager.dismissAll();
                    cekData(emailData,convertMD5(katasandiData));
                }
            }
        });

        dangerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(dangerEmail,textDangerEmail);
            }
        });
        dangerKataSandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(dangerKataSandi,textDangerKataSandi);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activityBefore.equals("Profil")){
                    Intent intent = new Intent(LoginScreen.this, MainProfile.class);
                    startActivity(intent);
                    Animatoo.animateSlideDown(LoginScreen.this);
                    finish();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        if(activityBefore.equals("Profil")){
            Intent intent = new Intent(LoginScreen.this, MainProfile.class);
            startActivity(intent);
            Animatoo.animateSlideDown(LoginScreen.this);
            finish();
        }
    }

    private void cekData(String email, String KataSandi){
        databaseReference = FirebaseDatabase.getInstance().getReference("Master-Data-Customer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cekLuar = 0;
                for (DataSnapshot postData : snapshot.getChildren()){
                    MasterDataCustomer masterDataCustomer = postData.getValue(MasterDataCustomer.class);

                    if(email.equals(masterDataCustomer.getEmailCustomer())){
                        cekLuar = 1;
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("Master-Data-Account-Customer");
                        databaseReference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotAccount) {
                                int cekDalam = 0;
                                for (DataSnapshot postDataAccount :snapshotAccount.getChildren()){
                                    MasterDataAccountCustomer masterDataAccountCustomer = postDataAccount.getValue(MasterDataAccountCustomer.class);
                                    if(postData.getKey().equals(postDataAccount.getKey())){
                                        if(KataSandi.equals(masterDataAccountCustomer.getKataSandi())){
                                            cekDalam =1;
                                        }
                                    }
                                }
                                if(cekDalam==1){
                                    toolTipsManager.dismissAll();
                                    dangerEmail.setVisibility(View.INVISIBLE);
                                    dangerKataSandi.setVisibility(View.INVISIBLE);

                                    formEmail.setBackgroundResource(R.drawable.form_control);
                                    formKataSandi.setBackgroundResource(R.drawable.form_control);
                                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Berhasil!")
                                            .setContentText("Anda berhasil masuk aplikasi!")
                                            .show();
                                }else{
                                    toolTipsManager.dismissAll();
                                    textDangerEmail="Email Salah!";
                                    textDangerKataSandi="Kata Sandi Salah!";
                                    displayToolTips(dangerEmail,"Email Salah!");
                                    displayToolTips(dangerKataSandi,"Kata Sandi Salah!");
                                    dangerEmail.setVisibility(View.VISIBLE);
                                    dangerKataSandi.setVisibility(View.VISIBLE);

                                    formEmail.setBackgroundResource(R.drawable.form_control_danger);
                                    formKataSandi.setBackgroundResource(R.drawable.form_control_danger);

//                                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.ERROR_TYPE)
//                                            .setTitleText("Oops...")
//                                            .setContentText("Email Atau Kata Sandi Tidak Salah!")
//                                            .setConfirmText("Okey")
//                                            .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
//                                            .show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

//

                }
                if(cekLuar==0){
                    toolTipsManager.dismissAll();
                    displayToolTips(dangerEmail,"Email Salah!");
                    textDangerEmail="Email Salah!";
                    textDangerKataSandi="Kata Sandi Salah!";
                    displayToolTips(dangerKataSandi,"Kata Sandi Salah!");
                    dangerEmail.setVisibility(View.VISIBLE);
                    dangerKataSandi.setVisibility(View.VISIBLE);

                    formEmail.setBackgroundResource(R.drawable.form_control_danger);
                    formKataSandi.setBackgroundResource(R.drawable.form_control_danger);
                }else{
                    toolTipsManager.dismissAll();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialize(){
        buttonLogin = findViewById(R.id.button);
        dangerEmail = findViewById(R.id.dangerEmail);
        dangerKataSandi = findViewById(R.id.dangerKatasandi);
        emailText = findViewById(R.id.editTextTextEmailAddress);
        KataSandiText = findViewById(R.id.editTextTextPassword);
        formKataSandi = findViewById(R.id.imageView6);
        formEmail = findViewById(R.id.imageView4);
        btnDaftar = findViewById(R.id.button4);
        layoutData = findViewById(R.id.layoutData);
        showPass= findViewById(R.id.showPass);
        back= findViewById(R.id.button3);
    }

    private boolean validasiEmail(String email){
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private String convertMD5(final String s){
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

    private  void displayToolTips(ImageView component, String textAlert){
        // create tooltip
        ToolTip.Builder builder=new ToolTip.Builder(this,component,layoutData,textAlert,ToolTip.POSITION_ABOVE);
        // set align
        builder.setAlign(ToolTip.ALIGN_RIGHT);
        // set background color
        builder.setBackgroundColor(Color.RED);
        // show tooltip
        toolTipsManager.show(builder.build());

    }

}