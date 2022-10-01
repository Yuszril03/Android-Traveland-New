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
import com.risqi.traveland.Firebase.TransactionRental;
import com.risqi.traveland.R;
import com.risqi.traveland.RentalScreen.DetailTransactionRentalScreen;
import com.risqi.traveland.SQLite.DataMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderingRentalRecyclerViewAdapter  extends RecyclerView.Adapter<OrderingRentalRecyclerViewAdapter.NameViewHolder>{
    public class NameViewHolder extends RecyclerView.ViewHolder {
        View garis2,garis1;
        TextView textTotal,detailJudul,textJudul,harga,valueTotal,StatusText;
        ConstraintLayout layoutREcycle;
        ImageView imageViewWisata;
        Button buttonSUbmit;

        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            garis1 = (View) itemView.findViewById(R.id.garis1);
            garis2 = (View) itemView.findViewById(R.id.garis2);
            layoutREcycle = (ConstraintLayout) itemView.findViewById(R.id.layoutREcycle);
            textTotal = (TextView) itemView.findViewById(R.id.textTotal);
            detailJudul = (TextView) itemView.findViewById(R.id.detailJudul);
            textJudul = (TextView) itemView.findViewById(R.id.textJudul);
            harga = (TextView) itemView.findViewById(R.id.harga);
            valueTotal = (TextView) itemView.findViewById(R.id.valueTotal);
            StatusText = (TextView) itemView.findViewById(R.id.StatusText);
            buttonSUbmit = (Button) itemView.findViewById(R.id.buttonSUbmit);
            imageViewWisata = (ImageView) itemView.findViewById(R.id.imageViewWisata);

        }
    }

    @NonNull
    @Override
    public OrderingRentalRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_ordering, parent, false);
        return new OrderingRentalRecyclerViewAdapter.NameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderingRentalRecyclerViewAdapter.NameViewHolder holder, @SuppressLint("RecyclerView") int position) {

        TransactionRental transactionRental = dataRental.get(position);

        holder.detailJudul.setText("Peminjaman "+transactionRental.getJumlahHari()+" Hari");
        holder.harga.setText("Rp."+transactionRental.getHargaMobil()+"/Hari");
        holder.valueTotal.setText("Rp."+transactionRental.getTotalSemua());

        if(transactionRental.getStatusTransaksi().equals("1")){
            holder.StatusText.setText("Belum Terbayar");
            holder.buttonSUbmit.setText("Bayar");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionRental.getStatusTransaksi().equals("3")){
            holder.StatusText.setText("Sudah Terbayar");
            holder.buttonSUbmit.setText("Scan QR");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionRental.getStatusTransaksi().equals("4")){
            holder.StatusText.setText("Sudah Diambil");
            holder.buttonSUbmit.setText("Scan QR");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionRental.getStatusTransaksi().equals("2")){
            holder.StatusText.setText("Dibatalkan");
            holder.buttonSUbmit.setText("Lihat Detail");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionRental.getStatusTransaksi().equals("5")){
            if(transactionRental.getUlasanCustomer().equals("")){
                holder.StatusText.setText("Belum dinilai");
                holder.buttonSUbmit.setText("Penilaian");
            }else{
                holder.StatusText.setText("Selesai");
                holder.buttonSUbmit.setText("Lihat Penilaian");
            }

            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }

        holder.buttonSUbmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(context, DetailTransactionRentalScreen.class);
                a.putExtra("idScreen", "" + keyData.get(position));
                context.startActivity(a);
                Animatoo.animateSlideRight(context);
                ((Activity)context).finish();
            }
        });

        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Rental-Detail").child(transactionRental.getIdMobil()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> taskKamar) {
                Map<String, Object> detailMobil = (Map<String, Object>) taskKamar.getResult().getValue();
                Glide.with(context).clear(holder.imageViewWisata);
                Glide.with(context)
                        .load(detailMobil.get("fotoKendaraan").toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()

                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(holder.imageViewWisata);

                database2 = FirebaseDatabase.getInstance().getReference();
                database2.child("Master-Data-Rental").child(transactionRental.getIdMitra()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> taskHotel) {
                        Map<String, Object> rental = (Map<String, Object>) taskHotel.getResult().getValue();
                        holder.textJudul.setText(wordCase(detailMobil.get("NamaKendaraan").toString()+" - "+rental.get("NamaRental").toString()));
                    }
                });
            }
        });

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
            holder.textTotal.setTextColor(context.getResources().getColor(R.color.darkTxt));
            holder.detailJudul.setTextColor(context.getResources().getColor(R.color.darkTxt));
            holder.textJudul.setTextColor(context.getResources().getColor(R.color.white));
            holder.layoutREcycle.setBackgroundResource(R.drawable.background_dark_order);
            holder.garis2.setBackgroundResource(R.color.colorGarisDark);
            holder.garis1.setBackgroundResource(R.color.colorGarisDark);
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

    @Override
    public int getItemCount() {
        return dataRental.size();
    }

    private Context context;
    private List<TransactionRental> dataRental;
    private List<String> keyData;
    private DatabaseReference database1,database2;

    //MODe
    DataMode dataMode;

    //dataBintang
    private BintangRecyclerViewAdapter bintangRecyclerViewAdapter;
    private List<String> fillBintang = new ArrayList<>();

    public OrderingRentalRecyclerViewAdapter(Context context, List<TransactionRental> dataRental,List<String> keyData) {
        this.context = context;
        this.dataRental = dataRental;
        this.keyData = keyData;
        dataMode = new DataMode(context);
    }
}
