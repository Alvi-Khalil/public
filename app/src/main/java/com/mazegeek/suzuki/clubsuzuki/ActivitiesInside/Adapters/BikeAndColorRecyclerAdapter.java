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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BikeAndColor;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.EcommerceLinks;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Interfaces.BikeAndColorClicked;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.BIKE;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.COLOR;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConstantsClass.isEmailValid;
import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class BikeAndColorRecyclerAdapter extends RecyclerView.Adapter<BikeAndColorRecyclerAdapter.CustomViewHolder> {
    private List<BikeAndColor> feedItemList;
    private Context mContext;
    private BikeAndColorClicked bikeAndColorClicked;



    public BikeAndColorRecyclerAdapter(Context context, List<BikeAndColor> feedItemList,BikeAndColorClicked bikeAndColorClicked) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.bikeAndColorClicked = bikeAndColorClicked;



    }

    @Override
    public int getItemViewType(int position) {
        final BikeAndColor dc_list = feedItemList.get(position);

        if(dc_list.getType().equals(BIKE)){
            return 1;
        }
        else{
            return 2;
        }


    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case 1:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bike_item, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;

            case 2:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item, parent, false);
                CustomViewHolder row2 = new CustomViewHolder(view2,1);
                return row2;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        final BikeAndColor dc_list = feedItemList.get(position);



        if(position==feedItemList.size()-1){
            holder.view.setVisibility(View.GONE);
        }
        else{
            holder.view.setVisibility(View.VISIBLE);
        }


        holder.textView.setText(dc_list.getName());

        if(dc_list.getType().equals(COLOR)){

            holder.image.setColorFilter(Color.parseColor(dc_list.getColor()), android.graphics.PorterDuff.Mode.MULTIPLY);

        }


//            //holder.image.setImageResource(dc_list.getTestImage());
//
//            gluideLoader(mContext,dc_list.getImage(),holder.image);
//
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bikeAndColorClicked.onSelected(dc_list);
                }
            });








    }


    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {



        ImageView image;
        ConstraintLayout constraintLayout;
        TextView textView;
        View view;



        public CustomViewHolder(View itemView,int item) {
            super(itemView);



            image = itemView.findViewById(R.id.image);
            constraintLayout = itemView.findViewById(R.id.main_con);
            textView = itemView.findViewById(R.id.name);
            view = itemView.findViewById(R.id.v);



        }
    }
}