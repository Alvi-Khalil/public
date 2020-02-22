/*
 * Created by Alvi Khalil on 11/2/18 2:49 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 12:42 PM
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
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.SoSItems;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class SoSListRecyclerAdapter extends RecyclerView.Adapter<SoSListRecyclerAdapter.CustomViewHolder> {
    private List<SoSItems> feedItemList;
    private Context mContext;



    public SoSListRecyclerAdapter(Context context, List<SoSItems> feedItemList) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sos_list_view, parent, false);
                CustomViewHolder row = new CustomViewHolder(view,1);
                return row;

        }
        return null;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        final SoSItems dc_list = feedItemList.get(position);





            //holder.image.setImageResource(dc_list.getTestImage());

            holder.header.setText(dc_list.getHeader());
            gluideLoader(mContext,dc_list.getImage(),holder.image);

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+dc_list.getPhoneNumber()));
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
        ConstraintLayout constraintLayout;



        public CustomViewHolder(View itemView,int item) {
            super(itemView);



            image = itemView.findViewById(R.id.image);
            header = itemView.findViewById(R.id.header_text);
            constraintLayout = itemView.findViewById(R.id.sos_cons_layout);



        }
    }
}