package com.risqi.traveland.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.risqi.traveland.R;

import java.util.List;

public class TagDataKegiatanRecyclerViewAdapter extends RecyclerView.Adapter<TagDataKegiatanRecyclerViewAdapter.NameViewHolder>{

    public class NameViewHolder extends  RecyclerView.ViewHolder {
        TextView textTag;


        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            textTag = (TextView) itemView.findViewById(R.id.textTag);

        }
    }
    @NonNull
    @Override
    public TagDataKegiatanRecyclerViewAdapter.NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_tag_data,parent,false);
        return new TagDataKegiatanRecyclerViewAdapter.NameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
        holder.textTag.setText(dataTag.get(position));
    }

    @Override
    public int getItemCount() { return  dataTag.size(); }
    private Context context;
    private List<String> dataTag;
    public TagDataKegiatanRecyclerViewAdapter(Context context,List<String> dataTag){
        this.context=context;
        this.dataTag=dataTag;
    }
}
