package com.risqi.traveland.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.risqi.traveland.Firebase.MasterDataRentalDetail;
import com.risqi.traveland.R;
import com.risqi.traveland.RentalScreen.DetailRentalScreen;
import com.risqi.traveland.SQLite.DataMode;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RentalRecyclerViewAdapter extends RecyclerView.Adapter<RentalRecyclerViewAdapter.NameViewHolder> {
    public class NameViewHolder extends RecyclerView.ViewHolder{
        TextView JudulWisata, AlamatWisata, hargaParent, bintang;
        Button btnlistwisata;
        ImageView fotowisata, imgChild;
        ConstraintLayout backgrooundMain;
        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            backgrooundMain = (ConstraintLayout) itemView.findViewById(R.id.backgrooundMain);
            JudulWisata = (TextView) itemView.findViewById(R.id.judulWisata);
            AlamatWisata = (TextView) itemView.findViewById(R.id.alamatwisata);
            hargaParent = (TextView) itemView.findViewById(R.id.hargaAnak);
            bintang = (TextView) itemView.findViewById(R.id.bintang);
            fotowisata = (ImageView) itemView.findViewById(R.id.imageViewWisata);
            imgChild = (ImageView) itemView.findViewById(R.id.imgChild);
            btnlistwisata = (Button) itemView.findViewById(R.id.btnlistwisata);

        }
    }
    @NonNull
    @Override
    public RentalRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vw1 = inflater.inflate(R.layout.item_list_rental,parent,false);
        return new RentalRecyclerViewAdapter.NameViewHolder(vw1);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalRecyclerViewAdapter.NameViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MasterDataRentalDetail masterdatarentaldetail = masterDataRentalDetail.get(position);

        DataMode dataMode = new DataMode(context);
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
            holder.backgrooundMain.setBackgroundResource(R.drawable.background_list_dark);
//            holder. imgChild.setBackgroundResource(R.drawable.icon_person_white);
            holder.JudulWisata.setTextColor(context.getResources().getColor(R.color.white));
            holder.hargaParent.setTextColor(context.getResources().getColor(R.color.white));
            holder.AlamatWisata.setTextColor(context.getResources().getColor(R.color.darkTxt));
        } else {
            holder.backgrooundMain.setBackgroundResource(R.drawable.background_list_white);
//            holder. imgChild.setBackgroundResource(R.drawable.icon_person_dark);
            holder.JudulWisata.setTextColor(context.getResources().getColor(R.color.darkMode));
            holder.hargaParent.setTextColor(context.getResources().getColor(R.color.darkMode));
            holder.AlamatWisata.setTextColor(context.getResources().getColor(R.color.accent));
        }

         getReference = FirebaseDatabase.getInstance().getReference();
         getReference.child("Master-Data-Rental").child(masterdatarentaldetail.getIdRental()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 Map<String,Object> rental = (Map<String, Object>) task.getResult().getValue();
                 String textTemp = rental.get("AlamatRental").toString();

                 String judul = masterdatarentaldetail.getNamaKendaraan() + " - " + rental.get("NamaRental");
                 if (judul.length() < 23) {
                     holder.JudulWisata.setText(wordCase(judul));
                     if (textTemp.length() <= 55) {
                         holder.AlamatWisata.setText(wordCase(textTemp));
                     } else {
                         String resultAlamat = "";
                         String[] arrayAlamat = textTemp.split("");
                         for (int i = 0; i < 52; i++) {
                             resultAlamat = resultAlamat + "" + arrayAlamat[i];
                         }
                         holder.AlamatWisata.setText(wordCase(resultAlamat) + "...");
                     }
                 }else{
                     String resultJudul = "";
                     String[] arrayJudul = judul.split("");
                     for (int a = 0; a < 23; a++) {
                         resultJudul = resultJudul + "" + arrayJudul[a];
                     }
                     holder.JudulWisata.setText(wordCase(resultJudul) + "...");
                     if (textTemp.length() <= 35) {
                         holder.AlamatWisata.setText(wordCase(textTemp));
                     } else {
                         String resultAlamat = "";
                         String[] arrayAlamat = textTemp.split("");
                         for (int i = 0; i < 32; i++) {
                             resultAlamat = resultAlamat + "" + arrayAlamat[i];
                         }
                         holder.AlamatWisata.setText(wordCase(resultAlamat) + "...");
                     }
                 }
             }
         });
//
        holder.btnlistwisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(context, DetailRentalScreen.class);
                a.putExtra("idDetail",idmasterDataRentalDetail.get(position));
                a.putExtra("idMaster",masterdatarentaldetail.getIdRental());
                context.startActivity(a);
                Animatoo.animateSlideRight(context);
                ((Activity)context).finish();
            }
        });

        holder.hargaParent.setText(formatRupiah(Double.parseDouble(masterdatarentaldetail.getHargaSewa().toString()))+"/Hari");

        if(masterdatarentaldetail.getFotoKendaraan().equals(""))
        {

        }else
        {
            Glide.with(context).clear(holder.fotowisata);
            Glide.with(context)
                    .load(masterdatarentaldetail.getFotoKendaraan())
//                    .transform(new MultiTransformation(new FitCenter()))
                    .apply(new RequestOptions()

                            .priority(Priority.HIGH)
                            .centerCrop())
                    .into(holder.fotowisata);

        }
    }

    private String wordCase(String str) {
        String words[] = str.split("\\s");
        String capitalizeWord = "";
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase() + afterfirst + " ";
        }
        return capitalizeWord.trim();
    }


    private String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    public int getItemCount() {return  masterDataRentalDetail.size();}
    private DatabaseReference getReference;
    private Context context;
    private List<MasterDataRentalDetail> masterDataRentalDetail;
    private List<String> idmasterDataRentalDetail;
    public RentalRecyclerViewAdapter(Context context, List<MasterDataRentalDetail> masterDataRentalDetail, List<String> idmasterDataRentalDetail){
        this.context=context;
        this.masterDataRentalDetail=masterDataRentalDetail;
        this.idmasterDataRentalDetail=idmasterDataRentalDetail;
    }


}
