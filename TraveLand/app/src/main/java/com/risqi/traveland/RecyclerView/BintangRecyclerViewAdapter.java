package com.risqi.traveland.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.risqi.traveland.R;

import java.util.List;

public class BintangRecyclerViewAdapter extends RecyclerView.Adapter<BintangRecyclerViewAdapter.NameViewHolder> {

    public class NameViewHolder extends  RecyclerView.ViewHolder {
        ImageView starr;


        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            starr = (ImageView) itemView.findViewById(R.id.starr);

        }
    }
    @NonNull
    @Override
    public BintangRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_bintang,parent,false);
        return new BintangRecyclerViewAdapter.NameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BintangRecyclerViewAdapter.NameViewHolder holder, int position) {
        if(dataBintang.get(position).equals("Fill")){
            holder.starr.setBackgroundResource(R.drawable.icon_star_primary);
        }else{
            holder.starr.setBackgroundResource(R.drawable.icon_star_nonfill_primary);
        }
    }

    @Override
    public int getItemCount() { return  dataBintang.size(); }
    private Context context;
    private List<String> dataBintang;
    public BintangRecyclerViewAdapter(Context context,List<String> dataBintang){
        this.context=context;
        this.dataBintang=dataBintang;
    }
}
