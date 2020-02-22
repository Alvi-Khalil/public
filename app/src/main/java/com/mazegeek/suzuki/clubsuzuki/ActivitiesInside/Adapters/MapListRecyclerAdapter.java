/*
 * Created by Alvi Khalil on 10/29/18 4:34 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 9/12/18 7:31 AM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.LocationPoints;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;


public class MapListRecyclerAdapter extends RecyclerView.Adapter<MapListRecyclerAdapter.CustomViewHolder> {
    private List<LocationPoints> feedItemList;
    private Context mContext;



    public MapListRecyclerAdapter(Context context, List<LocationPoints> feedItemList) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_list_view, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        final LocationPoints dc_list = feedItemList.get(position);





            holder.image.setImageResource(dc_list.getImageID());
            holder.header.setText(dc_list.getHeader());
            holder.body.setText(dc_list.getBody());
            holder.number.setText(dc_list.getContact());



            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+dc_list.getContact()));
                    mContext.startActivity(intent);
                }
            });






    }


    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {



        ImageView image;
        TextView header;
        TextView body;
        TextView number;
        ConstraintLayout constraintLayout;

        public CustomViewHolder(View itemView,int item) {
            super(itemView);



            image = itemView.findViewById(R.id.image_of_point);
            header = itemView.findViewById(R.id.header_text);
            body = itemView.findViewById(R.id.body_text);
            number = itemView.findViewById(R.id.mobile_number);
            constraintLayout = itemView.findViewById(R.id.call);


        }
    }
}