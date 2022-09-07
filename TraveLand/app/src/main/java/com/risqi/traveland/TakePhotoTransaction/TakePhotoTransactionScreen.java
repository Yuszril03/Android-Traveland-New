package com.risqi.traveland.TakePhotoTransaction;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.risqi.traveland.Firebase.TransactionWIisata;
import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.WisataScreen.DetailTransactionWisataScreen;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TakePhotoTransactionScreen extends AppCompatActivity {

    //Layout Utama
    private ConstraintLayout layoutUtamaFirst;

    //Top
    private Button backtoscroll;
    private TextView judulScreen;

    //Payment
    private TextView rekening, judulBank, total;

    //Take
    private ConstraintLayout bgTakePhoto, bgCancel;
    private ImageView takeImage, imageFoto;
    private TextView texttakeImage;

    //Cara Pembayaran
    private TextView textTutor, judulCara;

    //Buutoon SUbmit
    private Button buttonSUbmit;

    //Other
    private StorageReference mStorageRef;
    private TransactionWIisata transactionWIisata;
    private DatabaseReference database1, database2;
    private Task databaseupdate;
    private DataMode dataMode;
    private String idWisata = "";
    private String jenisScreen = "";
    private Uri image_uri, image_null;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo_transaction_screen);
        getDataInten();
        initialize();

        //SetMode
        setMode();

        //setData
        setData();

        //TakeImage
        setTakeImage(0);
        setOnclickImage();
        setCancelImage();

        //ToBack
        toBack();
        //to Submit
        toSubmit();


    }

    private void toSubmit() {
        if (jenisScreen.equals("Wisata")) {
            buttonSUbmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SweetAlertDialog(TakePhotoTransactionScreen.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Apakah Anda Yakin")
                            .setContentText("Untuk menyimpan bukti transaksi!")
                            .setConfirmText("Iya!")
                            .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                            .setCancelText("Tidak")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                    pDialog.setTitleText("Memuat Data...");
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (takeImage.getVisibility() == View.VISIBLE) {
                                                pDialog.dismiss();
                                                new SweetAlertDialog(TakePhotoTransactionScreen.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Opps")
                                                        .setContentText("Mohon untuk unggah bukti pembayaran")
                                                        .setConfirmText("Okey")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismissWithAnimation();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                FileuploaderWisata();
                                            }
                                        }
                                    }, 2000);

                                }
                            }).show();
                }
            });
        }
    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileuploaderWisata() {
        String getNama = System.currentTimeMillis() + "." + getExtension(image_uri);
        mStorageRef = FirebaseStorage.getInstance().getReference("transaksi-wisata");
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
//                        loginUser.updateDataFoto(uri.toString());
//                        String dateTime = simpleDateFormat.format(calendarCreated.getTime()).toString();
//                        textImage.setText(uri.toString());
//                        textDateTime.setText(dateTime);


                        HashMap updatesData = transactionWIisata.updateBuktiPembayaran("3", uri.toString());
                        databaseupdate = FirebaseDatabase.getInstance().getReference().child("Transaction-Wisata").child(idWisata).updateChildren(updatesData);
                        databaseupdate.addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                pDialog.dismiss();
                                new SweetAlertDialog(TakePhotoTransactionScreen.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Berhasil")
                                        .setContentText("Berhasil mengunggah bukti pembayaran")
                                        .setConfirmText("Okey")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                                Intent a = new Intent(TakePhotoTransactionScreen.this, DetailTransactionWisataScreen.class);
                                                a.putExtra("idScreen", idWisata);
                                                startActivity(a);
                                                Animatoo.animateSlideDown(TakePhotoTransactionScreen.this);
                                                onStop();
                                            }
                                        })
                                        .show();
                            }
                        });

                    }
                });
            }
        });
    }

    private void setData() {
        if (jenisScreen.equals("Wisata")) {
            database1 = FirebaseDatabase.getInstance().getReference();
            database1.child("Transaction-Wisata").child(idWisata).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Map<String, Object> transaksiWisata = (Map<String, Object>) task.getResult().getValue();
                    setDataBank(transaksiWisata.get("JenisPembayaran").toString());
                    total.setText("Rp." + transaksiWisata.get("TotalSemua").toString());

                }
            });
        }

    }

    private void setDataBank(String jenisPembayaran) {
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Bank").child(jenisPembayaran).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> dataBank = (Map<String, Object>) task.getResult().getValue();
                judulBank.setText(dataBank.get("NamaBank").toString());
                rekening.setText(dataBank.get("RekeningBank").toString());
                textTutor.setText(Html.fromHtml(dataBank.get("CaraPembayaran").toString()));
            }
        });
    }

    private void setCancelImage() {
        bgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_uri = image_null;
                imageFoto.setImageURI(image_uri);
                setTakeImage(0);
            }
        });
    }

    private void setOnclickImage() {
        bgTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (takeImage.getVisibility() == View.VISIBLE) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            image_uri = data.getData();
            imageFoto.setImageURI(image_uri);
            setTakeImage(1);
        }
    }

    private void toBack() {
        backtoscroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(TakePhotoTransactionScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah Anda Yakin")
                        .setContentText("Tidak menyimpan bukti transaksi!")
                        .setConfirmText("Iya!")
                        .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if (jenisScreen.equals("Wisata")) {
                                    Intent a = new Intent(TakePhotoTransactionScreen.this, DetailTransactionWisataScreen.class);
                                    a.putExtra("idScreen", idWisata);
                                    startActivity(a);
                                    Animatoo.animateSlideDown(TakePhotoTransactionScreen.this);
                                    onStop();
                                }
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(TakePhotoTransactionScreen.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah Anda Yakin")
                .setContentText("Tidak menyimpan bukti transaksi!")
                .setConfirmText("Iya!")
                .setConfirmButtonBackgroundColor(Color.parseColor("#008EFF"))
                .setCancelText("Tidak")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (jenisScreen.equals("Wisata")) {
                            Intent a = new Intent(TakePhotoTransactionScreen.this, DetailTransactionWisataScreen.class);
                            a.putExtra("idScreen", idWisata);
                            startActivity(a);
                            Animatoo.animateSlideDown(TakePhotoTransactionScreen.this);
                            onStop();
                        }

                    }
                }).show();
    }

    private void setTakeImage(int i) {
        if (i == 0) {
            bgCancel.setVisibility(View.GONE);
            imageFoto.setVisibility(View.GONE);
            takeImage.setVisibility(View.VISIBLE);
            texttakeImage.setVisibility(View.VISIBLE);
        } else {
            bgCancel.setVisibility(View.VISIBLE);
            imageFoto.setVisibility(View.VISIBLE);
            takeImage.setVisibility(View.GONE);
            texttakeImage.setVisibility(View.GONE);
        }
    }

    private void getDataInten() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idWisata = bundle.getString("idScreen");
            jenisScreen = bundle.getString("jenisScreen");
        } else {
            idWisata = getIntent().getStringExtra("idScreen");
            jenisScreen = getIntent().getStringExtra("jenisScreen");
        }
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
            judulScreen.setTextColor(getResources().getColor(R.color.white));
            textTutor.setTextColor(getResources().getColor(R.color.white));
            judulCara.setTextColor(getResources().getColor(R.color.white));
            texttakeImage.setTextColor(getResources().getColor(R.color.darkTxt));
            layoutUtamaFirst.setBackgroundResource(R.color.darkMode);
            bgTakePhoto.setBackgroundResource(R.drawable.background_border_take_photo_dark);
            takeImage.setBackgroundResource(R.drawable.icon_camera_dark);
            backtoscroll.setBackgroundResource(R.drawable.icon_cancel);

        }
    }

    private void initialize() {

        //Layout Utama
        layoutUtamaFirst = findViewById(R.id.layoutUtamaFirst);

        //Top
        backtoscroll = findViewById(R.id.backtoscroll);
        judulScreen = findViewById(R.id.judulScreen);

        //Payment
        rekening = findViewById(R.id.rekening);
        judulBank = findViewById(R.id.judulBank);
        total = findViewById(R.id.total);

        //Take
        bgTakePhoto = findViewById(R.id.bgTakePhoto);
        bgCancel = findViewById(R.id.bgCancel);
        takeImage = findViewById(R.id.takeImage);
        imageFoto = findViewById(R.id.imageFoto);
        texttakeImage = findViewById(R.id.texttakeImage);

        //Cara Pembayaran
        textTutor = findViewById(R.id.textTutor);
        judulCara = findViewById(R.id.judulCara);

        //Submit
        buttonSUbmit = findViewById(R.id.buttonSUbmit);

        //Other
        dataMode = new DataMode(this);
        transactionWIisata = new TransactionWIisata();
        pDialog = new SweetAlertDialog(TakePhotoTransactionScreen.this, SweetAlertDialog.PROGRESS_TYPE);
    }
}