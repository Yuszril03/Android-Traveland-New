package com.risqi.traveland.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.risqi.traveland.Firebase.MasterDataWisata;
import com.risqi.traveland.R;
import com.risqi.traveland.SQLite.DataMode;
import com.risqi.traveland.WisataScreen.DetailWisataScreen;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WisataRecyclerViewAdapter extends RecyclerView.Adapter<WisataRecyclerViewAdapter.NameViewHolder> {
    public class NameViewHolder extends RecyclerView.ViewHolder {
        TextView JudulWisata, AlamatWisata, hargaParent, bintang;
        Button btnlistwisata;
        ImageView fotowisata, imgChild;
        ConstraintLayout backgrooundMain;

        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            backgrooundMain = (ConstraintLayout) itemView.findViewById(R.id.backgrooundMain);
            JudulWisata = (TextView) itemView.findViewById(R.id.judulWisata);
            AlamatWisata = (TextView) itemView.findViewById(R.id.alamatwisata);
            hargaParent = (TextView) itemView.findViewById(R.id.hargaAnak);
            bintang = (TextView) itemView.findViewById(R.id.bintang);
            fotowisata = (ImageView) itemView.findViewById(R.id.imageViewWisata);
            imgChild = (ImageView) itemView.findViewById(R.id.imgChild);
            btnlistwisata = (Button) itemView.findViewById(R.id.btnlistwisata);
        }
    }

    @NonNull
    @Override
    public WisataRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_wisata, parent, false);
        return new WisataRecyclerViewAdapter.NameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WisataRecyclerViewAdapter.NameViewHolder holder, @SuppressLint("RecyclerView") int position) {

        DataMode dataMode = new DataMode(context);
        //MODE
        Cursor mod = dataMode.getDataOne();
        mod.moveToFirst();
        String modeApps = "";
        while (!mod.isAfterLast()) {
//            Toast.makeText(this, "" + mod.getString(mod.getColumnIndexOrThrow("mode")), Toast.LENGTH_SHORT).show();
            modeApps = mod.getString(mod.getColumnIndexOrThrow("mode"));

            mod.moveToNext();
        }
        mod.close();
        if (modeApps.equals("Malam")) {
            holder.backgrooundMain.setBackgroundResource(R.drawable.background_list_dark);
//            holder. imgChild.setBackgroundResource(R.drawable.icon_person_white);
            holder.JudulWisata.setTextColor(context.getResources().getColor(R.color.white));
            holder.hargaParent.setTextColor(context.getResources().getColor(R.color.white));
            holder.AlamatWisata.setTextColor(context.getResources().getColor(R.color.darkTxt));
        } else {
            holder.backgrooundMain.setBackgroundResource(R.drawable.background_list_white);
//            holder. imgChild.setBackgroundResource(R.drawable.icon_person_dark);
            holder.JudulWisata.setTextColor(context.getResources().getColor(R.color.darkMode));
            holder.hargaParent.setTextColor(context.getResources().getColor(R.color.darkMode));
            holder.AlamatWisata.setTextColor(context.getResources().getColor(R.color.accent));
        }

        MasterDataWisata masterdatawisata = masterDataWisata.get(position);

//        database1 = FirebaseDatabase.getInstance().getReference("Transaction-Wisata");
//        database1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int valueRating1 = 0;
//                int valueRating2 = 0;
//                int valueRating3 = 0;
//                int valueRating4 = 0;
//                int valueRating5 = 0;
//                for (DataSnapshot postData : snapshot.getChildren()) {
//                    TransactionWIisata wisata = postData.getValue(TransactionWIisata.class);
//                    if (wisata.getIdMitra().equals(idmasterDataWisata1.get(position))) {
//                        if(wisata.getStatusTransaksi().equals("4")){
//                            if(!wisata.getRating().equals("")){
//                                if (wisata.getRating().equals("1")) {
//                                    valueRating1++;
//                                } else if (wisata.getRating().equals("2")) {
//                                    valueRating2++;
//                                } else if (wisata.getRating().equals("3")) {
//                                    valueRating3++;
//                                } else if (wisata.getRating().equals("4")) {
//                                    valueRating4++;
//                                } else if (wisata.getRating().equals("5")) {
//                                    valueRating5++;
//                                }
//                            }
//                        }
//
//                    }
//                }
//                int totalRating = ((1 * valueRating1) + (2 * valueRating2) + (3 * valueRating3) + (4 * valueRating4) + (5 * valueRating5));
//                int totalAllRating = (valueRating1 + valueRating2 + valueRating3 + valueRating4 + valueRating5);
//                if (totalRating > 0) {
//                    holder.bintang.setText("" +ratingWisata.get(position)+".0");
//                } else {
//                    holder.bintang.setText("0.0");
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        holder.bintang.setText(ratingWisata.get(position)+".0");
        holder.JudulWisata.setText(wordCase(masterdatawisata.getNamaWisata()));
        holder.hargaParent.setText(formatRupiah(Double.parseDouble(masterdatawisata.getHargaDewasa())) + "/Orang");
        String textTemp = masterdatawisata.getAlamatWisata();
        if (textTemp.length() <= 55) {
            holder.AlamatWisata.setText(wordCase(masterdatawisata.getAlamatWisata()));
        } else {
            String resultAlamat = "";
            String[] arrayAlamat = textTemp.split("");
            for (int i = 0; i < 52; i++) {
                resultAlamat = resultAlamat + "" + arrayAlamat[i];
            }
            holder.AlamatWisata.setText(wordCase(resultAlamat) + "...");
        }

        holder.btnlistwisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(context, DetailWisataScreen.class);
                a.putExtra("idScreen", idmasterDataWisata1.get(position));
                context.startActivity(a);
                Animatoo.animateSlideRight(context);
                ((Activity) context).finish();
            }
        });
        if (masterdatawisata.getFotoWisata().equals("")) {

        } else {
            Glide.with(context).clear(holder.fotowisata);
            Glide.with(context)
                    .load(masterdatawisata.getFotoWisata())
//                    .transform(new MultiTransformation(new FitCenter()))
                    .apply(new RequestOptions()

                            .priority(Priority.HIGH)
                            .centerCrop())
                    .into(holder.fotowisata);

        }
    }

    private String wordCase(String str) {
        String words[] = str.split("\\s");
        String capitalizeWord = "";
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase() + afterfirst + " ";
        }
        return capitalizeWord.trim();
    }


    private String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    public int getItemCount() {
        return masterDataWisata.size();
    }

    private Context context;
    private DatabaseReference database1;
    private List<MasterDataWisata> masterDataWisata;
    private List<String> idmasterDataWisata1;
    private List<Integer> ratingWisata;

    public WisataRecyclerViewAdapter(Context context, List<MasterDataWisata> masterDataWisata, List<String> idmasterDataWisata1,List<Integer> ratingWisata) {
        this.context = context;
        this.masterDataWisata = masterDataWisata;
        this.idmasterDataWisata1 = idmasterDataWisata1;
        this.ratingWisata = ratingWisata;
    }
}
