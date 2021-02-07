package com.sharnoxz.ambuplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sharnoxz.ambuplus.R;
import com.sharnoxz.ambuplus.data.VData;

import java.util.ArrayList;


public class VerticalRecyclerViewAdapter extends RecyclerView.Adapter<VerticalRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<VData> data;

    public VerticalRecyclerViewAdapter(Context context, ArrayList<VData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_layout,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.topicName.setText(data.get(position).getTitle());
        holder.horizontalRecyclerView.setAdapter(new HorizontalRecyclerViewAdapter(context,data.get(position).gethData()));
        holder.horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        holder.horizontalRecyclerView.setHasFixedSize(true);
//        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ExpandContents.class);
//                intent.putExtra("data", data.get(position).gethData());
//                context.startActivity(intent);
//            }
//        });
//        holder.topicExpandButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context,ExpandContents.class);
//                intent.putExtra("data", data.get(position).gethData());
//                context.startActivity(intent);
//            }
//        });

        holder.appDetails.setText(data.get(position).getAppDetails());
        Glide.with(context).load(data.get(position).getAppIcon()).placeholder(R.drawable.spinner).into(holder.appIcon);
        Glide.with(context).load(data.get(position).getStayHome()).placeholder(R.drawable.spinner).into(holder.stayHome);
        Glide.with(context).load(data.get(position).getCoVid()).placeholder(R.drawable.spinner).into(holder.covid);
        holder.weather.setText(data.get(position).getWeather());
        if(data.get(position).isDetailsOfApp()){
            holder.DetailsOfApp.setVisibility(View.VISIBLE);
        }else{
            holder.DetailsOfApp.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RecyclerView horizontalRecyclerView;
        TextView topicName,appDetails,weather;
        ImageView appIcon,stayHome,covid;
        ImageButton topicExpandButton;
        RelativeLayout clickLayout,DetailsOfApp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontalRecyclerView);
            topicName = itemView.findViewById(R.id.topicName);
            topicExpandButton = itemView.findViewById(R.id.topicExpandButton);
            clickLayout = itemView.findViewById(R.id.topicRelativeLayout);
            appDetails = itemView.findViewById(R.id.appDetails);
            weather = itemView.findViewById(R.id.weather);
            appIcon = itemView.findViewById(R.id.appIcon);
            DetailsOfApp= itemView.findViewById(R.id.DetailsOfApp);
            stayHome = itemView.findViewById(R.id.stayHome);
            covid = itemView.findViewById(R.id.covid);
        }
    }
}
