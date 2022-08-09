package com.risqi.traveland.RecyclerView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.risqi.traveland.DetailKegiatanScreen;
import com.risqi.traveland.Firebase.DataKegiatan;
import com.risqi.traveland.R;
//import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class KegiatanRecyclerViewAdapter extends RecyclerView.Adapter<KegiatanRecyclerViewAdapter.NameViewHolder> {
    public class NameViewHolder extends RecyclerView.ViewHolder{
        TextView Judul, tanggalBuat,jenisKegiatan;
        Button buttonList;
        ImageView Linkimage;
        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            Judul = (TextView)itemView.findViewById(R.id.tvJudulKegiatan);
            jenisKegiatan = (TextView)itemView.findViewById(R.id.jenisKegiatan);
            tanggalBuat = (TextView)itemView.findViewById(R.id.textView13);
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
    public void onBindViewHolder(@NonNull KegiatanRecyclerViewAdapter.NameViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DataKegiatan datakegiatan = dataKegiatan.get(position);

        holder.Judul.setText(wordCase(datakegiatan.getJudul()));
        holder.jenisKegiatan.setText(datakegiatan.getJenisKegiatan());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat postFormater = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        try {
            Date dateObj = simpleDateFormat.parse(datakegiatan.getTanggalMulai());
            holder.tanggalBuat.setText(postFormater.format(dateObj));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new  Intent(context, DetailKegiatanScreen.class);
                a.putExtra("id",iddataKegiatan.get(position));
                context.startActivity(a);
                Animatoo.animateFade(context);
                ((Activity)context).finish();
            }
        });
        if(datakegiatan.getLinkImage().equals(""))
        {

        }else
        {
            Glide.with(context).clear(holder.Linkimage);
            Glide.with(context)
                    .load(datakegiatan.getLinkImage())
//                    .transform(new MultiTransformation(new FitCenter()))
                    .apply(new RequestOptions()
                            .override(194, 112)
                            .priority(Priority.HIGH)
                            .centerCrop())
                    .into(holder.Linkimage);

//            Picasso.with(context)
//                    .load(datakegiatan.getLinkImage()).resize(600, 350)
//                    .placeholder(R.drawable.ic_no_image) // optional
//                    .error(R.drawable.ic_error_image)         // option
////                    .fit()
//                    .transform(new RoundedCornersTransformation(10,10))
//                    .into(holder.Linkimage);
        }


    }

    @Override
    public int getItemCount() {
        return dataKegiatan.size();
    }
    private String wordCase(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }
    private Context context;
    private List<DataKegiatan> dataKegiatan;
    private List<String> iddataKegiatan;
    public  KegiatanRecyclerViewAdapter(Context context, List<DataKegiatan> dataKegiatan, List<String> iddataKegiatan)
    {
        this.context=context;
        this.dataKegiatan=dataKegiatan;
        this.iddataKegiatan=iddataKegiatan;
    }


}
