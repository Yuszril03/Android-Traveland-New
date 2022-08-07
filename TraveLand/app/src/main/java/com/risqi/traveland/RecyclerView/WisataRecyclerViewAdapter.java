package com.risqi.traveland.RecyclerView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.risqi.traveland.DetailWisata;
import com.risqi.traveland.Firebase.MasterDataWisata;
import com.risqi.traveland.FirstScreen;
import com.risqi.traveland.MainMenu;
import com.risqi.traveland.MenuRental;
import com.risqi.traveland.MenuWisata;
import com.risqi.traveland.R;
import com.risqi.traveland.SplashScreen;
//import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class WisataRecyclerViewAdapter extends RecyclerView.Adapter<WisataRecyclerViewAdapter.NameViewHolder> {
    public class NameViewHolder extends  RecyclerView.ViewHolder {
        TextView JudulWisata, AlamatWisata;
        Button btnlistwisata;
        ImageView fotowisata;

        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            JudulWisata = (TextView) itemView.findViewById(R.id.judulWisata);
            AlamatWisata = (TextView) itemView.findViewById(R.id.alamatwisata);
            fotowisata = (ImageView) itemView.findViewById(R.id.imageViewWisata);
            btnlistwisata = (Button) itemView.findViewById(R.id.btnlistwisata);
        }
    }

    @NonNull
    @Override
    public WisataRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_wisata,parent,false);
        return new WisataRecyclerViewAdapter.NameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WisataRecyclerViewAdapter.NameViewHolder holder, int position) {
        MasterDataWisata masterdatawisata = masterDataWisata.get(position);

        holder.JudulWisata.setText(masterdatawisata.getNamaWisata());
        holder.AlamatWisata.setText(masterdatawisata.getAlamatWisata());
        holder.btnlistwisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new  Intent(context, DetailWisata.class);
                a.putExtra("id",idmasterDataWisata1.get(position));
                context.startActivity(a);
                Animatoo.animateFade(context);
                ((Activity)context).finish();
            }
        });
        if(masterdatawisata.getFotoWisata().equals(""))
        {

        }else
        {
            Glide.with(context).clear(holder.fotowisata);
            Glide.with(context)
                    .load(masterdatawisata.getFotoWisata())
//                    .transform(new MultiTransformation(new FitCenter()))
                    .apply(new RequestOptions()
                            .override(125, 125)
                            .priority(Priority.HIGH)
                            .centerCrop())
                    .into(holder.fotowisata);

        }
    }

    @Override
    public int getItemCount() { return  masterDataWisata.size(); }
    private Context context;
    private List<MasterDataWisata> masterDataWisata;
    private List<String> idmasterDataWisata1;
    public WisataRecyclerViewAdapter(Context context, List<MasterDataWisata> masterDataWisata,List<String> idmasterDataWisata1){
        this.context=context;
        this.masterDataWisata=masterDataWisata;
        this.idmasterDataWisata1=idmasterDataWisata1;
    }
}
