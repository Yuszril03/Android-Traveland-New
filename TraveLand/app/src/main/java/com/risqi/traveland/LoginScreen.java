package com.risqi.traveland;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginScreen extends AppCompatActivity {

    Button buttonLogin;
    EditText emailText,KataSandiText;
    ImageView dangerEmail,dangerKataSandi,formEmail, formKataSandi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        initialize();
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
                String katasandiData= KataSandiText.getText().toString();

                if(TextUtils.isEmpty(emailData) || TextUtils.isEmpty(katasandiData)){
                    dangerEmail.setVisibility(View.VISIBLE);
                    dangerKataSandi.setVisibility(View.VISIBLE);

                    formEmail.setBackgroundResource(R.drawable.form_control_danger);
                    formKataSandi.setBackgroundResource(R.drawable.form_control_danger);

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
                }else{
                    dangerEmail.setVisibility(View.INVISIBLE);
                    dangerKataSandi.setVisibility(View.INVISIBLE);

                    formEmail.setBackgroundResource(R.drawable.form_control);
                    formKataSandi.setBackgroundResource(R.drawable.form_control);
                }
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

}