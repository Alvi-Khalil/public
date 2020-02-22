/*
 * Created by Alvi Khalil on 11/2/18 3:28 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 3:19 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.LoyaltyHistory;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.OfferDetails;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.LoyaltyHistoryItem;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.OfferItem;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.EXTRA_PREVIOUS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.OFFERS;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class LoyaltyHistoryRecyclerAdapter extends RecyclerView.Adapter<LoyaltyHistoryRecyclerAdapter.CustomViewHolder> {
    private List<LoyaltyHistoryItem> feedItemList;
    private Context mContext;



    public LoyaltyHistoryRecyclerAdapter(Context context, List<LoyaltyHistoryItem> feedItemList) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loyalty_history_item, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        final LoyaltyHistoryItem dc_list = feedItemList.get(position);



            holder.offerName.setText(dc_list.getOfferName());
            String temp="( Usage : "+dc_list.getUsageNumber()+" out of "+dc_list.getUserQuota()+" )";
            holder.usage.setText(temp);
//            holder.usage.setText(trancuateStringSub(dc_list.getSubTitle()).split("\n")[0]);

            String temp2="( Offer code : "+dc_list.getOfferCode()+" )";
            holder.offerCode.setText(temp2);

            String temp3="Offer Availed from : "+dc_list.getShopName();
            holder.offerShop.setText(temp3);

            String temp4="Sales Contact : "+dc_list.getSalesPhone();
            holder.shopContact.setText(temp4);

            String temp5="Availed at : "+dc_list.getUsageTime();
            holder.offerTime.setText(temp5);



            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent intent= new Intent(mContext,OfferDetails.class);
                    intent.putExtra("Extra",  dc_list);
                    intent.putExtra(EXTRA_PREVIOUS,  OFFERS);
                    mContext.startActivity(intent);*/
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

        TextView offerName;
        TextView usage;
        TextView offerCode;
        TextView offerShop;
        TextView shopContact;
        TextView offerTime;

        CardView constraintLayout;



        public CustomViewHolder(View itemView,int item) {
            super(itemView);

            offerName = itemView.findViewById(R.id.offer_name);
            usage = itemView.findViewById(R.id.usage);
            offerCode = itemView.findViewById(R.id.offer_code);
            offerShop = itemView.findViewById(R.id.offer_shop);
            shopContact = itemView.findViewById(R.id.offer_contact);
            offerTime = itemView.findViewById(R.id.offer_time);

            constraintLayout = itemView.findViewById(R.id.card);


        }
    }
}