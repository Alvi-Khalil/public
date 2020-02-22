/*
 * Created by Alvi Khalil on 10/31/18 2:54 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 10/31/18 2:53 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BannarImages;
import com.mazegeek.suzuki.clubsuzuki.HelperClasses.ConnectionDetector;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

public class HomeBannarAdapter2 extends RecyclerView.Adapter<HomeBannarAdapter2.MyViewHolder> {


    private Context applicationContext;
    private List<BannarImages> s;
    private LayoutInflater inflater;



    private ConnectionDetector connectionDetector;

    public HomeBannarAdapter2(Context applicationContext, List<BannarImages> s) {
        this.applicationContext = applicationContext;
        this.s = s;


    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        inflater = LayoutInflater.from(applicationContext);
        view = inflater.inflate(R.layout.slide, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final BannarImages st = s.get(position);



    /*    RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image);

        Glide.with(applicationContext).load(st.getBannar()).apply(options).into(holder.imageView);

        */

    }

    @Override
    public int getItemCount() {
        return s.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView scientificName, class_name, title, description, barcode;
        ImageView imageView, imageView2;
        LinearLayout layout_animal;
        ConnectionDetector connectionDetector;

        public MyViewHolder(View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.image);


        }
    }
}