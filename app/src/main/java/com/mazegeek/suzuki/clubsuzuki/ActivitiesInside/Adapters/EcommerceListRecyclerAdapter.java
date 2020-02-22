/*
 * Created by Alvi Khalil on 11/2/18 10:47 AM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 10:47 AM
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

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.EcommerceLinks;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.LocationPoints;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class EcommerceListRecyclerAdapter extends RecyclerView.Adapter<EcommerceListRecyclerAdapter.CustomViewHolder> {
    private List<EcommerceLinks> feedItemList;
    private Context mContext;



    public EcommerceListRecyclerAdapter(Context context, List<EcommerceLinks> feedItemList) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ecommerce_list_view, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        final EcommerceLinks dc_list = feedItemList.get(position);





            //holder.image.setImageResource(dc_list.getTestImage());

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
        ConstraintLayout constraintLayout;



        public CustomViewHolder(View itemView,int item) {
            super(itemView);



            image = itemView.findViewById(R.id.ecom_image);
            constraintLayout = itemView.findViewById(R.id.ecommerce_card_id);



        }
    }
}