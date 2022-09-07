package com.risqi.traveland.SettingScreen;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.risqi.traveland.Firebase.MasterDataCustomer;
import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataLoginUser;
import com.risqi.traveland.SQLite.DataMode;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditProfilScreen extends AppCompatActivity {
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

    //Form
    private String[] choiceGender = {"Pilih Jenis Kelamin", "Laki-Laki", "Perempuan"};
    private Spinner spinnerGender;
    private Button cancelTanggal;
    private EditText editTanggalahir, editNik, editNama, editEmail, editTelefon, editAlamat;
    private ImageView alertNik, alertNama, alertEmail, alertTelefon, alertGender, alertTanggalLahir, alertAlamat;
    private String textalertNik, textalertNama, textalertEmail, textalertTelefon, textalertGender, textalertTanggalLahir, textalertAlamat;
    private ConstraintLayout formNIK, formNama, formEmail, formTelefon, formGender, formTanggalahir, formAlamat;
    private ToolTipsManager toolTipsManager;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Button submit;

    ///Other
    private int internet = 0;
    private MasterDataCustomer masterDataCustomer;
    private DataMode dataMode;
    private DataLoginUser dataLoginUser;
    DatabaseReference dataBase1;
    Task updates1,updates2;
    SweetAlertDialog pDialog;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar calendarCreated = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_screen);
        cekKondisi();
        initialize();
        //back Menu PRofile
        backMenu();
        //SetMode
        setMode();
        //SetGender
        setGender();
        //SetTanggal()
        setTanggal();
        //SetDefaultForm
        setDefaultForm();
        ///setOnclick Alert
        setOnclickAlert();
        //setDataForm
        setDataForm();
        //SetUpButton + SetOnlick
        setButtonSubmit();

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
                new SweetAlertDialog(EditProfilScreen.this, SweetAlertDialog.WARNING_TYPE)
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
                                        for (int i = 1; i <= 7; i++) {
                                            if (i == 1) {

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
                                            }
                                        }
                                        if (jumlah == 0) {
                                            saveData();
                                        } else {
                                            pDialog.dismiss();
                                            new SweetAlertDialog(EditProfilScreen.this, SweetAlertDialog.ERROR_TYPE)
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
                    new SweetAlertDialog(EditProfilScreen.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Internet tidak terhubung")
                            .setContentText("Mohon cek kembali konkesi internet")
                            .setConfirmText("Okey")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(getIntent());
                                    Animatoo.animateFade(EditProfilScreen.this);
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

    private void saveData() {
        int gender = 0;
        if (spinnerGender.getSelectedItem().toString().equals("Laki-Laki")) {
            gender = 1;
        }
        HashMap dataCustomerString = masterDataCustomer.updateDataString(editAlamat.getText().toString(),editNama.getText().toString(),editTanggalahir.getText().toString(),editTelefon.getText().toString());
        HashMap dataCustomerInteger = masterDataCustomer.updateDataINT(gender);

        updates1 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Customer").child(editNik.getText().toString()).updateChildren(dataCustomerString);
        int finalGender = gender;
        updates1.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                updates2 = FirebaseDatabase.getInstance().getReference().child("Master-Data-Customer").child(editNik.getText().toString()).updateChildren(dataCustomerInteger);
                updates2.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        pDialog.dismiss();
                        dataLoginUser.updateDataOnlyMaster(editNama.getText().toString(),String.valueOf(finalGender));
                        new SweetAlertDialog(EditProfilScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Berhasil")
                                .setContentText("Data berhasil tersimpan")
                                .setConfirmText("Okey")
                                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent a = new Intent(EditProfilScreen.this, MainProfileScreen.class);
                                        startActivity(a);
                                        Animatoo.animateSwipeRight(EditProfilScreen.this);
                                        onStop();
                                    }
                                })
                                .show();

                    }
                });
            }
        });

    }

    private void setTanggal() {
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
                cancelTanggal.setVisibility(View.VISIBLE);
                DatePickerDialog datePickerDialogs = new DatePickerDialog(EditProfilScreen.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogs.getDatePicker().setMaxDate(calendarCreated.getTimeInMillis());
                datePickerDialogs.show();
            }
        });

        cancelTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTanggalahir.setText("");
                myCalendar.setTime(calendarCreated.getTime());
                cancelTanggal.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void setDataForm() {
        Cursor user = dataLoginUser.getDataOne();
        user.moveToFirst();
        String nik = "";
        while (!user.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            nik = user.getString(user.getColumnIndexOrThrow("nik"));

            user.moveToNext();
        }
        user.close();
        dataBase1 = FirebaseDatabase.getInstance().getReference();
        String finalNik = nik;
        dataBase1.child("Master-Data-Customer").child(nik).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> Customer = (Map<String, Object>) task.getResult().getValue();
                Log.d("ANEHH", "onComplete: " + String.valueOf(Customer));
                editNik.setText(finalNik + "");
                editNama.setText(Customer.get("NamaCustomer").toString());
                editEmail.setText(Customer.get("EmailCustomer").toString());
                editTelefon.setText(Customer.get("TelefonCustomer").toString());
                editTanggalahir.setText(Customer.get("TanggalLahirCustomer").toString());
                editAlamat.setText(Customer.get("AlamatCustomer").toString());
                if (Integer.parseInt(Customer.get("Gender").toString()) == 1) {
                    spinnerGender.setSelection(1);
                } else {
                    spinnerGender.setSelection(2);
                }
                if(Customer.get("TanggalLahirCustomer").toString().equals("")){

                    cancelTanggal.setVisibility(View.INVISIBLE);
                }else{
                    cancelTanggal.setVisibility(View.VISIBLE);
                    String[] splitDate = (Customer.get("TanggalLahirCustomer").toString()).split("-");

                    myCalendar.set(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]));

                }

            }
        });
    }

    private void setOnclickAlert() {
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
    }

    private void setDefaultForm() {
        //Invisable Danger
        alertNik.setVisibility(View.INVISIBLE);
        alertNama.setVisibility(View.INVISIBLE);
        alertEmail.setVisibility(View.INVISIBLE);
        alertTelefon.setVisibility(View.INVISIBLE);
        alertGender.setVisibility(View.INVISIBLE);
        alertTanggalLahir.setVisibility(View.INVISIBLE);
        alertAlamat.setVisibility(View.INVISIBLE);


        //Form_Control
        formNIK.setBackgroundResource(R.drawable.form_control_readonly);
        formNama.setBackgroundResource(R.drawable.form_control);
        formEmail.setBackgroundResource(R.drawable.form_control_readonly);
        formTelefon.setBackgroundResource(R.drawable.form_control);
        formGender.setBackgroundResource(R.drawable.form_control);
        formTanggalahir.setBackgroundResource(R.drawable.form_control);
        formAlamat.setBackgroundResource(R.drawable.form_control);

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
//            TitleSetting.setTextColor(getResources().getColor(R.color.white));
//            TitleMode.setTextColor(getResources().getColor(R.color.white));
//            //Profile
//            textProfile.setTextColor(getResources().getColor(R.color.white));
//            arrowProfile.setBackgroundResource(R.drawable.icon_right_arrow_dark);
//            //KataSandi
//            textKataSandi.setTextColor(getResources().getColor(R.color.white));
//            arrowKataSandi.setBackgroundResource(R.drawable.icon_right_arrow_dark);
//            //Profile
//            textKataRiwayat.setTextColor(getResources().getColor(R.color.white));
//            arrowRiwayat.setBackgroundResource(R.drawable.icon_right_arrow_dark);
//            //Profile
//            textKeluar.setTextColor(getResources().getColor(R.color.white));
//            arrowKeluar.setBackgroundResource(R.drawable.icon_right_arrow_dark);
//            //versi
//            textVersi1.setTextColor(getResources().getColor(R.color.white));
//            textVersi2.setTextColor(getResources().getColor(R.color.white));
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
//            TitleSetting.setTextColor(getResources().getColor(R.color.darkMode));
//            TitleMode.setTextColor(getResources().getColor(R.color.darkMode));

//            //Profile
//            textProfile.setTextColor(getResources().getColor(R.color.darkMode));
//            arrowProfile.setBackgroundResource(R.drawable.icon_right_arrow_light);
//            //KataSandi
//            textKataSandi.setTextColor(getResources().getColor(R.color.darkMode));
//            arrowKataSandi.setBackgroundResource(R.drawable.icon_right_arrow_light);
//            //Profile
//            textKataRiwayat.setTextColor(getResources().getColor(R.color.darkMode));
//            arrowRiwayat.setBackgroundResource(R.drawable.icon_right_arrow_light);
//            //Profile
//            textKeluar.setTextColor(getResources().getColor(R.color.darkMode));
//            arrowKeluar.setBackgroundResource(R.drawable.icon_right_arrow_light);
//            //versi
//            textVersi1.setTextColor(getResources().getColor(R.color.darkMode));
//            textVersi2.setTextColor(getResources().getColor(R.color.darkMode));
        }
    }

    private void backMenu() {
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(EditProfilScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Anda Yakin")
                        .setContentText("Tidak Menyimpan Data Ini")
                        .setConfirmText("Iya!")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent a = new Intent(EditProfilScreen.this, MainProfileScreen.class);
                                startActivity(a);
                                Animatoo.animateSwipeRight(EditProfilScreen.this);
                                onStop();
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(EditProfilScreen.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah Anda Yakin")
                .setContentText("Tidak Menyimpan Data Ini")
                .setConfirmText("Iya!")
                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                .setCancelText("Tidak")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent a = new Intent(EditProfilScreen.this, MainProfileScreen.class);
                        startActivity(a);
                        Animatoo.animateSwipeRight(EditProfilScreen.this);
                        onStop();
                    }
                })
                .show();
    }

    private void initialize() {

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

        //FORM
        //EditTExt
        spinnerGender = findViewById(R.id.editGender);
        editTanggalahir = findViewById(R.id.editTanggalahir);
        editNik = findViewById(R.id.editNik);
        editNama = findViewById(R.id.editNama);
        editEmail = findViewById(R.id.editEmail);
        editTelefon = findViewById(R.id.editTelefon);
        editAlamat = findViewById(R.id.editAlamat);
        //ALERT
        alertNik = findViewById(R.id.alertNik);
        alertNama = findViewById(R.id.alertNama);
        alertEmail = findViewById(R.id.alertEmail);
        alertTelefon = findViewById(R.id.alertTelefon);
        alertGender = findViewById(R.id.alertGender);
        alertTanggalLahir = findViewById(R.id.alertTanggalahir);
        alertAlamat = findViewById(R.id.alertAlamat);
        //LAYOUT
        formNIK = findViewById(R.id.formNIK);
        formNama = findViewById(R.id.formNama);
        formEmail = findViewById(R.id.formEmail);
        formTelefon = findViewById(R.id.formTelefon);
        formGender = findViewById(R.id.formGender);
        formTanggalahir = findViewById(R.id.formTanggalahir);
        formAlamat = findViewById(R.id.formAlamat);
        submit = findViewById(R.id.button);
        cancelTanggal = findViewById(R.id.cancelTanggal);

        //Other
        masterDataCustomer = new MasterDataCustomer();
        dataMode = new DataMode(this);
        dataLoginUser = new DataLoginUser(this);

        //Sweet Alert
        pDialog = new SweetAlertDialog(EditProfilScreen.this, SweetAlertDialog.PROGRESS_TYPE);

        // Initialize tooltip manager
        toolTipsManager = new ToolTipsManager();

    }

    private void setGender() {
        //set Spinner Gender
        ArrayAdapter tempGender = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, choiceGender);
        tempGender.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerGender.setAdapter(tempGender);

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

    private void updateLabel() {
//        String myFormat = "dd/MM/yyyy";
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.UK);
        editTanggalahir.setText(dateFormat.format(myCalendar.getTime()));
    }
}