/*
 * Created by Alvi Khalil on 11/5/18 9:45 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 3:19 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.OilClass;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.PartsClass;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class BikeDetailsPartsRecyclerAdapter extends RecyclerView.Adapter<BikeDetailsPartsRecyclerAdapter.CustomViewHolder> {
    private List<PartsClass> feedItemList;
    private Context mContext;



    public BikeDetailsPartsRecyclerAdapter(Context context, List<PartsClass> feedItemList) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parts_bike_details_list_view, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        final PartsClass dc_list = feedItemList.get(position);




            String s="\u09F3 "+dc_list.getPrice();

            //holder.image.setImageResource(dc_list.getTestImage());

            holder.header.setText(dc_list.getName());
            holder.description.setText(dc_list.getDescription());
            holder.price.setText(s);
            gluideLoader(mContext,dc_list.getImage(),holder.image);










    }


    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {



        ImageView image;
        TextView header,description,price;




        public CustomViewHolder(View itemView,int item) {
            super(itemView);



            image = itemView.findViewById(R.id.image);
            header = itemView.findViewById(R.id.header_text);
            description = itemView.findViewById(R.id.description_text);
            price = itemView.findViewById(R.id.price_text);




        }
    }
}