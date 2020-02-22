/*
 * Created by Alvi Khalil on 11/2/18 3:28 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 3:19 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.OfferDetails;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ClubItems;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.OfferItem;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.EXTRA_PREVIOUS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.OFFERS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class OffersRecyclerAdapter extends RecyclerView.Adapter<OffersRecyclerAdapter.CustomViewHolder> {
    private List<OfferItem> feedItemList;
    private Context mContext;



    public OffersRecyclerAdapter(Context context, List<OfferItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;



    }

    @Override
    public int getItemViewType(int position) {

        return 1;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case 1:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        final OfferItem dc_list = feedItemList.get(position);





            //holder.image.setImageResource(dc_list.getTestImage());

            holder.header.setText(trancuateStringtitle(dc_list.getTitle()));
            holder.body.setText(trancuateStringSub(dc_list.getSubTitle()).split("\n")[0]);
            holder.time.setText(dc_list.getDate());

            if(!dc_list.getImage().equals("null")){
                gluideLoader(mContext,dc_list.getImage(),holder.image);
            }
            else {
                holder.image.setVisibility(View.GONE);
            }



            if(dc_list.getType().equals("normal")){

                holder.featuredText.setVisibility(View.GONE);
                holder.featuredStar.setVisibility(View.GONE);
            }

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(mContext,OfferDetails.class);
                    intent.putExtra("Extra",  dc_list);
                    intent.putExtra(EXTRA_PREVIOUS,  OFFERS);
                    mContext.startActivity(intent);
                }
            });





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



        ImageView image;
        ImageView featuredStar;
        TextView header;
        TextView body;
        TextView time;
        TextView featuredText;

        CardView constraintLayout;



        public CustomViewHolder(View itemView,int item) {
            super(itemView);

            image = itemView.findViewById(R.id.image);

            header = itemView.findViewById(R.id.noti_title);
            body = itemView.findViewById(R.id.noti_body);
            time = itemView.findViewById(R.id.noti_time);
            featuredText = itemView.findViewById(R.id.offer_type);
            featuredStar = itemView.findViewById(R.id.star);

            constraintLayout = itemView.findViewById(R.id.card);


        }
    }
}