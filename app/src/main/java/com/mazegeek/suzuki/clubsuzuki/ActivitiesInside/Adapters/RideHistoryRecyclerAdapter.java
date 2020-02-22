/*
 *
 *  Created by Alvi Khalil on 11/28/18 4:58 PM
 *  Copyright (c) 2018 . All rights reserved.
 *  Last modified 11/7/18 6:00 PM
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

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ClubItems;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.RideHistory;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Interfaces.RideHistoryCardDelete;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class RideHistoryRecyclerAdapter extends RecyclerView.Adapter<RideHistoryRecyclerAdapter.CustomViewHolder> {
    private List<RideHistory> feedItemList;
    private Context mContext;
    private RideHistoryCardDelete mCallback;



    public RideHistoryRecyclerAdapter(Context context, List<RideHistory> feedItemList,RideHistoryCardDelete mCallback) {
        this.feedItemList = feedItemList;
        this.mContext = context;
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_history_list_view, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        final RideHistory dc_list = feedItemList.get(position);





            //holder.image.setImageResource(dc_list.getTestImage());

            holder.textViewStartLoc.setText("Location : "+dc_list.getStartLocation());
            holder.textViewStartTime.setText("Time : "+dc_list.getStartTime());
            holder.textViewEndLoc.setText("Location : "+dc_list.getEndLocation());
            holder.textViewEndTime.setText("Time : "+dc_list.getEndTime());
            holder.textViewDistance.setText(dc_list.getDistance()+" K.M.");

            gluideLoader(mContext,dc_list.getImage(),holder.image);



            holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onSelectionId(dc_list.getId());
                }
            });





    }


    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {



        ImageView image,imageViewDelete;
        TextView textViewStartLoc,textViewStartTime;
        TextView textViewEndLoc,textViewEndTime;
        TextView textViewDistance;





        public CustomViewHolder(View itemView,int item) {
            super(itemView);



            image = itemView.findViewById(R.id.image);
            textViewStartLoc = itemView.findViewById(R.id.start_loc);
            textViewStartTime = itemView.findViewById(R.id.start_time);
            textViewEndLoc = itemView.findViewById(R.id.end_loc);
            textViewEndTime = itemView.findViewById(R.id.end_time);
            textViewDistance = itemView.findViewById(R.id.distance_text);
            imageViewDelete = itemView.findViewById(R.id.delete);



        }
    }
}