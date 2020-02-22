/*
 * Created by Alvi Khalil on 11/1/18 4:23 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/1/18 1:35 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Activities.BikeDetails;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BannarImages;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BikeCard;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class MyCatalogAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    private List<BikeCard> s;







    public MyCatalogAdapter(Context context, List<BikeCard> s){
        this.context=context;
        this.s=s;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return s.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view=inflater.inflate(R.layout.bike_catalog_item,container,false);


        ImageView imageView= view.findViewById(R.id.image);
        TextView bikeName= view.findViewById(R.id.bike_name);
        TextView bikeDetails= view.findViewById(R.id.bike_details);
        Button details= view.findViewById(R.id.details_button);


        final BikeCard st = s.get(position);

        String detailsString =trancuateString(st.getDescription());

        bikeName.setText(st.getTitle());
        bikeDetails.setText(detailsString);
        gluideLoader(context,st.getImage(),imageView);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BikeDetails.class);
                intent.putExtra("bike_model",st.getID());
                context.startActivity(intent);
            }
        });

        container.addView(view);
        return view;
    }

    private String trancuateString(String description) {
        String s;
        if(description.length()>150){
            s=description.substring(0,100)+" ...";
        }
        else{
            s=description;
        }
        return s;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view.equals(object));
    }


}
