package com.risqi.traveland.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.risqi.traveland.Firebase.DataKegiatan;
import com.risqi.traveland.R;

import java.util.List;

public class PemberitahuanRecyclerViewAdapter extends RecyclerView.Adapter<PemberitahuanRecyclerViewAdapter.NameViewHolder>{
    @NonNull
    @Override
    public PemberitahuanRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_pemberitahuan,parent,false);
        return new PemberitahuanRecyclerViewAdapter.NameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PemberitahuanRecyclerViewAdapter.NameViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }
    public class NameViewHolder extends RecyclerView.ViewHolder{
//        TextView Judul, tanggalBuat;
//        Button buttonList;
//        ImageView Linkimage;
        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
//            Judul = (TextView)itemView.findViewById(R.id.tvJudulKegiatan);
//            tanggalBuat = (TextView)itemView.findViewById(R.id.textView13);
//            Linkimage = (ImageView) itemView.findViewById(R.id.imageView8);
//            buttonList = (Button)itemView.findViewById(R.id.btnlistkegiatan) ;
        }

    }
    private Context context;
    public  PemberitahuanRecyclerViewAdapter(Context context)
    {
        this.context=context;
    }
}
