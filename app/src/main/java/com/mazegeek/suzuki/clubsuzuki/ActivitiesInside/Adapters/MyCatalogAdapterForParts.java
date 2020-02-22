/*
 * Created by Alvi Khalil on 11/5/18 4:22 PM
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 11/2/18 4:35 PM
 *
 */

package com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.BikeCard;
import com.mazegeek.suzuki.clubsuzuki.ActivitiesInside.Classes.PartsClass;
import com.mazegeek.suzuki.clubsuzuki.R;

import java.util.List;

import static com.mazegeek.suzuki.clubsuzuki.HelperClasses.ImageLoaderGlide.gluideLoader;


public class MyCatalogAdapterForParts extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    private List<PartsClass> s;







    public MyCatalogAdapterForParts(Context context, List<PartsClass> s){
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

        View view=inflater.inflate(R.layout.part_catalog_item,container,false);


        ImageView imageView= view.findViewById(R.id.image);
        TextView bikeName= view.findViewById(R.id.bike_name);
        TextView bikeDetails= view.findViewById(R.id.bike_details);
        TextView price= view.findViewById(R.id.price_text);



        final PartsClass st = s.get(position);

        String s="\u09F3 "+st.getPrice();
        String s2="("+st.getDescription()+")";

        price.setText(s);
        bikeName.setText(st.getName());
        bikeDetails.setText(s2);
        gluideLoader(context,st.getImage(),imageView);


        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view.equals(object));
    }


}
