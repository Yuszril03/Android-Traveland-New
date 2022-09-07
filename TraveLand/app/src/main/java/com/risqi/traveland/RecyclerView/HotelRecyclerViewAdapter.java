package com.risqi.traveland.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.risqi.traveland.HotelScreen.DetailHotelScreen;
import com.risqi.traveland.Firebase.MasterDataHotelDetail;
import com.risqi.traveland.R;

import java.util.List;
import java.util.Map;

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
        MasterDataHotelDetail masterdatahoteldetail = masterDataHotelDetail.get(position);
        getReference = FirebaseDatabase.getInstance().getReference();
        getReference.child("Master-Data-Hotel").child(masterdatahoteldetail.getIdHotel()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String,Object> hotel = (Map<String, Object>) task.getResult().getValue();
                holder.AlamatHotel.setText(""+hotel.get("AlamatHotel"));

            }
        });

        holder.NamaHotel.setText(masterdatahoteldetail.getNamaKamar());
        holder.Btnlisthotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new  Intent(context, DetailHotelScreen.class);
                a.putExtra("idDetail",idmasterDataHotelDetail.get(position));
                a.putExtra("idMaster",masterdatahoteldetail.getIdHotel());
                context.startActivity(a);
                Animatoo.animateFade(context);
                ((Activity)context).finish();
            }
        });
        if(masterdatahoteldetail.getFotoKamar().equals(""))
        {

        }else
        {
            Glide.with(context).clear(holder.Fotohotel);
            Glide.with(context)
                    .load(masterdatahoteldetail.getFotoKamar())
//                    .transform(new MultiTransformation(new FitCenter()))
                    .apply(new RequestOptions()
                            .override(135, 135)
                            .priority(Priority.HIGH)
                            .centerCrop())
                    .into(holder.Fotohotel);

        }
    }

    @Override
    public int getItemCount() {return  masterDataHotelDetail.size();}
    private DatabaseReference getReference;
    private Context context;
    private List<MasterDataHotelDetail> masterDataHotelDetail;
    private List<String> idmasterDataHotelDetail;
    public HotelRecyclerViewAdapter(Context context, List<MasterDataHotelDetail> masterDataHotelDetail, List<String> idmasterDataHotelDetail){
        this.context=context;
        this.masterDataHotelDetail=masterDataHotelDetail;
        this.idmasterDataHotelDetail=idmasterDataHotelDetail;
    }

}
