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
import com.risqi.traveland.Firebase.TransactionHotel;
import com.risqi.traveland.HotelScreen.DetailTransactionHotelScreen;
import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderingHotelRecyclerViewAdapter  extends RecyclerView.Adapter<OrderingHotelRecyclerViewAdapter.NameViewHolder>{
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
    public OrderingHotelRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_ordering, parent, false);
        return new OrderingHotelRecyclerViewAdapter.NameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderingHotelRecyclerViewAdapter.NameViewHolder holder, @SuppressLint("RecyclerView") int position) {

        TransactionHotel transactionHotel = datorder.get(position);

        holder.detailJudul.setText(transactionHotel.getJumlahKamar()+" Kamar + "+transactionHotel.getJumlahHari()+" Hari");
        holder.harga.setText("Rp."+transactionHotel.getHargaKamar()+"/Malam");
        holder.valueTotal.setText("Rp."+transactionHotel.getTotalSemua());

        if(transactionHotel.getStatusTransaksi().equals("1")){
            holder.StatusText.setText("Belum Terbayar");
            holder.buttonSUbmit.setText("Bayar");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionHotel.getStatusTransaksi().equals("3")){
            holder.StatusText.setText("Sudah Terbayar");
            holder.buttonSUbmit.setText("Scan QR");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionHotel.getStatusTransaksi().equals("4")){
            holder.StatusText.setText("Sudah Check-In");
            holder.buttonSUbmit.setText("Scan QR");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionHotel.getStatusTransaksi().equals("2")){
            holder.StatusText.setText("Dibatalkan");
            holder.buttonSUbmit.setText("Lihat Detail");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionHotel.getStatusTransaksi().equals("5")){
            if(transactionHotel.getUlasanCustomer().equals("")){
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
                Intent a = new Intent(context, DetailTransactionHotelScreen.class);
                a.putExtra("idScreen", "" + keyData.get(position));
                context.startActivity(a);
                Animatoo.animateSlideRight(context);
                ((Activity)context).finish();
            }
        });

        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Hotel-Detail").child(transactionHotel.getIdKamar()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> taskKamar) {
                Map<String, Object> detailKamar = (Map<String, Object>) taskKamar.getResult().getValue();
                Glide.with(context).clear(holder.imageViewWisata);
                Glide.with(context)
                        .load(detailKamar.get("fotoKamar").toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()

                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(holder.imageViewWisata);

                database2 = FirebaseDatabase.getInstance().getReference();
                database2.child("Master-Data-Hotel").child(transactionHotel.getIdMitra()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> taskHotel) {
                        Map<String, Object> hotel = (Map<String, Object>) taskHotel.getResult().getValue();
                        holder.textJudul.setText(wordCase(detailKamar.get("NamaKamar").toString()+" - "+hotel.get("NamaHotel").toString()));
                    }
                });
            }
        });

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
            holder.textTotal.setTextColor(context.getResources().getColor(R.color.darkTxt));
            holder.detailJudul.setTextColor(context.getResources().getColor(R.color.darkTxt));
            holder.textJudul.setTextColor(context.getResources().getColor(R.color.white));
            holder.layoutREcycle.setBackgroundResource(R.drawable.background_dark_order);
            holder.garis2.setBackgroundResource(R.color.colorGarisDark);
            holder.garis1.setBackgroundResource(R.color.colorGarisDark);
        }


    }

    @Override
    public int getItemCount() {
        return datorder.size();
    }

    private Context context;
    private List<TransactionHotel> datorder;
    private List<String> keyData;
    private DatabaseReference database1,database2;

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

    //MODe
    DataMode dataMode;

    //dataBintang
    private BintangRecyclerViewAdapter bintangRecyclerViewAdapter;
    private List<String> fillBintang = new ArrayList<>();

    public OrderingHotelRecyclerViewAdapter(Context context, List<TransactionHotel> datorder,List<String> keyData) {
        this.context = context;
        this.datorder = datorder;
        this.keyData = keyData;
        dataMode = new DataMode(context);
    }
}
