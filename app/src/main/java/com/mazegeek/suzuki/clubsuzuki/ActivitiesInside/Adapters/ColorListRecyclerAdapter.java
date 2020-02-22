/*
 * Created by Alvi Khalil on 11/2/18 10:47 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 10:47 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.EcommerceLinks;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.Variant;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Interfaces.ColorSelectionOfBike;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class ColorListRecyclerAdapter extends RecyclerView.Adapter<ColorListRecyclerAdapter.CustomViewHolder> {
    private List<Variant> feedItemList;
    private Context mContext;
    int Selectedposition;
    int oldPos=-1;
    private ColorSelectionOfBike mCallback;



    public ColorListRecyclerAdapter(Context context, List<Variant> feedItemList,int position,ColorSelectionOfBike mCallback) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.Selectedposition = position;
        this.mCallback = mCallback;



    }

    @Override
    public int getItemViewType(int position) {

        return 1;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case 1:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_layout, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        final Variant dc_list = feedItemList.get(position);

        if(oldPos!=-1){
            if(position==oldPos){
                holder.cardView.setVisibility(View.GONE);
                holder.constraintLayoutShadow.setVisibility(View.GONE);
            }
        }
            if(position==Selectedposition){
                holder.cardView.setVisibility(View.VISIBLE);
                holder.constraintLayoutShadow.setVisibility(View.VISIBLE);
                mCallback.onClickColorButton(dc_list.getImageUrl());
            }





            holder.constraintLayout.setBackgroundColor(Color.parseColor(dc_list.getColorCode()));


            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                oldPos = Selectedposition;
                Selectedposition = position;
                notifyItemChanged(oldPos);
                notifyItemChanged(Selectedposition);


                }
            });








    }


    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {




        ConstraintLayout constraintLayout;
        ConstraintLayout constraintLayoutShadow;
        CardView cardView;



        public CustomViewHolder(View itemView,int item) {
            super(itemView);




            constraintLayout = itemView.findViewById(R.id.color_cons);
            constraintLayoutShadow = itemView.findViewById(R.id.cons_shadow);
            cardView = itemView.findViewById(R.id.selected_card);



        }
    }
}