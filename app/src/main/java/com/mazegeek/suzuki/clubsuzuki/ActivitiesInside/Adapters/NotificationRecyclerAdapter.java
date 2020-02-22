/*
 * Created by Alvi Khalil on 11/2/18 2:49 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 12:42 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.NotificationDetails;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.OfferDetails;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Notifications;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.ALL_NOTIFICATION;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.EXTRA_PREVIOUS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.LATEST_NOTIFICATION;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.OFFERS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.CustomViewHolder> {
    private List<Notifications> feedItemList;
    private Context mContext;
    private String NotiType;
   



    public NotificationRecyclerAdapter(Context context, List<Notifications> feedItemList, String NotiType) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.NotiType = NotiType;



    }

    @Override
    public int getItemViewType(int position) {
        final Notifications dc_list = feedItemList.get(position);

        String string=dc_list.getImage();

        if(NotiType.equals(LATEST_NOTIFICATION)){
            return 1;
        }
        else if(NotiType.equals(ALL_NOTIFICATION)){
            if(string.equals("null")){
                return 2;
            }
            else{
                return 3;
            }

        }

        return 0;


    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case 1:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_home, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;
            case 2:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_all_text, parent, false);
                CustomViewHolder row2 = new CustomViewHolder(view2,1);
                return row2;
            case 3:
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_all_images, parent, false);
                CustomViewHolder row3 = new CustomViewHolder(view3,1);
                return row3;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        final Notifications dc_list = feedItemList.get(position);

        String string=dc_list.getImage();

        holder.header.setText(trancuateStringtitle(dc_list.getTitle()));
        holder.time.setText(dc_list.getDate());

        if(NotiType.equals(LATEST_NOTIFICATION)){
            String description=trancuateString(dc_list.getDescription());

            holder.body.setText(description);
        }
        else if(NotiType.equals(ALL_NOTIFICATION)){
            holder.body.setText(trancuateStringSub(dc_list.getDescription()).split("\n")[0]);
            if(!string.equals("null")){
                gluideLoader(mContext,dc_list.getImage(),holder.imageView);
            }

        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext,NotificationDetails.class);
                intent.putExtra("Extra",  dc_list);
                mContext.startActivity(intent);
            }
        });

            //holder.image.setImageResource(dc_list.getTestImage());
            //gluideLoader(mContext,dc_list.getImage(),holder.image);

    }

    private String trancuateString(String description) {
        String s;
        if(description.length()>45){
            s=description.substring(0,45)+" ...";
        }
        else{
            s=description;
        }
        return s;
    }


    private String trancuateStringtitle(String description) {
        String s;
        if(description.length()>28){
            s=description.substring(0,28)+"...";
        }
        else{
            s=description;
        }
        return s;
    }

    private String trancuateStringSub(String description) {
        String s;
        if(description.length()>35){
            s=description.substring(0,35)+"...";
        }
        else{
            s=description;
        }
        return s;
    }



    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {



        //ImageView imageGrey,imageCheck,imageBlue;
        TextView header,body,time;
        ImageView imageView;
        CardView cardView;




        public CustomViewHolder(View itemView,int item) {
            super(itemView);




            header = itemView.findViewById(R.id.noti_title);
            body = itemView.findViewById(R.id.noti_body);
            time = itemView.findViewById(R.id.noti_time);
            cardView = itemView.findViewById(R.id.card);

            if(NotiType.equals(ALL_NOTIFICATION)){
                imageView = itemView.findViewById(R.id.image);
            }




        }
    }
}