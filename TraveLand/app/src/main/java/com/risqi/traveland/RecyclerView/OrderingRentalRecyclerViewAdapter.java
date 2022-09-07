package com.risqi.traveland.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataMode;

import java.util.ArrayList;
import java.util.List;

public class OrderingRentalRecyclerViewAdapter  extends RecyclerView.Adapter<OrderingRentalRecyclerViewAdapter.NameViewHolder>{
    public class NameViewHolder extends RecyclerView.ViewHolder {
        View garis2,garis1;
        TextView textTotal,detailJudul,textJudul;
        ConstraintLayout layoutREcycle;

        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            garis1 = (View) itemView.findViewById(R.id.garis1);
            garis2 = (View) itemView.findViewById(R.id.garis2);
            layoutREcycle = (ConstraintLayout) itemView.findViewById(R.id.layoutREcycle);
            textTotal = (TextView) itemView.findViewById(R.id.textTotal);
            detailJudul = (TextView) itemView.findViewById(R.id.detailJudul);
            textJudul = (TextView) itemView.findViewById(R.id.textJudul);

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
    public void onBindViewHolder(@NonNull OrderingRentalRecyclerViewAdapter.NameViewHolder holder, int position) {
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
        return datakomentar.size();
    }

    private Context context;
    private List<String> datakomentar;

    //MODe
    DataMode dataMode;

    //dataBintang
    private BintangRecyclerViewAdapter bintangRecyclerViewAdapter;
    private List<String> fillBintang = new ArrayList<>();

    public OrderingRentalRecyclerViewAdapter(Context context, List<String> datakomentar) {
        this.context = context;
        this.datakomentar = datakomentar;
        dataMode = new DataMode(context);
    }
}
