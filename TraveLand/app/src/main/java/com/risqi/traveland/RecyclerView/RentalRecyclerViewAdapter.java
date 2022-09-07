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
import com.risqi.traveland.RentalScreen.DetailRentalScreen;
import com.risqi.traveland.Firebase.MasterDataRentalDetail;
import com.risqi.traveland.R;

import java.util.List;
import java.util.Map;

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
        MasterDataRentalDetail masterdatarentaldetail = masterDataRentalDetail.get(position);
         getReference = FirebaseDatabase.getInstance().getReference();
         getReference.child("Master-Data-Rental").child(masterdatarentaldetail.getIdRental()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 Map<String,Object> rental = (Map<String, Object>) task.getResult().getValue();
                 holder.AlamatRental.setText(""+rental.get("AlamatRental"));
             }
         });

        holder.NamaRental.setText(masterdatarentaldetail.getNamaKendaraan());
        holder.Btnlistrental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(context, DetailRentalScreen.class);
                a.putExtra("idDetail",idmasterDataRentalDetail.get(position));
                a.putExtra("idMaster",masterdatarentaldetail.getIdRental());
                context.startActivity(a);
                Animatoo.animateFade(context);
                ((Activity)context).finish();
            }
        });
        if(masterdatarentaldetail.getFotoKendaraan().equals(""))
        {

        }else
        {
            Glide.with(context).clear(holder.Fotorental);
            Glide.with(context)
                    .load(masterdatarentaldetail.getFotoKendaraan())
//                    .transform(new MultiTransformation(new FitCenter()))
                    .apply(new RequestOptions()
                            .override(135, 135)
                            .priority(Priority.HIGH)
                            .centerCrop())
                    .into(holder.Fotorental);

        }
    }

    @Override
    public int getItemCount() {return  masterDataRentalDetail.size();}
    private DatabaseReference getReference;
    private Context context;
    private List<MasterDataRentalDetail> masterDataRentalDetail;
    private List<String> idmasterDataRentalDetail;
    public RentalRecyclerViewAdapter(Context context, List<MasterDataRentalDetail> masterDataRentalDetail, List<String> idmasterDataRentalDetail){
        this.context=context;
        this.masterDataRentalDetail=masterDataRentalDetail;
        this.idmasterDataRentalDetail=idmasterDataRentalDetail;
    }


}
