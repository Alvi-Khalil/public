/*
 * Created by Alvi Khalil on 11/7/18 1:29 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/7/18 1:29 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ServiceScheduleItem;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;


public class ServiceRecyclerAdapter extends RecyclerView.Adapter<ServiceRecyclerAdapter.CustomViewHolder> {
    private List<ServiceScheduleItem> feedItemList;
    private Context mContext;



    public ServiceRecyclerAdapter(Context context, List<ServiceScheduleItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;



    }

    @Override
    public int getItemViewType(int position) {

        ServiceScheduleItem dc_list = feedItemList.get(position);

        if(dc_list.getStatus().equals("UpComing")||dc_list.getStatus().equals("Running"))
        {
            return 2;
        }
        else{
            return 1;
        }




    }

    @Override
    public ServiceRecyclerAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View viewONE = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item_general, parent, false);
                CustomViewHolder rowONE = new CustomViewHolder(viewONE,1);
                return rowONE;

            case 2:
                View viewTWO = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item_up_coming, parent, false);
                CustomViewHolder rowTWO = new CustomViewHolder(viewTWO,2);
                return rowTWO;



        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {


        if(position==0){
            holder.v_up.setVisibility(View.GONE);
            holder.v_down.setVisibility(View.VISIBLE);
            //Toast.makeText(mContext, "first", Toast.LENGTH_SHORT).show();
        }


        if(position==feedItemList.size()-1){
            holder.v_down.setVisibility(View.GONE);
            holder.v_up.setVisibility(View.VISIBLE);

            //Toast.makeText(mContext, "last", Toast.LENGTH_SHORT).show();
        }

        ServiceScheduleItem dc_list = feedItemList.get(position);


        if(dc_list.getStatus().equals("Passed"))
        {
            holder.image.setColorFilter(ContextCompat.getColor(mContext, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.ring.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.v_down.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            holder.v_up.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            holder.header.setTextColor(ContextCompat.getColor(mContext, R.color.grey5));
            holder.status.setTextColor(ContextCompat.getColor(mContext, R.color.grey5));
            holder.date.setTextColor(ContextCompat.getColor(mContext, R.color.grey5));
            holder.milage.setTextColor(ContextCompat.getColor(mContext, R.color.grey5));

        }
        else if(dc_list.getStatus().equals("Missed"))
        {
            holder.image.setColorFilter(ContextCompat.getColor(mContext, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        else if(dc_list.getStatus().equals("UpComing"))
        {
            holder.image.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow), android.graphics.PorterDuff.Mode.MULTIPLY);

            if(position==0){
                holder.v_up.setVisibility(View.GONE);
                holder.v_down.setVisibility(View.VISIBLE);
                //Toast.makeText(mContext, "first", Toast.LENGTH_SHORT).show();
            }


            else if(position==feedItemList.size()-1){
                holder.v_down.setVisibility(View.GONE);
                holder.v_up.setVisibility(View.VISIBLE);

                //Toast.makeText(mContext, "last", Toast.LENGTH_SHORT).show();
            }
            else{
                holder.v_down.setVisibility(View.GONE);
            }
        }
        else if(dc_list.getStatus().equals("Running"))
        {
            holder.image.setColorFilter(ContextCompat.getColor(mContext, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
            if(position==0){
                holder.v_up.setVisibility(View.GONE);
                holder.v_down.setVisibility(View.VISIBLE);
                //Toast.makeText(mContext, "first", Toast.LENGTH_SHORT).show();
            }


            else if(position==feedItemList.size()-1){
                holder.v_down.setVisibility(View.GONE);
                holder.v_up.setVisibility(View.VISIBLE);

                //Toast.makeText(mContext, "last", Toast.LENGTH_SHORT).show();
            }
            else{
                holder.v_down.setVisibility(View.GONE);
            }
        }
        else{
            holder.image.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.ring.setColorFilter(ContextCompat.getColor(mContext, R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.v_down.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey));
            holder.v_up.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey));
            holder.header.setTextColor(ContextCompat.getColor(mContext, R.color.grey3));
            holder.status.setTextColor(ContextCompat.getColor(mContext, R.color.grey3));
            holder.date.setTextColor(ContextCompat.getColor(mContext, R.color.grey3));
            holder.milage.setTextColor(ContextCompat.getColor(mContext, R.color.grey3));

        }


        String s="Status : "+dc_list.getStatus();
        String s2="Date : "+dc_list.getServiceStartDate();
        //holder.image.setImageResource(dc_list.getId());
        holder.header.setText(dc_list.getServiceName());
        holder.status.setText(s);
        holder.date.setText(s2);
        holder.milage.setText(dc_list.getMileage());

    }

    private void setAnimation(FrameLayout container, int position) {
        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
        container.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {



        Button whiteBackGreenSign,whiteBackGreenLog;


        ImageView image,ring;
        TextView header;
        TextView status;
        TextView date;
        TextView milage;
        View v_up,v_down;


        public CustomViewHolder(View itemView,int item) {
            super(itemView);






            image = itemView.findViewById(R.id.light);
            ring = itemView.findViewById(R.id.ring);
            header = itemView.findViewById(R.id.name);
            milage = itemView.findViewById(R.id.milage);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            v_up = itemView.findViewById(R.id.up_view);
            v_down = itemView.findViewById(R.id.down_view);


        }
    }
}
