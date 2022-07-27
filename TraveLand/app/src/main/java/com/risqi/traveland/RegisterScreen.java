package com.risqi.traveland;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.MasterDataCustomer;
import com.risqi.traveland.SQLite.DataMode;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterScreen extends AppCompatActivity {

    private Button login, daftar;
    private String[] choiceGender = {"Pilih Jenis Kelamin", "Laki-Laki", "Perempuan"};
    private Spinner spinnerGender;
    private EditText editTanggalahir, editNik, editNama, editEmail, editTelefon, editAlamat, editPassword, editPassword2;
    private ImageView alertNik, alertNama, alertEmail, alertTelefon, alertGender, alertTanggalLahir, alertAlamat, alertPassword, alertPassword2, alertCheckBox;
    private String textalertNik, textalertNama, textalertEmail, textalertTelefon, textalertGender, textalertTanggalLahir, textalertAlamat, textalertPassword, textalertPassword2, textalertCheckBox;
    ImageView showPass1, showPass2;
    private int show1 = 0;
    private int show2 = 0;
    CheckBox checkBox;
    private ConstraintLayout layoutData,layoutUtama, formNIK, formNama, formEmail, formTelefon, formGender, formTanggalahir, formAlamat, formPassword, formPassword2;
    private ToolTipsManager toolTipsManager;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date dateNow, dateInput;

    DatabaseReference dataBase1;
    DatabaseReference dataBase2;
    DatabaseReference dataBase3;
    Task dataBase5;
    SweetAlertDialog pDialog;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar calendarCreated = Calendar.getInstance();

    private DataMode dataMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        initialize();

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
        }else{
            layoutUtama.setBackgroundResource(R.color.white);
        }

//        insertData();
//Sweet Alert
        pDialog = new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.PROGRESS_TYPE);


        // Initialize tooltip manager
        toolTipsManager = new ToolTipsManager();

        //set Spinner Gender
        ArrayAdapter tempGender = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, choiceGender);
        tempGender.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerGender.setAdapter(tempGender);

        //set Datepicker
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        editTanggalahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialogs = new DatePickerDialog(RegisterScreen.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogs.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
                datePickerDialogs.show();
            }
        });

        //Back To Login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(RegisterScreen.this);
                finish();
            }
        });

        //SetOnlick Alert Active
        alertNik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertNik, textalertNik);
            }
        });
        alertNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertNama, textalertNama);
            }
        });
        alertEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertEmail, textalertEmail);
            }
        });
        alertTelefon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertTelefon, textalertTelefon);
            }
        });
        alertGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertGender, textalertGender);
            }
        });
        alertTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertTanggalLahir, textalertTanggalLahir);
            }
        });
        alertAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertAlamat, textalertAlamat);
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
        alertCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToolTips(alertCheckBox, textalertCheckBox);
            }
        });

        daftar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    daftar.setBackgroundResource(R.drawable.button_white_press);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    daftar.setBackgroundResource(R.drawable.button_white);
                }
                return false;
            }
        });

        //Invisable Danger
        alertNik.setVisibility(View.INVISIBLE);
        alertNama.setVisibility(View.INVISIBLE);
        alertEmail.setVisibility(View.INVISIBLE);
        alertTelefon.setVisibility(View.INVISIBLE);
        alertGender.setVisibility(View.INVISIBLE);
        alertTanggalLahir.setVisibility(View.INVISIBLE);
        alertAlamat.setVisibility(View.INVISIBLE);
        alertPassword.setVisibility(View.INVISIBLE);
        alertPassword2.setVisibility(View.INVISIBLE);
        alertCheckBox.setVisibility(View.INVISIBLE);

        //Form_Control
        formNIK.setBackgroundResource(R.drawable.form_control);
        formNama.setBackgroundResource(R.drawable.form_control);
        formEmail.setBackgroundResource(R.drawable.form_control);
        formTelefon.setBackgroundResource(R.drawable.form_control);
        formGender.setBackgroundResource(R.drawable.form_control);
        formTanggalahir.setBackgroundResource(R.drawable.form_control);
        formAlamat.setBackgroundResource(R.drawable.form_control);
        formPassword.setBackgroundResource(R.drawable.form_control);
        formPassword2.setBackgroundResource(R.drawable.form_control);


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

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Anda Yakin?")
                        .setContentText("Menyimpan Data Ini!")
                        .setConfirmText("Iya!")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                        .setCancelText("Tidak")
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
                                        int jumlah = 0;
                                        toolTipsManager.dismissAll();
                                        for (int i = 1; i <= 10; i++) {
                                            if (i == 1) {
                                                if (TextUtils.isEmpty(editNik.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertNik, "NIK Harus Diisi!");
                                                    textalertNik = "NIK Harus Diisi!";
                                                    alertNik.setVisibility(View.VISIBLE);
                                                    formNIK.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if ((editNik.getText().toString()).length() < 16) {
                                                    jumlah++;
                                                    displayToolTips(alertNik, "NIK Kurang Dari 16 Digit!");
                                                    textalertNik = "NIK Kurang Dari 16 Digit!";
                                                    alertNik.setVisibility(View.VISIBLE);
                                                    formNIK.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if ((editNik.getText().toString()).length() > 16) {
                                                    jumlah++;
                                                    displayToolTips(alertNik, "NIK Lebih Dari 16 Digit!");
                                                    textalertNik = "NIK Lebih Dari 16 Digit!";
                                                    alertNik.setVisibility(View.VISIBLE);
                                                    formNIK.setBackgroundResource(R.drawable.form_control_danger);
                                                }else {
                                                    toolTipsManager.findAndDismiss(alertNik);
                                                    alertNik.setVisibility(View.INVISIBLE);
                                                    formNIK.setBackgroundResource(R.drawable.form_control);
                                                }
                                            } else if (i == 2) {
                                                if (TextUtils.isEmpty(editNama.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertNama, "Nama Harus Diisi!");
                                                    textalertNama = "Nama Harus Diisi!";
                                                    alertNama.setVisibility(View.VISIBLE);
                                                    formNama.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertNama);
                                                    alertNama.setVisibility(View.INVISIBLE);
                                                    formNama.setBackgroundResource(R.drawable.form_control);
                                                }
                                            } else if (i == 3) {
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
                                            } else if (i == 4) {
                                                if (TextUtils.isEmpty(editTelefon.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertTelefon, "Nomor Telefon Harus Diisi!");
                                                    textalertTelefon = "Nomor Telefon Harus Diisi!";
                                                    alertTelefon.setVisibility(View.VISIBLE);
                                                    formTelefon.setBackgroundResource(R.drawable.form_control_danger);
                                                } else if ((editTelefon.getText().toString()).length() < 12) {
                                                    jumlah++;
                                                    displayToolTips(alertTelefon, "Nomor Telefon Kurang 12 Digit!");
                                                    textalertTelefon = "Nomor Telefon Kurang 12 Digit!";
                                                    alertTelefon.setVisibility(View.VISIBLE);
                                                    formTelefon.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertTelefon);
                                                    alertTelefon.setVisibility(View.INVISIBLE);
                                                    formTelefon.setBackgroundResource(R.drawable.form_control);
                                                }
                                            } else if (i == 5) {
                                                if (spinnerGender.getSelectedItem().toString().equals("Pilih Jenis Kelamin")) {
                                                    jumlah++;
                                                    displayToolTips(alertGender, "Jenis Kelamin Harus Dipilih!");
                                                    textalertGender = "Jenis Kelamin Harus Dipilih!";
                                                    alertGender.setVisibility(View.VISIBLE);
                                                    formGender.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertGender);
                                                    alertGender.setVisibility(View.INVISIBLE);
                                                    formGender.setBackgroundResource(R.drawable.form_control);
                                                }
                                            } else if (i == 6) {

                                                if (TextUtils.isEmpty(editTanggalahir.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertTanggalLahir, "Tanggal Lahir Harus Dipilih!");
                                                    textalertTanggalLahir = "Tanggal Lahir Harus Dipilih!";
                                                    alertTanggalLahir.setVisibility(View.VISIBLE);
                                                    formTanggalahir.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertTanggalLahir);
                                                    alertTanggalLahir.setVisibility(View.INVISIBLE);
                                                    formTanggalahir.setBackgroundResource(R.drawable.form_control);
                                                }
                                            } else if (i == 7) {
                                                if (TextUtils.isEmpty(editAlamat.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertAlamat, "Alamat Harus Diisi!");
                                                    textalertAlamat = "Alamat Harus Diisi!";
                                                    alertAlamat.setVisibility(View.VISIBLE);
                                                    formAlamat.setBackgroundResource(R.drawable.form_control_danger);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertAlamat);
                                                    alertAlamat.setVisibility(View.INVISIBLE);
                                                    formAlamat.setBackgroundResource(R.drawable.form_control);
                                                }
                                            } else if (i == 8) {
                                                if (TextUtils.isEmpty(editPassword.getText())) {
                                                    jumlah++;
                                                    displayToolTips(alertPassword, "Kata Sandi Harus Diisi!");
                                                    textalertPassword = "Kata Sandi Harus Diisi!";
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
                                            } else if (i == 9) {
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
                                            } else if (i == 10) {
                                                if (checkBox.isChecked() == false) {
                                                    jumlah++;
                                                    displayToolTips(alertCheckBox, "Ketentuan Harus Disetujui!");
                                                    textalertCheckBox = "Ketentuan Harus Disetujui!";
                                                    alertCheckBox.setVisibility(View.VISIBLE);
                                                } else {
                                                    toolTipsManager.findAndDismiss(alertCheckBox);
                                                    alertCheckBox.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        }
                                        if (jumlah == 0) {
                                            checkUser();
                                        } else {
                                            pDialog.dismiss();
                                            new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.ERROR_TYPE)
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
        startActivity(intent);
        Animatoo.animateSlideLeft(RegisterScreen.this);
        finish();
    }

    private void insertData() {
        String dateTime = simpleDateFormat.format(myCalendar.getTime()).toString();
        Map<String, String> insertCustomer = new HashMap<>();
        insertCustomer.put("AlamatCustomer", "Tes");
        insertCustomer.put("EmailCustomer", "Tes@gmail.com");
        insertCustomer.put("Gender", "1");
        insertCustomer.put("NamaCustomer", "Tes@gmail.com");
        insertCustomer.put("StatusCustomer", "1");
        insertCustomer.put("TanggalBuat", dateTime);
        insertCustomer.put("TanggalLahirCustomer", "Tes@gmail.com");
        insertCustomer.put("TanggalUpdate", dateTime);
        insertCustomer.put("TelefonCustomer", "Tes@gmail.com");
        insertCustomer.put("fotoCustomer", "");
        Map<String, String> insertAccount = new HashMap<>();
        insertAccount.put("KataSandi", convertMD5("123456dcd78"));

        HashMap insertCustomerUpdate = new HashMap();
        insertCustomerUpdate.put("Gender", 1);
        insertCustomerUpdate.put("StatusCustomer", 1);
//
//        Toast.makeText(this, "Coba", Toast.LENGTH_SHORT).show();

        dataBase1 = FirebaseDatabase.getInstance().getReference();
        dataBase1.child("Master-Data-Customer").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Map<String, Object> Customer = (Map<String, Object>) task.getResult().getValue();
                    int cekNIK = 0;
                    int cekEmail = 0;
                    for (Map.Entry<String, Object> entry : Customer.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        Map<String, Object> Temps = (Map<String, Object>) entry.getValue();
                        if (key.equals("31313131")) {
                            cekNIK = 1;
                        }
                        if (Temps.get("EmailCustomer").equals("Tes@gmail.com")) {
                            cekEmail = 1;
                        }


                    }
                    if (cekEmail == 0 && cekNIK == 0) {
                        dataBase1 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Customer").child("31313131");
                        dataBase1.setValue(insertCustomer).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dataBase2 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Account-Customer").child("31313131");
                                dataBase2.setValue(insertAccount).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dataBase5 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Customer").child("31313131").updateChildren(insertCustomerUpdate);
                                        dataBase5.addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                Toast.makeText(RegisterScreen.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Oops...")
                                                        .setContentText("Data Gagal Tersimpan!")
                                                        .setConfirmText("Okey")
                                                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                                        .show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Oops...")
                                                .setContentText("Data Gagal Tersimpan!")
                                                .setConfirmText("Okey")
                                                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                                .show();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Data Gagal Tersimpan!")
                                        .setConfirmText("Okey")
                                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                        .show();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterScreen.this, "Jangan", Toast.LENGTH_SHORT).show();
                    }
//
//                    Iterator Loop = Customer.keySet().iterator();
//                    while (Loop.hasNext()) {
//                        Log.d("firebase", (String) Loop.next());
//                    }


                }
            }
        });

    }


    private void checkUser() {
        int gender = 0;
        if (spinnerGender.getSelectedItem().toString().equals("Laki-Laki")) {
            gender = 1;
        }
        String dateTime = simpleDateFormat.format(calendarCreated.getTime()).toString();
        Map<String, String> insertCustomer = new HashMap<>();
        insertCustomer.put("AlamatCustomer", editAlamat.getText().toString());
        insertCustomer.put("EmailCustomer", editEmail.getText().toString());
        insertCustomer.put("Gender", "" + gender);
        insertCustomer.put("NamaCustomer", editNama.getText().toString());
        insertCustomer.put("StatusCustomer", "1");
        insertCustomer.put("TanggalBuat", dateTime);
        insertCustomer.put("TanggalLahirCustomer", editTanggalahir.getText().toString());
        insertCustomer.put("TanggalUpdate", dateTime);
        insertCustomer.put("TelefonCustomer", editTelefon.getText().toString());
        insertCustomer.put("fotoCustomer", "");
        Map<String, String> insertAccount = new HashMap<>();
        insertAccount.put("KataSandi", convertMD5(editPassword.getText().toString()));

        HashMap insertCustomerUpdate = new HashMap();
        insertCustomerUpdate.put("Gender", gender);
        insertCustomerUpdate.put("StatusCustomer", 1);
//
        dataBase1 = FirebaseDatabase.getInstance().getReference();
        dataBase1.child("Master-Data-Customer").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Map<String, Object> Customer = (Map<String, Object>) task.getResult().getValue();
                    int cekNIK = 0;
                    int cekEmail = 0;
                    for (Map.Entry<String, Object> entry : Customer.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        Map<String, Object> Temps = (Map<String, Object>) entry.getValue();
                        if (key.equals(editNik.getText().toString())) {
                            cekNIK = 1;
                        }
                        if (Temps.get("EmailCustomer").equals(editEmail.getText().toString())) {
                            cekEmail = 1;
                        }


                    }
                    if (cekEmail == 0 && cekNIK == 0) {
                        dataBase1 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Customer").child(editNik.getText().toString());
                        dataBase1.setValue(insertCustomer).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dataBase2 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Account-Customer").child(editNik.getText().toString());
                                dataBase2.setValue(insertAccount).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dataBase5 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Customer").child(editNik.getText().toString()).updateChildren(insertCustomerUpdate);
                                        dataBase5.addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Berhasil")
                                                        .setContentText("Data Berhasil Tersimpan")
                                                        .setConfirmText("Okey")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                                Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
                                                                startActivity(intent);
                                                                Animatoo.animateSlideRight(RegisterScreen.this);
                                                                finish();
                                                            }
                                                        })
                                                        .show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pDialog.dismiss();
                                                new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Oops...")
                                                        .setContentText("Data Gagal Tersimpan!")
                                                        .setConfirmText("Okey")
                                                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                                        .show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pDialog.dismiss();
                                        new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Oops...")
                                                .setContentText("Data Gagal Tersimpan!")
                                                .setConfirmText("Okey")
                                                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                                .show();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pDialog.dismiss();
                                new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Data Gagal Tersimpan!")
                                        .setConfirmText("Okey")
                                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                        .show();
                            }
                        });
                    } else {
                        pDialog.dismiss();
                        if (cekEmail == 1) {
                            displayToolTips(alertEmail, "Email Sudah Digunakan!");
                            textalertEmail = "Email Sudah Digunakan!";
                            alertEmail.setVisibility(View.VISIBLE);
                            formEmail.setBackgroundResource(R.drawable.form_control_danger);
                        }
                        if (cekNIK == 1) {
                            displayToolTips(alertNik, "NIK Sudah Digunakan!");
                            textalertNik = "NIK Sudah Digunakan!";
                            alertNik.setVisibility(View.VISIBLE);
                            formNIK.setBackgroundResource(R.drawable.form_control_danger);
                        }

                        new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Kolom Tidak Valid!")
                                .setConfirmText("Okey")
                                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                .show();
                    }
//
//                    Iterator Loop = Customer.keySet().iterator();
//                    while (Loop.hasNext()) {
//                        Log.d("firebase", (String) Loop.next());
//                    }


                }
            }
        });


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


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        editTanggalahir.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void initialize() {
        login = findViewById(R.id.button5);
        daftar = findViewById(R.id.button);
        //FORM
        spinnerGender = findViewById(R.id.editGender);
        editTanggalahir = findViewById(R.id.editTanggalahir);
        editNik = findViewById(R.id.editNik);
        editNama = findViewById(R.id.editNama);
        editEmail = findViewById(R.id.editEmail);
        editTelefon = findViewById(R.id.editTelefon);
        editAlamat = findViewById(R.id.editAlamat);
        editPassword = findViewById(R.id.editPassword);
        editPassword2 = findViewById(R.id.editPassword2);
        checkBox = findViewById(R.id.checkBox);

        //ALERT
        alertNik = findViewById(R.id.alertNik);
        alertNama = findViewById(R.id.alertNama);
        alertEmail = findViewById(R.id.alertEmail);
        alertTelefon = findViewById(R.id.alertTelefon);
        alertGender = findViewById(R.id.alertGender);
        alertTanggalLahir = findViewById(R.id.alertTanggalahir);
        alertAlamat = findViewById(R.id.alertAlamat);
        alertPassword = findViewById(R.id.alertPassword);
        alertPassword2 = findViewById(R.id.alertPassword2);
        alertCheckBox = findViewById(R.id.alertCheckbox);

        //LAYOUT
        layoutData = findViewById(R.id.layoutData);
        formNIK = findViewById(R.id.formNIK);
        formNama = findViewById(R.id.formNama);
        formEmail = findViewById(R.id.formEmail);
        formTelefon = findViewById(R.id.formTelefon);
        formGender = findViewById(R.id.formGender);
        formTanggalahir = findViewById(R.id.formTanggalahir);
        formAlamat = findViewById(R.id.formAlamat);
        formPassword = findViewById(R.id.formPassword);
        formPassword2 = findViewById(R.id.formPassword2);

        //Eyes
        showPass1 = findViewById(R.id.showPass1);
        showPass2 = findViewById(R.id.showPass2);

        //DarkMODE
        layoutUtama= findViewById(R.id.layoutUtama);
        dataMode = new DataMode(this);
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
}