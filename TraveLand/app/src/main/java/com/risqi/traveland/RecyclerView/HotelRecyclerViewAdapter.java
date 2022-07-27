package com.risqi.traveland.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.risqi.traveland.Firebase.MasterDataHotel;
import com.risqi.traveland.Firebase.MasterDataWisata;
import com.risqi.traveland.R;

import java.util.List;

public class HotelRecyclerViewAdapter extends RecyclerView.Adapter<HotelRecyclerViewAdapter.NameViewHolder> {
    public class NameViewHolder extends RecyclerView.ViewHolder{
        TextView NamaHotel,AlamatHotel;
        ImageView Fotohotel;
        Button Btnlisthotel;
        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            NamaHotel = (TextView) itemView.findViewById(R.id.namahotel);
            AlamatHotel = (TextView) itemView.findViewById(R.id.alamathotel);
            Fotohotel = (ImageView) itemView.findViewById(R.id.imageViewHotel);
            Btnlisthotel = (Button) itemView.findViewById(R.id.btnlisthotel);

        }
    }
    @NonNull
    @Override
    public HotelRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vw = inflater.inflate(R.layout.item_list_hotel,parent,false);
        return new HotelRecyclerViewAdapter.NameViewHolder(vw);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelRecyclerViewAdapter.NameViewHolder holder, int position) {
        MasterDataHotel masterdatahotel = masterDataHotel.get(position);

        holder.NamaHotel.setText(masterdatahotel.getNamaHotel());
        holder.AlamatHotel.setText(masterdatahotel.getAlamatHotel());
        holder.Btnlisthotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show();
            }
        });
        if(masterdatahotel.getFotoHotel().equals(""))
        {

        }else
        {
            Glide.with(context).clear(holder.Fotohotel);
            Glide.with(context)
                    .load(masterdatahotel.getFotoHotel())
//                    .transform(new MultiTransformation(new FitCenter()))
                    .apply(new RequestOptions()
                            .override(135, 135)
                            .priority(Priority.HIGH)
                            .centerCrop())
                    .into(holder.Fotohotel);

        }
    }

    @Override
    public int getItemCount() {return  masterDataHotel.size();}
    private Context context;
    private List<MasterDataHotel> masterDataHotel;
    public HotelRecyclerViewAdapter(Context context, List<MasterDataHotel> masterDataHotel){
        this.context=context;
        this.masterDataHotel=masterDataHotel;
    }

}
