package com.risqi.traveland.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KomentarRecyclerViewAdapter extends RecyclerView.Adapter<KomentarRecyclerViewAdapter.NameViewHolder> {

    public class NameViewHolder extends RecyclerView.ViewHolder {
        //        ImageView starr;
        RecyclerView recyclerBintang;
        View garis;
        ConstraintLayout bgkomeMitra;
        TextView tanggalKomen,namaUser,komentarUser,KomentarMitra;
        ImageView imageUser;


        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerBintang = (RecyclerView) itemView.findViewById(R.id.starData);
            garis = (View) itemView.findViewById(R.id.garis);
            bgkomeMitra = (ConstraintLayout) itemView.findViewById(R.id.bgkomeMitra);
            imageUser = (ImageView) itemView.findViewById(R.id.imageUser);
            tanggalKomen = (TextView) itemView.findViewById(R.id.tanggalKomen);
            namaUser = (TextView) itemView.findViewById(R.id.namaUser);
            komentarUser = (TextView) itemView.findViewById(R.id.komentarUser);
            KomentarMitra = (TextView) itemView.findViewById(R.id.KomentarMitra);

        }
    }

    @NonNull
    @Override
    public KomentarRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_komentar, parent, false);
        return new KomentarRecyclerViewAdapter.NameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KomentarRecyclerViewAdapter.NameViewHolder holder, int position) {

        TransactionWIisata transactionWIisatas = datakomentar.get(position);

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
            holder.namaUser.setTextColor(context.getResources().getColor(R.color.white));
            holder.tanggalKomen.setTextColor(context.getResources().getColor(R.color.darkTxt));
        }else{
            holder.namaUser.setTextColor(context.getResources().getColor(R.color.darkMode));
            holder.tanggalKomen.setTextColor(context.getResources().getColor(R.color.accent));
        }

        database1 = FirebaseDatabase.getInstance().getReference();
        database1.child("Master-Data-Customer").child(transactionWIisatas.getIdCutomer()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Object> customer = (Map<String, Object>) task.getResult().getValue();
                holder.namaUser.setText(customer.get("NamaCustomer").toString());
                if(customer.get("fotoCustomer").equals("")){

                }else {
                    Glide.with(context).clear(holder.imageUser);
                    Glide.with(context)
                            .load(customer.get("fotoCustomer").toString())
//                    .transform(new MultiTransformation(new FitCenter()))
                            .apply(new RequestOptions()

                                    .priority(Priority.HIGH)
                                    .centerCrop())
                            .into(holder.imageUser);
                }
            }
        });



        bintangRecyclerViewAdapter = new BintangRecyclerViewAdapter(context, fillBintang);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        holder.recyclerBintang.setLayoutManager(layoutManager);
        holder.recyclerBintang.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerBintang.setAdapter(bintangRecyclerViewAdapter);
        fillBintang.clear();

        int rate =0;
        if(!transactionWIisatas.getRating().equals("")){
            rate = Integer.parseInt(transactionWIisatas.getRating());
        }

        for (int i = 1; i <= 5; i++) {
            if(i>rate){
                fillBintang.add("NonFill");
            }else{
                fillBintang.add("Fill");
            }

        }
        bintangRecyclerViewAdapter.notifyDataSetChanged();

        if(transactionWIisatas.getUlasanCustomer().equals("")){
            holder.komentarUser.setText("Tidak ada ulasan.");
        }else{
            holder.komentarUser.setText(transactionWIisatas.getUlasanCustomer().toString());
        }
        holder.KomentarMitra.setText(transactionWIisatas.getUlasanMitra().toString());
        if(transactionWIisatas.getUlasanMitra().equals("")){
//            holder.bgkomeMitra.setMaxHeight(0);
            holder.bgkomeMitra.setVisibility(View.GONE);
        }else{
            holder.bgkomeMitra.setVisibility(View.VISIBLE);
        }

        if(position==(datakomentar.size()-1)){
            holder.garis.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return datakomentar.size();
    }

    private Context context;
    private List<TransactionWIisata> datakomentar;
    private DatabaseReference database1;

    //MODe
    DataMode dataMode;

    //dataBintang
    private BintangRecyclerViewAdapter bintangRecyclerViewAdapter;
    private List<String> fillBintang = new ArrayList<>();

    public KomentarRecyclerViewAdapter(Context context, List<TransactionWIisata> datakomentar) {
        this.context = context;
        this.datakomentar = datakomentar;
        dataMode = new DataMode(context);
    }
}
