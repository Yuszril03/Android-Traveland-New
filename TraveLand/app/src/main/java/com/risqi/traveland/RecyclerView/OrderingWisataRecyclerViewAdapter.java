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
import com.risqi.traveland.Firebase.TransactionWIisata;
import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.WisataScreen.DetailTransactionWisataScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderingWisataRecyclerViewAdapter extends RecyclerView.Adapter<OrderingWisataRecyclerViewAdapter.NameViewHolder> {
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
    public OrderingWisataRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_ordering, parent, false);
        return new OrderingWisataRecyclerViewAdapter.NameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderingWisataRecyclerViewAdapter.NameViewHolder holder, @SuppressLint("RecyclerView") int position) {

        TransactionWIisata transactionWIisata = dataWisata.get(position);
        holder.detailJudul.setText(transactionWIisata.getJumlahAnak()+"(Anak) + "+transactionWIisata.getJumlahDewasa()+"(Dewasa)");
        holder.harga.setText("Rp."+transactionWIisata.getHargaAnak()+"("+transactionWIisata.getJumlahAnak()+") + Rp."+transactionWIisata.getHargaDewasa()+"("+transactionWIisata.getJumlahDewasa()+")");
        holder.valueTotal.setText("Rp."+transactionWIisata.getTotalSemua());

        if(transactionWIisata.getStatusTransaksi().equals("1")){
            holder.StatusText.setText("Belum Terbayar");
            holder.buttonSUbmit.setText("Bayar");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionWIisata.getStatusTransaksi().equals("3")){
            holder.StatusText.setText("Sudah Terbayar");
            holder.buttonSUbmit.setText("Scan QR");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionWIisata.getStatusTransaksi().equals("2")){
            holder.StatusText.setText("Dibatalkan");
            holder.buttonSUbmit.setText("Lihat Detail");
            holder.StatusText.setTextColor(context.getResources().getColor(R.color.secondary));
        }else  if(transactionWIisata.getStatusTransaksi().equals("4")){
            if(transactionWIisata.getUlasanCustomer().equals("")){
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
                Intent a = new Intent(context, DetailTransactionWisataScreen.class);
                a.putExtra("idScreen", "" + KeyData.get(position));
                context.startActivity(a);
                Animatoo.animateSlideRight(context);
                ((Activity)context).finish();
            }
        });

        //getData WIsata
        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Wisata").child(transactionWIisata.getIdMitra()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> wisata = (Map<String, Object>) task.getResult().getValue();
                holder.textJudul.setText(wordCase(wisata.get("NamaWisata").toString()));
                Glide.with(context).clear(holder.imageViewWisata);
                Glide.with(context)
                        .load(wisata.get("fotoWisata").toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                        .apply(new RequestOptions()

                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(holder.imageViewWisata);
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
//
//        bintangRecyclerViewAdapter = new BintangRecyclerViewAdapter(context, fillBintang);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//
//        holder.recyclerBintang.setLayoutManager(layoutManager);
//        holder.recyclerBintang.setItemAnimator(new DefaultItemAnimator());
//        holder.recyclerBintang.setAdapter(bintangRecyclerViewAdapter);
//        fillBintang.clear();
//
//        if(position%2==0){
//            holder.bgkomeMitra.setMaxHeight(0);
//        }
//
//        if(position==(datakomentar.size()-1)){
//            holder.garis.setVisibility(View.GONE);
//        }
//
//        for (int i = 1; i <= 5; i++) {
//            if(i>4){
//                fillBintang.add("NonFill");
//            }else{
//                fillBintang.add("Fill");
//            }
//
//        }
//        bintangRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return dataWisata.size();
    }

    private Context context;
    private List<TransactionWIisata> dataWisata;
    private List<String> KeyData;
    private DatabaseReference database1,database2;

    //MODe
    DataMode dataMode;

    //dataBintang
    private BintangRecyclerViewAdapter bintangRecyclerViewAdapter;
    private List<String> fillBintang = new ArrayList<>();
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



    public OrderingWisataRecyclerViewAdapter(Context context, List<TransactionWIisata> dataWisata, List<String> KeyData) {
        this.context = context;
        this.dataWisata = dataWisata;
        this.KeyData = KeyData;
        dataMode = new DataMode(context);
    }
}
