package com.sharnoxz.ambuplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sharnoxz.ambuplus.ExpandActivity;
import com.sharnoxz.ambuplus.R;
import com.sharnoxz.ambuplus.data.HData;

import java.util.ArrayList;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<HData> data;

    public HorizontalRecyclerViewAdapter(Context context, ArrayList<HData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_layout,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.details.setText(data.get(position).getText());
        Glide.with(context).load(data.get(position).getImage()).placeholder(R.drawable.spinner).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExpandActivity.class);
                intent.putExtra("expand",data.get(position).getExpand());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView details;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.details = itemView.findViewById(R.id.contentText);
            this.image = itemView.findViewById(R.id.contentImage);
        }
    }
}
