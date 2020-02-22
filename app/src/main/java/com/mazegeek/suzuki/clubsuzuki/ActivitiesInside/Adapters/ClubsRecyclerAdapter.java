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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.ClubItems;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.SoSItems;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class ClubsRecyclerAdapter extends RecyclerView.Adapter<ClubsRecyclerAdapter.CustomViewHolder> {
    private List<ClubItems> feedItemList;
    private Context mContext;



    public ClubsRecyclerAdapter(Context context, List<ClubItems> feedItemList) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_list_view, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        final ClubItems dc_list = feedItemList.get(position);





            //holder.image.setImageResource(dc_list.getTestImage());

            holder.header.setText(dc_list.getHeader());
            holder.body.setText(dc_list.getBody());
            gluideLoader(mContext,dc_list.getImage(),holder.image);

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String url = dc_list.getUrl();
                    //String url =  "https://wildmentor.org/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);

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

        ConstraintLayout constraintLayout;



        public CustomViewHolder(View itemView,int item) {
            super(itemView);



            image = itemView.findViewById(R.id.image);

            header = itemView.findViewById(R.id.header_text);
            body = itemView.findViewById(R.id.body);
            constraintLayout = itemView.findViewById(R.id.club_cons_layout);



        }
    }
}