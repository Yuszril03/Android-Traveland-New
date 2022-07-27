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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.risqi.traveland.Firebase.MasterDataRental;
import com.risqi.traveland.R;

import java.util.List;

public class RentalRecyclerViewAdapter extends RecyclerView.Adapter<RentalRecyclerViewAdapter.NameViewHolder> {
    public class NameViewHolder extends RecyclerView.ViewHolder{
        TextView NamaRental,AlamatRental;
        ImageView Fotorental;
        Button Btnlistrental;
        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            NamaRental = (TextView) itemView.findViewById(R.id.namarental);
            AlamatRental = (TextView) itemView.findViewById(R.id.alamatrental);
            Fotorental = (ImageView) itemView.findViewById(R.id.imageViewRental);
            Btnlistrental = (Button) itemView.findViewById(R.id.btnlistrental);

        }
    }
    @NonNull
    @Override
    public RentalRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vw1 = inflater.inflate(R.layout.item_list_rental,parent,false);
        return new RentalRecyclerViewAdapter.NameViewHolder(vw1);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalRecyclerViewAdapter.NameViewHolder holder, int position) {
        MasterDataRental masterdatarental = masterDataRental.get(position);

        holder.NamaRental.setText(masterdatarental.getNamaRental());
        holder.AlamatRental.setText(masterdatarental.getAlamatRental());
        holder.Btnlistrental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show();
            }
        });
        if(masterdatarental.getFotoRental().equals(""))
        {

        }else
        {
            Glide.with(context).clear(holder.Fotorental);
            Glide.with(context)
                    .load(masterdatarental.getFotoRental())
//                    .transform(new MultiTransformation(new FitCenter()))
                    .apply(new RequestOptions()
                            .override(135, 135)
                            .priority(Priority.HIGH)
                            .centerCrop())
                    .into(holder.Fotorental);

        }
    }

    @Override
    public int getItemCount() {return  masterDataRental.size();}
    private Context context;
    private List<MasterDataRental> masterDataRental;
    public RentalRecyclerViewAdapter(Context context, List<MasterDataRental> masterDataRental){
        this.context=context;
        this.masterDataRental=masterDataRental;
    }


}
