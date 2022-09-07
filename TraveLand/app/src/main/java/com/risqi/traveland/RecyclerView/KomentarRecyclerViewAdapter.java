package com.risqi.traveland.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataMode;

import java.util.ArrayList;
import java.util.List;

public class KomentarRecyclerViewAdapter extends RecyclerView.Adapter<KomentarRecyclerViewAdapter.NameViewHolder> {

    public class NameViewHolder extends RecyclerView.ViewHolder {
        //        ImageView starr;
        RecyclerView recyclerBintang;
        View garis;
        ConstraintLayout bgkomeMitra;
        TextView tanggalKomen,namaUser;

        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerBintang = (RecyclerView) itemView.findViewById(R.id.starData);
            garis = (View) itemView.findViewById(R.id.garis);
            bgkomeMitra = (ConstraintLayout) itemView.findViewById(R.id.bgkomeMitra);
            tanggalKomen = (TextView) itemView.findViewById(R.id.tanggalKomen);
            namaUser = (TextView) itemView.findViewById(R.id.namaUser);

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
//        if(dataBintang.get(position).equals("Fill")){
//            holder.starr.setBackgroundResource(R.drawable.icon_star_primary);
//        }else{
//            holder.starr.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
//        }

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

        bintangRecyclerViewAdapter = new BintangRecyclerViewAdapter(context, fillBintang);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        holder.recyclerBintang.setLayoutManager(layoutManager);
        holder.recyclerBintang.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerBintang.setAdapter(bintangRecyclerViewAdapter);
        fillBintang.clear();

        if(position%2==0){
            holder.bgkomeMitra.setMaxHeight(0);
        }

        if(position==(datakomentar.size()-1)){
            holder.garis.setVisibility(View.GONE);
        }

        for (int i = 1; i <= 5; i++) {
            if(i>4){
                fillBintang.add("NonFill");
            }else{
                fillBintang.add("Fill");
            }

        }
        bintangRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return datakomentar.size();
    }

    private Context context;
    private List<String> datakomentar;

    //MODe
    DataMode dataMode;

    //dataBintang
    private BintangRecyclerViewAdapter bintangRecyclerViewAdapter;
    private List<String> fillBintang = new ArrayList<>();

    public KomentarRecyclerViewAdapter(Context context, List<String> datakomentar) {
        this.context = context;
        this.datakomentar = datakomentar;
        dataMode = new DataMode(context);
    }
}
