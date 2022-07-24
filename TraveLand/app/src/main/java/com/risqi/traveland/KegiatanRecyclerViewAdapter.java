package com.risqi.traveland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.risqi.traveland.Firebase.DataKegiatan;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class KegiatanRecyclerViewAdapter extends RecyclerView.Adapter<KegiatanRecyclerViewAdapter.NameViewHolder> {
    public class NameViewHolder extends RecyclerView.ViewHolder{
        TextView Judul, LinkImage;
        Button buttonList;
        ImageView Linkimage;
        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            Judul = (TextView)itemView.findViewById(R.id.tvJudulKegiatan);
            Linkimage = (ImageView) itemView.findViewById(R.id.imageView8);
            buttonList = (Button)itemView.findViewById(R.id.btnlistkegiatan) ;
        }

    }
    @NonNull
    @Override
    public KegiatanRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_kegiatan,parent,false);
        return new KegiatanRecyclerViewAdapter.NameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KegiatanRecyclerViewAdapter.NameViewHolder holder, int position) {
        DataKegiatan datakegiatan = dataKegiatan.get(position);
        holder.Judul.setText(datakegiatan.getJudul());

        holder.buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show();
            }
        });
        if(datakegiatan.getLinkImage().equals(""))
        {

        }else
        {
            Picasso.with(context)
                    .load(datakegiatan.getLinkImage())
                    .placeholder(R.drawable.ic_no_image) // optional
                    .error(R.drawable.ic_error_image)         // option
                    .fit()
                    .into(holder.Linkimage);
        }


    }

    @Override
    public int getItemCount() {
        return dataKegiatan.size();
    }
    private Context context;
    private List<DataKegiatan> dataKegiatan;
    public  KegiatanRecyclerViewAdapter(Context context, List<DataKegiatan> dataKegiatan)
    {
        this.context=context;
        this.dataKegiatan=dataKegiatan;

    }


}
