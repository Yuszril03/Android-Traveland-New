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
import com.risqi.traveland.TempData.TempDataInformation;

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
        TempDataInformation information = dataInformations.get(position);

        holder.informasi.setText(information.getNamaInformasi());
    }

    @Override
    public int getItemCount() {
        return dataInformations.size();
    }
    public class NameViewHolder extends RecyclerView.ViewHolder{
        TextView informasi;
//        Button buttonList;
//        ImageView Linkimage;
        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            informasi = (TextView)itemView.findViewById(R.id.textView12);
        }

    }
    private Context context;
    private List<TempDataInformation> dataInformations;
    public  PemberitahuanRecyclerViewAdapter(Context context,  List<TempDataInformation> dataInformations)
    {
        this.context=context;
        this.dataInformations = dataInformations;
    }
}
