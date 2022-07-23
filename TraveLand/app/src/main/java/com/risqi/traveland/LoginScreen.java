package com.risqi.traveland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.MasterDataAccountCustomer;
import com.risqi.traveland.Firebase.MasterDataCustomer;
import com.risqi.traveland.TempData.TempDataCustomer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginScreen extends AppCompatActivity {

    //Layout
    private Button buttonLogin;
    private EditText emailText,KataSandiText;
    private ImageView dangerEmail,dangerKataSandi,formEmail, formKataSandi;

    //Database
    private DatabaseReference databaseReference,databaseReference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        initialize();
//        Log.d("COBA", "onCreate: "+convertMD5("12345678"));
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
                    buttonLogin.setBackgroundResource(R.drawable.button_primary_pressed);
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    buttonLogin.setBackgroundResource(R.drawable.button_primary);
                }
                return false;
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
                        dangerEmail.setVisibility(View.VISIBLE);
                        formEmail.setBackgroundResource(R.drawable.form_control_danger);
                    }else{
                        dangerEmail.setVisibility(View.INVISIBLE);
                        formEmail.setBackgroundResource(R.drawable.form_control);
                    }
                    if(TextUtils.isEmpty(katasandiData)){
                        dangerKataSandi.setVisibility(View.VISIBLE);
                        formKataSandi.setBackgroundResource(R.drawable.form_control_danger);
                    }else{
                        dangerKataSandi.setVisibility(View.INVISIBLE);
                        formKataSandi.setBackgroundResource(R.drawable.form_control);
                    }

                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Harap kolom harus diisi!")
                            .setConfirmText("Okey")
                            .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                            .show();
                }else if(validasiEmail(emailData)==false){
                    dangerEmail.setVisibility(View.VISIBLE);
                    dangerKataSandi.setVisibility(View.INVISIBLE);

                    formEmail.setBackgroundResource(R.drawable.form_control_danger);
                    formKataSandi.setBackgroundResource(R.drawable.form_control);

                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Email Tidak Valid!")
                            .setConfirmText("Okey")
                            .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                            .show();
                }
                else{
                    cekData(emailData,convertMD5(katasandiData));
                }
            }
        });

    }

    private void cekData(String email, String KataSandi){
        databaseReference = FirebaseDatabase.getInstance().getReference("Master-Data-Customer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int[] cekLuar = {0};
                for (DataSnapshot postData : snapshot.getChildren()){
                    MasterDataCustomer masterDataCustomer = postData.getValue(MasterDataCustomer.class);

                    if(email.equals(masterDataCustomer.getEmailCustomer())){
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
                                    dangerEmail.setVisibility(View.INVISIBLE);
                                    dangerKataSandi.setVisibility(View.INVISIBLE);

                                    formEmail.setBackgroundResource(R.drawable.form_control);
                                    formKataSandi.setBackgroundResource(R.drawable.form_control);
                                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Berhasil!")
                                            .setContentText("Anda berhasil masuk aplikasi!")
                                            .show();
                                }else{
                                    dangerEmail.setVisibility(View.VISIBLE);
                                    dangerKataSandi.setVisibility(View.VISIBLE);

                                    formEmail.setBackgroundResource(R.drawable.form_control_danger);
                                    formKataSandi.setBackgroundResource(R.drawable.form_control_danger);

                                    new SweetAlertDialog(LoginScreen.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Email Atau Kata Sandi Tidak Salah!")
                                            .setConfirmText("Okey")
                                            .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                            .show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
//

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

}